package com.truevalue.dreamappeal.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityGalleryCamera extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.sp_title)
    Spinner mSpTitle;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;
    @BindView(R.id.gv_gallery)
    GridView mGvGallery;

    private ArrayList<String> mImagePath;

    /**
     * 갤러리의 이미지가 있는 폴더 명 가져오기
     *
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
        setContentView(R.layout.activity_gallery_camera);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        mImagePath = new ArrayList<>();
//        mImagePath = Utils.getImageFilePath(ActivityGalleryCamera.this);
        JSONObject object = Utils.getImageFilePath(ActivityGalleryCamera.this);
        try {
            JSONArray imageInfo = object.getJSONArray("image_info");
            JSONArray buckedName = object.getJSONArray("bucket_name_list");
            JSONArray bucketId = object.getJSONArray("bucket_id_list");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.);

            for (int i = 0; i < buckedName.length(); i++) {
                String title = buckedName.getString(i);
            }

            for (int i = 0; i < imageInfo.length(); i++) {
                JSONObject imageObject = imageInfo.getJSONObject(i);
                String imagePath = imageObject.getString("image_path");
                mImagePath.add(imagePath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mGvGallery.setAdapter(new GridAdapter(ActivityGalleryCamera.this, mImagePath));
    }

    @OnClick({R.id.iv_back, R.id.tv_text_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_text_btn:
                break;
        }
    }

    class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<String> pictureList;
        private Context mContext;

        public GridAdapter(Context context, ArrayList<String> list) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pictureList = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return pictureList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listitem_achivement_list, parent, false);
            }
            ImageView imageView = convertView.findViewById(R.id.iv_achivement);

            //onCreate에서 정해준 크기로 이미지를 붙인다.
            Glide.with(mContext).load(pictureList.get(position)).into(imageView);

            return convertView;
        }
    }
}
