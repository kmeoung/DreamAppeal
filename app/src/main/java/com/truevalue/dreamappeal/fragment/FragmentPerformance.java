package com.truevalue.dreamappeal.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAchivementDetail;
import com.truevalue.dreamappeal.activity.ActivityAddAchivement;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPerformance extends BaseFragment implements IORecyclerViewListener {

    private static final int TYPE_LIST_HEADER = 0;
    private static final int TYPE_LIST_OTHER = 1;

    @BindView(R.id.rv_dream_description)
    RecyclerView mRvRecycle;

    private BaseRecyclerViewAdapter mAdapter;
    private ViewPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);
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
        mPagerAdapter = new ViewPagerAdapter(getContext());

        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvRecycle.setAdapter(mAdapter);
        mRvRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    /**
     * BaseRecyclerView Interface
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LIST_HEADER) {
            return BaseViewHolder.newInstance(R.layout.listitem_header_achivement, parent, false);
        } else {
            return BaseViewHolder.newInstance(R.layout.listitem_achivement_post, parent, false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if (getItemViewType(i) == TYPE_LIST_HEADER) {
            onBindViewHeader(h, i);
        } else if (getItemViewType(i) == TYPE_LIST_OTHER) {
            onBindViewOther(h, i);
        }
    }

    /**
     * Header ViewPager 대표성과
     *
     * @param h
     * @param i
     */
    private void onBindViewHeader(BaseViewHolder h, int i) {
        ViewPager pager = h.getItemView(R.id.vp_pager);
        pager.setAdapter(mPagerAdapter);
        ImageView ivAddAchivement = h.getItemView(R.id.iv_add_achivement);
        ivAddAchivement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityAddAchivement.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 최근 성과
     *
     * @param h
     * @param i
     */
    private void onBindViewOther(BaseViewHolder h, int i) {
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityAchivementDetail.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return TYPE_LIST_HEADER;
        } else return TYPE_LIST_OTHER;
    }

    /**
     * ViewPager Adapter
     */
    public class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        public ViewPagerAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return 3; // 페이지 3개 고정
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override

        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.layout_achivement, container, false);
            container.addView(view);
            return view;
        }


    }
}
