package com.example.cg24;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    private Button btn_google;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private static final int RED_SIGN_GOOGLE=100;

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googoleSigninOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail() // email addresses도 요청함
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // connection failed, should be handled
                        Toast.makeText(LoginActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googoleSigninOptions)
                .build();

        auth=FirebaseAuth.getInstance();
        btn_google=findViewById(R.id.googleLoginBtn);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RED_SIGN_GOOGLE);
            }
        });

        signOut2();


        // 일반 로그인
        Button btn_login=findViewById(R.id.normalLoginBtn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(), LoginActivity2.class);
                // intent.putExtra("nickname", account.getDisplayName());
                startActivity(intent);

            }
        });

    }

    // 구글 로그인 진행 시 결과 되돌려 받는 장소
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RED_SIGN_GOOGLE) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // 인증 결과가 성공적이면...
                // 구글 로그인 정보 담고 있다.
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;
    String txUserName;

    // 로그인 결과 값 출력 수행
    private void resultLogin(GoogleSignInAccount account) {

        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("nickname", account.getDisplayName());
                    startActivity(intent);

                    String text;
                    if (user != null) {
                        text =user.getUid();
                    } else {
                        text ="no user";
                    }


                    DatabaseReference ref_total_account=dataRef.child("CG24");
                    ref_total_account.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            String current_uid=user.getUid();

                            String nickname_total=snapshot.child("ID1").getValue().toString();
                            Log.d("nickname_total", nickname_total);
                            System.out.println(nickname_total);

                            // 2. ", "를 구분자로 문자열 split
                            String nickname_final=nickname_total.substring(1, nickname_total.length()-1);
                            String[] strArr = nickname_final.split(", |=");

                            Log.d("Arrays", Arrays.toString(strArr));
                            Log.d("strArr[0]", strArr[0]);

                            String result_arr=new Boolean(Arrays.asList(strArr).contains(user.getUid())).toString();

                            Log.d("Array contains", result_arr);
                            Log.d("Array user id", user.getUid());

                            System.out.println(Arrays.asList(strArr).contains(user.getUid()));

                            if (result_arr=="true") {
                                Log.d("nickname_2", "resutl true");
                            } else {
                                // 로그인한 사용자 로그인 정보 update
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                UserAccount account=new UserAccount();
                                account.setIdToken(user.getUid());
                                account.setEmailId(user.getEmail());
                                account.setNickname(user.getDisplayName());
                                dataRef.child("CG24").child("UserAccount").child(user.getUid()).setValue(account);

                                // 로그인한 사용자 CupCount update
                                dataRef.child("CG24").child("UserAccount").child(user.getUid()).child("cup").child("CupCount").child("count").setValue(0);
                                dataRef.child("CG24").child("UserAccount").child(user.getUid()).child("cup").child("CupCount").child("point").setValue(0);

                                // IDs에 내용 저장
                                dataRef.child("CG24").child("ID1").child(user.getUid()).setValue(user.getUid());
                                Log.d("nickname_2", "no user exists");
                            }


                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResutl) {
    }

    public void signOut2() {
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {
                auth.signOut();
                if (googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                //setResult(ResultCode.SIGN_OUT_FAIL);
                finish();
            }
        });
    }

}
