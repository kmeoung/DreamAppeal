package com.truevalue.dreamappeal.bean;

public class BeanDreamList {

    private int idx;
    private int level;
    private int point;
    private String image;
    private String value_style;
    private String job;
    private int achievement_post_count;
    private int action_post_count;

    public BeanDreamList(int idx, int level, int point, String image, String value_style, String job, int achievement_post_count, int action_post_count) {
        this.idx = idx;
        this.level = level;
        this.point = point;
        this.image = image;
        this.value_style = value_style;
        this.job = job;
        this.achievement_post_count = achievement_post_count;
        this.action_post_count = action_post_count;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
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
