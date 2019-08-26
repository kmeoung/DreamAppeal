package com.truevalue.dreamappeal.activity.profile;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityBestAchivementDetail extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_achivement_title)
    TextView mTvAchivementTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
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
        setContentView(R.layout.activity_best_achivement_post_detail);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        initView();
    }

    private void initView() {
        Point size = Utils.getDisplaySize(this);
        Utils.setResizeView(mIvImg, size.x, size.x);
    }

    @OnClick({R.id.iv_back, R.id.ll_comment, R.id.ll_cheering})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 뒤로가기
                finish();
                break;
            case R.id.ll_comment: // 댓글
                Intent intent = new Intent(ActivityBestAchivementDetail.this,ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering: // 응원하기
                break;
        }
    }
}
