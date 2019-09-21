package com.truevalue.dreamappeal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.utils.Comm_Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityPermission extends BaseActivity {

    private final int REQUEST_CODE_PERMISSION = 3000;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btn_permission)
    Button mBtnPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_permission)
    public void onViewClicked() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가
// 해당 권한을 사용해서 작업을 진행할 수 있습니다

                Intent intent;
                Comm_Prefs prefs = new Comm_Prefs(ActivityPermission.this);

                if (prefs.isLogin()) { // 바로 메인
                    intent = new Intent(ActivityPermission.this, ActivityMain.class);
                } else { // 로그인 페이지
                    intent = new Intent(ActivityPermission.this, ActivityLogin.class);
                }
                startActivity(intent);
                finish();
            } else {
                // 권한 거부
// 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
//                CustomToast.makeText(this, "권한을 허용해주셔야 테스트가 가능합니다.", CustomToast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            }

        }
    }
}
