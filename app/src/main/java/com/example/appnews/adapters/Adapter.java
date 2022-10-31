package com.example.appnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnews.interfaces.OnClickAdapterListener;
import com.example.appnews.R;
import com.example.appnews.models.News;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{


    private final LayoutInflater inflater;
    private final ArrayList<News> news;


    // создаем поле объекта-колбэка
    private static OnClickAdapterListener mListener;

    // метод-сеттер для привязки колбэка к получателю событий
    public void setOnClickAdapterListener(OnClickAdapterListener listener) {
        mListener = listener;
    }


    public Adapter(Context context, ArrayList<News> news) {
        this.inflater = LayoutInflater.from(context);
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = this.news.get(position);
        holder.txtPreview.setText(news.getTxtBody());

    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layout;

        private TextView txtPreview;

        ViewHolder(View view){
            super(view);
            txtPreview = itemView.findViewById(R.id.txtPreview);
            layout = itemView.findViewById(R.id.item_news_layout);

            layout.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mListener.onLayoutClick(position);
            });
        }
    }
}
