package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseItemDecorationVertical;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanComment;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityCommentDetail extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    public static final String EXTRA_COMMENT_TYPE = "EXTRA_COMMENT_TYPE";
    public static final String EXTRA_POST_INDEX = "EXTRA_POST_INDEX";

    public static final int TYPE_DREAM_PRESENT = 0;
    public static final int TYPE_PERFORMANCE = 1;
    public static final int TYPE_BLUEPRINT = 2;
    public static final int TYPE_ACTION_POST = 3;

    private final int LISTITEM_COMMENT = 0;
    private final int LISTITEM_REPLY = 1;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.rv_comment)
    RecyclerView mRvComment;
    @BindView(R.id.iv_user)
    ImageView mIvUser;
    @BindView(R.id.et_input_comment)
    EditText mEtInputComment;
    @BindView(R.id.btn_commit_comment)
    Button mBtnCommitComment;
    @BindView(R.id.tv_writer)
    TextView mTvWriter;
    @BindView(R.id.iv_writer_reply_close)
    ImageView mIvWriterReplyClose;
    @BindView(R.id.ll_writer)
    LinearLayout mLlWriter;
    private BaseRecyclerViewAdapter mAdapter;
    private int mType = -1;
    private boolean isEdit = false;
    private int mCommentIndex = -1;
    private int mPostCount = -1;
    private String mGetCommentUrl = null;
    private String mCommentUrl = null;
    private String mCheeringUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        // Init Adapter
        initAdapter();
        // 데이터 초기화
        initData();
    }

    private void initData() {
        mType = getIntent().getIntExtra(EXTRA_COMMENT_TYPE, -1);
        switch (mType) {
            case TYPE_DREAM_PRESENT:
                mGetCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_INDEX_PRESENTCOMMENTS;
                mCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_PRESENTCOMMENTS;
                mCheeringUrl = Comm_Param.URL_API_PROFILES_INDEX_PRESENTCOMMENTS_INDEX_MYINDEX;
                break;
            case TYPE_PERFORMANCE:
                mPostCount = getIntent().getIntExtra(EXTRA_POST_INDEX, -1);
                mGetCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_INDEX_PERFORMANCE_INDEX_COMMENTS.replace(Comm_Param.POST_INDEX,String.valueOf(mPostCount));
                mCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_PERFORMANCE.replace(Comm_Param.POST_INDEX,String.valueOf(mPostCount));
                mCheeringUrl = Comm_Param.URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS_INDEX_MYINDEX;
                break;
            case TYPE_BLUEPRINT:
                mGetCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_INDEX_BLUEPRINTCOMMENTS;
                mCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS;
                mCheeringUrl = Comm_Param.URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS_INDEX_MYINDEX;
                break;
            case TYPE_ACTION_POST:
                mPostCount = getIntent().getIntExtra(EXTRA_POST_INDEX, -1);
                mGetCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_INDEX_ACTIONPOST_INDEX_COMMENTS.replace(Comm_Param.POST_INDEX,String.valueOf(mPostCount));
                mCommentUrl = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOST_INDEX_COMMENTS.replace(Comm_Param.POST_INDEX,String.valueOf(mPostCount));
                mCheeringUrl = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS_INDEX_MYINDEX;
                break;
        }
        // 댓글 조회
        httpGetComment();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityCommentDetail.this, this);
        mRvComment.setAdapter(mAdapter);
        mRvComment.setLayoutManager(new LinearLayoutManager(ActivityCommentDetail.this));
        BaseItemDecorationVertical item = new BaseItemDecorationVertical(ActivityCommentDetail.this, 20);
        mRvComment.addItemDecoration(item);
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return BaseViewHolder.newInstance(R.layout.listitem_comment, parent, false);
        } else if (viewType == 1) {
            return BaseViewHolder.newInstance(R.layout.listitem_comment_reply, parent, false);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        BeanComment bean = (BeanComment) mAdapter.get(i);
        if (getItemViewType(i) == LISTITEM_COMMENT) {
            TextView tvValueStyle = h.getItemView(R.id.tv_value_style);
            TextView tvJob = h.getItemView(R.id.tv_job);
            TextView tvComment = h.getItemView(R.id.tv_comment);
            TextView tvAddReply = h.getItemView(R.id.tv_add_reply);
            LinearLayout llCheering = h.getItemView(R.id.ll_cheering);
            ImageView ivCheering = h.getItemView(R.id.iv_cheering);
            TextView tvCheering = h.getItemView(R.id.tv_cheering);
            TextView tvCommentTime = h.getItemView(R.id.tv_comment_time);

            tvValueStyle.setText(bean.getValue_style());
            tvJob.setText(bean.getJob());
            tvComment.setText(bean.getContent());
            ivCheering.setSelected(bean.getStatus());
            tvCheering.setText(bean.getLike_count() + "개");

            llCheering.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    httpPatchCheering(bean.getIdx());
                }
            });

            tvAddReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCommentIndex = bean.getIdx();
                    mLlWriter.setVisibility(View.VISIBLE);
                    mTvWriter.setText(bean.getName());
                }
            });
        } else if (getItemViewType(i) == LISTITEM_REPLY) {
            TextView tvName = h.getItemView(R.id.tv_name);
            TextView tvTag = h.getItemView(R.id.tv_tag);
            TextView tvComment = h.getItemView(R.id.tv_comment);
            TextView tvCommentTime = h.getItemView(R.id.tv_comment_time);

            LinearLayout llCheering = h.getItemView(R.id.ll_cheering);
            ImageView ivCheering = h.getItemView(R.id.iv_cheering);
            TextView tvCheering = h.getItemView(R.id.tv_cheering);

            tvName.setText(bean.getName());
            tvTag.setText("@" + bean.getParent_name());
            tvComment.setText(bean.getContent());
            ivCheering.setSelected(bean.getStatus());
            tvCheering.setText(bean.getLike_count() + "개");

            llCheering.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    httpPatchCheering(bean.getIdx());
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
        BeanComment bean = (BeanComment) mAdapter.get(i);
        if (bean.getParent_idx() == 0) return LISTITEM_COMMENT;
        else return LISTITEM_REPLY;
    }

    /**
     * http Get
     * 내 꿈 소개 댓글 조회
     */
    private void httpGetComment() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = mGetCommentUrl
                .replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()))
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance()
                .Get(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(ActivityCommentDetail.this, message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            mAdapter.clear();
                            JSONObject json = new JSONObject(body);
                            JSONArray array = json.getJSONArray("comments");
                            Gson gson = new Gson();
                            ArrayList<BeanComment> parent = new ArrayList<>();
                            LinkedHashMap<Integer, ArrayList<BeanComment>> reply = new LinkedHashMap<>();
                            for (int i = 0; i < array.length(); i++) {
                                BeanComment bean = gson.fromJson(array.getJSONObject(i).toString(), BeanComment.class);
                                if (bean.getParent_idx() == 0) {
                                    parent.add(bean);
                                } else {
                                    if (reply.get(bean.getParent_idx()) == null)
                                        reply.put(bean.getParent_idx(), new ArrayList<>());
                                    reply.get(bean.getParent_idx()).add(bean);
                                }
                            }

                            for (int i = 0; i < parent.size(); i++) {
                                BeanComment bean = parent.get(i);
                                mAdapter.add(bean);

                                if (reply.get(bean.getIdx()) != null) {
                                    for (int j = 0; j < reply.get(bean.getIdx()).size(); j++) {
                                        BeanComment replyBean = reply.get(bean.getIdx()).get(j);
                                        replyBean.setParent_name(bean.getName());
                                        mAdapter.add(replyBean);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /**
     * http Post
     * 댓글 등록
     *
     * @param parent_index 하위 댓글일 경우 index 실어서 보냄 없으면 -1
     */
    private void httpPostComment(int parent_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = mCommentUrl
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("writer_idx", String.valueOf(prefs.getMyProfileIndex()));
        body.put("content", mEtInputComment.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        body.put("register_date", sdf.format(new Date()));
        if (parent_index != -1) {
            body.put("parent_idx", String.valueOf(parent_index));
        }
        DAHttpClient.getInstance()
                .Post(url, header, body, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(ActivityCommentDetail.this, message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            mEtInputComment.setText("");
                            mCommentIndex = -1;
                            mLlWriter.setVisibility(View.GONE);

                            httpGetComment();
                        }
                    }
                });
    }

    /**
     * http Patch
     * 댓글 좋아요
     * @param comment_index
     */
    private void httpPatchCheering(int comment_index){
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = mCheeringUrl
                .replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()))
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()))
                .replace(Comm_Param.COMMENTS_INDEX,String.valueOf(comment_index));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance()
                .Patch(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(ActivityCommentDetail.this, message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            mEtInputComment.setText("");
                            mCommentIndex = -1;
                            mLlWriter.setVisibility(View.GONE);

                            httpGetComment();
                        }
                    }
                });
    }

    /**
     * http Post
     * 댓글 등록
     */
    private void httpPostComment() {
        httpPostComment(-1);
    }

    @OnClick({R.id.iv_writer_reply_close, R.id.btn_commit_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_writer_reply_close:
                mCommentIndex = -1;
                mLlWriter.setVisibility(View.GONE);
                break;
            case R.id.btn_commit_comment:
                if (TextUtils.isEmpty(mEtInputComment.getText().toString())) {
                    Toast.makeText(this, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                if (mCommentIndex == -1) {
                    // 댓글 등록
                    httpPostComment();
                } else {
                    // 대 댓글 등록
                    httpPostComment(mCommentIndex);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
