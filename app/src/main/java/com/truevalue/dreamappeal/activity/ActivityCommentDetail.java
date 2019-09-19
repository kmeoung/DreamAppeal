package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityCommentDetail extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    public static final String EXTRA_COMMENT_TYPE = "EXTRA_COMMENT_TYPE";

    public static final int TYPE_DREAM_PRESENT = 0;

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
    private BaseRecyclerViewAdapter mAdapter;
    private int mType = -1;
    private boolean isEdit = false;

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
                // 내 꿈 소개 댓글 조회
                httpGetPresentComment();
                break;
        }
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

        // 뷰를 클릭했을 경우
        h.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    /**
     * http Get
     * 내 꿈 소개 댓글 조회
     */
    private void httpGetPresentComment() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_INDEX_PRESENTCOMMENTS
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
                            for (int i = 0; i < array.length(); i++) {
                                BeanComment bean = gson.fromJson(array.getJSONObject(i).toString(), BeanComment.class);
                                mAdapter.add(bean);
                            }
                        }
                    }
                });
    }

    /**
     * http Post
     * 내 꿈 소개 댓글 등록
     *
     * @param parent_index 하위 댓글일 경우 index 실어서 보냄 없으면 -1
     */
    private void httpPostPresentComment(int parent_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_PRESENTCOMMENTS
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("writer_idx", String.valueOf(prefs.getMyProfileIndex()));
        body.put("content", mEtInputComment.getText().toString());
        if (parent_index != -1) {
            body.put("parent_idx", String.valueOf(parent_index));
        }
        DAHttpClient.getInstance()
                .Post(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(ActivityCommentDetail.this, message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {

                        }
                    }
                });
    }

    /**
     * http Post
     * 내 꿈 소개 댓글 등록
     */
    private void httpPostPresentComment() {
        httpPostPresentComment(-1);
    }

    @OnClick(R.id.btn_commit_comment)
    public void onViewClicked() {
        switch (mType) {
            case TYPE_DREAM_PRESENT:
                // 내 꿈 소개 댓글 조회
                httpPostPresentComment();
                break;
        }
    }
}
