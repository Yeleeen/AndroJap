package com.example.myapplication;

public class Text {

    public String id;
    private String title;
    public String content;


    public Text(String title){
        this.title = title;
    }
    public Text(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


}
