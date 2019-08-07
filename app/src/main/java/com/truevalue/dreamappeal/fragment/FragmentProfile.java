package com.truevalue.dreamappeal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;

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

    private ArrayList<String> mTabList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        initTab();
        initAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 상단바 설정
        ((ActivityMain)getActivity()).showToolbarBtn(false,true,true,false);
    }

    /**
     * 탭 초기화
     */
    private void initTab() {
        mTabList = new ArrayList<>();
        mTabList.add("내 꿈 소개");
        mTabList.add("실현 성과");
        mTabList.add("발전 계획");
    }

    /**
     * 어댑터 초기화
     */
    private void initAdapter() {
        mTlTab.setupWithViewPager(mVpViewpager);
        mVpViewpager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));

        for (int i = 0;i < mTabList.size();i++){
            mTlTab.getTabAt(i).setText(mTabList.get(i));
        }
    }

    /**
     * ViewPager Adapter
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // 내 꿈 소개
                    return new FragmentMyDreamInfo();
                case 1: // 실현 성과
                    return new FragmentPerformance();
                case 2: // 발전 계획
                    return new FragmentMyDreamInfo();
            }

            return null;
        }

        @Override
        public int getCount() {
            return mTabList.size(); // 페이지 3개 고정
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position);
        }
    }
}
