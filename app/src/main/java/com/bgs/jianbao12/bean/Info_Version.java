package com.bgs.jianbao12.bean;

/**
 * Created by HaiBo on 2016/12/8.
 */

public class Info_Version {
    private String ver;
    private String url;
    private int date;
     private String notes;

    public Info_Version(String ver, String url, int date, String notes) {
        this.ver = ver;
        this.url = url;
        this.date = date;
        this.notes = notes;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
