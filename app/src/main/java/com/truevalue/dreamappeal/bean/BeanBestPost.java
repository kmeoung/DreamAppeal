package com.truevalue.dreamappeal.bean;

public class BeanBestPost {
    private int profile_idx;
    private int idx;
    private String title;

    public BeanBestPost(int profile_idx, int idx, String title) {
        this.profile_idx = profile_idx;
        this.idx = idx;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
