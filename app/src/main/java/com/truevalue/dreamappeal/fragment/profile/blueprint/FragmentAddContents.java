package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.http.DreamAppealHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentAddContents extends BaseFragment implements IOBaseTitleBarListener {

    public static final int EXTRA_TYPE_OBJECTS = 0; // 실천 목표 추가
    public static final int EXTRA_TYPE_OBJECT_STEP = 1; // 실천 목표 세부단계 추가
    public static final int EXTRA_TYPE_EDIT_OBJECTS = 2; // 실천 목표 수정
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_ability_opportunity)
    EditText mEtAbilityOpportunity;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;

    private int mViewType = -1;
    private String mTitle = null;

    public static FragmentAddContents newInstance(String title, int view_type) {
        FragmentAddContents fragment = new FragmentAddContents();
        fragment.mTitle = title;
        fragment.mViewType = view_type;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contents, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        // 데이터 초기화
        initData();
        // View 초기화
        initVIew();
    }

    private void initData() {
        if (mViewType == EXTRA_TYPE_EDIT_OBJECTS) {

        }
        if (mTitle != null) mBtbBar.setTitle(mTitle);
    }

    private void initVIew() {
        if (mViewType == EXTRA_TYPE_OBJECTS) {
            mTvHint.setText("갖출 능력과 만들어갈 기회를 위해\n어떤 실천을 해볼까?");
        } else if (mViewType == EXTRA_TYPE_OBJECT_STEP) {
            mTvHint.setText("꾸준히 노력하기 쉽도록\n" + "어떤 단계로 나눠서 실천해볼까?");
        }
        // 처음 Hint 글자 안보이게 하고 Focus잡기
        mTvHint.setOnClickListener(v -> {
            mEtAbilityOpportunity.setFocusableInTouchMode(true);
            mEtAbilityOpportunity.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtAbilityOpportunity, 0);
            mTvHint.setVisibility(View.GONE);
        });
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        if (TextUtils.isEmpty(mEtAbilityOpportunity.getText())) {
            Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mViewType == EXTRA_TYPE_OBJECTS) {
            httpPostObjects();
        } else if (mViewType == EXTRA_TYPE_OBJECT_STEP) {

        }
    }

    /**
     * http Post
     * 실천 목표 추가
     */
    private void httpPostObjects() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap();
        body.put("object_name", mEtAbilityOpportunity.getText().toString());
        DreamAppealHttpClient client = DreamAppealHttpClient.getInstance();
        client.Post(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    getActivity().onBackPressed();
                }
            }
        });

    }
}
