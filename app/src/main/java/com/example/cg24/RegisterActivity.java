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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;                  // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스

    private EditText mEtEmail, mEtPwd, mEtNickname;     // 회원가입 입력필드
    private Button mBtnRegister;                        // 회원가입 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth  = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("CG24");

        mEtEmail=findViewById(R.id.etRegisterEmail);
        mEtPwd=findViewById(R.id.etRegisterPassword);
        mEtNickname=findViewById(R.id.etRegisterNickname);

        mBtnRegister=findViewById(R.id.registerSignUpButton);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPwd=mEtPwd.getText().toString();
                String strEmail=mEtEmail.getText().toString();
                String strNickname=mEtNickname.getText().toString();

                // Firebase Auth 진행
                mAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user=mAuth.getCurrentUser();
                            UserAccount account=new UserAccount();
                            account.setIdToken(user.getUid());          // 로그인하면 부여되는 고유값
                            account.setEmailId(user.getEmail());
                            account.setPassword(strPwd);
                            account.setNickname(strNickname);

                            mDatabaseRef.child("UserAccount").child(user.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.w("로그인", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}