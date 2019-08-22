package com.truevalue.dreamappeal.bean;

import java.util.ArrayList;

public class BeanGalleryInfoList {

    private ArrayList<String> bucketList;
    private ArrayList<String> bucketIdList;
    private ArrayList<BeanGalleryInfo> imageInfoList;

    public ArrayList<String> getBucketList() {
        return bucketList;
    }

    public void setBucketList(ArrayList<String> bucketList) {
        this.bucketList = bucketList;
    }

    public ArrayList<String> getBucketIdList() {
        return bucketIdList;
    }

    public void setBucketIdList(ArrayList<String> bucketIdList) {
        this.bucketIdList = bucketIdList;
    }

    public ArrayList<BeanGalleryInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(ArrayList<BeanGalleryInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }
}
