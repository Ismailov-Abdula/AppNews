package com.example.appnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnews.adapters.Adapter;
import com.example.appnews.database.SManager;
import com.example.appnews.database.DBHelper;
import com.example.appnews.fragment.FragmentListNews;
import com.example.appnews.fragment.FragmentNewsDetails;
import com.example.appnews.interfaces.OnClickAdapterListener;
import com.example.appnews.interfaces.OnFragmentListNews;
import com.example.appnews.interfaces.OnFragmentNewsDetailsListener;
import com.example.appnews.models.News;
import com.example.appnews.models.User;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements OnFragmentNewsDetailsListener, OnClickAdapterListener, OnFragmentListNews {

    FragmentNewsDetails fragmentNewsDetails;
    FragmentListNews fragmentListNews;
    Adapter adapter;
    private static ArrayList<News> list_news;
    private static boolean back = false;
    Animation animScale, animScaleReverse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        DBHelper helper = new DBHelper(this);
        list_news = helper.getAllRecords();


        fragmentNewsDetails = new FragmentNewsDetails();
        fragmentListNews = new FragmentListNews();

        fragmentListNews.setOnFragmentListNews(this);
        fragmentNewsDetails.setOnFragmentNewsDetailsListener(this);

        adapter = new Adapter(this, list_news);
        adapter.setOnClickAdapterListener(this);

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        animScaleReverse = AnimationUtils.loadAnimation(this, R.anim.scale_reverse);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragmentListNews);
        fragmentTransaction.commit();
    }



    public void notifyList(){
        DBHelper helper = new DBHelper(getApplicationContext());
        ArrayList<News> list_news = helper.getAllRecords();
        this.list_news.clear();
        for(News news : list_news){
            this.list_news.add(news);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onLayoutClick(int position) {
        if(SManager.getUser().getRole().equals(User.ROLE.ADMIN.toString())){
            SManager.setNews(list_news.get(position));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.transform_left_end, R.anim.transform_right_end);
            fragmentTransaction.replace(R.id.fragment, fragmentNewsDetails);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void save(News news) {
        try{
            DBHelper helper = new DBHelper(getApplicationContext());
            helper.insertNews(news);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.transform_left_begin, R.anim.transform_right_begin);
            fragmentTransaction.replace(R.id.fragment, fragmentListNews);
            fragmentTransaction.commit();
            notifyList();
        }catch (Exception e){

        }
    }

    @Override
    public void back() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.transform_left_begin, R.anim.transform_right_begin);
        fragmentTransaction.replace(R.id.fragment, fragmentListNews);
        fragmentTransaction.commit();
    }

    @Override
    public void delete(int id) {
        DBHelper helper = new DBHelper(getApplicationContext());
        helper.deleteNews(id);
        notifyList();
    }

    @Override
    public News getNews() {
        return SManager.getNews();
    }



    @Override
    public void startAnimation(View v) {
        v.startAnimation(animScale);
        v.startAnimation(animScaleReverse);
    }

    @Override
    public void onBackPressed() {
        if(!back){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public void setAdapter(RecyclerView view) {
        view.setAdapter(adapter);
    }

    @Override
    public void clickAdd() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.transform_left_end, R.anim.transform_right_end);
        fragmentTransaction.replace(R.id.fragment, fragmentNewsDetails);
        fragmentTransaction.commit();
    }

    @Override
    public void clickLogout() {
        back = true;
        SManager.setUser(null);
        SManager.setNews(null);
        onBackPressed();
    }
}