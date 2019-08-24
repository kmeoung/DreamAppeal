package com.truevalue.dreamappeal.fragment.image;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.bean.BeanGalleryInfo;
import com.truevalue.dreamappeal.bean.BeanGalleryInfoList;
import com.truevalue.dreamappeal.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentGallery extends BaseFragment {

    @BindView(R.id.iv_select_image)
    ImageView mIvSelectImage;
    @BindView(R.id.gv_gallery)
    GridView mGvGallery;
    @BindView(R.id.btn_resize)
    Button mBtnResize;
    @BindView(R.id.rl_select_image)
    RelativeLayout mRlSelectImage;

    private ArrayList<BeanGalleryInfo> mOldPath;
    private ArrayList<BeanGalleryInfo> mItemPath;
    private ArrayList<BeanGalleryInfo> mBucked;

    boolean isImageResize = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initAdapter();
    }

    private void initView() {
        // 화면 사이즈 가져오기
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int displayWidth = size.x;

        int viewWidth = displayWidth;

        int viewHeight = displayWidth;

        // 화면 사이즈
        ViewGroup.LayoutParams params = mRlSelectImage.getLayoutParams();
        params.width = viewWidth;
        params.height = viewHeight;
        mRlSelectImage.setLayoutParams(params);
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mOldPath = new ArrayList<>();
        mBucked = new ArrayList<>();
        mItemPath = new ArrayList<>();

        boolean firstImage = false;

        BeanGalleryInfoList beanGallery = Utils.getImageFilePath(getContext());
        ArrayList<BeanGalleryInfo> beanImageInfoList = beanGallery.getImageInfoList();
        ArrayList<String> bucketNameList = beanGallery.getBucketList();
        ArrayList<String> bucketIdList = beanGallery.getBucketIdList();

        ArrayList<String> strBucketNameList = new ArrayList<>();
        for (int i = 0; i < bucketNameList.size(); i++) {
            String title = bucketNameList.get(i);
            String id = bucketIdList.get(i);
            mBucked.add(new BeanGalleryInfo(title, id, null));
            strBucketNameList.add(title);
        }

        Spinner titleSpinner = ((ActivityGalleryCamera) getActivity()).getTitleSpinner();

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, strBucketNameList);
        titleSpinner.setAdapter(arrayAdapter);

        for (int i = 0; i < beanImageInfoList.size(); i++) {
            BeanGalleryInfo imageObject = beanImageInfoList.get(i);
            String imagePath = imageObject.getImagePath();
            String bucketId = imageObject.getBucketId();
            String bucketName = imageObject.getBucketName();
            mOldPath.add(new BeanGalleryInfo(bucketName, bucketId, imagePath));
            mItemPath.add(new BeanGalleryInfo(bucketName, bucketId, imagePath));

            if (!firstImage) {
                Glide.with(getContext())
                        .load(mItemPath.get(0).getImagePath())
                        .into(mIvSelectImage);
                firstImage = true;
            }
        }
        GridAdapter mGridAdapter = new GridAdapter(getContext(), mItemPath);
        mGvGallery.setAdapter(mGridAdapter);

        mGvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Glide.with(getContext())
                        .load(mItemPath.get(i).getImagePath())
                        .into(mIvSelectImage);
            }
        });

        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeanGalleryInfo bean = mBucked.get(position);
                mItemPath.clear();
                if (TextUtils.equals(bean.getBucketId(), "All")) {
                    mItemPath.addAll(mOldPath);
                } else {
                    for (int i = 0; i < mOldPath.size(); i++) {
                        BeanGalleryInfo oldBean = mOldPath.get(i);
                        if (TextUtils.equals(bean.getBucketId(), oldBean.getBucketId())) {
                            mItemPath.add(oldBean);
                        }
                    }
                }
                // 이미지뷰 초기화
                Glide.with(getContext())
                        .load(mItemPath.get(0).getImagePath())
                        .into(mIvSelectImage);

                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // TODO : 수정 필요
    @OnClick(R.id.btn_resize)
    public void onViewClicked() {
        if(!isImageResize) {
            isImageResize = true;
            mIvSelectImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            isImageResize = false;
            mIvSelectImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        mIvSelectImage.invalidate();
    }

    /**
     * GridAdapter
     */
    class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<BeanGalleryInfo> pictureList;
        private Context mContext;

        public GridAdapter(Context context, ArrayList<BeanGalleryInfo> list) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                convertView = inflater.inflate(R.layout.listitem_gallery, parent, false);
            }
            ImageView imageView = convertView.findViewById(R.id.iv_image);

            //onCreate에서 정해준 크기로 이미지를 붙인다.
            Glide.with(mContext)
                    .load(pictureList.get(position).getImagePath())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(imageView);

            return convertView;
        }
    }
}
