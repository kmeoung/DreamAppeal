package com.truevalue.dreamappeal.fragment.profile;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseItemDecorationHorizontal;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAddActionPost extends BaseFragment implements IORecyclerViewListener, IOBaseTitleBarListener {

    @BindView(R.id.rv_action_post_img)
    RecyclerView mRvActionPostImg;
    @BindView(R.id.et_input_comment)
    EditText mEtInputComment;

    private BaseRecyclerViewAdapter mAdapter;
    private int mPostIndex = -1;

    public static FragmentAddActionPost newInstance(int post_index) {
        FragmentAddActionPost fragment = new FragmentAddActionPost();
        fragment.mPostIndex = post_index;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_action_post, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init Adapter
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // init Title Bar
        initTitleBar();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvActionPostImg.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        mRvActionPostImg.setLayoutManager(llm);
        mRvActionPostImg.addItemDecoration(new BaseItemDecorationHorizontal(getContext(), 10));
    }

    private void initTitleBar() {
        ((ActivityAddActionPost) getActivity()).getmBtbBar().setIOBaseTitleBarListener(this);
    }

    /**
     * Bind Temp Data
     */
    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_achivement_list, parent, false);
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

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        String comment = mEtInputComment.getText().toString();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        ((ActivityAddActionPost) getActivity()).replaceFragmentRight(FragmentLevelChoice.newInstance(comment), true);
    }
}
