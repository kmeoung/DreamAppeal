package com.truevalue.dreamappeal.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSearchPopular extends BaseFragment implements IORecyclerViewListener {

    @BindView(R.id.rv_popular)
    RecyclerView mRvPopular;

    private BaseRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_popular, container, false);
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
        mRvPopular.setAdapter(mAdapter);
        mRvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData(){
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_search_popular,parent,false);
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
