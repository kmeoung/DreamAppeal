package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.activity.profile.ActivityActionPost;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanActionPost;
import com.truevalue.dreamappeal.bean.BeanObjectStepHeader;
import com.truevalue.dreamappeal.bean.BeanObjectStepSubHeader;
import com.truevalue.dreamappeal.fragment.FragmentMain;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class FragmentObjectStep extends BaseFragment implements IOBaseTitleBarListener, IORecyclerViewListener {

    private final int TYPE_TITLE_HEADER = 1;
    private final int TYPE_HEADER_SUB = 2;
    private final int TYPE_IMAGE = 3;
    private final int TYPE_DEFAULT = 4;

    private final int REQUEST_ACTION_POST_DETAIL = 4050;

    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.rv_achivement_ing)
    RecyclerView mRvAchivementIng;
    @BindView(R.id.iv_profile)
    ImageView mIvProfile;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_commit_comment)
    ImageButton mBtnCommitComment;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.rl_comment)
    RelativeLayout mRlComment;


    private BaseRecyclerViewAdapter mAdapter;
    private int mObjectIndex = -1;

    public static FragmentObjectStep newInstance(int object_index) {
        FragmentObjectStep fragment = new FragmentObjectStep();
        fragment.mObjectIndex = object_index;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        initView();
        // Adapter 초기화
        initAdapter();
    }

    private void initView() {
        mBtnCommitComment.setSelected(true);
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtComment.getText().length() > 0) {
                    mBtnCommitComment.setVisibility(View.VISIBLE);
                    mRlComment.setVisibility(View.GONE);
                } else {
                    mBtnCommitComment.setVisibility(View.GONE);
                    mRlComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 서버 초기화
        httpGetObjects();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FragmentMain.REQUEST_BLUEPRINT_COMMENT) {
            httpGetObjects();
        } else if (requestCode == REQUEST_ACTION_POST_DETAIL) {
            if (resultCode == RESULT_OK) {
                httpGetObjects();
            }
        }
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvAchivementIng.setAdapter(mAdapter);

        GridLayoutManager glm = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (mAdapter.get(i) instanceof BeanObjectStepHeader
                        || mAdapter.get(i) instanceof BeanObjectStepSubHeader
                        || mAdapter.get(i) instanceof String)
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
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILE_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (!TextUtils.equals(code, SUCCESS) || Comm_Param.REAL)
                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    mAdapter.clear();
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(body);
                    String image = json.getString("user_image");
                    if (TextUtils.isEmpty(image))
                        Glide.with(getContext()).load(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(mIvProfile);
                    else
                        Glide.with(getContext()).load(image).placeholder(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(mIvProfile);
                    int commentCount = json.getInt("comment_count");
                    if (commentCount < 1000) {
                        mTvComment.setText(commentCount + "");
                    } else {
                        int k = (commentCount / 1000);
                        if (k < 1000) {
                            mTvComment.setText(k + "K");
                        } else {
                            int m = (k / 1000);
                            mTvComment.setText(m + "M");
                        }
                    }
                    // todo : 임시 이미지
                    JSONObject object = json.getJSONObject("object");
                    int total_action_post_count = json.getInt("total_action_post_count");
                    BeanObjectStepHeader header = gson.fromJson(object.toString(), BeanObjectStepHeader.class);
                    header.setTotal_action_post_count(total_action_post_count);
                    mAdapter.add(header);
                    JSONArray noneStep = null;
                    try {
                        noneStep = json.getJSONArray("none_step_action_post");
                        for (int i = 0; i < noneStep.length(); i++) {
                            BeanActionPost post = gson.fromJson(noneStep.getJSONObject(i).toString(), BeanActionPost.class);
                            mAdapter.add(post);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONArray object_steps = null;
                    try {
                        object_steps = json.getJSONArray("object_steps");
                        if ((noneStep == null || noneStep.length() < 1) && (object_steps == null || object_steps.length() < 1))
                            mAdapter.add("Default");
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (object_steps == null || object_steps.length() < 1) mAdapter.add("세부단계");
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
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext()).Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    /**
     * http Delete
     * 실천 목표 세부단계 삭제
     */
    private void httpDeleteObjectStep(int step_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));
        url = url.replace(Comm_Param.STEPS_INDEX, String.valueOf(step_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext()).Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetObjects();
                }
            }
        });
    }

    /**
     * http Patch
     * 실천 목표 수정
     *
     * @param isComplete true 면 완료 될 때 false 면 완료 취소 할 때
     */
    private void httpPatchObject(boolean isComplete) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OBJECTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(mObjectIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        if (isComplete) {
            body.put("complete", "1");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            body.put("complete_date", sdf.format(new Date()));
        } else {
            body.put("complete", "0");
            body.put("complete_date", null);
        }
        DAHttpClient.getInstance(getContext()).Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetObjects();
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
        else if (TYPE_DEFAULT == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_deafult_text, parent, false);
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        if (getItemViewType(i) == TYPE_TITLE_HEADER) {
            BeanObjectStepHeader bean = (BeanObjectStepHeader) mAdapter.get(i);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            TextView tvDetailStep = h.getItemView(R.id.tv_detail_step);
            ImageView ivMore = h.getItemView(R.id.iv_more);
            TextView tvTotalCount = h.getItemView(R.id.tv_total_count);
            LinearLayout llComplete = h.getItemView(R.id.ll_complete);

            if (prefs.getProfileIndex() == prefs.getMyProfileIndex()) {
                ivMore.setVisibility(View.VISIBLE);
                tvDetailStep.setVisibility(View.VISIBLE);
            } else {
                ivMore.setVisibility(View.GONE);
                tvDetailStep.setVisibility(View.GONE);
            }

            tvTitle.setText(bean.getObject_name());
            tvTotalCount.setText(String.format("총 인증 %d회", bean.getTotal_action_post_count()));

            tvDetailStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.equals(bean.getComplete(), "0")) {
                        ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddContents.newInstance("실천목표에 세부단계 등록하기", bean.getIdx()), true);
                    }
                }

            });

            // todo : 컴플리트 이미지 필요

            PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_object_step, popupMenu.getMenu());

            if (TextUtils.equals(bean.getComplete(), "1")) {
                popupMenu.getMenu().findItem(R.id.menu_complete).setTitle("완료취소");
                llComplete.setVisibility(View.VISIBLE);
            } else {
                popupMenu.getMenu().findItem(R.id.menu_complete).setTitle("완료하기");
                llComplete.setVisibility(View.GONE);
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    AlertDialog.Builder builder;
                    AlertDialog dialog;
                    switch (id) {
                        case R.id.menu_complete:
                            if (TextUtils.equals(bean.getComplete(), "1")) {
                                httpPatchObject(false);
                            } else {
                                httpPatchObject(true);
                            }
                            break;
                        case R.id.menu_edit:
                            ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddContents.newInstance("꿈에 맞는 실천 목표를 세워보세요",
                                    bean.getObject_name(), bean.getIdx()), true);
                            break;
                        case R.id.menu_delete:
                            builder = new AlertDialog.Builder(getContext())
                                    .setTitle("실천 목표 삭제")
                                    .setMessage("목표를 삭제하시겠습니까?\n\n목표를 삭제하게 되면 하위에 있는 모든 게시물들이 삭제됩니다.")
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

            PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_achivement_post, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    AlertDialog.Builder builder;
                    AlertDialog dialog;
                    switch (id) {
                        case R.id.menu_edit:
                            ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddContents.newInstance("실천목표에 세부단계 등록하기",
                                    bean.getTitle(), bean.getObject_idx(), bean.getIdx()), true);
                            break;
                        case R.id.menu_delete:
                            builder = new AlertDialog.Builder(getContext())
                                    .setTitle("실천 목표 세부사항 삭제")
                                    .setMessage("세부사항을 삭제하시겠습니까?\n\n세부사항을 삭제하게 되면 하위에 있는 모든 게시물들이 삭제됩니다.")
                                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            httpDeleteObjectStep(bean.getIdx());
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

            if (prefs.getProfileIndex() == prefs.getMyProfileIndex()) {
                ivMore.setVisibility(View.VISIBLE);
            } else {
                ivMore.setVisibility(View.GONE);
            }

            ivMore.setOnClickListener(view -> {
                popupMenu.show();
            });

            tvPosition.setText(String.valueOf(bean.getPosition()));
            tvtitle.setText(bean.getTitle());
        } else if (getItemViewType(i) == TYPE_IMAGE) {
            BeanActionPost bean = (BeanActionPost) mAdapter.get(i);
            ImageView iv = h.getItemView(R.id.iv_image);
            if (TextUtils.isEmpty(bean.getThumbnail_image()))
                Glide.with(this).load(R.drawable.ic_image_black_24dp).into(iv);
            else
                Glide.with(this).load(bean.getThumbnail_image()).placeholder(R.drawable.ic_image_black_24dp).into(iv);
            Point point = Utils.getDisplaySize(getActivity());
            Utils.setResizeView(iv, point.x / 3, point.x / 3);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityActionPost.class);
                    intent.putExtra(ActivityActionPost.EXTRA_ACTION_POST_INDEX, bean.getIdx());
                    startActivityForResult(intent, REQUEST_ACTION_POST_DETAIL);
                }
            });
        } else if (getItemViewType(i) == TYPE_DEFAULT) {
            TextView tvDefault = h.getItemView(R.id.tv_default_text);
            String str1 = "세부단계를 추가해 작은 것부터 실천해보세요";
            String str2 = "실천을 인증하면 여기에 표시됩니다";
//            SpannableStringBuilder spannable1 = Utils.replaceTextColor(getContext(), str1, "세부단계");
            SpannableStringBuilder spannable2 = Utils.replaceTextColor(getContext(), str2, R.color.light_gray, str2);
            tvDefault.setText(TextUtils.concat(str1, "\n\n", spannable2));
            tvDefault.setBackground(null);

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
        else if (mAdapter.get(i) instanceof String)
            return TYPE_DEFAULT;
        else return 0;

    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick({R.id.iv_comment, R.id.iv_profile, R.id.btn_commit_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_comment:
                Intent intent = new Intent(getContext(), ActivityCommentDetail.class);
                intent.putExtra(ActivityCommentDetail.EXTRA_COMMENT_TYPE, ActivityCommentDetail.TYPE_BLUEPRINT);
                intent.putExtra(ActivityCommentDetail.EXTRA_OFF_KEYBOARD, "OFF");
                startActivityForResult(intent, FragmentMain.REQUEST_BLUEPRINT_COMMENT);
                break;
            case R.id.iv_profile:
                break;
            case R.id.btn_commit_comment:
                httpPostComment();
                mEtComment.setText("");
                break;
        }
    }

    /**
     * http Post
     * 댓글 등록
     */
    private void httpPostComment() {
        if (TextUtils.isEmpty(mEtComment.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("writer_idx", String.valueOf(prefs.getMyProfileIndex()));
        body.put("content", mEtComment.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        body.put("register_date", sdf.format(new Date()));
        DAHttpClient.getInstance(getContext())
                .Post(url, header, body, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            httpGetObjects();
                            mEtComment.setText("");
                        }
                    }
                });
    }

}
