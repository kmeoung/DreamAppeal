package com.truevalue.dreamappeal.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

//    DpToPixel 코드

    public static int DpToPixel(Context context, float DP) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources()
                .getDisplayMetrics());
        return (int) px;
    }

    // 휴대전화 이미지 가져오기
    public static JSONObject getImageFilePath(Context context) {
        JSONObject object = new JSONObject();
        try {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String projection[] = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            int columnBucketID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int columnBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            JSONArray bucketNameList = new JSONArray();
            JSONArray bucketIdList = new JSONArray();
            JSONArray imageInfoList = new JSONArray();

            int lastIndex;
            while (cursor.moveToNext()) {
                String absolutePathOfImage = cursor.getString(columnIndex);
                String nameOfFile = cursor.getString(columnDisplayname);
                String bucketName = cursor.getString(columnBucketName);
                String bucketId = cursor.getString(columnBucketID);

                boolean equal = false;

                for (int i = 0; i < bucketNameList.length(); i++) {
                    String name = bucketNameList.getString(i);
                    if (TextUtils.equals(name, bucketName)) {
                        equal = true;
                    }
                }

                if (!equal) {
                    bucketNameList.put(bucketName);
                    bucketIdList.put(bucketId);
                }

                lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
                lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

                if (!TextUtils.isEmpty(absolutePathOfImage)) {

                    JSONObject imageObject = new JSONObject();

                    imageObject.put("image_path", absolutePathOfImage);
                    imageObject.put("image_bucket_name", bucketName);
                    imageObject.put("image_bucket_id", bucketId);

                    imageInfoList.put(imageObject);
                }
            }

            object.put("bucket_name_list", bucketNameList);
            object.put("bucket_id_list", bucketIdList);
            object.put("image_info", imageInfoList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
