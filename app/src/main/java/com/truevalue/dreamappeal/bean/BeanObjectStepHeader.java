package com.truevalue.dreamappeal.bean;

public class BeanObjectStepHeader {

    private String complete_date;
    private String complete;
    private String thumbnail_image;
    private String object_name;
    private int profile_idx;
    private int idx;
    private int total_action_post_count = -1;

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public void setProfile_idx(int profile_idx) {
        this.profile_idx = profile_idx;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getTotal_action_post_count() {
        return this.total_action_post_count;
    }

    public void setTotal_action_post_count(int total_action_post_count) {
        this.total_action_post_count = total_action_post_count;
    }
}
