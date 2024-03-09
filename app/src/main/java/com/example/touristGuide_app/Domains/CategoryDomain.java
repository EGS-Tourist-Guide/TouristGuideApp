package com.example.touristGuide_app.Domains;

public class CategoryDomain {

    private String Title;
    private String picPath;

    public CategoryDomain(String title, String picPath) {
        Title = title;
        this.picPath = picPath;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
