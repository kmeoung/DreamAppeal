package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.FragmentDrawer;
import com.truevalue.dreamappeal.fragment.FragmentProfile;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMain extends BaseActivity {

    public static final int TYPE_HOME = 0;
    public static final int TYPE_TIMELINE = 1;
    public static final int TYPE_ADD_BOARD = 2;
    public static final int TYPE_NOTIFICATION = 3;
    public static final int TYPE_PROFILE = 4;


    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
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
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        replaceFragment(R.id.base_container, new FragmentProfile(), false);
        replaceFragment(R.id.drawer_container, new FragmentDrawer(), false);
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
        mTvTitle.setText(title);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isTextBtn
     */
    public void showToolbarBtn(boolean isBack,boolean isMenu,boolean isSearch,boolean isTextBtn){
        if(isBack) mIvBack.setVisibility(View.VISIBLE);
        else mIvBack.setVisibility(View.GONE);

        if(isMenu) mIvMenu.setVisibility(View.VISIBLE);
        else mIvMenu.setVisibility(View.GONE);

        if(isSearch) mIvSearch.setVisibility(View.VISIBLE);
        else mIvSearch.setVisibility(View.GONE);

        if(isTextBtn) mTvTextBtn.setVisibility(View.VISIBLE);
        else mTvTextBtn.setVisibility(View.GONE);
    }

    /**
     * Activity Main View replace
     * @param fragment
     * @param addToBackstack
     */
    public void replaceFragmentRight(Fragment fragment,boolean addToBackstack){
        replaceFragment(R.id.base_container,fragment,addToBackstack);
    }

    @OnClick({R.id.iv_back,R.id.iv_menu,R.id.iv_search,R.id.tv_text_btn,R.id.iv_home, R.id.iv_timeline, R.id.iv_add_board, R.id.iv_notification, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 뒤로가기 버튼
                onBackPressed();
                break;
            case R.id.iv_menu: // 메뉴 버튼
                mDlDrawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_search: // 검색 버튼

                break;
            case R.id.tv_text_btn: // 오른쪽 텍스트 버튼

                break;
            case R.id.iv_home: // 홈
                break;
            case R.id.iv_timeline: // 타임라인
                break;
            case R.id.iv_add_board: // 게시글 추가
                break;
            case R.id.iv_notification: // 알림
                break;
            case R.id.iv_profile: // 프로필
                break;
        }
    }
}
