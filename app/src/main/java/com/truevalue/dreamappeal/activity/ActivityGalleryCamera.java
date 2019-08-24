package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.image.FragmentCamera;
import com.truevalue.dreamappeal.fragment.image.FragmentGallery;

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
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;
    @BindView(R.id.tv_gallery)
    TextView mTvGallery;
    @BindView(R.id.tv_camera)
    TextView mTvCamera;

    public static boolean IsCamera = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);

        initView();
    }

    private void initView() {
        replaceFragment(R.id.base_container, new FragmentGallery(), false);
        setBottomBar();
    }

    private void setBottomBar(){
        if(!IsCamera){ // 갤러리
            mTvGallery.setSelected(true);
            mTvCamera.setSelected(false);
        }else{
            mTvGallery.setSelected(false);
            mTvCamera.setSelected(true);
        }
    }

    public Spinner getTitleSpinner() {
        return mSpTitle;
    }


    @OnClick({R.id.iv_back, R.id.tv_text_btn,R.id.tv_gallery,R.id.tv_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_text_btn:
                break;
            case R.id.tv_gallery:
                if(IsCamera) replaceFragmentLeft(R.id.base_container, new FragmentGallery(), false);
                IsCamera = false;
                setBottomBar();
                break;
            case R.id.tv_camera:
                if(!IsCamera) replaceFragmentRight(R.id.base_container, new FragmentCamera(), false);
                IsCamera = true;
                setBottomBar();
                break;
        }
    }
}
