package com.truevalue.dreamappeal.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentNormalLogin extends BaseFragment implements IOBaseTitleBarListener {


    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtbBar.setIOBaseTitleBarListener(this);
        // TODO : 테스트용
        if (!Comm_Param.REAL) {
            mEtId.setText("debug@gmail.com");
            mEtPassword.setText("debug");
        }
    }


    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                httpPostLogin();
                break;
        }
    }

    /**
     * Post Login
     */
    private void httpPostLogin() {

        DAHttpClient client = DAHttpClient.getInstance(getContext());
        HashMap<String, String> body = new HashMap<>();
        String id = mEtId.getText().toString();
        String password = mEtPassword.getText().toString();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(getContext().getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext().getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("email", id);
        body.put("password", password);

        client.Post(Comm_Param.URL_API_USERS_TOKENS, null, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (TextUtils.equals(SUCCESS, code)) {
                    Toast.makeText(getContext().getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(body);
                    String token = object.getString("token");
                    int profile_idx = object.getInt("profile_idx"); // 프로필이 없을 시 -1이 넘어 옴
                    Comm_Prefs prefs = new Comm_Prefs(getContext());
                    prefs.setProfileIndex(profile_idx, true);
                    prefs.setLogined(true);
                    prefs.setToken(token);

                    Intent intent = new Intent(getContext(), ActivityMain.class);
                    startActivity(intent);
                    getActivity().finish();
                    // 로그인 저장

                } else {
                    Toast.makeText(getContext().getApplicationContext(), "아이디 / 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }
}
