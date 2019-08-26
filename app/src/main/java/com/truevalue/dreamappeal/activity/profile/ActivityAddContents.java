package com.truevalue.dreamappeal.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddContents extends BaseActivity implements IOBaseTitleBarListener {

    public static final String STR_TITLE = "STR_TITLE";

    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_ability_opportunity)
    EditText mEtAbilityOpportunity;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.v_status)
    View mVStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contents);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initVIew();
    }

    private void initVIew(){
        String title = getIntent().getStringExtra(STR_TITLE);
        mBtbBar.setTitle(title);
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {

    }
}
