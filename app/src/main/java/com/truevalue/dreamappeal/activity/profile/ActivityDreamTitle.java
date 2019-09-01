package com.truevalue.dreamappeal.activity.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanProfiles;
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
import okhttp3.Call;

public class ActivityDreamTitle extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_DREAM_TITLE = "EXTRA_DREAM_TITLE";
    public static final String EXTRA_DREAM_TITLE_DIVIDER = "<@<DREAMTITLE_DIVIDER>@>";
    public static final String EXTRA_ADD_PROFILES = "EXTRA_ADD_PROFILES";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_value_style)
    EditText mEtValueStyle;
    @BindView(R.id.et_job)
    EditText mEtJob;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;

    private boolean mIsAddProfiles = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_title);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initData();
    }

    private void initData(){
        mIsAddProfiles = getIntent().getBooleanExtra(EXTRA_ADD_PROFILES,false);

        if(getIntent().getStringExtra(EXTRA_DREAM_TITLE) != null){
            String dreamTitles = getIntent().getStringExtra(EXTRA_DREAM_TITLE);
            String[] dreamTitle = dreamTitles.split(EXTRA_DREAM_TITLE_DIVIDER);
            if(!TextUtils.isEmpty(dreamTitle[0])) mEtValueStyle.setText(dreamTitle[0]);
            if(!TextUtils.isEmpty(dreamTitle[1])) mEtJob.setText(dreamTitle[1]);
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
        // TODO : 내 꿈 명칭 등록 후 서버 요청
        if(mIsAddProfiles) httpPostProfiles();
        else httpPatchProfiles();
    }

    /**
     * 회원 등록
     *
     */
    private void httpPostProfiles() {
        BaseOkHttpClient client = new BaseOkHttpClient();
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamTitle.this);
        HashMap header = Utils.getHttpHeader(prefs.getToken());

        if(TextUtils.isEmpty(mEtJob.getText().toString()) || TextUtils.isEmpty(mEtValueStyle.getText().toString())){
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ActivityDreamTitle.this, message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    JSONObject result = object.getJSONObject("result");
                    Gson gson = new Gson();
                    BeanProfiles bean = gson.fromJson(result.toString(), BeanProfiles.class);
                    Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamTitle.this);
                    prefs.setProfileIndex(bean.getInsertId());

                    // 내 꿈 조회 호출
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    /**
     * Update Profiles
     */
    private void httpPatchProfiles() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamTitle.this);
        int profile_index = prefs.getProfileIndex();
        String token = prefs.getToken();
        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, profile_index + "");

        HashMap header = Utils.getHttpHeader(token);
        HashMap<String,String> body = new HashMap<>();

        if(TextUtils.isEmpty(mEtJob.getText().toString()) || TextUtils.isEmpty(mEtValueStyle.getText().toString())){
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("job",mEtJob.getText().toString());
        body.put("value_style",mEtValueStyle.getText().toString());
        BaseOkHttpClient client = new BaseOkHttpClient();

        client.Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityDreamTitle.this,message,Toast.LENGTH_SHORT).show();
                if(TextUtils.equals(code,SUCCESS)){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
