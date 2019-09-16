package com.truevalue.dreamappeal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.search.FragmentSearchAppealer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySearch extends BaseActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_popular)
    TextView mTvPopular;
    @BindView(R.id.tv_appealer)
    TextView mTvAppealer;
    @BindView(R.id.tv_performance)
    TextView mTvPerformance;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;
    @BindView(R.id.v_status)
    View mVStatus;

    private final int TYPING_DELAY = 1000 * 1;
    private IOSearchListener mListener = null;
    private Handler textHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(getmListener() != null) getmListener().search(mEtSearch.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslateColor(mVStatus,R.color.colorPrimary);
        replaceFragment(R.id.base_container, new FragmentSearchAppealer(), false);

        onAction();
    }

    private void onAction(){
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textHandler.removeMessages(0);
                textHandler.sendEmptyMessageDelayed(0,TYPING_DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.btn_cancel, R.id.tv_popular, R.id.tv_appealer, R.id.tv_performance, R.id.tv_tag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.tv_popular: // 인기
                Toast.makeText(this, "오픈 준비 중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_appealer: // 어필러

                break;
            case R.id.tv_performance: // 실천인증
                Toast.makeText(this, "오픈 준비 중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_tag: // 태그
                Toast.makeText(this, "오픈 준비 중 입니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public IOSearchListener getmListener() {
        return mListener;
    }

    public void setmListener(IOSearchListener mListener) {
        this.mListener = mListener;
    }

    public interface IOSearchListener{
        void search(String text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(textHandler != null) textHandler.removeMessages(0);
    }
}
