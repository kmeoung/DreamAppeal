package com.truevalue.dreamappeal.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseTitleBar extends LinearLayout {
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;

    private IOBaseTitleBarListener mIOBaseTitleBarListener;

    public BaseTitleBar(Context context) {
        super(context);
        initView();
    }

    public BaseTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public BaseTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_title_bar, this, false);
        addView(v);
        ButterKnife.bind(this, v);
    }

    public void setIOBaseTitleBarListener(IOBaseTitleBarListener listener) {
        this.mIOBaseTitleBarListener = listener;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     *
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isTextBtn
     */
    public void showToolbarBtn(boolean isBack, boolean isMenu, boolean isSearch, boolean isTextBtn) {
        if (isBack) mIvBack.setVisibility(View.VISIBLE);
        else mIvBack.setVisibility(View.GONE);

        if (isMenu) mIvMenu.setVisibility(View.VISIBLE);
        else mIvMenu.setVisibility(View.GONE);

        if (isSearch) mIvSearch.setVisibility(View.VISIBLE);
        else mIvSearch.setVisibility(View.GONE);

        if (isTextBtn) mTvTextBtn.setVisibility(View.VISIBLE);
        else mTvTextBtn.setVisibility(View.GONE);
    }


    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BaseTitleBar);
        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BaseTitleBar, defStyle, 0);
        setTypeArray(typedArray);
    }


    private void setTypeArray(TypedArray typedArray) {
        boolean isMenu = typedArray.getBoolean(R.styleable.BaseTitleBar_is_menu, false);
        boolean isSearch = typedArray.getBoolean(R.styleable.BaseTitleBar_is_search, false);
        boolean isBack = typedArray.getBoolean(R.styleable.BaseTitleBar_is_back, false);
        boolean isRightText = typedArray.getBoolean(R.styleable.BaseTitleBar_is_right_text, false);
        String title = typedArray.getString(R.styleable.BaseTitleBar_title);
        String rightText = typedArray.getString(R.styleable.BaseTitleBar_right_text);
        float titleSize = typedArray.getFloat(R.styleable.BaseTitleBar_title_size, 16);

        // 메뉴
        if (isMenu)
            mIvMenu.setVisibility(VISIBLE);
        else mIvMenu.setVisibility(GONE);
        // 검색
        if (isSearch)
            mIvSearch.setVisibility(VISIBLE);
        else mIvSearch.setVisibility(GONE);
        // 뒤로가기
        if (isBack)
            mIvBack.setVisibility(VISIBLE);
        else mIvBack.setVisibility(GONE);
        // 오른쪽 버튼
        if (isRightText)
            mTvTextBtn.setVisibility(VISIBLE);
        else mTvTextBtn.setVisibility(GONE);
        // 타이틀
        mTvTitle.setText(title);
        // 오른쪽 버튼
        mTvTextBtn.setText(rightText);
        // 타이틀 크기
        mTvTitle.setTextSize(titleSize);

        typedArray.recycle();

    }

    @OnClick({R.id.iv_menu, R.id.iv_back, R.id.iv_search, R.id.tv_text_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_menu: // 메뉴
                if(mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickMenu();
                break;
            case R.id.iv_back: // 뒤로가기
                if(mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickBack();
                break;
            case R.id.iv_search: // 검색
                if(mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickSearch();
                break;
            case R.id.tv_text_btn: // 오른쪽 글자 버튼
                if(mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickRightTextBtn();
                break;
        }
    }
}
