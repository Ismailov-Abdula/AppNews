package com.example.appnews.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.appnews.R;
import com.example.appnews.interfaces.OnFragmentAuthListener;
import com.example.appnews.interfaces.OnFragmentRegistrationListener;
import com.example.appnews.models.User;

public class FragmentRegistration extends Fragment {

    Button btnBack, btnRegistration;
    EditText edtName, edtLogin, edtPasswordR, edtPassword;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        btnRegistration = v.findViewById(R.id.btnRegistration);
        edtName = v.findViewById(R.id.edtName);
        edtLogin = v.findViewById(R.id.edtLogin);
        edtPasswordR = v.findViewById(R.id.edtPasswordR);
        edtPassword = v.findViewById(R.id.edtPassword);
        checkBox = v.findViewById(R.id.checkBox);

        btnBack.setOnClickListener(view -> {
            if(mListener != null){
                mListener.startAnimation(view);
                mListener.clickOpenAuthFragment();
            }

        });

        btnRegistration.setOnClickListener(view -> {
            User user = new User();
            user.setName(edtName.getText().toString());
            user.setLogin(edtLogin.getText().toString());
            user.setPassword(edtPassword.getText().toString());
            if(checkBox.isChecked()){
                user.setRole(User.ROLE.ADMIN.toString());
            }else
                user.setRole(User.ROLE.USER.toString());
            if(mListener != null){
                mListener.startAnimation(view);
                mListener.clickRegistration(user);
            }
        });

        return v;
    }

    private static OnFragmentRegistrationListener mListener;

    public void setOnFragmentRegistrationListener(OnFragmentRegistrationListener listener) {
        mListener = listener;
    }
}