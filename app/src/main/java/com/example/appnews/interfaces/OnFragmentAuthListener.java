package com.example.appnews.interfaces;

import android.view.View;

public interface OnFragmentAuthListener {
    void clickOpenRegistrationFragment();
    void clickSignIn(String login, String password);
    void startAnimation(View v);
}
