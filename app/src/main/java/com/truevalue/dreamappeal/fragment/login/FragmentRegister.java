package com.truevalue.dreamappeal.fragment.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentRegister extends BaseFragment implements IOBaseTitleBarListener {


    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.btn_gender_man)
    Button mBtnGenderMan;
    @BindView(R.id.btn_gender_woman)
    Button mBtnGenderWoman;
    @BindView(R.id.tv_year)
    TextView mTvYear;
    @BindView(R.id.tv_month)
    TextView mTvMonth;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_re_password)
    EditText mEtRePassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    // 성별 확인 false 남자 true 여자
    private boolean isGender = false;
    // 사용자 생년월일
    private Calendar mCal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtbBar.setIOBaseTitleBarListener(this);
        initView();
    }

    /**
     * View 초기화
     */
    private void initView() {
        mBtnGenderMan.setSelected(true);
        mBtnGenderWoman.setSelected(false);
        mCal = Calendar.getInstance();
        int year = mCal.get(Calendar.YEAR);
        int month = mCal.get(Calendar.MONTH) + 1;
        int date = mCal.get(Calendar.DAY_OF_MONTH);
        mTvYear.setText(String.format("%04d", year));
        mTvMonth.setText(String.format("%02d", month));
        mTvDate.setText(String.format("%02d", date));

        // TODO : 테스트용
        if (!Comm_Param.REAL) {
            mEtId.setText("debug@gmail.com");
            mEtName.setText("debug");
            mEtPassword.setText("debug");
            mEtRePassword.setText("debug");
        }
    }

    /**
     * DatePickerDialog
     * 사용자에게 보여줄 시 혹은 데이터를 저장할시에는 Month에 + 1 을 해야합니다
     */
    private void showDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCal.set(Calendar.YEAR, year);
                mCal.set(Calendar.MONTH, month);
                mCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mTvYear.setText(String.format("%04d", year));
                mTvMonth.setText(String.format("%02d", (month + 1)));
                mTvDate.setText(String.format("%02d", dayOfMonth));
            }
        }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }


    @OnClick({R.id.btn_gender_man, R.id.btn_gender_woman, R.id.btn_register, R.id.tv_year, R.id.tv_month, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gender_man: // 성별 남자 선택
                isGender = false;
                mBtnGenderMan.setSelected(true);
                mBtnGenderWoman.setSelected(false);
                break;
            case R.id.btn_gender_woman: // 성별 여자 선택
                isGender = true;
                mBtnGenderMan.setSelected(false);
                mBtnGenderWoman.setSelected(true);
                break;
            case R.id.btn_register:
                httpGetCheckEmail();
                break;
            case R.id.tv_year: // 날짜 설정
            case R.id.tv_month:
            case R.id.tv_date:
                showDatePickerDialog();
                break;
        }
    }

    /**
     * Get Check Email
     */
    private void httpGetCheckEmail() {
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        HashMap<String, String> body = new HashMap<>();

        String id = mEtId.getText().toString();
        String password = mEtPassword.getText().toString();
        String rePassword = mEtRePassword.getText().toString();
        String name = mEtName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext().getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(getContext().getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.isEmailValid(id)) {
            Toast.makeText(getContext().getApplicationContext(), "아이디를 이메일형식으로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext().getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(password, rePassword)) {
            Toast.makeText(getContext().getApplicationContext(), "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        body.put("email", id);
        client.Get(Comm_Param.URL_API_CHECK_EMAIL, null, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (TextUtils.equals(code, VALIDATE_EMAIL)) httpPostRegister(id, password, name);
                else {
                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * Post Register
     */
    private void httpPostRegister(String id, String password, String name) {
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        HashMap<String, String> body = new HashMap<>();

        body.put("email", id);
        body.put("password", password);
        body.put("name", name);
        int gender = (!isGender) ? 1 : 0;
        body.put("gender", gender + "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birth = sdf.format(mCal.getTime());
        body.put("birth", birth);
        body.put("privacy", "1"); // 개인정보 동의 1
        body.put("location", null);
        body.put("affiliation", null);

        client.Post(Comm_Param.URL_API_USERS_SIGNUP, null, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (code.equals(SUCCESS)) {
                    // 로그인 저장
                    Comm_Prefs prefs = new Comm_Prefs(getContext());
                    prefs.setLogined(true);

                    getActivity().onBackPressed();
                }
            }
        });
    }


    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }
}
