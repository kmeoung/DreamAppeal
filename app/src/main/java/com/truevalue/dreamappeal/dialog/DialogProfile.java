package com.truevalue.dreamappeal.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.bean.BeanUser;
import com.truevalue.dreamappeal.fragment.FragmentNormalProfile;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogProfile extends Dialog {

    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_age)
    TextView mTvAge;
    @BindView(R.id.tv_gender)
    TextView mTvGender;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.rv_clan)
    RecyclerView mRvClan;
    @BindView(R.id.iv_normal_profile_set)
    ImageView mIvNormalProfileSet;

    private ActivityMain mActivityMain;
    private BeanUser mBean;

    public DialogProfile(@NonNull Activity activity) {
        super(activity);
        mActivityMain = (ActivityMain) activity;
    }

    public DialogProfile(@NonNull Activity activity,BeanUser user) {
        super(activity);
        mActivityMain = (ActivityMain) activity;
        mBean = user;
    }

    public DialogProfile(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogProfile(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_profile);
        ButterKnife.bind(this);
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        Window window = getWindow();
        window.setAttributes(layoutParams);

        initData();
    }

    private void initData(){
        if(mBean != null) {
            mTvAddress.setText(mBean.getLocation());
            String gender = (mBean.getGender() == 1) ? "여" : "남";
            mTvGender.setText(gender);
            mTvEmail.setText(mBean.getEmail());
            mTvName.setText(mBean.getName());
            mTvAge.setText(String.valueOf(calculateAgeForKorean(mBean.getBirth())));
            // todo : 전화번호 추가 필요
            mTvPhone.setText("");
            mTvPhone.setVisibility(View.GONE);
        }
    }

    public static int calculateAgeForKorean(String ssn) { // ssn의 형식은 yyyy-MM-dd 임

        String today = ""; // 오늘 날짜
        int manAge = 0; // 만 나이

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        today = formatter.format(new Date()); // 시스템 날짜를 가져와서 yyyy-MM-dd 형태로 변환

        // today yyyy-MM-dd
        int todayYear = Integer.parseInt(today.substring(0, 4));
        int todayMonth = Integer.parseInt(today.substring(5, 7));
        int todayDay = Integer.parseInt(today.substring(8, 10));

        int ssnYear = Integer.parseInt(ssn.substring(0, 4));
        int ssnMonth = Integer.parseInt(ssn.substring(5, 7));
        int ssnDay = Integer.parseInt(ssn.substring(8, 10));


        manAge = todayYear - ssnYear;

        if (todayMonth < ssnMonth) { // 생년월일 "월"이 지났는지 체크
            manAge--;
        } else if (todayMonth == ssnMonth) { // 생년월일 "일"이 지났는지 체크
            if (todayDay < ssnDay) {
                manAge--; // 생일 안지났으면 (만나이 - 1)
            }
        }

        return manAge + 1; // 한국나이를 측정하기 위해서 +1살 (+1을 하지 않으면 외국나이 적용됨)
    }


    @OnClick({R.id.iv_normal_profile_set, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_normal_profile_set:
                // todo : 추가 설정 필요
//                mActivityMain.replaceFragment(new FragmentNormalProfile(),true);
//                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
