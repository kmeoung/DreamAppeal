package com.truevalue.dreamappeal.bean;

import org.json.JSONArray;

public class BeanProfilesIndex {

    private int idx;
    private int user_idx;
    private int number;
    private int level;
    private int point;
    private String image;
    private String value_style;
    private String job;
    private String description;
    private String name;
//    private JSONArray description_spec;
    private String meritNmotive;
    private int achievement_post_count;
    private int action_post_count;
    private int comment_count;
    private int like_count;
    private boolean status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(int user_idx) {
        this.user_idx = user_idx;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue_style() {
        return value_style;
    }

    public void setValue_style(String value_style) {
        this.value_style = value_style;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeritNmotive() {
        return meritNmotive;
    }

    public void setMeritNmotive(String meritNmotive) {
        this.meritNmotive = meritNmotive;
    }

    public int getAchievement_post_count() {
        return achievement_post_count;
    }

    public void setAchievement_post_count(int achievement_post_count) {
        this.achievement_post_count = achievement_post_count;
    }

    public int getAction_post_count() {
        return action_post_count;
    }

    public void setAction_post_count(int action_post_count) {
        this.action_post_count = action_post_count;
    }
}
