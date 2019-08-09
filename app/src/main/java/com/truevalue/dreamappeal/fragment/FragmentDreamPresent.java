package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityDreamDescription;
import com.truevalue.dreamappeal.activity.ActivityDreamTitle;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentDreamPresent extends BaseFragment implements IORecyclerViewListener {


    BaseRecyclerViewAdapter mAdapter;
    @BindView(R.id.tv_dream_name)
    TextView mTvDreamName;
    @BindView(R.id.tv_dream_level)
    TextView mTvDreamLevel;
    @BindView(R.id.ll_dreams)
    LinearLayout mLlDreams;
    @BindView(R.id.tv_follwer)
    TextView mTvFollwer;
    @BindView(R.id.ll_follower)
    LinearLayout mLlFollower;
    @BindView(R.id.tv_achivement_post_count)
    TextView mTvAchivementPostCount;
    @BindView(R.id.tv_action_post_count)
    TextView mTvActionPostCount;
    @BindView(R.id.iv_dream_profile)
    ImageView mIvDreamProfile;
    @BindView(R.id.tv_value_style)
    TextView mTvValueStyle;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.ll_dream_title)
    LinearLayout mLlDreamTitle;
    @BindView(R.id.tv_dream_description)
    TextView mTvDreamDescription;
    @BindView(R.id.rv_dream_description)
    RecyclerView mRvDreamDescription;
    @BindView(R.id.btn_dream_description_more)
    Button mBtnDreamDescriptionMore;
    @BindView(R.id.tv_merit_and_motive)
    TextView mTvMeritAndMotive;
    @BindView(R.id.btn_merit_and_motive_more)
    Button mBtnMeritAndMotiveMore;
    @BindView(R.id.tv_cheering)
    TextView mTvCheering;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_cheering)
    LinearLayout mLlCheering;

    private boolean isMyDreamMore = false;
    private boolean isMyDreamReason = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_present, container, false);
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
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvDreamDescription.setAdapter(mAdapter);
        mRvDreamDescription.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 3; i++) {
            mAdapter.add("");
        }
        mTvMeritAndMotive.setMaxLines(3);

        mBtnDreamDescriptionMore.setOnClickListener(v -> {
            if (isMyDreamMore) {
                isMyDreamMore = false;
                mAdapter.clear();
                for (int i = 0; i < 3; i++) {
                    mAdapter.add("");
                }
            } else {
                isMyDreamMore = true;
                mAdapter.clear();
                for (int i = 0; i < 10; i++) {
                    mAdapter.add("");
                }
            }
        });


        mBtnMeritAndMotiveMore.setOnClickListener(v -> {
            if (isMyDreamReason) {
                isMyDreamReason = false;
                mTvMeritAndMotive.setMaxLines(3);
            } else {
                isMyDreamReason = true;
                mTvMeritAndMotive.setMaxLines(1000);
            }
        });


    }

    @OnClick({R.id.ll_dreams, R.id.ll_follower,
            R.id.iv_dream_profile, R.id.btn_dream_description_more,
            R.id.btn_merit_and_motive_more, R.id.ll_comment,
            R.id.ll_cheering, R.id.ll_dream_title, R.id.tv_dream_description})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dreams: // 내 꿈 레벨 (꿈 선택)
                break;
            case R.id.ll_follower: // 팔로워
                break;
            case R.id.iv_dream_profile: // 내 꿈 프로필 이미지
                break;
            case R.id.btn_dream_description_more: // 내 꿈 더보기
                break;
            case R.id.btn_merit_and_motive_more: // 내 꿈 이유 더보기
                break;
            case R.id.ll_comment: // 댓글
                break;
            case R.id.ll_cheering: // 불꽃(좋아요)
                break;
            case R.id.ll_dream_title: // 내 꿈 명칭 정하기 페이지 이동
                intent = new Intent(getContext(), ActivityDreamTitle.class);
                startActivity(intent);
                break;
            case R.id.tv_dream_description: // 핵심 문장으로 내 꿈 설명하기 페이지 이동
                intent = new Intent(getContext(), ActivityDreamDescription.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * RecyclerView Interface
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_dream_description, parent, false);
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
}
