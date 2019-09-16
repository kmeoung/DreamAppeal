package com.truevalue.dreamappeal.bean;

public class BeanSearchAppealer {
    private int idx;
    private String image;
    private String value_style;
    private String job;
    private String name;

    public BeanSearchAppealer(int idx, String image, String value_style, String job, String name) {
        this.idx = idx;
        this.image = image;
        this.value_style = value_style;
        this.job = job;
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
