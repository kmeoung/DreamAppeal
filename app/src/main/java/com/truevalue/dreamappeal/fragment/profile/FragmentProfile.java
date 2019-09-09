package com.truevalue.dreamappeal.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseTitleBar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentProfile extends BaseFragment {

    @BindView(R.id.vp_viewpager)
    ViewPager mVpViewpager;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;

    private ArrayList<String> mTabList = new ArrayList<>();
    private ViewPagerAdapter mAdapter;
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mAdapter == null) {
            initTab();
            initAdapter();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // 상단바 설정
        ((ActivityMain) getActivity()).showToolbarBtn(BaseTitleBar.GONE, BaseTitleBar.VISIBLE, BaseTitleBar.VISIBLE, BaseTitleBar.GONE);
    }

    /**
     * 탭 초기화
     */
    private void initTab() {
        mTabList.add("꿈 소개");
        mTabList.add("주요성과");
        mTabList.add("발전계획");

        mFragmentList.add(new FragmentDreamPresent());
        mFragmentList.add(new FragmentPerformance());
        mFragmentList.add(new FragmentBlueprint());
    }

    /**
     * 어댑터 초기화
     */
    private void initAdapter() {
        if (mAdapter == null) {
            mTlTab.setupWithViewPager(mVpViewpager);
            mAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mVpViewpager.setAdapter(mAdapter);

            for (int i = 0; i < mTabList.size(); i++) {
                mTlTab.getTabAt(i).setText(mTabList.get(i));
            }
            mVpViewpager.setOffscreenPageLimit(5);
            mVpViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < mFragmentList.size(); i++) {
                        if (i == position)
                            mFragmentList.get(i).onViewPaged(true);
                        else
                            mFragmentList.get(i).onViewPaged(false);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    /**
     * ViewPager Adapter
     * 페이지 넘어갈 때 마다 서버 호출 필요
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size(); // 페이지 3개 고정
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position);
        }
    }
}
