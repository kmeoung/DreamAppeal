package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDreamDescription extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;
    @BindView(R.id.et_dream_description)
    EditText mEtMyDreamKeyword;
    @BindView(R.id.et_dream_description_detail_1)
    EditText mEtDreamDetail1;
    @BindView(R.id.et_dream_description_detail_2)
    EditText mEtDreamDetail2;
    @BindView(R.id.et_dream_description_detail_3)
    EditText mEtDreamDetail3;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_description);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
    }

    @OnClick({R.id.iv_back, R.id.tv_text_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 뒤로가기
                finish();
                break;
            case R.id.tv_text_btn: // 확인
                onBackPressed(); // todo : 임시
                break;
        }
    }
}
