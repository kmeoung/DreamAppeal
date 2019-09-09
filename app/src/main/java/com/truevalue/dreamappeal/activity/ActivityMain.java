package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseMainTitleBar;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.profile.FragmentDrawer;
import com.truevalue.dreamappeal.fragment.profile.FragmentProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMain extends BaseActivity implements IOBaseTitleBarListener {

    public static final int TYPE_HOME = 0;
    public static final int TYPE_TIMELINE = 1;
    public static final int TYPE_ADD_BOARD = 2;
    public static final int TYPE_NOTIFICATION = 3;
    public static final int TYPE_PROFILE = 4;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseMainTitleBar mBtbBar;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;
    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.iv_timeline)
    ImageView mIvTimeline;
    @BindView(R.id.iv_add_board)
    ImageView mIvAddBoard;
    @BindView(R.id.iv_notification)
    ImageView mIvNotification;
    @BindView(R.id.iv_profile)
    ImageView mIvProfile;
    @BindView(R.id.drawer_container)
    LinearLayout mDrawerContainer;
    @BindView(R.id.dl_drawer)
    DrawerLayout mDlDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        replaceFragment(R.id.base_container, new FragmentProfile(), false);
        replaceFragment(R.id.drawer_container, new FragmentDrawer(), false);
        mBtbBar.setIOBaseTitleBarListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 상단 바 Title 설정
     *
     * @param title
     */
    public void setTitle(String title) {
        mBtbBar.setTitle(title);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     *
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isTextBtn
     */
    public void showToolbarBtn(String isBack, String isMenu, String isSearch, String isTextBtn) {
        mBtbBar.showToolbarBtn(isBack, isMenu, isSearch, isTextBtn);
    }

    /**
     * Activity Main View replace
     *
     * @param fragment
     * @param addToBackstack
     */
    public void replaceFragmentRight(Fragment fragment, boolean addToBackstack) {
        replaceFragment(R.id.base_container, fragment, addToBackstack);
    }

    @OnClick({R.id.iv_home, R.id.iv_timeline, R.id.iv_add_board, R.id.iv_notification, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home: // 홈
                break;
            case R.id.iv_timeline: // 타임라인
                break;
            case R.id.iv_add_board: // 게시글 추가
                Intent intent = new Intent(ActivityMain.this,ActivityGalleryCamera.class);
                intent.putExtra(ActivityGalleryCamera.VIEW_TYPE_ADD_ACTION_POST,"VIEW_TYPE_ADD_ACTION_POST");
                startActivity(intent);
                break;
            case R.id.iv_notification: // 알림
                break;
            case R.id.iv_profile: // 프로필
                break;
        }
    }

    @Override
    public void OnClickMenu() {
        mDlDrawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void OnClickBack() {
        onBackPressed();
    }

    @Override
    public void OnClickSearch() {
        Intent intent = new Intent(ActivityMain.this,ActivitySearch.class);
        startActivity(intent);
    }

    @Override
    public void OnClickRightTextBtn() {

    }
}
