package com.example.cg24;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;

public class FragmentMypage extends Fragment {
        private FirebaseAuth mAuth;

        private ImageView iv;
        private String text;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

            Button logoutBtn=(Button)rootView.findViewById(R.id.mypage_logout_btn);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "sign out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            mAuth=FirebaseAuth.getInstance();
            final FirebaseUser user=mAuth.getCurrentUser();

            if (user != null) {
                text =user.getUid();
            } else {
                text ="no user";
            }

            TextView nickname=(TextView)rootView.findViewById(R.id.loginNickname);

            // 구글 로그인 이름 띄우기
            // nickname.setText(user.getDisplayName());

            DatabaseReference dataRef;
            dataRef = FirebaseDatabase.getInstance().getReference();

            // 마이페이지에 이름 띄우기
            DatabaseReference newRef=dataRef.child("CG24").child("UserAccount").child(text);
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname1=dataSnapshot.child("nickname").getValue().toString();
                    nickname.setText(nickname1);

                    // FirebaseUser user=mAuth.getCurrentUser();
                    // dataRef.child("CG24").child("UserAccount").child(user.getUid()).setValue(nickname1);

                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("find nickname", "Failed to read value.", error.toException());
                }
            });


            // QR 코드
            iv = (ImageView) rootView.findViewById(R.id.qrcode);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,650,650);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                iv.setImageBitmap(bitmap);
            }catch (Exception e){}


            // 이름 변경하기
            /*
            Button rename_btn=(Button)rootView.findViewById(R.id.mypage_rename_btn);
            rename_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "rename finish", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(getActivity(), LoginActivity.class);
                    // startActivity(intent);

                    final EditText etName = new EditText(getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("변경할 이름을 입력해주세요").setMessage("이름 입력 후 OK 버튼을 눌러주세요").setView(etName);


                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                            String new_name=etName.getText().toString();
                            nickname.setText(new_name);

                            // 이름 변경
                            // dataRef.child("CG24").child("UserAccount").child(text).child("nickname").setValue(new_name);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

             */
            return rootView;
        }
}