package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import okhttp3.Call;

public class ActivityCommentDetail extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.rv_comment)
    RecyclerView mRvComment;
    private BaseRecyclerViewAdapter mAdapter;
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
        // 내 꿈 소개 댓글 조회
        httpGetPresentComment();
    }

    public String check(String str,int n){
        char[] c = str.toCharArray();
        String text = "";
        for (int i = 0; i < c.length; i++) {
            char newC = ' ';
            if(c[i] != ' ') {
                newC = (char) ((int) c[i] + n);
            }else newC = c[i];
            text += newC;
        }
        return text;
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
     * http Get
     * 내 꿈 소개 댓글 조회
     */
    private void httpGetPresentComment() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityCommentDetail.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_PRESENTCOMMENTS
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

                        if(TextUtils.equals(code,SUCCESS)){
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
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        finish();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        onBackPressed();
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
}
