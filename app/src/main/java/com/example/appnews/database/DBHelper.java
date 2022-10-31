package com.example.appnews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.appnews.models.News;
import com.example.appnews.models.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BDNews";
    public static final String CONTACTS_COLUMN_ID = "id";

    public static final String CONTACTS_TABLE_NEWS = "news";
    public static final String CONTACTS_COLUMN_NEWS_TXT_BODY = "txtBody";
    public static final String CONTACTS_COLUMN_NEWS_IMG = "img";

    public static final String CONTACTS_TABLE_USERS = "users";
    public static final String CONTACTS_TABLE_USER_ROLE = "userRole";
    public static final String CONTACTS_TABLE_USER_NAME = "userName";
    public static final String CONTACTS_TABLE_USER_LOGIN = "userLogin";
    public static final String CONTACTS_TABLE_USER_PASSWORD = "password";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("LOG_TAG", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + CONTACTS_TABLE_NEWS + " ("
                + CONTACTS_COLUMN_ID + " integer primary key autoincrement,"
                + CONTACTS_COLUMN_NEWS_TXT_BODY + " text,"
                + CONTACTS_COLUMN_NEWS_IMG + " text" + ");");

        db.execSQL("create table " + CONTACTS_TABLE_USERS + " ("
                + CONTACTS_COLUMN_ID + " integer primary key autoincrement,"
                + CONTACTS_TABLE_USER_ROLE + " text,"
                + CONTACTS_TABLE_USER_NAME + " text,"
                + CONTACTS_TABLE_USER_PASSWORD + " text,"
                + CONTACTS_TABLE_USER_LOGIN + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_USERS);
        onCreate(db);
    }

    public boolean insertNews (News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NEWS_TXT_BODY, news.getTxtBody());
        contentValues.put(CONTACTS_COLUMN_NEWS_IMG, news.getImg());
        if(news.getId() == 0){
            db.insert(CONTACTS_TABLE_NEWS, null, contentValues);
        }else{
            db.update(CONTACTS_TABLE_NEWS,contentValues, "id = ?",
                    new String[] {String.valueOf(news.getId())});
        }
        return true;
    }



    public boolean insertUser (User user) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_TABLE_USER_ROLE, user.getRole());
            contentValues.put(CONTACTS_TABLE_USER_NAME, user.getName());
            contentValues.put(CONTACTS_TABLE_USER_LOGIN, user.getLogin());
            contentValues.put(CONTACTS_TABLE_USER_PASSWORD, user.getPassword());
            db.insert(CONTACTS_TABLE_USERS, null, contentValues);
            db.close();
            return true;
        }catch (Exception e){

        }
        return false;
    }

    public Cursor getData(int id, String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + table + " where id="+id, null);
        return res;
    }

    public User getUserByLoginAndPassword(String login, String password){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            User user = new User();
            Cursor cursor =  db.rawQuery("select * from " +
                            CONTACTS_TABLE_USERS +
                            " where " +
                            CONTACTS_TABLE_USER_LOGIN + "=" + "'" + login + "'" +
                            " and "+
                            CONTACTS_TABLE_USER_PASSWORD + "=" + "'" + password + "'",
                    null);

            if(cursor.getCount() == 0)
                return null;

            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    user.setRole(cursor.getString(1));
                    user.setName(cursor.getString(2));
                    user.setPassword(cursor.getString(3));
                    user.setLogin(cursor.getString(4));
                }
            }
            cursor.close();
            db.close();
            return user;
        }catch (Exception e){
            Log.d("Exception", e.getMessage());
        }
        return null;
    }

    public ArrayList<News> getAllRecords() {
         SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CONTACTS_TABLE_NEWS,
                null, null, null, null, null, null);
        ArrayList<News> subs = new ArrayList<>();
        News news;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                news = new News();
                news.setId(cursor.getInt(0));
                news.setTxtBody(cursor.getString(1));
                news.setImg(cursor.getString(2));
                subs.add(news);
            }
        }
        cursor.close();
        db.close();
        return subs;
    }

    public boolean deleteNews(int id){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + CONTACTS_TABLE_NEWS + " WHERE " + CONTACTS_COLUMN_ID + " = " + id + ";");
            return true;
        }catch (Exception e){

        }
        return false;
    }

    public String getLastNews(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                CONTACTS_TABLE_NEWS + " WHERE " + CONTACTS_COLUMN_ID + " = " +
                "(SELECT MAX("+CONTACTS_COLUMN_ID+") FROM " + CONTACTS_TABLE_NEWS + ");",
                null);
        cursor.moveToLast();
        String title;
        if (cursor.getCount() > 0){
            cursor.moveToLast();
            title = cursor.getString(1);
            cursor.close();
            db.close();
        }
        else
            return "Новостей нету";


        return title;
    }
}
