package com.truevalue.dreamappeal.bean;

public class BeanActionPostDetail {

    private boolean status;
    private int like_count;
    private int comment_count;
    private String register_date;
    private String tags;
    private String thumbnail_image;
    private String content;
    private int step_idx;
    private int object_idx;
    private int profile_idx;
    private int idx;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStep_idx() {
        return step_idx;
    }

    public void setStep_idx(int step_idx) {
        this.step_idx = step_idx;
    }

    public int getObject_idx() {
        return object_idx;
    }

    public void setObject_idx(int object_idx) {
        this.object_idx = object_idx;
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
}
