package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import butterknife.OnClick;

public class ActivityNormalProfile extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.et_age)
    EditText mEtAge;
    @BindView(R.id.btn_male)
    Button mBtnMale;
    @BindView(R.id.btn_female)
    Button mBtnFemale;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.iv_add_affiliation)
    ImageView mIvAddAffiliation;
    @BindView(R.id.rv_affiliation)
    RecyclerView mRvAffiliation;

    private BaseRecyclerViewAdapter mAdapter;
    private boolean isGender = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_profile);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        mBtbBar.setIOBaseTitleBarListener(this);
        initAdapter();
        setGender();
    }

    private void setGender(){
        if(isGender){
            mBtnMale.setSelected(false);
            mBtnFemale.setSelected(true);
        }else{
            mBtnMale.setSelected(true);
            mBtnFemale.setSelected(false);
        }
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityNormalProfile.this, this);
        mRvAffiliation.setAdapter(mAdapter);
        mRvAffiliation.setLayoutManager(new LinearLayoutManager(ActivityNormalProfile.this));

        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickRightTextBtn() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_profile_affiliation, parent, false);
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
        return 0;
    }

    @OnClick({R.id.btn_male, R.id.btn_female, R.id.iv_add_affiliation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_male:
                if(isGender) isGender = false;
                setGender();
                break;
            case R.id.btn_female:
                if(!isGender) isGender = true;
                setGender();
                break;
            case R.id.iv_add_affiliation:
                break;
        }
    }
}
