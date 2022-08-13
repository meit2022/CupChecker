package com.example.cg24;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cg24.recyclerview.Data;
import com.example.cg24.recyclerview.MyRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class FragmentHome extends Fragment {
    private FirebaseAuth mAuth;

    TextView hash_point;
    TextView hash_date;
    RecyclerView mRecyclerView;
    MyRecyclerAdapter mRecyclerAdapter;
    ArrayList<Data> mdataItems;

    FirebaseDatabase database2;
    DatabaseReference databaseReference2;

    public static FragmentHome context_detail1;
    public String point, date;
    Map<String, Object> map2;
    List<String> list2;
    String[] items2;

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

/*
           //포인트 받기
            Intent intent=getActivity().getIntent();
            //part=intent.getStringExtra("part");
            point=intent.getStringExtra("point");
            date=intent.getStringExtra("date");*/
            context_detail1=this; //전역변수로 사용
/*
            //# 뒤에 포인트 입력
            hash_point=(TextView)rootView.findViewById(R.id.point);
            hash_point.setText(point);
            hash_date=(TextView)rootView.findViewById(R.id.date);
            hash_date.setText(date);*/

            //recyclerview list data 만들기
            mdataItems=new ArrayList<Data>();
            //recyclerview와 layout 연결
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
            mRecyclerAdapter = new MyRecyclerAdapter();
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //recyclerview에 들어갈 data 넣기
            database2= FirebaseDatabase.getInstance();

            databaseReference2=database2.getReference();

            //count값 가져오기
            DatabaseReference count=databaseReference2.child("CG24").child("UserAccount").child(user.getUid()).child("cup").child("CupCount");
            count.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   String count1;
                    count1 = dataSnapshot.child("count").getValue().toString();
                    Log.d(TAG, "count " + count1);
                    if (count1 == "0"){
                        String date = "";
                        String point = "";
                        mdataItems.add(new Data(point, date));
                        mRecyclerAdapter.setData(mdataItems);
                        Log.w("null", "null");
                    }
                    else {
                        int count2 = Integer.parseInt(count1);


                        //count값 만큼 돌리기기

                        for (int i = 0; i < count2; i++) {
                            String i2 = Integer.toString(i);
                            DatabaseReference Ref = databaseReference2.child("CG24").child("UserAccount").child(user.getUid()).child("cup").child(i2);
                            Ref.addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String plz;
                                    plz = dataSnapshot.getValue().toString();
                                    Log.d(TAG, "plz " + plz);


                                    String date = dataSnapshot.child("datetime").getValue().toString();
                                    String point = dataSnapshot.child("prediction").getValue().toString();
                                    mdataItems.add(new Data(point, date));
                                    mRecyclerAdapter.setData(mdataItems);

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("find nickname", "Failed to read value.", error.toException());
                }
            });








            //점수에 따라 이미지 바꾸기
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



            // 리사이클러뷰에 LinearLayoutManager 객체 지정.
            // RecyclerView recyclerView = (TextView)rootView.findViewById(R.id.recyclerView);

            /*
            RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;

            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
            MyRecyclerAdapter adapter = new MyRecyclerAdapter() ;
            recyclerView.setAdapter(adapter) ;
             */

            return rootView;
        }
}