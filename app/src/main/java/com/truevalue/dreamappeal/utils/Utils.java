package com.truevalue.dreamappeal.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.truevalue.dreamappeal.bean.BeanGalleryInfo;
import com.truevalue.dreamappeal.bean.BeanGalleryInfoList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * DpToPixel 코드
     *
     * @param context
     * @param DP
     * @return
     */
    public static int DpToPixel(Context context, float DP) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources()
                .getDisplayMetrics());
        return (int) px;
    }

    /**
     * 휴대전화 이미지 가져오기
     *
     * @param context
     * @return
     */
    public static BeanGalleryInfoList getImageFilePath(Context context) {
        BeanGalleryInfoList bean = new BeanGalleryInfoList();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String projection[] = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int columnBucketID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        int columnBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        ArrayList<String> bucketNameList = new ArrayList();
        ArrayList<String> bucketIdList = new ArrayList();
        ArrayList<BeanGalleryInfo> imageInfoList = new ArrayList();

        // init 설정
        bucketNameList.add("All");
        bucketIdList.add("All");

        int lastIndex;
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            String bucketName = cursor.getString(columnBucketName);
            String bucketId = cursor.getString(columnBucketID);

            boolean equal = false;

            for (int i = 0; i < bucketNameList.size(); i++) {
                String name = bucketNameList.get(i);
                if (TextUtils.equals(name, bucketName)) {
                    equal = true;
                }
            }

            if (!equal) {
                bucketNameList.add(bucketName);
                bucketIdList.add(bucketId);
            }

            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage)) {

                BeanGalleryInfo info = new BeanGalleryInfo();

                info.setImagePath(absolutePathOfImage);
                info.setBucketName(bucketName);
                info.setBucketId(bucketId);

                imageInfoList.add(info);
            }
        }

        bean.setBucketList(bucketNameList);
        bean.setBucketIdList(bucketIdList);
        bean.setImageInfoList(imageInfoList);

        return bean;
    }

    /**
     * 문자열이 Email 방식인지 인지 확인
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Display Size 가져오기
     * @param activity
     * @return
     */
    public static Point getDisplaySize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Resize View
     * @param view
     * @param width
     * @param height
     */
    public static void setResizeView(View view,int width,int height){
        // 화면 사이즈
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public static int getStatusBarHeight(Context context){
        int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusHeight = context.getResources().getDimensionPixelSize(id);
        return statusHeight;
    }

    public static HashMap<String,String> getHttpHeader(String token){
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        return header;
    }
}
