package com.truevalue.dreamappeal.fragment.profile.dream_present;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.profile.ActivityRecentAchivementDetail;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BasePagerAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.bean.BeanProfiles;
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
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentDreamTitle extends BaseFragment implements IOBaseTitleBarListener {


    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_value_style)
    EditText mEtValueStyle;
    @BindView(R.id.et_job)
    EditText mEtJob;
    @BindView(R.id.pager_image)
    ViewPager mPagerImage;
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;
    @BindView(R.id.ll_indicator)
    LinearLayout mLlIndicator;

    private boolean mIsAddProfiles = false;
    private ArrayList<String> mArrayTitles = null;
    private BasePagerAdapter mAdapter = null;


    public static FragmentDreamTitle newInstance(ArrayList<String> dream_titles) {
        FragmentDreamTitle fragment = new FragmentDreamTitle();
        fragment.mIsAddProfiles = true;
        fragment.mArrayTitles = dream_titles;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_title, container, false);
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

        httpGetExampleImage();
    }


    private void initAdapter(){
        mAdapter = new BasePagerAdapter(getContext());
        mPagerImage.setAdapter(mAdapter);
        mPagerImage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTvIndicator.setText((position + 1) + " / " + mAdapter.getCount());
            }
        });
    }

    /**
     * http Get
     * Get Example Image
     */
    private void httpGetExampleImage(){
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_EXAMPLE_PROFILE_INDEX.replace(Comm_Param.EX_INDEX,String.valueOf(1));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext()).Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if(TextUtils.equals(code,SUCCESS)){
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

    private void initData() {
        if (mArrayTitles != null) {
            if (!TextUtils.isEmpty(mArrayTitles.get(0))) mEtValueStyle.setText(mArrayTitles.get(0));
            if (!TextUtils.isEmpty(mArrayTitles.get(1))) mEtJob.setText(mArrayTitles.get(1));
            initRightBtn();
        }
        mEtJob.addTextChangedListener(new TextWatcher() {
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

        mEtValueStyle.addTextChangedListener(new TextWatcher() {
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
        if (TextUtils.isEmpty(mEtJob.getText().toString()) || TextUtils.isEmpty(mEtValueStyle.getText().toString())) {
            mBtbBar.getmTvTextBtn().setSelected(false);
        } else mBtbBar.getmTvTextBtn().setSelected(true);
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {

    }

    @Override
    public void OnClickClose() {
        getActivity().onBackPressed();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        // TODO : 내 꿈 명칭 등록 후 서버 요청
        if (!mIsAddProfiles) httpPostProfiles();
        else httpPatchProfiles();
    }

    /**
     * 회원 등록
     */
    private void httpPostProfiles() {
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        HashMap header = Utils.getHttpHeader(prefs.getToken());

        if (TextUtils.isEmpty(mEtJob.getText().toString()) || TextUtils.isEmpty(mEtValueStyle.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> body = new HashMap<>();
        body.put("image", "");
        body.put("value_style", mEtValueStyle.getText().toString());
        body.put("job", mEtJob.getText().toString());
        body.put("description", "");
        body.put("description_spec", new JSONObject().toString());
        body.put("meritNmotive", "");

        client.Post(Comm_Param.URL_API_PROFILES, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    JSONObject result = object.getJSONObject("result");
                    Gson gson = new Gson();
                    BeanProfiles bean = gson.fromJson(result.toString(), BeanProfiles.class);
                    Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                    prefs.setProfileIndex(bean.getInsertId(), true);

                    getActivity().onBackPressed();
                }
            }
        });
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

        if (TextUtils.isEmpty(mEtJob.getText().toString()) || TextUtils.isEmpty(mEtValueStyle.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("job", mEtJob.getText().toString());
        body.put("value_style", mEtValueStyle.getText().toString());
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
