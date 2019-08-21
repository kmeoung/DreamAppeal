package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAchivementIng extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    private final int TYPE_TITLE_HEADER = 1;
    private final int TYPE_HEADER_SUB = 2;
    private final int TYPE_HEADER_OTHER_SUB = 3;
    private final int TYPE_IMAGE = 4;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivement_ing);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mVStatus);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        // Adapter 초기화
        initAdapter();
        // 임시 데이터
//        bindTempData();
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityAchivementIng.this, this);
        GridLayoutManager glm = new GridLayoutManager(ActivityAchivementIng.this,3,RecyclerView.VERTICAL,false);
        mRvAchivementIng.setLayoutManager(glm);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_achivement_ing_header, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0)
            return TYPE_TITLE_HEADER;
        else if (i == 1)
            return TYPE_HEADER_SUB;
        return TYPE_IMAGE;

    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {

    }
}
