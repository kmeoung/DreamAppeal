package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.profile.FragmentAddActionPost;
import com.truevalue.dreamappeal.fragment.profile.FragmentLevelChoice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddActionPost extends BaseActivity {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action_post);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);

        replaceFragment(R.id.base_container, new FragmentAddActionPost(), false);
    }

    public void replaceFragmentRight(Fragment fragment, boolean addToBack){
        replaceFragmentRight(R.id.base_container,fragment,addToBack);
    }

    public BaseTitleBar getmBtbBar() {
        return mBtbBar;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
