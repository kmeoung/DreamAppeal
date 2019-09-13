package com.truevalue.dreamappeal.activity.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.http.DreamAppealHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
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

public class ActivityMeritAndMotive extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_MERIT_AND_MOTIVE = "EXTRA_MERIT_AND_MOTIVE";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_merit_and_motive)
    EditText mEtMeritAndMotive;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;
    @BindView(R.id.tv_hint)
    TextView mTvHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merit_and_motive);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initData();

        initView();
    }

    private void initData(){
        if(getIntent().getStringExtra(EXTRA_MERIT_AND_MOTIVE) != null){
            String meritAndMotive = getIntent().getStringExtra(EXTRA_MERIT_AND_MOTIVE);
            if(!TextUtils.isEmpty(meritAndMotive)) {
                mEtMeritAndMotive.setText(meritAndMotive);
                mTvHint.setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        // 처음 Hint 글자 안보이게 하고 Focus잡기
        mTvHint.setOnClickListener(v -> {
            mEtMeritAndMotive.setFocusableInTouchMode(true);
            mEtMeritAndMotive.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtMeritAndMotive, 0);
            mTvHint.setVisibility(View.GONE);
        });
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {
        httpPatchProfiles();
    }

    /**
     * Update Profiles
     */
    private void httpPatchProfiles() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityMeritAndMotive.this);
        int profile_index = prefs.getProfileIndex();
        String token = prefs.getToken();
        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, profile_index + "");

        HashMap header = Utils.getHttpHeader(token);
        HashMap<String, String> body = new HashMap<>();

        if (TextUtils.isEmpty(mEtMeritAndMotive.getText().toString())) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("meritNmotive", mEtMeritAndMotive.getText().toString());
        DreamAppealHttpClient client = DreamAppealHttpClient.getInstance();

        client.Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityMeritAndMotive.this, message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
