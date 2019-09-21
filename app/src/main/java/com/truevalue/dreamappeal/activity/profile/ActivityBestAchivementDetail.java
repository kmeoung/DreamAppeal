package com.truevalue.dreamappeal.activity.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.bean.BeanPostDetail;
import com.truevalue.dreamappeal.fragment.FragmentMain;
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

public class ActivityBestAchivementDetail extends BaseActivity {

    public static final String EXTRA_BEST_ACHIVEMENT_INDEX = "EXTRA_BEST_ACHIVEMENT_INDEX";
    public static final String EXTRA_BEST_ACHIVEMENT_BEST_INDEX = "EXTRA_BEST_ACHIVEMENT_BEST_INDEX";
    public static final int REQUEST_EDIT_RECENT_ACHIVEMENT = 2011;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_achivement_title)
    TextView mTvAchivementTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
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
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.tv_contents)
    TextView mTvContents;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;

    private BeanPostDetail mBean = null;
    private int mBestIndex = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_achivement_post_detail);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mLlTitle);
        initView();
        initData();
    }

    private void initView() {
        Point size = Utils.getDisplaySize(this);
        Utils.setResizeView(mIvImg, size.x, size.x);
    }

    private void initData() {
        int index = getIntent().getIntExtra(EXTRA_BEST_ACHIVEMENT_INDEX, -1);
        mBestIndex = getIntent().getIntExtra(EXTRA_BEST_ACHIVEMENT_BEST_INDEX, -1);
        httpGetBestPostAchivement(index);
    }

    /**
     * 최근 성과 상세 조회
     *
     * @param index
     */
    private void httpGetBestPostAchivement(int index) {
        if (index == -1) return;
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityBestAchivementDetail.this);
        String url = Comm_Param.URL_API_MYPROFILEINDEX_ACHIVEMENT_POSTS_INDEX;
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityBestAchivementDetail.this);
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    Gson gson = new Gson();
                    JSONObject object = new JSONObject(body);
                    JSONObject post = object.getJSONObject("achievement_post");
                    mBean = gson.fromJson(post.toString(), BeanPostDetail.class);

                    mTvTitle.setText(mBean.getTitle());
                    // todo : 아직 검증이 필요함
                    Utils.setReadMore(mTvContents, mBean.getContent(), 3);
                    if (TextUtils.isEmpty(mBean.getThumbnail_image()))
                        Glide.with(ActivityBestAchivementDetail.this).load(R.drawable.user).into(mIvImg);
                    else
                        Glide.with(ActivityBestAchivementDetail.this).load(mBean.getThumbnail_image()).placeholder(R.drawable.user).into(mIvImg);

                    mTvCheering.setText(String.format("%d개", mBean.getLike_count()));
                    mTvComment.setText(String.format("%d개", mBean.getComment_count()));
                    mLlCheering.setSelected(mBean.isStatus());
                    mTvTime.setText(Utils.convertFromDate(mBean.getRegister_date()));
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.ll_comment, R.id.ll_cheering, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 뒤로가기
                finish();
                break;
            case R.id.ll_comment: // 댓글
                Intent intent = new Intent(ActivityBestAchivementDetail.this, ActivityCommentDetail.class);
                intent.putExtra(ActivityCommentDetail.EXTRA_COMMENT_TYPE, ActivityCommentDetail.TYPE_PERFORMANCE);
                intent.putExtra(ActivityCommentDetail.EXTRA_POST_INDEX, mBean.getIdx());
                startActivityForResult(intent, FragmentMain.REQUEST_BLUEPRINT_COMMENT);
                break;
            case R.id.ll_cheering: // 응원하기
                httpPatchLike(mBean.getIdx());
                break;
            case R.id.iv_more: // 더보기
                actionMore();
                break;

        }
    }

    private void actionMore() {
        String[] list = {"대표 성과 내리기", "수정", "삭제"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBestAchivementDetail.this);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (list[i]) {
                    case "대표 성과 내리기":
                        if (mBestIndex != -1) httpPatchBestAchivement(mBestIndex);
                        break;
                    case "수정":
                        if (mBean != null) {
                            Intent intent = new Intent(ActivityBestAchivementDetail.this, ActivityAddAchivement.class);
                            intent.putExtra(ActivityAddAchivement.EXTRA_EDIT_ACHIVEMENT_POST, mBean);
                            startActivityForResult(intent, REQUEST_EDIT_RECENT_ACHIVEMENT);
                        }
                        break;
                    case "삭제":
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBestAchivementDetail.this)
                                .setTitle("게시글 삭제")
                                .setMessage("게시글을 삭제하시겠습니까?")
                                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mBean != null)
                                            httpDeletePostAchivement(mBean.getIdx());
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 대표 성과 내리기
     */
    private void httpPatchBestAchivement(int best_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityBestAchivementDetail.this);
        String url = Comm_Param.URL_API_ACHIVEMENT_BEST_POST_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.BEST_POST_INDEX, String.valueOf(best_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityBestAchivementDetail.this);
        client.Patch(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    /**
     * 성과 삭제
     *
     * @param index
     */
    private void httpDeletePostAchivement(int index) {
        if (index == -1) return;
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityBestAchivementDetail.this);
        String url = Comm_Param.URL_API_ACHIVEMENT_POSTS_INDEX;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replaceAll(Comm_Param.POST_INDEX, String.valueOf(index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityBestAchivementDetail.this);
        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    if (mBean != null) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });
    }

    /**
     * http patch
     * 응원하기 / 취소
     *
     * @param post_index
     */
    private void httpPatchLike(int post_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityBestAchivementDetail.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_PERFORMANCE_INDEX_LIKE_INDEX;
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(post_index));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(ActivityBestAchivementDetail.this).Patch(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    int likeCount = json.getInt("count");
                    mTvCheering.setText(likeCount + "개");
                    mIvCheering.setSelected(!mIvCheering.isSelected());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT_RECENT_ACHIVEMENT) {
                setResult(RESULT_OK);
                finish();
            } else if (requestCode == FragmentMain.REQUEST_BLUEPRINT_COMMENT) {
                httpGetBestPostAchivement(mBean.getIdx());
            }
        }
    }
}
