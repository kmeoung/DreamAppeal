package com.truevalue.dreamappeal.bean;

public class BeanBlueprintObject {
    private int idx;
    private int profile_idx;
    private String object_name;
    private String thumbnail_image;
    private int complete; // 0 아님 1
    private String complete_date;
    private int total_action_post_count;

    public BeanBlueprintObject(int idx, int profile_idx, String object_name, String thumbnail_image, int complete, String complete_date, int total_action_post_count) {
        this.idx = idx;
        this.profile_idx = profile_idx;
        this.object_name = object_name;
        this.thumbnail_image = thumbnail_image;
        this.complete = complete;
        this.complete_date = complete_date;
        this.total_action_post_count = total_action_post_count;
    }

    public int getTotal_action_post_count() {
        return total_action_post_count;
    }

    public void setTotal_action_post_count(int total_action_post_count) {
        this.total_action_post_count = total_action_post_count;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public void setProfile_idx(int profile_idx) {
        this.profile_idx = profile_idx;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }
}
