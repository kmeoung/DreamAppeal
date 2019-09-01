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
//    private JSONArray description_spec;
    private String meritNmotive;
    private int achievement_post_count;
    private int action_post_count;

    public BeanProfilesIndex(int idx, int user_idx, int number, int level, int point, String image, String value_style, String job, String description, String meritNmotive, int achievement_post_count, int action_post_count) {
        this.idx = idx;
        this.user_idx = user_idx;
        this.number = number;
        this.level = level;
        this.point = point;
        this.image = image;
        this.value_style = value_style;
        this.job = job;
        this.description = description;
        this.meritNmotive = meritNmotive;
        this.achievement_post_count = achievement_post_count;
        this.action_post_count = action_post_count;
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
