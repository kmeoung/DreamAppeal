package com.truevalue.dreamappeal.activity.profile;

import android.content.Intent;
import android.graphics.Point;
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
import com.truevalue.dreamappeal.bean.BeanObjectStepHeader;
import com.truevalue.dreamappeal.bean.BeanObjectStepOtherHeader;
import com.truevalue.dreamappeal.bean.BeanObjectStepSubHeader;
import com.truevalue.dreamappeal.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityObjectStep extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

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
        setContentView(R.layout.activity_object_step);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        // Adapter 초기화
        initAdapter();

    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityObjectStep.this, this);
        mRvAchivementIng.setAdapter(mAdapter);
        // 임시 데이터
        bindTempData();

        GridLayoutManager glm = new GridLayoutManager(ActivityObjectStep.this, 3, RecyclerView.VERTICAL, false);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (mAdapter.get(i) instanceof BeanObjectStepHeader
                        || mAdapter.get(i) instanceof BeanObjectStepSubHeader
                        || mAdapter.get(i) instanceof BeanObjectStepOtherHeader)
                    return 3;
                else return 1;
            }
        });
        mRvAchivementIng.setLayoutManager(glm);
    }

    private void bindTempData() {

        mAdapter.add(new BeanObjectStepHeader()); // Header

        int sub_position = 1;
        mAdapter.add(new BeanObjectStepSubHeader(sub_position)); // Sub Header
        for (int i = 0; i < 5; i++) {
            mAdapter.add("");
        }
        sub_position++;
        mAdapter.add(new BeanObjectStepSubHeader(sub_position)); // Sub Header
        for (int i = 0; i < 4; i++) {
            mAdapter.add("");
        }
        sub_position++;
        mAdapter.add(new BeanObjectStepSubHeader(sub_position)); // Sub Header
        for (int i = 0; i < 3; i++) {
            mAdapter.add("");
        }
        mAdapter.add(new BeanObjectStepOtherHeader()); // Other Header
        for (int i = 0; i < 2; i++) {
            mAdapter.add("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_TITLE_HEADER == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_header, parent, false);
        else if (TYPE_HEADER_SUB == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_sub_header, parent, false);
        else if (TYPE_HEADER_OTHER_SUB == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_other_header, parent, false);
        else if (TYPE_IMAGE == viewType)
            return BaseViewHolder.newInstance(R.layout.listitem_object_step_image, parent, false);
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if (getItemViewType(i) == TYPE_TITLE_HEADER) {
            TextView tvDetailStep = h.getItemView(R.id.tv_detail_step);
            tvDetailStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityObjectStep.this, ActivityAddContents.class);
                    intent.putExtra(ActivityAddContents.STR_TITLE, "목표 달성을 위해 세부단계를 만들어주세요");
                    startActivity(intent);
                    // TODO : Activity 애니메이션 없애기
//                    overridePendingTransition(0, 0);
                }
            });
        } else if (getItemViewType(i) == TYPE_HEADER_SUB) {
            BeanObjectStepSubHeader bean = (BeanObjectStepSubHeader) mAdapter.get(i);
            TextView tvPosition = h.getItemView(R.id.tv_position);
            tvPosition.setText(String.valueOf(bean.getPosition()));
        } else if (getItemViewType(i) == TYPE_IMAGE) {
            ImageView iv = h.getItemView(R.id.iv_image);
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
        else if (mAdapter.get(i) instanceof BeanObjectStepOtherHeader)
            return TYPE_HEADER_OTHER_SUB;
        else if (mAdapter.get(i) instanceof String)
            return TYPE_IMAGE;
        else return 0;

    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {
    }

    @OnClick({R.id.iv_comment, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_comment:
                Intent intent = new Intent(ActivityObjectStep.this,ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.iv_profile:
                break;
        }
    }
}
