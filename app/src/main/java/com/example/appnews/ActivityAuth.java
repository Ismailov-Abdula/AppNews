package com.example.appnews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.appnews.database.SManager;
import com.example.appnews.database.DBHelper;
import com.example.appnews.fragment.FragmentAuth;
import com.example.appnews.fragment.FragmentRegistration;
import com.example.appnews.interfaces.OnFragmentAuthListener;
import com.example.appnews.interfaces.OnFragmentRegistrationListener;
import com.example.appnews.models.User;

import java.util.concurrent.Executor;


public class ActivityAuth extends AppCompatActivity
implements OnFragmentAuthListener,
        OnFragmentRegistrationListener {

    private FragmentAuth fragmentAuth;
    private FragmentRegistration fragmentRegistration;

    private final String SIMPLE_FRAGMENT_AUTH_TAG = "SIMPLE_FRAGMENT_AUTH_TAG";
    private final String SIMPLE_FRAGMENT_REGISTRATION_TAG = "SIMPLE_FRAGMENT_REGISTRATION_TAG";
    private String current_fragment = SIMPLE_FRAGMENT_AUTH_TAG;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    Animation animScale, animScaleReverse;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("current_fragment", current_fragment);
        super.onSaveInstanceState(outState);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        animScaleReverse = AnimationUtils.loadAnimation(this, R.anim.scale_reverse);
        //animTransformLeft = AnimationUtils.loadAnimation(this, R.anim.transform_left);
        //animTransformRight = AnimationUtils.loadAnimation(this, R.anim.transform_right);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            fragmentAuth = (FragmentAuth)
                    getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_AUTH_TAG);
            fragmentRegistration = (FragmentRegistration)
                    getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_REGISTRATION_TAG);

            current_fragment = savedInstanceState.getString("current_fragment");

        }
        // only create fragment if they haven't been instantiated already
        if (fragmentAuth == null)
            fragmentAuth = new FragmentAuth();
        if(fragmentRegistration == null)
          fragmentRegistration = new FragmentRegistration();

        fragmentAuth.setOnFragmentAuthListener(this);
        fragmentRegistration.setOnFragmentRegistrationListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(current_fragment.equals(SIMPLE_FRAGMENT_AUTH_TAG)){
            fragmentTransaction.replace(R.id.fragment, fragmentAuth, SIMPLE_FRAGMENT_AUTH_TAG);
        }else if(current_fragment.equals(SIMPLE_FRAGMENT_REGISTRATION_TAG)){
            fragmentTransaction.replace(R.id.fragment, fragmentRegistration, SIMPLE_FRAGMENT_REGISTRATION_TAG);
        }
        fragmentTransaction.commit();

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();


                SManager.setUser(new User("Guest", User.ROLE.USER.toString()));
                Intent intent = new Intent(ActivityAuth.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }



    @Override
    public void clickOpenRegistrationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.transform_left_begin, R.anim.transform_right_begin);
        fragmentTransaction.replace(R.id.fragment, fragmentRegistration, SIMPLE_FRAGMENT_REGISTRATION_TAG);
        fragmentTransaction.commit();
        current_fragment = SIMPLE_FRAGMENT_REGISTRATION_TAG;
    }

    @Override
    public void clickSignIn(String  login, String password) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        User user = dbHelper.getUserByLoginAndPassword(login, password);
        if(user != null){
            SManager.setUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void startAnimation(View v) {
        v.startAnimation(animScale);
        v.startAnimation(animScaleReverse);
    }

    @Override
    public void clickOpenAuthFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.transform_left_end, R.anim.transform_right_end);
        fragmentTransaction.replace(R.id.fragment, fragmentAuth, SIMPLE_FRAGMENT_AUTH_TAG);
        fragmentTransaction.commit();
        current_fragment = SIMPLE_FRAGMENT_AUTH_TAG;
    }

    @Override
    public void clickRegistration(User user) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        boolean success = dbHelper.insertUser(user);
        if(success){
            SManager.setUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}