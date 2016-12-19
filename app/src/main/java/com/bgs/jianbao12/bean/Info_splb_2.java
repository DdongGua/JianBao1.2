package com.bgs.jianbao12.bean;

/**
 * Created by 醇色 on 2016/12/5.
 */

public class Info_splb_2 {
    private int id;
    private String title;
    private String image;
    private String issue_time;
    private int state;
    private String price;

    public Info_splb_2(int id, String title, String image, String issue_time, int state, String price) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.issue_time = issue_time;
        this.state = state;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
