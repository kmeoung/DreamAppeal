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
import com.truevalue.dreamappeal.fragment.FragmentNormalProfile;

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

    public DialogProfile(@NonNull Activity activity) {
        super(activity);
        mActivityMain = (ActivityMain) activity;
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


    }


    @OnClick({R.id.iv_normal_profile_set, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_normal_profile_set:
                mActivityMain.replaceFragment(new FragmentNormalProfile(),true);
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
