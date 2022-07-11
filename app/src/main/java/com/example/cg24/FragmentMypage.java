package com.example.cg24;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.qrcode.QRCodeWriter;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class FragmentMypage extends Fragment {
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

            return rootView;
        }

    }