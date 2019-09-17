package com.truevalue.dreamappeal.bean;

public abstract class BeanComment {
    // comment_idx, tmp 는 Query 문 조회할 때 쓰는 임시변수니까 신경 쓰지 마세요
// comment_idx, tmp -> 서버 안정화 작업하면서 삭제될 수 있음

    private String image;
    private String job;
    private String value_style;
    private int tmp;
    private String register_date;
    private int like_count;
    private String content;
    private String parent_idx;
    private int writer_idx;
    private int profile_idx;
    private int idx;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getValue_style() {
        return value_style;
    }

    public void setValue_style(String value_style) {
        this.value_style = value_style;
    }

    public int getTmp() {
        return tmp;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParent_idx() {
        return parent_idx;
    }

    public void setParent_idx(String parent_idx) {
        this.parent_idx = parent_idx;
    }

    public int getWriter_idx() {
        return writer_idx;
    }

    public void setWriter_idx(int writer_idx) {
        this.writer_idx = writer_idx;
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
