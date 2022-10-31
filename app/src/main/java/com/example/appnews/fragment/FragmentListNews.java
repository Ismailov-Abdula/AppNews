package com.example.appnews.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.appnews.R;
import com.example.appnews.database.DBHelper;
import com.example.appnews.database.SManager;
import com.example.appnews.interfaces.OnFragmentListNews;
import com.example.appnews.models.User;


public class FragmentListNews extends Fragment {

    private User mainUser;
    Button btnLogOut, btnAdd;
    RecyclerView recyclerView;
    TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_news, container, false);

        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnAdd = v.findViewById(R.id.btnAdd);
        recyclerView = v.findViewById(R.id.list_news);
        txt = v.findViewById(R.id.txt);

        mainUser = SManager.getUser();

        if(mainUser != null){
            txt.setText(mainUser.getName());

            if(!mainUser.getRole().equals(User.ROLE.ADMIN.toString())){
                btnAdd.setVisibility(View.GONE);
            }

            btnAdd.setOnClickListener(view -> {
                if(mListener != null){
                    mListener.startAnimation(view);
                    mListener.clickAdd();
                }

            });

            btnLogOut.setOnClickListener(view -> {
                if(mListener != null){
                    mListener.startAnimation(view);
                    mListener.clickLogout();
                }

            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    RecyclerView.VERTICAL, false));

            if(mListener != null)
                mListener.setAdapter(recyclerView);
        }



        return v;
    }

    private static OnFragmentListNews mListener;

    public void setOnFragmentListNews(OnFragmentListNews listener) {
        mListener = listener;
    }

}