package com.truevalue.dreamappeal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentLogin extends BaseFragment {

    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        String strTemp = "";
        switch (view.getId()) {
            case R.id.btn_login: // 로그인 버튼
                strTemp = "로그인";
                break;
            case R.id.btn_register: // 회원가입 버튼
                strTemp = "회원가입";
                break;
        }
        Toast.makeText(getContext(), strTemp + "버튼 클릭", Toast.LENGTH_SHORT).show();
    }
}
