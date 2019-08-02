package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
