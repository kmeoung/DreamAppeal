package com.truevalue.dreamappeal.activity.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
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
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityDreamDescription extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_DREAM_DESCRIPTION = "EXTRA_DREAM_DESCRIPTION";
    public static final String EXTRA_DESCRIPTION_DIVIDER = "<@<DESCRIPTION_DIVIDER>@>";

    @BindView(R.id.v_status)
    View mVStatus;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_description);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initData();
    }

    private void initData(){
        if(getIntent().getStringExtra(EXTRA_DREAM_DESCRIPTION) != null){
            String dreamDescriptions = getIntent().getStringExtra(EXTRA_DREAM_DESCRIPTION);
            String[] dreamDescription = dreamDescriptions.split(EXTRA_DESCRIPTION_DIVIDER);
            if(!TextUtils.isEmpty(dreamDescription[0])) mEtDreamDescription.setText(dreamDescription[0]);
            if(!TextUtils.isEmpty(dreamDescription[1])) mEtDreamDescriptionDetail1.setText(dreamDescription[1]);
            if(!TextUtils.isEmpty(dreamDescription[2])) mEtDreamDescriptionDetail2.setText(dreamDescription[2]);
            if(!TextUtils.isEmpty(dreamDescription[3])) mEtDreamDescriptionDetail3.setText(dreamDescription[3]);
        }
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        finish();
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
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamDescription.this);
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
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
        BaseOkHttpClient client = new BaseOkHttpClient();

        client.Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityDreamDescription.this, message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
