package com.example.cg24;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

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

            TextView nickname=(TextView)rootView.findViewById(R.id.loginNickname);
            nickname.setText(user.getDisplayName());
            // TextView useremail=(TextView)rootView.findViewById(R.id.loginEmail);
            // useremail.setText(user.getEmail());

            iv = (ImageView) rootView.findViewById(R.id.qrcode);

            if (user != null) {
                text =user.getUid();
            } else {
                text ="no user";
            }

            // TextView qrcode_tv=(TextView)rootView.findViewById(R.id.qr_text);
            // qrcode_tv.setText(text);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,650,650);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                iv.setImageBitmap(bitmap);
            }catch (Exception e){}

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
                            // user.s
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

            return rootView;
        }
}