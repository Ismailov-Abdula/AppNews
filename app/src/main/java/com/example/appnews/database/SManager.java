package com.example.appnews.database;

import com.example.appnews.models.News;
import com.example.appnews.models.User;

import java.util.ArrayList;

public class SManager {
    private static User user;
    private static News news;


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SManager.user = user;
    }

    public static News getNews() {
        return news;
    }

    public static void setNews(News news) {
        SManager.news = news;
    }
}
