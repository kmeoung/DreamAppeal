package com.truevalue.dreamappeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
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
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

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
    }


    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                httpLogin();
                break;
        }
    }

    Handler handler = new Handler();

    private void httpLogin() {
        BaseOkHttpClient client = new BaseOkHttpClient();
        HashMap<String, String> body = new HashMap<>();
        String id = mEtId.getText().toString();
        String password = mEtPassword.getText().toString();

        body.put("id", id);
        body.put("password", password);

        client.Post(Comm_Param.tempUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String strResponse = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(strResponse);
                    String status = jsonObject.getString("status");
                    String value = jsonObject.getString("value");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (status.equals("DAOK")) {
                        // 로그인 저장
                        Comm_Prefs prefs = new Comm_Prefs(getContext());
                        prefs.setLogined(true);

                        Intent intent = new Intent(getContext(), ActivityMain.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }
}
