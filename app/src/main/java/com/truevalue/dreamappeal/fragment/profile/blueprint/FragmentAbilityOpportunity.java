package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.profile.ActivityAbilityOpportunity;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentAbilityOpportunity extends BaseFragment {
    @BindView(R.id.rv_ability)
    RecyclerView mRvAbility;
    @BindView(R.id.rv_opportunity)
    RecyclerView mRvOpportunity;
    @BindView(R.id.iv_add_ability)
    ImageView mIvAddAbility;
    @BindView(R.id.iv_add_opportunity)
    ImageView mIvAddOpportunity;

    private BaseRecyclerViewAdapter mAbilityAdapter;
    private BaseRecyclerViewAdapter mOpportunityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ability_opportunity, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Adapter 초기화
        initAdapter();
        // 임시 데이터
        bindTempData();
    }

    private void initAdapter() {
        mAbilityAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_ability_opportunity, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
                h.getItemView(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spinner spinner = new Spinner(getContext(), Spinner.MODE_DROPDOWN);
                        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, new String[]{"수정", "삭제"}));
                    }
                });
            }

            @Override
            public int getItemCount() {
                if (mAbilityAdapter != null) {
                    return mAbilityAdapter.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mOpportunityAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_ability_opportunity, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
                h.getItemView(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spinner spinner = new Spinner(getContext(), Spinner.MODE_DROPDOWN);
                        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, new String[]{"수정", "삭제"}));
                    }
                });
            }

            @Override
            public int getItemCount() {
                if (mOpportunityAdapter != null) {
                    return mOpportunityAdapter.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mRvAbility.setAdapter(mAbilityAdapter);
        mRvAbility.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvOpportunity.setAdapter(mOpportunityAdapter);
        mRvOpportunity.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAbilityAdapter.add("");
        }

        for (int i = 0; i < 10; i++) {
            mOpportunityAdapter.add("");
        }
    }

    @OnClick({R.id.iv_add_ability, R.id.iv_add_opportunity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_ability: // 갖출 능력
                ((ActivityAbilityOpportunity) getActivity()).replaceFragment(new FragmentAddAbilityOpportunity(), true);
                break;
            case R.id.iv_add_opportunity: // 만들고픈 기회
                ((ActivityAbilityOpportunity) getActivity()).replaceFragment(new FragmentAddAbilityOpportunity(), true);
                break;
        }
    }
}
