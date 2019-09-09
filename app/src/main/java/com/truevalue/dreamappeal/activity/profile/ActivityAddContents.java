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
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
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

public class ActivityAddContents extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_STR_TITLE = "EXTRA_STR_TITLE";
    public static final String EXTRA_VIEW_TYPE = "EXTRA_VIEW_TYPE";

    public static final int EXTRA_TYPE_OBJECTS = 0; // 실천 목표 추가
    public static final int EXTRA_TYPE_OBJECT_STEP = 1; // 실천 목표 세부단계 추가
    public static final int EXTRA_TYPE_EDIT_OBJECTS = 2; // 실천 목표 수정

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
    @BindView(R.id.v_status)
    View mVStatus;

    private int mViewType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contents);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initData();
        initVIew();
    }

    private void initData(){
        String title = getIntent().getStringExtra(EXTRA_STR_TITLE);
        mViewType = getIntent().getIntExtra(EXTRA_VIEW_TYPE,-1);
        if(mViewType == EXTRA_TYPE_EDIT_OBJECTS){

        }
        mBtbBar.setTitle(title);
    }

    private void initVIew(){

        // 처음 Hint 글자 안보이게 하고 Focus잡기
        mTvHint.setOnClickListener(v -> {
            mEtAbilityOpportunity.setFocusableInTouchMode(true);
            mEtAbilityOpportunity.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtAbilityOpportunity, 0);
            mTvHint.setVisibility(View.GONE);
        });
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {
        if(TextUtils.isEmpty(mEtAbilityOpportunity.getText())){
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mViewType == EXTRA_TYPE_OBJECTS){
            httpPostObjects();
        }else if(mViewType == EXTRA_TYPE_OBJECT_STEP){

        }
    }

    /**
     * http Post
     * 실천 목표 추가
     */
    private void httpPostObjects(){
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddContents.this);
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS;
        url = url.replace(Comm_Param.PROFILES_INDEX,String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String,String> body = new HashMap();
        body.put("object_name",mEtAbilityOpportunity.getText().toString());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Post(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityAddContents.this, message, Toast.LENGTH_SHORT).show();

                if(TextUtils.equals(code,SUCCESS)){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }
}
