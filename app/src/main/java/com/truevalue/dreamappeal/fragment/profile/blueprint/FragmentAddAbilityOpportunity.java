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
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
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

public class FragmentAddAbilityOpportunity extends BaseFragment implements IOBaseTitleBarListener {

    public static final int TYPE_ABILITY = 0;
    public static final int TYPE_OPPORTUNITY = 1;

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

    private int mType = -1;
    private boolean isEdit = false;
    private BeanBlueprintAbilityOpportunity mBean = null;
    private String mTitle;


    public static FragmentAddAbilityOpportunity newInstance(int type, String title) {
        FragmentAddAbilityOpportunity fragment = new FragmentAddAbilityOpportunity();
        fragment.mType = type;
        fragment.mTitle = title;
        return fragment;
    }

    public static FragmentAddAbilityOpportunity newInstance(int type, String title, BeanBlueprintAbilityOpportunity bean) {
        FragmentAddAbilityOpportunity fragment = new FragmentAddAbilityOpportunity();
        fragment.isEdit = true;
        fragment.mType = type;
        fragment.mTitle = title;
        fragment.mBean = bean;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ability_opportunity, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtbBar.setIOBaseTitleBarListener(this);
        // 뷰 초기화
        initView();
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        if (TextUtils.isEmpty(mEtAbilityOpportunity.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEdit) {
            if (mType == TYPE_ABILITY) httpPostAbilities();
            else if (mType == TYPE_OPPORTUNITY) httpPostOpportunities();
        } else {
            int idx = mBean.getIdx();
            if (mType == TYPE_ABILITY) httpPatchAbility(idx);
            else if (mType == TYPE_OPPORTUNITY) httpPatchOpportunity(idx);
        }
    }

    /**
     * View 초기화
     */
    private void initView() {
        // Title 설정
        mBtbBar.setTitle(mTitle);

        if (isEdit) {
            mTvHint.setVisibility(View.GONE);
            mEtAbilityOpportunity.setText(mBean.getContents());
        } else {
            if (mType == TYPE_ABILITY) {
                mTvHint.setText("수식어나 직업명에 어울리기 위해\n" + "어떤 능력이 있어야 할까?");
            } else if (mType == TYPE_OPPORTUNITY) {
                mTvHint.setText("능동적으로 실력/경험을 쌓으려면\n" + "어떤 기회를 만들어야 할까?");
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
    }

    /**
     * http Post
     * 갖출 능력 등록
     */
    private void httpPostAbilities() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_ABILITIES;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("ability", mEtAbilityOpportunity.getText().toString());

        DAHttpClient client = DAHttpClient.getInstance(getContext());
        client.Post(url, header, body, new IOServerCallback() {
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
     * 만들고픈 기회 등록
     */
    private void httpPostOpportunities() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OPPORTUNITIES;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("opportunity", mEtAbilityOpportunity.getText().toString());

        DAHttpClient client = DAHttpClient.getInstance(getContext());
        client.Post(url, header, body, new IOServerCallback() {
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
     * 갖출 능력 수정
     *
     * @param ability_index
     */
    private void httpPatchAbility(int ability_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_ABILITIES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.ABILITY_INDEX, String.valueOf(ability_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("ability", mEtAbilityOpportunity.getText().toString());

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

    /**
     * http Patch
     * 만들고픈 기회 수정
     *
     * @param opportunity_index
     */
    private void httpPatchOpportunity(int opportunity_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OPPORTUNITIES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OPPORTUNITY_INDEX, String.valueOf(opportunity_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("opportunity", mEtAbilityOpportunity.getText().toString());

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
