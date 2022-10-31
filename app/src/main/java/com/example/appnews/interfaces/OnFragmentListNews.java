package com.example.appnews.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnFragmentListNews {
    void setAdapter(RecyclerView view);
    void clickAdd();
    void clickLogout();
    void startAnimation(View v);
}
