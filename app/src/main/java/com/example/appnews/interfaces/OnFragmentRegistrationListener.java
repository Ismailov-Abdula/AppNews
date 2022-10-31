package com.example.appnews.interfaces;

import android.view.View;

import com.example.appnews.models.User;

public interface OnFragmentRegistrationListener {
    void clickOpenAuthFragment();
    void clickRegistration(User user);
    void startAnimation(View v);
}
