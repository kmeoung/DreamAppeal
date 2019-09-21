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
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.fragment.profile.FragmentAddActionPost;
import com.truevalue.dreamappeal.fragment.profile.FragmentLevelChoice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddActionPost extends BaseActivity {

    public static final int TYPE_ADD_ACTION_POST = 0;
    public static final int TYPE_EDIT_ACTION_POST = 1;
    public static final int TYPE_RESET_LEVEL = 2;

    public static final String EXTRA_ACTION_POST_TYPE = "EXTRA_ACTION_POST_TYPE";
    public static final String EXTRA_ACTION_POST_INDEX = "EXTRA_ACTION_POST_INDEX";
    public static final String EXTRA_ACTION_POST_CONTENTS = "EXTRA_ACTION_POST_CONTENTS";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;

    private int mExtraType = -1;
    private int mPostIndex = -1;
    private BeanActionPostDetail mBean = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action_post);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // init Data
        initData();
        // View init
        initView();
    }

    private void initData() {
        mExtraType = getIntent().getIntExtra(EXTRA_ACTION_POST_TYPE, -1);
        if (mExtraType == TYPE_EDIT_ACTION_POST || mExtraType == TYPE_RESET_LEVEL) {
            mPostIndex = getIntent().getIntExtra(EXTRA_ACTION_POST_INDEX, -1);
            if (getIntent().getSerializableExtra(EXTRA_ACTION_POST_CONTENTS) != null) {
                mBean = (BeanActionPostDetail) getIntent().getSerializableExtra(EXTRA_ACTION_POST_CONTENTS);
            }
        }
    }

    private void initView() {
        switch (mExtraType) {
            case TYPE_ADD_ACTION_POST:
                replaceFragment(R.id.base_container, new FragmentAddActionPost(), false);
                break;
            case TYPE_EDIT_ACTION_POST:
                replaceFragment(R.id.base_container, FragmentAddActionPost.newInstance(mPostIndex, mBean), false);
                break;
            case TYPE_RESET_LEVEL:
                replaceFragment(R.id.base_container, FragmentLevelChoice.newInstance(mPostIndex, mBean), false);
                break;
        }
    }

    public void replaceFragmentRight(Fragment fragment, boolean addToBack) {
        replaceFragmentRight(R.id.base_container, fragment, addToBack);
    }

    public BaseTitleBar getmBtbBar() {
        return mBtbBar;
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
        overridePendingTransition(0, 0);
    }

    public int getmExtraType() {
        return mExtraType;
    }
}
