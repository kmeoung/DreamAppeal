package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.image.FragmentCamera;
import com.truevalue.dreamappeal.fragment.image.FragmentGallery;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityGalleryCamera extends BaseActivity implements LifecycleOwner {

    public static final String VIEW_TYPE_ADD_ACTION_POST = "VIEW_TYPE_ADD_ACTION_POST";

    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.vp_viewpager)
    ViewPager mVpViewpager;
    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.sp_title)
    Spinner mSpTitle;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;

    private ArrayList<String> mTabList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);


        initView();
        initTab();
        initAdapter();
    }

    private void initView(){
        if(getIntent().getStringExtra(VIEW_TYPE_ADD_ACTION_POST) != null){
            mTvTextBtn.setText("다음");

            mTvTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityGalleryCamera.this,ActivityAddActionPost.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }
    }


    public Spinner getTitleSpinner() {
        return mSpTitle;
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

    /**
     * 어댑터 초기화
     */
    private void initAdapter() {
        mTlTab.setupWithViewPager(mVpViewpager);
        mVpViewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        for (int i = 0; i < mTabList.size(); i++) {
            mTlTab.getTabAt(i).setText(mTabList.get(i));
        }
        mVpViewpager.setOffscreenPageLimit(2);
    }

    /**
     * 탭 초기화
     */
    private void initTab() {
        mTabList = new ArrayList<>();
        mTabList.add("갤러리");
        mTabList.add("카메라");
    }

    /**
     * ViewPager Adapter
     * TODO : 사진 찍고 난 후 설정이 필요 , 사진찍고 난 후 갤러리 refresh도 해줘야 함
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // 내 꿈 소개
                    return new FragmentGallery();
                case 1: // 실현 성과
                    return new FragmentCamera();
            }

            return null;
        }

        @Override
        public int getCount() {
            return mTabList.size(); // 페이지 2개 고정
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position);
        }
    }
}
