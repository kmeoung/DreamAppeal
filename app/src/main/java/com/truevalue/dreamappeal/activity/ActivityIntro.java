package com.truevalue.dreamappeal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.utils.Comm_Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityIntro extends BaseActivity {

    private static final int DELAY = 1000 * 1;
    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int cameraCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.CAMERA);
            int writeStorageCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStorageCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            Intent intent;
            if (cameraCheck == PackageManager.PERMISSION_GRANTED
                    && writeStorageCheck == PackageManager.PERMISSION_GRANTED
                    && readStorageCheck == PackageManager.PERMISSION_GRANTED) {
                // 권한 있음
                Comm_Prefs prefs = new Comm_Prefs(ActivityIntro.this);

                if (prefs.isLogin() && prefs.getMyProfileIndex() != -1) { // 바로 메인
                    intent = new Intent(ActivityIntro.this, ActivityMain.class);
                } else { // 로그인 페이지
                    intent = new Intent(ActivityIntro.this, ActivityLogin.class);
                }
                // todo : 이부분 확인 필요
                // 앱 껏다 켰을 시 다시 내 profile index로 설정
                if(prefs.getMyProfileIndex() != prefs.getProfileIndex()){
                    prefs.setProfileIndex(prefs.getMyProfileIndex(),true);

                }

            } else {
                // 권한 없음
                intent = new Intent(ActivityIntro.this, ActivityPermission.class);
            }

            startActivity(intent);

            // TODO : Activity 애니메이션 없애기
//            overridePendingTransition(0, 0);
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0, DELAY);
    }
}
