package com.example.appnews.models;

public class News {
    private int id;
    private String txtBody;
    private String img;

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxtBody() {
        return txtBody;
    }

    public void setTxtBody(String txtBody) {
        this.txtBody = txtBody;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
