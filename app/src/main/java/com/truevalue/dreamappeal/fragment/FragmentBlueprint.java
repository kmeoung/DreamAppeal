package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAbilityOpportunity;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentBlueprint extends BaseFragment {

    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.rv_ability_opportunity)
    RecyclerView mRvAbilityOpportunity;
    @BindView(R.id.btn_abliity_opportunity)
    Button mBtnAbliityOpportunity;
    @BindView(R.id.iv_add_object)
    ImageView mIvAddObject;
    @BindView(R.id.rv_object)
    RecyclerView mRvObject;
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
    @BindView(R.id.ll_ability_opportunity)
    LinearLayout mLlAbilityOpportunity;

    private BaseRecyclerViewAdapter mAbilityOpprotunityAdapter;
    private BaseRecyclerViewAdapter mObjectAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blueprint, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();

        bindTempData();
    }

    private void initAdapter() {
        mAbilityOpprotunityAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_dream_description, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

            }

            @Override
            public int getItemCount() {
                if (mAbilityOpprotunityAdapter != null)
                    return mAbilityOpprotunityAdapter.size();
                else return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mObjectAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_object, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

            }

            @Override
            public int getItemCount() {
                if (mObjectAdapter != null)
                    return mObjectAdapter.size();
                else return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mRvAbilityOpportunity.setAdapter(mAbilityOpprotunityAdapter);
        mRvAbilityOpportunity.setLayoutManager(new LinearLayoutManager(getContext()));

        mRvObject.setAdapter(mObjectAdapter);
        mRvObject.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 3; i++) {
            mAbilityOpprotunityAdapter.add("");
        }

        for (int i = 0; i < 10; i++) {
            mObjectAdapter.add("");
        }
    }

    @OnClick({R.id.iv_add_object, R.id.btn_commit_comment, R.id.ll_ability_opportunity})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_add_object:
                break;
            case R.id.btn_commit_comment:
                break;
            case R.id.ll_ability_opportunity: // 갖출 능력 / 만들고픈 기회
                intent = new Intent(getContext(), ActivityAbilityOpportunity.class);
                startActivity(intent);
                break;
        }
    }
}
