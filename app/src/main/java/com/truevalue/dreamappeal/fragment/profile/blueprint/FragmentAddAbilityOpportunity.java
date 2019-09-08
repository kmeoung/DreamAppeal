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
import com.truevalue.dreamappeal.activity.profile.ActivityAbilityOpportunity;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
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


    public static FragmentAddAbilityOpportunity newInstance(int type) {
        FragmentAddAbilityOpportunity fragment = new FragmentAddAbilityOpportunity();
        fragment.mType = type;
        return fragment;
    }

    public static FragmentAddAbilityOpportunity newInstance(int type, BeanBlueprintAbilityOpportunity bean) {
        FragmentAddAbilityOpportunity fragment = new FragmentAddAbilityOpportunity();
        fragment.isEdit = true;
        fragment.mType = type;
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
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityAbilityOpportunity) getActivity()).setListener(this);
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        if(TextUtils.isEmpty(mEtAbilityOpportunity.getText().toString())){
            Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
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

    private void initView() {
        if(isEdit){
            mTvHint.setVisibility(View.GONE);
            mEtAbilityOpportunity.setText(mBean.getContents());
        }else{
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
        HashMap<String,String> body = new HashMap<>();
        body.put("ability",mEtAbilityOpportunity.getText().toString());

        BaseOkHttpClient client = new BaseOkHttpClient();
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

    /**
     * http Post
     * 만들고픈 기회 등록
     */
    private void httpPostOpportunities() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OPPORTUNITIES;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String,String> body = new HashMap<>();
        body.put("opportunity",mEtAbilityOpportunity.getText().toString());

        BaseOkHttpClient client = new BaseOkHttpClient();
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
        HashMap<String,String> body = new HashMap<>();
        body.put("ability",mEtAbilityOpportunity.getText().toString());

        BaseOkHttpClient client = new BaseOkHttpClient();
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
        HashMap<String,String> body = new HashMap<>();
        body.put("opportunity",mEtAbilityOpportunity.getText().toString());

        BaseOkHttpClient client = new BaseOkHttpClient();
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
