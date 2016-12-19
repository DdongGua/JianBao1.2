package com.bgs.jianbao12.bean;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Info_Collection {
    private int id;//编号
    private String  title;//标题
    private String image;//照片
    private String price;//价格
    private String  issue_time;//发布时间
    private int state;//状态

    public Info_Collection(int id, String title, String image, String price, String issue_time, int state) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.issue_time = issue_time;
        this.state = state;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public void setStats(int state) {
        this.state = state;
    }
}
