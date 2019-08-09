package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityIntro extends BaseActivity {

    private static final int DELAY = 1000 * 1;
    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ActivityIntro.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0, DELAY);
    }
}