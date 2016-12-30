package com.bgs.jianbao12.bean;

import java.util.List;

/**
 * Created by 毛毛 on 2016/12/20.
 */

public class Goods_xiangqing_Info {

    /**
     * status : 200
     * info : 成功
     * data : {"id":48,"user_id":100032,"title":"654654","description":"15","price":"5","contact":"李女士","mobile":"15501274169","photos":[],"issue_time":"2016-12-14 02:23:12","follow":0,"state":0,"followed":false,"owned":true}
     */

    private String status;
    private String info;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 48
         * user_id : 100032
         * title : 654654
         * description : 15
         * price : 5
         * contact : 李女士
         * mobile : 15501274169
         * photos : []
         * issue_time : 2016-12-14 02:23:12
         * follow : 0
         * state : 0
         * followed : false
         * owned : true
         */

        private int id;
        private int user_id;
        private String title;
        private String description;
        private String price;
        private String mobile;
        private String issue_time;
        private String final_time;
        private String qq;
        private String head;
        private String contact;
        private String email;
        private String wechat;
        private int follow;
        private int state;
        private boolean followed;
        private boolean owned;
        private List<?> photos;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getFinal_time() {
            return final_time;
        }

        public void setFinal_time(String final_time) {
            this.final_time = final_time;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
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

        public String getIssue_time() {
            return issue_time;
        }

        public void setIssue_time(String issue_time) {
            this.issue_time = issue_time;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public boolean isOwned() {
            return owned;
        }

        public void setOwned(boolean owned) {
            this.owned = owned;
        }

        public List<?> getPhotos() {
            return photos;
        }

        public void setPhotos(List<?> photos) {
            this.photos = photos;
        }
    }
}
