package com.truevalue.dreamappeal.bean;

public class BeanLevelChoiceCategory {

    private String object_name;
    private int idx;

    public BeanLevelChoiceCategory(String object_name, int idx) {
        this.object_name = object_name;
        this.idx = idx;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
