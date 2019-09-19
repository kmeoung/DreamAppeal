package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseMainTitleBar;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.fragment.profile.FragmentProfile;
import com.truevalue.dreamappeal.activity.ActivitySearch;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class FragmentMain extends BaseFragment implements IOBaseTitleBarListener {

    private final int REQUEST_SEARCH = 111;

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

        mDlDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // 메뉴 열릴 때 마다 데이터 초기화
                httpGetUser();
            }
        });
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
                Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                if(prefs.getMyProfileIndex() != prefs.getProfileIndex())
                    prefs.setProfileIndex(prefs.getMyProfileIndex(),true);
                ((ActivityMain) getActivity()).setmProfileIndex(0);
                ((ActivityMain) getActivity()).initPage();
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
        Intent intent = new Intent(getContext(),ActivitySearch.class);
        startActivityForResult(intent,REQUEST_SEARCH);
    }

    /**
     * http Get
     * 회원 정보 가져오기
     */
    private void httpGetUser() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_USERS_INDEX
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance().Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    String name = json.getString("name");
                    mBtbBar.setTitle(name + " 드림프로필");
                    // todo : Drawer 설정 필요
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_SEARCH){
                ((ActivityMain) getActivity()).setmProfileIndex(0);
                ((ActivityMain) getActivity()).initPage();
            }
        }
    }
}