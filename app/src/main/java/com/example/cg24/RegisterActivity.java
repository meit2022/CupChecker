package com.example.cg24;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스

    private EditText mEtEmail, mEtPwd;          // 회원가입 입력필드
    private EditText mEtNickname, mEtStoreAddress, mEtStoreCategory;
    private Button mBtnRegister, mBtnImage;     // 회원가입 버튼

    ImageView imageViewProfile;
    Uri selectedImageURi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("NolFI");

        mEtEmail=findViewById(R.id.registerEmail);
        mEtPwd=findViewById(R.id.registerPassword);
        mBtnRegister=findViewById(R.id.registerSignUpBtn);

        //mBtnImage=findViewById(R.id.register_image_btn);
        mEtNickname=findViewById(R.id.registerNickname);
        mEtStoreAddress=findViewById(R.id.registerStoreAddress);
        mEtStoreCategory=findViewById(R.id.registerStoreCategory);

        imageViewProfile=findViewById(R.id.register_image);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail=mEtEmail.getText().toString();
                String strPwd=mEtPwd.getText().toString();

                String strNickname=mEtNickname.getText().toString();
                String strAddress=mEtStoreAddress.getText().toString();
                String strStoreCategory=mEtStoreCategory.getText().toString();
                String strProfile="profile "+strPwd+".jpg";

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
                            UserAccount account=new UserAccount();
                            account.setIdToken(firebaseUser.getUid());  // 로그인하면 부여되는 고유값
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            account.setAddress(strNickname);
                            account.setAddress(strAddress);
                            account.setAddress(strStoreCategory);
                            // account.setAddress(strStoreCategory);

                            // setValue: database에 삽입하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //image 클릭시
        imageViewProfile=(ImageView) findViewById(R.id.register_image);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리에서 이미지 클릭해서 이미지 뷰에 보여주기
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null & data.getData() != null) {
            selectedImageURi = data.getData();
            imageViewProfile.setImageURI(selectedImageURi);
        }
    }

}
