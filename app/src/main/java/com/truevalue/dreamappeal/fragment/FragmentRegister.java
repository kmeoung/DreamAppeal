package com.truevalue.dreamappeal.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    }

    /**
     * DatePickerDialog
     * todo : 사용자에게 보여줄 시 혹은 데이터를 저장할시에는 Month에 + 1 을 해야합니다
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
                break;
            case R.id.tv_year: // 날짜 설정
            case R.id.tv_month:
            case R.id.tv_date:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }
}
