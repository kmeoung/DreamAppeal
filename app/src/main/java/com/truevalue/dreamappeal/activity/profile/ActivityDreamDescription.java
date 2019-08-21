package com.truevalue.dreamappeal.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDreamDescription extends BaseActivity implements IOBaseTitleBarListener {


    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_dream_description)
    EditText mEtDreamDescription;
    @BindView(R.id.et_dream_description_detail_1)
    EditText mEtDreamDescriptionDetail1;
    @BindView(R.id.et_dream_description_detail_2)
    EditText mEtDreamDescriptionDetail2;
    @BindView(R.id.et_dream_description_detail_3)
    EditText mEtDreamDescriptionDetail3;
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
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        finish();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        onBackPressed();
    }
}