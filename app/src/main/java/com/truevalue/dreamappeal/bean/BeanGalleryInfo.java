package com.truevalue.dreamappeal.bean;

import java.util.ArrayList;

public class BeanGalleryInfo {
    private String bucketName;
    private String bucketId;
    private String imagePath;

    public BeanGalleryInfo(String bucketName, String bucketId, String imagePath) {
        this.bucketName = bucketName;
        this.bucketId = bucketId;
        this.imagePath = imagePath;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
