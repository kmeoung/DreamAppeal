package com.truevalue.dreamappeal.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityActionPost extends BaseActivity implements IOBaseTitleBarListener {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.iv_dream_profile)
    ImageView mIvDreamProfile;
    @BindView(R.id.tv_value_style)
    TextView mTvValueStyle;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.ll_dream_title)
    LinearLayout mLlDreamTitle;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.iv_cheering)
    ImageView mIvCheering;
    @BindView(R.id.tv_cheering)
    TextView mTvCheering;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_cheering)
    LinearLayout mLlCheering;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_post);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
//        initView();
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @OnClick({R.id.ll_comment, R.id.ll_cheering})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_comment:
                Intent intent = new Intent(ActivityActionPost.this,ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering:
                break;
        }
    }
}
