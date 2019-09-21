package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityLogin;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.dialog.DialogProfile;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentDrawer extends BaseFragment {

    @BindView(R.id.ll_profile)
    LinearLayout mLlProfile;
    @BindView(R.id.tv_following)
    TextView mTvFollowing;
    @BindView(R.id.tv_dream_point)
    TextView mTvDreamPoint;
    @BindView(R.id.ll_logout)
    LinearLayout mLlLogout;
    @BindView(R.id.ll_drawer)
    LinearLayout mLlDrawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.ll_profile, R.id.ll_logout, R.id.ll_drawer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_profile:
                DialogProfile dialog = new DialogProfile(getActivity());
                dialog.show();
                break;
            case R.id.ll_logout:
                Toast.makeText(getActivity().getApplicationContext(), "성공적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                Comm_Prefs prefs = new Comm_Prefs(getContext());
                prefs.setLogined(false);
                prefs.setProfileIndex(-1, true);
                prefs.setToken(null);
                prefs.setUserName(null);

                Intent intent = new Intent(getContext(), ActivityLogin.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.ll_drawer:
                break;
        }
    }
}
