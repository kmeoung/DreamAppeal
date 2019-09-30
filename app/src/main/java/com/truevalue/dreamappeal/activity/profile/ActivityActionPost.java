package com.truevalue.dreamappeal.activity.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BasePagerAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.bean.BeanActionPostProfile;
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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityActionPost extends BaseActivity implements IOBaseTitleBarListener {

    public static final String EXTRA_ACTION_POST_INDEX = "EXTRA_ACTION_POST_INDEX";

    public static final int REQUEST_ACTIVITY = 1000;
    public static final int REQUEST_COMMENT_DETAIL = 1001;

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
    @BindView(R.id.tv_target)
    TextView mTvTarget;
    @BindView(R.id.tv_target_detail)
    TextView mTvTargetDetail;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.tv_contents)
    TextView mTvContents;
    @BindView(R.id.pager_image)
    ViewPager mPagerImage;
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;
    @BindView(R.id.ll_indicator)
    LinearLayout mLlIndicator;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.iv_action_more)
    ImageView mIvActionMore;
    @BindView(R.id.ll_comment_detail)
    LinearLayout mLlCommentDetail;

    private int mPostIndex = -1;
    private BeanActionPostDetail mBean = null;
    private BasePagerAdapter mAdapter = null;


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

        initView();
        initAdapter();
        // 데이터 초기화
        initData();
    }

    private void initView() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityActionPost.this);
        if (prefs.getProfileIndex() == prefs.getMyProfileIndex()) {
            mIvActionMore.setVisibility(View.VISIBLE);
        } else {
            mIvActionMore.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        mAdapter = new BasePagerAdapter(ActivityActionPost.this);
        mPagerImage.setAdapter(mAdapter);
        mPagerImage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTvIndicator.setText((position + 1) + " / " + mAdapter.getCount());
            }
        });
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
        DAHttpClient.getInstance(ActivityActionPost.this).Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (!TextUtils.equals(code, SUCCESS) || Comm_Param.IS_TEST)
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    Gson gson = new Gson();
                    BeanActionPostProfile actionPostProfile = gson.fromJson(json.getJSONObject("profile").toString(), BeanActionPostProfile.class);
                    if (TextUtils.isEmpty(actionPostProfile.getImage()))
                        Glide.with(ActivityActionPost.this).load(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(mIvDreamProfile);
                    else
                        Glide.with(ActivityActionPost.this).
                                load(actionPostProfile.getImage()).
                                apply(new RequestOptions().circleCrop()).
                                placeholder(R.drawable.drawer_user).
                                into(mIvDreamProfile);

                    mTvValueStyle.setText(actionPostProfile.getValue_style());
                    mTvJob.setText(actionPostProfile.getJob());
                    JSONObject action_post = json.getJSONObject("action_post");

                    mBean = gson.fromJson(action_post.toString(), BeanActionPostDetail.class);
//                    if (!TextUtils.isEmpty(actionPostProfile.getImage()))
//                        Glide.with(ActivityActionPost.this).load(R.drawable.user).into(mIvImg);
//                    else
//                        Glide.with(ActivityActionPost.this).load(bean.get()).placeholder(R.drawable.user).into(mIvImg);
                    mTvComment.setText(mBean.getComment_count() + "개");
                    mTvCheering.setText(mBean.getLike_count() + "개");
                    mTvContents.setText(mBean.getContent());
                    mTvTarget.setText(mBean.getObject_name());
                    mTvTargetDetail.setText(mBean.getStep_name());
                    mIvCheering.setSelected(mBean.getStatus());
                    mTvTime.setText(Utils.convertFromDate(mBean.getRegister_date()));

                    mTvIndicator.setText("0 / 0");
                    JSONArray jArray = action_post.getJSONArray("image");
                    mTvIndicator.setText("1 / " + jArray.length());
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        String url = jObject.getString("url");
                        mAdapter.add(url);
                    }
                    mAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    /**
     * http Delete
     * 실천 인증 상세 조회
     */
    private void httpDeleteActionPost() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityActionPost.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(mPostIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(ActivityActionPost.this).Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    finish();
                }
            }
        });
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @OnClick({R.id.ll_comment, R.id.ll_cheering, R.id.iv_action_more, R.id.iv_comment,R.id.ll_comment_detail})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_comment:
                intent = new Intent(ActivityActionPost.this, ActivityCommentDetail.class);
                intent.putExtra(ActivityCommentDetail.EXTRA_COMMENT_TYPE, ActivityCommentDetail.TYPE_ACTION_POST);
                intent.putExtra(ActivityCommentDetail.EXTRA_POST_INDEX, mBean.getIdx());
                intent.putExtra(ActivityCommentDetail.EXTRA_OFF_KEYBOARD,"OFF");
                startActivityForResult(intent, REQUEST_COMMENT_DETAIL);
                break;
            case R.id.iv_comment:
            case R.id.ll_comment_detail:
                intent = new Intent(ActivityActionPost.this, ActivityCommentDetail.class);
                intent.putExtra(ActivityCommentDetail.EXTRA_COMMENT_TYPE, ActivityCommentDetail.TYPE_ACTION_POST);
                intent.putExtra(ActivityCommentDetail.EXTRA_POST_INDEX, mBean.getIdx());
                intent.putExtra(ActivityCommentDetail.EXTRA_OFF_KEYBOARD,"OFF");
                startActivityForResult(intent, REQUEST_COMMENT_DETAIL);
                break;
            case R.id.ll_cheering:
                httpPatchLike(mBean.getIdx());
                break;
            case R.id.iv_action_more:
                showPopupMenu();
                break;
        }
    }

    /**
     * http patch
     * 응원하기 / 취소
     *
     * @param post_index
     */
    private void httpPatchLike(int post_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityActionPost.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOST_INDEX_LIKE_INDEX;
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(post_index));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(ActivityActionPost.this).Patch(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (!TextUtils.equals(code, SUCCESS) || Comm_Param.IS_TEST)
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
        if (requestCode == REQUEST_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                httpGetActionPost();
            }
        } else if (requestCode == REQUEST_COMMENT_DETAIL) {
            httpGetActionPost();
        }
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(ActivityActionPost.this, mIvActionMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_action_post, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                AlertDialog.Builder builder;
                AlertDialog dialog;
                Intent intent = null;
                switch (id) {
                    case R.id.menu_level_reset:
                        intent = new Intent(ActivityActionPost.this, ActivityAddActionPost.class);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_TYPE, ActivityAddActionPost.TYPE_RESET_LEVEL);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_INDEX, mPostIndex);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_CONTENTS, mBean);
                        startActivityForResult(intent, REQUEST_ACTIVITY);
                        break;
                    case R.id.menu_edit:
                        intent = new Intent(ActivityActionPost.this, ActivityAddActionPost.class);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_TYPE, ActivityAddActionPost.TYPE_EDIT_ACTION_POST);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_INDEX, mPostIndex);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_CONTENTS, mBean);
                        startActivityForResult(intent, REQUEST_ACTIVITY);
                        break;
                    case R.id.menu_delete:
                        builder = new AlertDialog.Builder(ActivityActionPost.this)
                                .setTitle("실천 인증 삭제")
                                .setMessage("인증을 삭제하시겠습니까?")
                                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        httpDeleteActionPost();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog = builder.create();
                        dialog.show();
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }

}
