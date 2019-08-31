package com.truevalue.dreamappeal.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanLevelChoice;
import com.truevalue.dreamappeal.bean.BeanLevelChoiceHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentLevelChoice extends BaseFragment implements IORecyclerViewListener {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    @BindView(R.id.rv_recycle)
    RecyclerView mRvRecycle;

    private BaseRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_choice, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        bindTempData();
    }

    private void initAdapter(){
        mAdapter = new BaseRecyclerViewAdapter(getContext(),this);
        mRvRecycle.setAdapter(mAdapter);
        mRvRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData(){
        mAdapter.add(new BeanLevelChoiceHeader("실천목표"));
        for (int i = 0; i < 10; i++) {
            mAdapter.add(new BeanLevelChoice("임시 분류"));
        }
        mAdapter.add(new BeanLevelChoiceHeader("세부단계"));
        for (int i = 0; i < 10; i++) {
            mAdapter.add(new BeanLevelChoice("임시 분류"));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) return BaseViewHolder.newInstance(R.layout.listitem_level_choice_header,parent,false);
        else return BaseViewHolder.newInstance(R.layout.listitem_level_choice_item,parent,false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if(getItemViewType(i) == TYPE_HEADER){
            BeanLevelChoiceHeader bean = (BeanLevelChoiceHeader) mAdapter.get(i);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            tvTitle.setText(bean.getTitle());
        }else{
            BeanLevelChoice bean = (BeanLevelChoice) mAdapter.get(i);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            tvTitle.setText(bean.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if(mAdapter.get(i) instanceof BeanLevelChoiceHeader)
        return TYPE_HEADER;
        else return TYPE_ITEM;
    }
}
