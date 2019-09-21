package com.truevalue.dreamappeal.fragment.profile.dream_present;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentDreamDescription extends BaseFragment implements IOBaseTitleBarListener {
    
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_dream_description)
    EditText mEtDreamDescription;
    @BindView(R.id.et_dream_description_detail_1)
    EditText mEtDreamDescriptionDetail1;
    @BindView(R.id.et_dream_description_detail_2)
    EditText mEtDreamDescriptionDetail2;
    @BindView(R.id.et_dream_description_detail_3)
    EditText mEtDreamDescriptionDetail3;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    
    private ArrayList<String> mArrayDescription = null;

    public static FragmentDreamDescription newInstance(ArrayList<String> array_description){
        FragmentDreamDescription fragment = new FragmentDreamDescription();
        fragment.mArrayDescription = array_description;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_description,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        // 데이터 초기화
        initData();
    }

    private void initData() {
        if (mArrayDescription != null) {
            if (!TextUtils.isEmpty(mArrayDescription.get(0)))
                mEtDreamDescription.setText(mArrayDescription.get(0));
            if (!TextUtils.isEmpty(mArrayDescription.get(1)))
                mEtDreamDescriptionDetail1.setText(mArrayDescription.get(1));
            if (!TextUtils.isEmpty(mArrayDescription.get(2)))
                mEtDreamDescriptionDetail2.setText(mArrayDescription.get(2));
            if (!TextUtils.isEmpty(mArrayDescription.get(3)))
                mEtDreamDescriptionDetail3.setText(mArrayDescription.get(3));
        }
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    /**
     * Right Text Button Clicked
     */
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

        if (TextUtils.isEmpty(mEtDreamDescription.getText().toString())
                || TextUtils.isEmpty(mEtDreamDescriptionDetail1.getText().toString())
                || TextUtils.isEmpty(mEtDreamDescriptionDetail2.getText().toString())
                || TextUtils.isEmpty(mEtDreamDescriptionDetail3.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("description", mEtDreamDescription.getText().toString());
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(new JSONObject().put("content", mEtDreamDescriptionDetail1.getText().toString()));
            jsonArray.put(new JSONObject().put("content", mEtDreamDescriptionDetail2.getText().toString()));
            jsonArray.put(new JSONObject().put("content", mEtDreamDescriptionDetail3.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        body.put("description_spec", jsonArray.toString());
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
