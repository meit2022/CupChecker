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
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity2 extends AppCompatActivity {

    private FirebaseAuth auth;


    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_login);

        // 일반 로그인
        Button btn_register=findViewById(R.id.normalRegisterBtn);
        Button btn_login=findViewById(R.id.normalLoginBtn2);

        etEmail=findViewById(R.id.loginEmail);
        etPassword=findViewById(R.id.loginPassword);
        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동
                Intent intent=new Intent(LoginActivity2.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success","signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                            startActivity(intent);
                            // user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail","signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
            }
        });

    }

}
