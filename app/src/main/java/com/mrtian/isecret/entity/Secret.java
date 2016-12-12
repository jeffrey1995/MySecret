package com.mrtian.isecret.entity;

/**
 * Created by tianxiying on 16/10/26.
 */
public class Secret {
    private int id;

    private String title;

    private String text;

    private int imageId;

    private String date;


    public Secret() {
        this.id = 0;
        this.title = "";
        this.text = "";
        this.imageId = 0;
        this.date = "";
    }

    public Secret(String title, int imageId, String text, String date) {
        this.title = title;
        this.text = text;
        this.imageId = imageId;
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = Integer.parseInt(imageId);
    }
}
