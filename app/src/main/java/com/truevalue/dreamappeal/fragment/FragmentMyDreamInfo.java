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

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityKeywordDreamInfo;
import com.truevalue.dreamappeal.activity.ActivitySetMyDreamName;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentMyDreamInfo extends BaseFragment implements IORecyclerViewListener {

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
    @BindView(R.id.tv_performance)
    TextView mTvPerformance;
    @BindView(R.id.tv_certified)
    TextView mTvCertified;
    @BindView(R.id.iv_dream_profile)
    ImageView mIvDreamProfile;
    @BindView(R.id.tv_dream_modifier)
    TextView mTvDreamModifier;
    @BindView(R.id.tv_dream_title)
    TextView mTvDreamTitle;
    @BindView(R.id.tv_dream_info)
    TextView mTvDreamInfo;
    @BindView(R.id.rv_recycle)
    RecyclerView mRvRecycle;
    @BindView(R.id.btn_dream_more_view)
    Button mBtnDreamMoreView;
    @BindView(R.id.tv_reason)
    TextView mTvReason;
    @BindView(R.id.btn_reason_more_view)
    Button mBtnReasonMoreView;
    @BindView(R.id.tv_flame)
    TextView mTvFlame;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_flame)
    LinearLayout mLlFlame;
    @BindView(R.id.ll_set_dream_name)
    LinearLayout mLlSetDreamName;

    BaseRecyclerViewAdapter mAdapter;

    private boolean isMyDreamMore = false;
    private boolean isMyDreamReason = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduce_my_dream, container, false);
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
        mRvRecycle.setAdapter(mAdapter);
        mRvRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 3; i++) {
            mAdapter.add("");
        }
        mTvReason.setMaxLines(3);

        mBtnDreamMoreView.setOnClickListener(v->{
            if(isMyDreamMore){
                isMyDreamMore = false;
                mAdapter.clear();
                for (int i = 0; i < 3; i++) {
                    mAdapter.add("");
                }
            }else{
                isMyDreamMore = true;
                mAdapter.clear();
                for (int i = 0; i < 10; i++) {
                    mAdapter.add("");
                }
            }
        });



       mBtnReasonMoreView.setOnClickListener(v->{
           if(isMyDreamReason) {
               isMyDreamReason = false;
               mTvReason.setMaxLines(3);
           }else{
               isMyDreamReason = true;
               mTvReason.setMaxLines(1000);
           }
       });



    }

    @OnClick({R.id.ll_dreams, R.id.ll_follower,
            R.id.iv_dream_profile, R.id.btn_dream_more_view,
            R.id.btn_reason_more_view, R.id.ll_comment,
            R.id.ll_flame,R.id.ll_set_dream_name,R.id.tv_dream_info})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dreams: // 내 꿈 레벨 (꿈 선택)
                break;
            case R.id.ll_follower: // 팔로워
                break;
            case R.id.iv_dream_profile: // 내 꿈 프로필 이미지
                break;
            case R.id.btn_dream_more_view: // 내 꿈 더보기
                break;
            case R.id.btn_reason_more_view: // 내 꿈 이유 더보기
                break;
            case R.id.ll_comment: // 댓글
                break;
            case R.id.ll_flame: // 불꽃(좋아요)
                break;
            case R.id.ll_set_dream_name: // 내 꿈 명칭 정하기 페이지 이동
                intent = new Intent(getContext(), ActivitySetMyDreamName.class);
                startActivity(intent);
                break;
            case R.id.tv_dream_info: // 핵심 문장으로 내 꿈 설명하기 페이지 이동
                intent = new Intent(getContext(), ActivityKeywordDreamInfo.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * RecyclerView Interface
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_dream_info, parent, false);
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
