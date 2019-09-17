package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.FragmentMain;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends BaseActivity implements IOBaseTitleBarListener {


    @BindView(R.id.v_status)
    View mVStatus;

    private int mProfileIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateStatusbarTranslateColor(mVStatus, R.color.colorAccent);
        replaceFragment(R.id.activity_container, new FragmentMain(), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void replaceFragment(Fragment fragment, boolean addToStack) {
        replaceFragment(R.id.activity_container, fragment, addToStack);
    }

    public void replaceFragmentRight(Fragment fragment, boolean addToStack) {
        replaceFragmentRight(R.id.activity_container, fragment, addToStack);
    }

    public void replaceFragmentLeft(Fragment fragment, boolean addToStack) {
        replaceFragmentLeft(R.id.activity_container, fragment, addToStack);
    }

    public int getmProfileIndex() {
        return mProfileIndex;
    }

    public void setmProfileIndex(int mProfileIndex) {
        this.mProfileIndex = mProfileIndex;
    }

    /**
     * Page 초기화
     */
    public void initPage(){
//        FragmentManager fm = getSupportFragmentManager();
//        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
        replaceFragment(R.id.activity_container, new FragmentMain(), false);
    }
}
