package com.truevalue.dreamappeal.fragment.profile.dream_present;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BasePagerAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.pager_image)
    ViewPager mPagerImage;
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;
    @BindView(R.id.ll_indicator)
    LinearLayout mLlIndicator;
    @BindView(R.id.pager_ad)
    ViewPager mPagerAd;
    @BindView(R.id.tv_ad_indicator)
    TextView mTvAdIndicator;
    @BindView(R.id.ll_ad_indicator)
    LinearLayout mLlAdIndicator;
    @BindView(R.id.rl_ad)
    RelativeLayout mRlAd;
    private String mMeritAndMotive = null;
    private BasePagerAdapter mAdapter = null;
    private BasePagerAdapter mAdAdapter = null;

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
        mBtbBar.getmIvClose().setVisibility(View.VISIBLE);
        initAdapter();
        // 데이터 초기화
        initData();
        // 뷰 초기화
        initView();

        httpGetExampleImage();
        httpGetAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private void initAdapter() {
        mAdapter = new BasePagerAdapter(getContext());
        mPagerImage.setAdapter(mAdapter);
        mPagerImage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTvIndicator.setText((position + 1) + " / " + mAdapter.getCount());
            }
        });

        mAdAdapter = new BasePagerAdapter(getContext());
        mPagerAd.setAdapter(mAdAdapter);
        mPagerAd.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTvAdIndicator.setText((position + 1) + " / " + mAdAdapter.getCount());
            }
        });
    }

    /**
     * http Get
     * Get Example Image
     */
    private void httpGetExampleImage() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_EXAMPLE_PROFILE_INDEX.replace(Comm_Param.EX_INDEX, String.valueOf(3));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext()).Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    JSONArray list = json.getJSONArray("ex_url");
                    mTvIndicator.setText(1 + " / " + list.length());
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject urls = list.getJSONObject(i);
                        String imageUrl = urls.getString("url");
                        mAdapter.add(imageUrl);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * http Get
     * Get Ad Image
     */
    private void httpGetAd() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_AD;
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext()).Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    JSONArray list = json.getJSONArray("adlist");
                    mTvAdIndicator.setText(1 + " / " + list.length());
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject urls = list.getJSONObject(i);
                        String imageUrl = urls.getString("url");
                        mAdAdapter.add(imageUrl);
                    }
                    mAdAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        if (mMeritAndMotive != null) {
            if (!TextUtils.isEmpty(mMeritAndMotive)) {
                mEtMeritAndMotive.setText(mMeritAndMotive);
                mTvHint.setVisibility(View.GONE);
                initRightBtn();
            }
        }
    }

    private void initView() {
        Point point = Utils.getDisplaySize(getActivity());
        Utils.setResizeView(mRlAd,point.x,point.x / 3);
        // 처음 Hint 글자 안보이게 하고 Focus잡기
        mTvHint.setOnClickListener(v -> {
            mEtMeritAndMotive.setFocusableInTouchMode(true);
            mEtMeritAndMotive.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtMeritAndMotive, 0);
            mTvHint.setVisibility(View.GONE);
        });

        mEtMeritAndMotive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initRightBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRightBtn() {
        if (TextUtils.isEmpty(mEtMeritAndMotive.getText().toString())) {
            mBtbBar.getmTvTextBtn().setSelected(false);
        } else {
            mBtbBar.getmTvTextBtn().setSelected(true);
        }
    }

    @Override
    public void OnClickBack() {

    }

    @Override
    public void OnClickClose() {
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
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("meritNmotive", mEtMeritAndMotive.getText().toString());
        DAHttpClient client = DAHttpClient.getInstance(getContext());

        client.Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    getActivity().onBackPressed();
                }
            }
        });
    }
}
