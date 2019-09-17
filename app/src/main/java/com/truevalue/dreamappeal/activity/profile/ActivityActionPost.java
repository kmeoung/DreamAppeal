package com.truevalue.dreamappeal.activity.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
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
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityActionPost extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_ACTION_POST_INDEX = "EXTRA_ACTION_POST_INDEX";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.iv_dream_profile)
    ImageView mIvDreamProfile;
    @BindView(R.id.tv_value_style)
    TextView mTvValueStyle;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.ll_dream_title)
    LinearLayout mLlDreamTitle;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.iv_cheering)
    ImageView mIvCheering;
    @BindView(R.id.tv_cheering)
    TextView mTvCheering;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_cheering)
    LinearLayout mLlCheering;

    private int mPostIndex = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_post);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
//        initView();
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        // 데이터 초기화
        initData();
    }

    public void initData() {
        mPostIndex = getIntent().getIntExtra(EXTRA_ACTION_POST_INDEX, -1);
        // 상세 조회
        httpGetActionPost();
    }

    /**
     * http Get
     * 실천 인증 상세 조회
     */
    private void httpGetActionPost() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityActionPost.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_INDEX_ACTIONPOSTS_INDEX;
        // 내 실천인증
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(mPostIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance().Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityActionPost.this, message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    Gson gson = new Gson();
                    BeanActionPostDetail bean = gson.fromJson(json.getJSONObject("action_post").toString(), BeanActionPostDetail.class);

                }
            }
        });
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @OnClick({R.id.ll_comment, R.id.ll_cheering})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_comment:
                Intent intent = new Intent(ActivityActionPost.this, ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering:
                break;
        }
    }


}
