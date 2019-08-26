package com.truevalue.dreamappeal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.utils.Comm_Prefs;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
            Comm_Prefs prefs = new Comm_Prefs(ActivityIntro.this);

//            if (prefs.isLogin()) { // 바로 메인
//                Intent intent = new Intent(ActivityIntro.this, ActivityMain.class);
//                startActivity(intent);
//                finish();
//            } else { // 로그인 페이지
//                Intent intent = new Intent(ActivityIntro.this, ActivityLogin.class);
//                startActivity(intent);
//                finish();
//            }

            int cameraCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.CAMERA);
            int writeStorageCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStorageCheck = ContextCompat.checkSelfPermission(ActivityIntro.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            Intent intent;
            if(cameraCheck == PackageManager.PERMISSION_GRANTED
            && writeStorageCheck == PackageManager.PERMISSION_GRANTED
            && readStorageCheck == PackageManager.PERMISSION_GRANTED){

                // 권한 있음
                // TODO : 테스트용
                intent = new Intent(ActivityIntro.this, ActivityMain.class);

            }else{
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
        updateStatusbarTranslate(mVStatus);
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
