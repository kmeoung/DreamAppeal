package com.truevalue.dreamappeal.bean;

public class BeanAchivementPostMain {

//    profile_idx: number,
//    idx: number,
//    title: string,
//    content: string,
//    thumbnail_image: string,
//    register_date: string,
//    tags: string,

    private int profile_idx;
    private int idx;
    private String title;
    private String content;
    private String thumbnail_image;
    private String register_date;
    private String tags;

    public BeanAchivementPostMain(int profile_idx, int idx, String title, String content, String thumbnail_image, String register_date, String tags) {
        this.profile_idx = profile_idx;
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.thumbnail_image = thumbnail_image;
        this.register_date = register_date;
        this.tags = tags;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public void setProfile_idx(int profile_idx) {
        this.profile_idx = profile_idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
