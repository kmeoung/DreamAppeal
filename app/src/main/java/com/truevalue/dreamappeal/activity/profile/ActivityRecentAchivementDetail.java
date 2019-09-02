package com.truevalue.dreamappeal.activity.profile;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityRecentAchivementDetail extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_RECENT_ACHIVEMENT_INDEX = "EXTRA_RECENT_ACHIVEMENT_INDEX";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_achivement_post_detail);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        initView();
        initData();
    }

    private void initView() {
        Point size = Utils.getDisplaySize(this);
        Utils.setResizeView(mIvImg, size.x, size.x);
    }

    private void initData() {
        int index = getIntent().getIntExtra(EXTRA_RECENT_ACHIVEMENT_INDEX, -1);
        httpGetPostAchivement(index);
    }

    /**
     * 최근 성과 상세 조회
     *
     * @param index
     */
    private void httpGetPostAchivement(int index) {
        // todo : 제대로 된 설정 필요
        if (index == -1) return;
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityRecentAchivementDetail.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_INDEX;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replaceAll(Comm_Param.POST_INDEX, String.valueOf(index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityRecentAchivementDetail.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_comment, R.id.ll_cheering})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 뒤로가기
                finish();
                break;
            case R.id.ll_comment: // 댓글
                Intent intent = new Intent(ActivityRecentAchivementDetail.this, ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering: // 응원하기
                break;
        }
    }

    @Override
    public void OnClickBack() {
        finish();
    }
}
