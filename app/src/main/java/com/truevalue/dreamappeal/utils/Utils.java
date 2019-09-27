package com.truevalue.dreamappeal.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.bean.BeanGalleryInfo;
import com.truevalue.dreamappeal.bean.BeanGalleryInfoList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
     * image RealPath
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
////            String[] proj = {MediaStore.Video.Media.DATA};
//            cursor = context.getContentResolver().query(contentUri, null, null, null, null);
//            cursor.moveToFirst();
//            String document_id = cursor.getString(0);
//
//            cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String id = cursor.getString(0);
//
//                    if (document_id.contains(id)) {
//                        break;
//                    }
//                } while (cursor.moveToNext());
//
//            }
//
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
        String result = null;

        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentUri.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;

    }

    /**
     * 비트맵 파일 변환
     *
     * @param bitmap
     * @param strFilePath
     * @param filename
     */
    public static File SaveBitmapToFileCache(Bitmap bitmap, String strFilePath,
                                             String filename) {

        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
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
     *
     * @param activity
     * @return
     */
    public static Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Resize View
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setResizeView(View view, int width, int height) {
        // 화면 사이즈
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public static int getStatusBarHeight(Context context) {
        int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusHeight = context.getResources().getDimensionPixelSize(id);
        return statusHeight;
    }

    public static HashMap<String, String> getHttpHeader(String token) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        return header;
    }

    /**
     * SNS 더보기 설정
     *
     * @param view
     * @param text
     * @param maxLine
     */
    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = " ... 더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능
        view.post(new Runnable() { //getLineCount()는 UI 백그라운드에서만 가져올수 있음
            @Override
            public void run() {
                if (view.getLineCount() >= maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                            } else {
                                lessText += item + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.tab_none_select_gray));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }

    public static String convertFromDate(String strPostDate) {

        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy. MM. dd");
        Calendar nowCal = Calendar.getInstance();

        int nowHour = nowCal.get(Calendar.HOUR);
        int nowMinute = nowCal.get(Calendar.MINUTE);
        int nowSeconds = nowCal.get(Calendar.SECOND);
        try {
            Date nowDate = sdf2.parse(sdf.format(nowCal.getTime()));

            nowCal.setTime(sdf.parse(strPostDate));

            Date postDate = sdf2.parse(sdf.format(nowCal.getTime()));

            if (nowDate.compareTo(postDate) > 0) {
                strDate = sdf2.format(postDate);
            } else {

                int postHour = nowCal.get(Calendar.HOUR);
                int postMinute = nowCal.get(Calendar.MINUTE);
                int postSeconds = nowCal.get(Calendar.SECOND);

                if (postHour < nowHour) {
                    strDate = String.format("%d시간전", nowHour - postHour);
                } else {
                    if (postMinute < nowMinute) {
                        strDate = String.format("%d분전", nowMinute - postMinute);
                    } else {
                        strDate = String.format("%d초전", nowSeconds - postSeconds);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDate;
    }
}
