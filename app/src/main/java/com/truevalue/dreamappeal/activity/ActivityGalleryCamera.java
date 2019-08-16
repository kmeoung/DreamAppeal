package com.truevalue.dreamappeal.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;

public class ActivityGalleryCamera extends BaseActivity {

    /**
     * 갤러리의 이미지가 있는 폴더 명 가져오기
     * @return
     */
    private ArrayList<String> getBucketNames() {
        ArrayList<String> folderLists = new ArrayList<>();
        String[] projection = new String[]{"DISTINCT " + MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            String bucket;
            int bucketColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

            do {
                bucket = cursor.getString(bucketColumn);
                Log.e("folderName", bucket);
                folderLists.add(bucket);
            } while (cursor.moveToNext());
        }
        return folderLists;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
