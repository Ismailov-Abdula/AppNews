package com.example.appnews.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appnews.R;
import com.example.appnews.database.SManager;
import com.example.appnews.interfaces.OnFragmentNewsDetailsListener;
import com.example.appnews.interfaces.OnFragmentRegistrationListener;
import com.example.appnews.models.News;

import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class FragmentNewsDetails extends Fragment {

    Button btnSave, btnDelete, btnBack;
    EditText txtNews;
    ImageView img;
    News news;
    Uri chosenImageUri;
    @Override
    public void onStart() {
        news = mListener.getNews();

        if(news != null){
            txtNews.setText(news.getTxtBody());
            try{
                Uri uri = Uri.parse(news.getImg());
                //img.setImageURI(uri);
            }catch (Exception e){
                String s = e.getMessage();
            }

        }
        super.onStart();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    chosenImageUri = data.getData();
                    img.setImageURI(chosenImageUri);
                }
                break;
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_details, container, false);

        btnSave = v.findViewById(R.id.btnSave);
        btnBack = v.findViewById(R.id.btnBack);
        btnDelete = v.findViewById(R.id.btnDelete);


        img = v.findViewById(R.id.img);
        txtNews = v.findViewById(R.id.txtNews);

        img.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
            mListener.startAnimation(view);
        });

        btnBack.setOnClickListener(view -> {
            if(mListener != null){
                mListener.startAnimation(view);
                txtNews.setText("");
                SManager.setNews(null);
                mListener.back();
            }
        });

        btnDelete.setOnClickListener(view -> {
            if(news != null){
                mListener.startAnimation(view);
                mListener.delete(news.getId());
                txtNews.setText("");
                SManager.setNews(null);
                mListener.back();

            }else{
                Toast.makeText(getContext(), "Нельзя удалить еще не созданую новость", Toast.LENGTH_SHORT).show();
            }
        });


        btnSave.setOnClickListener(view -> {
            if(mListener != null){
                mListener.startAnimation(view);
                if(news == null)
                    news = new News();
                String s = txtNews.getText().toString();
                news.setTxtBody(s);

                if(chosenImageUri != null)
                    news.setImg(chosenImageUri.toString());
                else
                    news.setImg("");

                mListener.save(news);
                txtNews.setText("");
                SManager.setNews(null);
                mListener.back();
            }
        });

        return v;
    }

    private static OnFragmentNewsDetailsListener mListener;

    public void setOnFragmentNewsDetailsListener(OnFragmentNewsDetailsListener listener) {
        mListener = listener;
    }

}