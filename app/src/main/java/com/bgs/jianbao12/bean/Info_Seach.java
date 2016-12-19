package com.bgs.jianbao12.bean;

/**
 * Created by Administrator on 2016/12/6 0006.
 */

public class Info_Seach {

    private int  id;
    private String title;
    private String imgstr;
    private String price;
    private String time;
    private int state;

    public Info_Seach(int id, String title , String imgstr, String price, String time, int state) {
        super();
        this.id=id;
        this.title=title;
        this.price=price;
        this.imgstr=imgstr;
        this.time=time;
        this.state=state;
    }

    public String getImgstr() {
        return imgstr;
    }
    public void setImgstr(String imgstr) {
        this.imgstr = imgstr;
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
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }


}
