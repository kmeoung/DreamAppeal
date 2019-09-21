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

public class FragmentAddContents extends BaseFragment implements IOBaseTitleBarListener {

    public static final int EXTRA_TYPE_OBJECTS = 0; // 실천 목표 추가
    public static final int EXTRA_TYPE_OBJECT_STEP = 1; // 실천 목표 세부단계 추가
    public static final int EXTRA_TYPE_EDIT_OBJECTS = 2; // 실천 목표 수정
    public static final int EXTRA_TYPE_EDIT_OBJECTS_STEPS = 3; // 실천 목표 세부단계 수정
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
    private String mObjectTitle = null;
    private int mObjectIndex = -1;
    private int mObjectStepIndex = -1;

    /**
     * 실천목표 추가
     * @param title
     * @return
     */
    public static FragmentAddContents newInstance(String title) {
        FragmentAddContents fragment = new FragmentAddContents();
        fragment.mTitle = title;
        fragment.mViewType = EXTRA_TYPE_OBJECTS;
        return fragment;
    }

    /**
     * 세부사항 추가
     * @param title
     * @param object_index
     * @return
     */
    public static FragmentAddContents newInstance(String title, int object_index) {
        FragmentAddContents fragment = new FragmentAddContents();
        fragment.mTitle = title;
        fragment.mViewType = EXTRA_TYPE_OBJECT_STEP;
        fragment.mObjectIndex = object_index;
        return fragment;
    }

    /**
     * 실천목표 수정
     * @param title
     * @param object_title
     * @param object_index
     * @return
     */
    public static FragmentAddContents newInstance(String title, String object_title, int object_index) {
        FragmentAddContents fragment = new FragmentAddContents();
        fragment.mTitle = title;
        fragment.mViewType = EXTRA_TYPE_EDIT_OBJECTS;
        fragment.mObjectTitle = object_title;
        fragment.mObjectIndex = object_index;
        return fragment;
    }

    /**
     * 세부사항 수정
     * @param title
     * @param object_title
     * @param object_index
     * @param object_step_index
     * @return
     */
    public static FragmentAddContents newInstance(String title, String object_title, int object_index, int object_step_index) {
        FragmentAddContents fragment = new FragmentAddContents();
        fragment.mTitle = title;
        fragment.mViewType = EXTRA_TYPE_EDIT_OBJECTS_STEPS;
        fragment.mObjectTitle = object_title;
        fragment.mObjectIndex = object_index;
        fragment.mObjectStepIndex = object_step_index;
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
        if (mObjectTitle != null) {
            mEtAbilityOpportunity.setText(mObjectTitle);
        }
        if (mTitle != null) mBtbBar.setTitle(mTitle);
    }

    private void initVIew() {
        if (mViewType == EXTRA_TYPE_OBJECTS) {
            mTvHint.setText("갖출 능력과 만들어갈 기회를 위해\n어떤 실천을 해볼까?");
        } else if (mViewType == EXTRA_TYPE_OBJECT_STEP) {
            mTvHint.setText("꾸준히 노력하기 쉽도록\n" + "어떤 단계로 나눠서 실천해볼까?");
        } else if (mViewType == EXTRA_TYPE_EDIT_OBJECTS) {
            mTvHint.setVisibility(View.GONE);
        } else if (mViewType == EXTRA_TYPE_EDIT_OBJECTS_STEPS) {
            mTvHint.setVisibility(View.GONE);
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
        if (TextUtils.isEmpty(mEtAbilityOpportunity.getText().toString())) {
            Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mViewType == EXTRA_TYPE_OBJECTS) { // 실천 목표 추가
            httpPostObjects();
        } else if (mViewType == EXTRA_TYPE_OBJECT_STEP) { // 실천 목표 세부단계 추가
            httpPostObjectStep();
        } else if (mViewType == EXTRA_TYPE_EDIT_OBJECTS) { // 실천 목표 수정
            httpPatchObject();
        } else if (mViewType == EXTRA_TYPE_EDIT_OBJECTS_STEPS) { // 실천 목표 세부단계 수정
            httpPatchObjectStep();
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
        DAHttpClient.getInstance().Post(url, header, body, new IOServerCallback() {
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

    /**
     * http Patch
     * 실천 목표 수정
     */
    private void httpPatchObject() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("object_name", mEtAbilityOpportunity.getText().toString());
        DAHttpClient.getInstance().Patch(url, header, body, new IOServerCallback() {
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

    /**
     * http Post
     * 실천 목표 세부단계 추가
     */
    private void httpPostObjectStep() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("title", mEtAbilityOpportunity.getText().toString());
        DAHttpClient.getInstance().Post(url, header, body, new IOServerCallback() {
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

    /**
     * http Patch
     * 실천 목표 세부단계 수정
     */
    private void httpPatchObjectStep() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));
        url = url.replace(Comm_Param.STEPS_INDEX, String.valueOf(mObjectStepIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("title", mEtAbilityOpportunity.getText().toString());
        DAHttpClient.getInstance().Patch(url, header, body, new IOServerCallback() {
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
