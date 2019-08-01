package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.FragmentLogin;

public class ActivityLogin extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        replaceFragment(R.id.login_container,new FragmentLogin(),false);
    }


}
