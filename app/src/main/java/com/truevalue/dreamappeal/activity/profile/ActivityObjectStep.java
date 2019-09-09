package com.truevalue.dreamappeal.activity.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanActionPost;
import com.truevalue.dreamappeal.bean.BeanObjectHeader;
import com.truevalue.dreamappeal.bean.BeanObjectStepHeader;
import com.truevalue.dreamappeal.bean.BeanObjectStepSubHeader;
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

public class ActivityObjectStep extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    private final int TYPE_TITLE_HEADER = 1;
    private final int TYPE_HEADER_SUB = 2;
    private final int TYPE_IMAGE = 3;

    public static final String EXTRA_OBJECT_INDEX = "EXTRA_OBJECT_INDEX";

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.rv_achivement_ing)
    RecyclerView mRvAchivementIng;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.tv_comment_size)
    TextView mTvCommentSize;
    @BindView(R.id.iv_profile)
    ImageView mIvProfile;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_commit_comment)
    Button mBtnCommitComment;

    private BaseRecyclerViewAdapter mAdapter;
    private int mObjectIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_step);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initData();
        // Adapter 초기화
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpGetObjects();
    }

    private void initData() {
        mObjectIndex = getIntent().getIntExtra(EXTRA_OBJECT_INDEX, -1);
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityObjectStep.this, this);
        mRvAchivementIng.setAdapter(mAdapter);

        GridLayoutManager glm = new GridLayoutManager(ActivityObjectStep.this, 3, RecyclerView.VERTICAL, false);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (mAdapter.get(i) instanceof BeanObjectStepHeader
                        || mAdapter.get(i) instanceof BeanObjectStepSubHeader)
                    return 3;
                else return 1;
            }
        });
        mRvAchivementIng.setLayoutManager(glm);
    }

    /**
     * Http Get
     * 실천 목표 조회
     */
    private void httpGetObjects() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityObjectStep.this);
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityObjectStep.this, message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    mAdapter.clear();
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(body);
                    JSONObject object = json.getJSONObject("object");
                    int total_action_post_count = json.getInt("total_action_post_count");
                    BeanObjectStepHeader header = gson.fromJson(object.toString(), BeanObjectStepHeader.class);
                    header.setTotal_action_post_count(total_action_post_count);
                    mAdapter.add(header);

                    try {
                        JSONArray noneStep = json.getJSONArray("none_step_action_post");
                        for (int i = 0; i < noneStep.length(); i++) {
                            BeanActionPost post = gson.fromJson(noneStep.getJSONObject(i).toString(), BeanActionPost.class);
                            mAdapter.add(post);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray object_steps = json.getJSONArray("object_steps");
                    for (int i = 0; i < object_steps.length(); i++) {
                        JSONObject object_step = object_steps.getJSONObject(i);
                        BeanObjectStepSubHeader bean = gson.fromJson(object_step.toString(), BeanObjectStepSubHeader.class);
                        bean.setPosition(i + 1);
                        mAdapter.add(bean);

                        try {
                            JSONArray actionPosts = object_step.getJSONArray("action_posts");
                            for (int j = 0; j < actionPosts.length(); j++) {
                                BeanActionPost post = gson.fromJson(actionPosts.getJSONObject(j).toString(), BeanActionPost.class);
                                mAdapter.add(post);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * Http Delete
     * 실천 목표 삭제
     */
    private void httpDeleteObjects() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityObjectStep.this);
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityObjectStep.this, message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_TITLE_HEADER == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_header, parent, false);
        else if (TYPE_HEADER_SUB == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_sub_header, parent, false);
        else if (TYPE_IMAGE == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_image, parent, false);
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if (getItemViewType(i) == TYPE_TITLE_HEADER) {
            BeanObjectStepHeader bean = (BeanObjectStepHeader) mAdapter.get(i);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            TextView tvDetailStep = h.getItemView(R.id.tv_detail_step);
            ImageView ivMore = h.getItemView(R.id.iv_more);
            TextView tvTotalCount = h.getItemView(R.id.tv_total_count);

            tvTitle.setText(bean.getObject_name());
            // todo : 총 인증 추가 필요
            tvTotalCount.setText(String.format("총 인증 %d회", bean.getTotal_action_post_count()));

            tvDetailStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityObjectStep.this, ActivityAddContents.class);
                    intent.putExtra(ActivityAddContents.EXTRA_STR_TITLE, "목표 달성을 위해 세부단계를 만들어주세요");
                    intent.putExtra(ActivityAddContents.EXTRA_VIEW_TYPE, ActivityAddContents.EXTRA_TYPE_OBJECT_STEP);
                    startActivity(intent);
                    // TODO : Activity 애니메이션 없애기
//                    overridePendingTransition(0, 0);
                }
            });

            PopupMenu popupMenu = new PopupMenu(ActivityObjectStep.this, ivMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_achivement_post, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    AlertDialog.Builder builder;
                    AlertDialog dialog;
                    switch (id) {
                        case R.id.menu_edit:
                            Intent intent = new Intent(ActivityObjectStep.this, ActivityAddContents.class);
                            intent.putExtra(ActivityAddContents.EXTRA_STR_TITLE, "꿈에 맞는 실천 목표를 세워보세요");
                            intent.putExtra(ActivityAddContents.EXTRA_VIEW_TYPE, ActivityAddContents.EXTRA_TYPE_EDIT_OBJECTS);
                            startActivity(intent);
                            break;
                        case R.id.menu_delete:
                            builder = new AlertDialog.Builder(ActivityObjectStep.this)
                                    .setTitle("실천 목표 삭제")
                                    .setMessage("목표를 삭제하시겠습니까?")
                                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            httpDeleteObjects();
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

            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        } else if (getItemViewType(i) == TYPE_HEADER_SUB) {
            BeanObjectStepSubHeader bean = (BeanObjectStepSubHeader) mAdapter.get(i);
            TextView tvPosition = h.getItemView(R.id.tv_position);
            TextView tvtitle = h.getItemView(R.id.tv_title);
            ImageView ivMore = h.getItemView(R.id.iv_more);

            tvPosition.setText(String.valueOf(bean.getPosition()));
            tvtitle.setText(bean.getTitle());
        } else if (getItemViewType(i) == TYPE_IMAGE) {
            BeanActionPost bean = (BeanActionPost) mAdapter.get(i);
            ImageView iv = h.getItemView(R.id.iv_image);
            if (TextUtils.isEmpty(bean.getThumbnail_image()))
                Glide.with(this).load(R.drawable.ic_image_black_24dp).into(iv);
            else
                Glide.with(this).load(bean.getThumbnail_image()).thumbnail(R.drawable.ic_image_black_24dp).into(iv);
            Point point = Utils.getDisplaySize(this);
            Utils.setResizeView(iv, point.x / 3, point.x / 3);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityObjectStep.this, ActivityActionPost.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (mAdapter.get(i) instanceof BeanObjectStepHeader)
            return TYPE_TITLE_HEADER;
        else if (mAdapter.get(i) instanceof BeanObjectStepSubHeader)
            return TYPE_HEADER_SUB;
        else if (mAdapter.get(i) instanceof BeanActionPost)
            return TYPE_IMAGE;
        else return 0;

    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @OnClick({R.id.iv_comment, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_comment:
                Intent intent = new Intent(ActivityObjectStep.this, ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.iv_profile:
                break;
        }
    }
}
