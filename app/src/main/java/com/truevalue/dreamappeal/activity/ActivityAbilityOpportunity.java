package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.FragmentAbilityOpportunity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAbilityOpportunity extends BaseActivity implements IOBaseTitleBarListener {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability_opportunity);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        replaceFragment(R.id.base_container, new FragmentAbilityOpportunity(), false);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     *
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isTextBtn
     * @param title
     * @param rightText
     */
    public void showToolbarBtn(String isBack, String isMenu, String isSearch, String isTextBtn, String title, String rightText) {
        mBtbBar.showToolbarBtn(isBack, isMenu, isSearch, isTextBtn, title, rightText);
    }

    /**
     * Fragment 에서 변환하도록 설정
     *
     * @param fragment
     * @param addToStack
     */
    public void replaceFragment(BaseFragment fragment, boolean addToStack) {
        replaceFragmentRight(R.id.base_container, fragment, addToStack);
    }

    @Override
    public void OnClickBack() {
        onBackPressed();
    }
}
