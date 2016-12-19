package com.bgs.jianbao12.bean;

import java.util.List;

/**
 * Created by 醇色 on 2016/12/5.
 */

public class Info_spxq_2 {
    private int id;
    private int user_id;
    private String title;
    private String description;
    private String price;
    private String contact;
    private String mobile;
    private String qq;
    private String wechat;
    private String email;
    private String issue_time;
    private String follow;
    private String state;
    private boolean owner;
    private boolean followed;
    private List<String> list;

    public Info_spxq_2() {
    }

    public Info_spxq_2(int id, int user_id, String title, String description, String price, String contact, String mobile, String qq, String wechat, String email, String issue_time, String follow, String state, boolean owner, boolean followed, List<String> list) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.contact = contact;
        this.mobile = mobile;
        this.qq = qq;
        this.wechat = wechat;
        this.email = email;
        this.issue_time = issue_time;
        this.follow = follow;
        this.state = state;
        this.owner = owner;
        this.followed = followed;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
