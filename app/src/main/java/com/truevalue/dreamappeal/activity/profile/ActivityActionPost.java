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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.bean.BeanActionPostProfile;
import com.truevalue.dreamappeal.fragment.profile.blueprint.FragmentAddContents;
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
    @BindView(R.id.iv_more)
    ImageView mIvMore;

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
                    BeanActionPostProfile actionPostProfile = gson.fromJson(json.getJSONObject("profile").toString(), BeanActionPostProfile.class);
                    if (TextUtils.isEmpty(actionPostProfile.getImage()))
                        Glide.with(ActivityActionPost.this).load(R.drawable.drawer_user).into(mIvDreamProfile);
                    else
                        Glide.with(ActivityActionPost.this).load(actionPostProfile.getImage()).placeholder(R.drawable.drawer_user).into(mIvDreamProfile);
                    mTvValueStyle.setText(actionPostProfile.getValue_style());
                    mTvJob.setText(actionPostProfile.getJob());
                    BeanActionPostDetail bean = gson.fromJson(json.getJSONObject("action_post").toString(), BeanActionPostDetail.class);

//                    if (!TextUtils.isEmpty(actionPostProfile.getImage()))
//                        Glide.with(ActivityActionPost.this).load(R.drawable.user).into(mIvImg);
//                    else
//                        Glide.with(ActivityActionPost.this).load(bean.get()).placeholder(R.drawable.user).into(mIvImg);
                    mTvComment.setText(bean.getComment_count() + "개");
                    mTvCheering.setText(bean.getLike_count() + "개");
                    mTvContents.setText(bean.getContent());
                    mTvTarget.setText(bean.getObject_name());
                    mTvTargetDetail.setText(bean.getStep_name());
                    mIvCheering.setSelected(bean.getStatus());

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
        DAHttpClient.getInstance().Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityActionPost.this, message, Toast.LENGTH_SHORT).show();

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

    @OnClick({R.id.ll_comment, R.id.ll_cheering, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_comment:
                Intent intent = new Intent(ActivityActionPost.this, ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering:
                break;
            case R.id.iv_more:

                break;
        }
    }

    private void showPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(ActivityActionPost.this, mIvMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_object_step, popupMenu.getMenu());

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
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_TYPE,ActivityAddActionPost.TYPE_RESET_LEVEL);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_INDEX,mPostIndex);
                        startActivity(intent);
                        break;
                    case R.id.menu_edit:
                        intent = new Intent(ActivityActionPost.this, ActivityAddActionPost.class);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_TYPE,ActivityAddActionPost.TYPE_EDIT_ACTION_POST);
                        intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_INDEX,mPostIndex);
                        startActivity(intent);
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
