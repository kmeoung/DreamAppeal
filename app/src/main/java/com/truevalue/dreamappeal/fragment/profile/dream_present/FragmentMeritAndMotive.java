package com.truevalue.dreamappeal.fragment.profile.dream_present;

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
import com.truevalue.dreamappeal.http.DAHttpClient;
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

public class FragmentMeritAndMotive extends BaseFragment implements IOBaseTitleBarListener {

    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_merit_and_motive)
    EditText mEtMeritAndMotive;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    private String mMeritAndMotive = null;

    public static FragmentMeritAndMotive newInstance(String merit_motive) {
        FragmentMeritAndMotive fragment = new FragmentMeritAndMotive();
        fragment.mMeritAndMotive = merit_motive;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merit_and_motive, container, false);
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
        // 뷰 초기화
        initView();
    }

    private void initData() {
        if (mMeritAndMotive != null) {
            if (!TextUtils.isEmpty(mMeritAndMotive)) {
                mEtMeritAndMotive.setText(mMeritAndMotive);
                mTvHint.setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        // 처음 Hint 글자 안보이게 하고 Focus잡기
        mTvHint.setOnClickListener(v -> {
            mEtMeritAndMotive.setFocusableInTouchMode(true);
            mEtMeritAndMotive.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtMeritAndMotive, 0);
            mTvHint.setVisibility(View.GONE);
        });
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        httpPatchProfiles();
    }

    /**
     * Update Profiles
     */
    private void httpPatchProfiles() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        int profile_index = prefs.getProfileIndex();
        String token = prefs.getToken();
        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, profile_index + "");

        HashMap header = Utils.getHttpHeader(token);
        HashMap<String, String> body = new HashMap<>();

        if (TextUtils.isEmpty(mEtMeritAndMotive.getText().toString())) {
            Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("meritNmotive", mEtMeritAndMotive.getText().toString());
        DAHttpClient client = DAHttpClient.getInstance();

        client.Patch(url, header, body, new IOServerCallback() {
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
