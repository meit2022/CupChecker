package com.example.cg24;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends Fragment {
    private FirebaseAuth mAuth;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

            //닉네임
            mAuth= FirebaseAuth.getInstance();
            final FirebaseUser user=mAuth.getCurrentUser();

            TextView nickname=(TextView)rootView.findViewById(R.id.loginNickname);
            TextView pointTV=(TextView)rootView.findViewById(R.id.home_total_point);
            ImageView pointIV=(ImageView)rootView.findViewById(R.id.home_point_iv);

            // nickname.setText(user.getDisplayName());

            DatabaseReference dataRef;
            dataRef = FirebaseDatabase.getInstance().getReference();

            // 마이페이지에 이름 띄우기
            DatabaseReference newRef=dataRef.child("CG24").child("UserAccount").child(user.getUid());
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname1=dataSnapshot.child("nickname").getValue().toString();
                    nickname.setText(nickname1);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("find nickname", "Failed to read value.", error.toException());
                }
            });


            pointTV.setText("1500");
            String str_point=pointTV.getText().toString();
            int int_point = Integer.parseInt(str_point);

            if (int_point==0) {
                pointIV.setImageResource(R.drawable.point1);
            } else if (500>=int_point && int_point>0) {
                pointIV.setImageResource(R.drawable.point2);
            } else if (1000>=int_point && int_point>500) {
                pointIV.setImageResource(R.drawable.point3);
            } else if ( int_point>1000) {
                pointIV.setImageResource(R.drawable.point4);
            } else {
                pointIV.setImageResource(R.drawable.point1);
            }


            return rootView;
        }
}