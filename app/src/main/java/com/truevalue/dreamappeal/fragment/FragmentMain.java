package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.activity.ActivitySearch;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseMainTitleBar;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.profile.FragmentProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentMain extends BaseFragment implements IOBaseTitleBarListener {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // todo : 1차개발에만 설정
        // 상단바 설정
        mBtbBar.showToolbarBtn(BaseTitleBar.GONE, BaseTitleBar.VISIBLE, BaseTitleBar.VISIBLE, BaseTitleBar.GONE);
        replaceFragment(R.id.base_container, new FragmentProfile(), false);
        replaceFragment(R.id.drawer_container, new FragmentDrawer(), false);

        mBtbBar.setIOBaseTitleBarListener(this);
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
                Intent intent = new Intent(getContext(), ActivityGalleryCamera.class);
                intent.putExtra(ActivityGalleryCamera.VIEW_TYPE_ADD_ACTION_POST, "VIEW_TYPE_ADD_ACTION_POST");
                startActivity(intent);
                break;
            case R.id.iv_notification: // 알림
                break;
            case R.id.iv_profile: // 프로필
                // todo : 1차 출시에는 고정입니다
                // 드림프로필 Viewpager 초기화
                ((ActivityMain)getActivity()).setmProfileIndex(0);
                break;
        }
    }

    @Override
    public void OnClickMenu() {
        mDlDrawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickSearch() {
        Intent intent = new Intent(getContext(), ActivitySearch.class);
        startActivity(intent);
    }

    @Override
    public void OnClickRightTextBtn() {

    }
}
