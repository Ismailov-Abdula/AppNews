package com.example.appnews.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.appnews.models.News;

public interface OnFragmentNewsDetailsListener {
    void save(News news);
    void back();
    void delete(int id);
    News getNews();
    void startAnimation(View v);
}
