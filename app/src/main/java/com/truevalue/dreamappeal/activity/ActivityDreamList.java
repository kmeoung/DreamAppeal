package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import butterknife.OnClick;

public class ActivityDreamList extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.ibtn_add_dream_list)
    ImageButton mIbtnAddDreamList;
    @BindView(R.id.rv_dream_list)
    RecyclerView mRvDreamList;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    private BaseRecyclerViewAdapter mAdapter;
    private boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_list);
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
        mAdapter = new BaseRecyclerViewAdapter(ActivityDreamList.this, this);
        mRvDreamList.setAdapter(mAdapter);
        mRvDreamList.setLayoutManager(new LinearLayoutManager(ActivityDreamList.this));
        BaseItemDecorationVertical item = new BaseItemDecorationVertical(ActivityDreamList.this,6);
        mRvDreamList.addItemDecoration(item);
        // Set Edit Mode
        isEditMode(false);
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
     * 수정 모드에 따라 설정이 변경
     *
     * @param isEdit
     */
    private void isEditMode(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) { // 수정 모드일 경우
            mIbtnAddDreamList.setColorFilter(R.color.gray);
            mIbtnAddDreamList.setEnabled(false);
            mBtnEdit.setText("확인");
        } else { // 수정 모드가 아닐 경우
            mIbtnAddDreamList.setColorFilter(R.color.colorAccent);
            mIbtnAddDreamList.setEnabled(true);
            mBtnEdit.setText("편집");
        }
        mAdapter.notifyDataSetChanged();
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
        return BaseViewHolder.newInstance(R.layout.listitem_dream_list, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

        LinearLayout llItemView = h.getItemView(R.id.ll_dream_list_item);
        ImageView ivDelete = h.getItemView(R.id.iv_delete);

        if (isEdit) { // 수정 모드일 경우
            llItemView.setBackground(getResources().getDrawable(R.drawable.dream_list_box_black));
            ivDelete.setVisibility(View.VISIBLE);
        } else { // 수정 모드가 아닐 경우
            llItemView.setBackground(getResources().getDrawable(R.drawable.dream_list_box_gray));
            ivDelete.setVisibility(View.GONE);
        }

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

    @OnClick({R.id.ibtn_add_dream_list, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_add_dream_list: // 드림 리스트 추가 버튼
                // 버튼이 활성화 되어 있을 경우에만
                if(mIbtnAddDreamList.isEnabled()){

                }
                break;
            case R.id.btn_edit: // 수정 버튼
                isEdit = !isEdit;
                isEditMode(isEdit);
                break;
        }
    }
}
