package com.truevalue.dreamappeal.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseItemDecorationVertical;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        updateStatusbarTranslate(mVStatus);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        // Init Adapter
        initAdapter();
        // Bind Temp Data
        bindTempData();
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
     * Bind Temp Data
     */
    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
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
        if (i == 1) {
            return 1;
        }
        return 0;
    }
}
