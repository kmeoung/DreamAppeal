package com.truevalue.dreamappeal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;

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

    public DialogProfile(@NonNull Context context) {
        super(context);
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

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }
}
