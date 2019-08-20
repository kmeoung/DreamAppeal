package com.truevalue.dreamappeal.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.truevalue.dreamappeal.bean.BeanGalleryInfo;
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

    private ArrayList<BeanGalleryInfo> mOldPath;
    private ArrayList<BeanGalleryInfo> mItemPath;
    private ArrayList<BeanGalleryInfo> mBucked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        mOldPath = new ArrayList<>();
        mBucked = new ArrayList<>();
        mItemPath = new ArrayList<>();


//        mImagePath = Utils.getImageFilePath(ActivityGalleryCamera.this);
        JSONObject object = Utils.getImageFilePath(ActivityGalleryCamera.this);
        try {
            JSONArray imageInfo = object.getJSONArray("image_info");
            JSONArray bucketNameList = object.getJSONArray("bucket_name_list");
            JSONArray bucketIdList = object.getJSONArray("bucket_id_list");

            ArrayList<String> strBucketNameList = new ArrayList<>();
            for (int i = 0; i < bucketNameList.length(); i++) {
                String title = bucketNameList.getString(i);
                String id = bucketIdList.getString(i);
                mBucked.add(new BeanGalleryInfo(title, id, null));
                strBucketNameList.add(title);
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, strBucketNameList);
            mSpTitle.setAdapter(arrayAdapter);

            for (int i = 0; i < imageInfo.length(); i++) {
                JSONObject imageObject = imageInfo.getJSONObject(i);
                String imagePath = imageObject.getString("image_path");
                String bucketId = imageObject.getString("image_bucket_id");
                String bucketName = imageObject.getString("image_bucket_name");
                mOldPath.add(new BeanGalleryInfo(bucketName, bucketId, imagePath));
                mItemPath.add(new BeanGalleryInfo(bucketName, bucketId, imagePath));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GridAdapter mGridAdapter = new GridAdapter(ActivityGalleryCamera.this, mItemPath);
        mGvGallery.setAdapter(mGridAdapter);

        mSpTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeanGalleryInfo bean = mBucked.get(position);
                mItemPath.clear();
                for (int i = 0; i < mOldPath.size(); i++) {
                    BeanGalleryInfo oldBean = mOldPath.get(i);
                    if (TextUtils.equals(bean.getBucketId(), oldBean.getBucketId())) {
                        mItemPath.add(oldBean);
                    }
                }
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        private ArrayList<BeanGalleryInfo> pictureList;
        private Context mContext;

        public GridAdapter(Context context, ArrayList<BeanGalleryInfo> list) {
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
            Glide.with(mContext).load(pictureList.get(position).getImagePath()).into(imageView);

            return convertView;
        }
    }
}
