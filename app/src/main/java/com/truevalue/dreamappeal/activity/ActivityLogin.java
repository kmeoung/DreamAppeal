package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.login.FragmentLogin;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLogin extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.login_container)
    FrameLayout mLoginContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate();
        replaceFragment(R.id.login_container, new FragmentLogin(), false);
    }
}
