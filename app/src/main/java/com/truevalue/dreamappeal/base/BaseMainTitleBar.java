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

public class BaseMainTitleBar extends LinearLayout {
    public static final String GONE = "gone";
    public static final String VISIBLE = "visible";
    public static final String INVISIBLE = "invisible";


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
    @BindView(R.id.ll_header)
    LinearLayout mLlHeader;

    private IOBaseTitleBarListener mIOBaseTitleBarListener;

    public BaseMainTitleBar(Context context) {
        super(context);
        initView();
    }

    public BaseMainTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public BaseMainTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_main_title_bar, this, false);
        addView(v);
        ButterKnife.bind(this, v);
    }

    public void setIOBaseTitleBarListener(IOBaseTitleBarListener listener) {
        this.mIOBaseTitleBarListener = listener;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mLlHeader.setPadding(left,top,right,bottom);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     *
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isRightText
     * @param title
     * @param rightText
     */
    public void showToolbarBtn(String isBack, String isMenu, String isSearch, String isRightText, String title, String rightText) {
        // 메뉴
        switch (isMenu) {
            case VISIBLE:
                mIvMenu.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvMenu.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvMenu.setVisibility(View.GONE);
                break;
            default:
                mIvMenu.setVisibility(View.GONE);
        }

        // 검색
        switch (isSearch) {
            case VISIBLE:
                mIvSearch.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvSearch.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvSearch.setVisibility(View.GONE);
                break;
            default:
                mIvSearch.setVisibility(View.GONE);
        }
        // 뒤로가기
        switch (isBack) {
            case VISIBLE:
                mIvBack.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvBack.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvBack.setVisibility(View.GONE);
                break;
            default:
                mIvBack.setVisibility(View.GONE);
        }
        // 오른쪽 버튼
        switch (isRightText) {
            case VISIBLE:
                mTvTextBtn.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mTvTextBtn.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mTvTextBtn.setVisibility(View.GONE);
                break;
            default:
                mTvTextBtn.setVisibility(View.GONE);
        }

        // 타이틀
        if (title != null)
            mTvTitle.setText(title);
        // 오른쪽 버튼
        if (rightText != null)
            mTvTextBtn.setText(rightText);
    }

    /**
     * 상단 바 버튼 보이는 여부 설정
     *
     * @param isBack
     * @param isMenu
     * @param isSearch
     * @param isRightText
     */
    public void showToolbarBtn(String isBack, String isMenu, String isSearch, String isRightText) {
        showToolbarBtn(isBack, isMenu, isSearch, isRightText, null, null);
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
        String isMenu = typedArray.getString(R.styleable.BaseTitleBar_is_menu_visibility);
        String isSearch = typedArray.getString(R.styleable.BaseTitleBar_is_search_visibility);
        String isBack = typedArray.getString(R.styleable.BaseTitleBar_is_back_visibility);
        String isRightText = typedArray.getString(R.styleable.BaseTitleBar_is_right_text_visibility);
        String title = typedArray.getString(R.styleable.BaseTitleBar_title);
        String rightText = typedArray.getString(R.styleable.BaseTitleBar_right_text);
        float titleSize = typedArray.getFloat(R.styleable.BaseTitleBar_title_size, 16);

        // 메뉴
        switch (isMenu) {
            case VISIBLE:
                mIvMenu.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvMenu.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvMenu.setVisibility(View.GONE);
                break;
            default:
                mIvMenu.setVisibility(View.GONE);
        }

        // 검색
        switch (isSearch) {
            case VISIBLE:
                mIvSearch.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvSearch.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvSearch.setVisibility(View.GONE);
                break;
            default:
                mIvSearch.setVisibility(View.GONE);
        }
        // 뒤로가기
        switch (isBack) {
            case VISIBLE:
                mIvBack.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mIvBack.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mIvBack.setVisibility(View.GONE);
                break;
            default:
                mIvBack.setVisibility(View.GONE);
        }
        // 오른쪽 버튼
        switch (isRightText) {
            case VISIBLE:
                mTvTextBtn.setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                mTvTextBtn.setVisibility(View.INVISIBLE);
                break;
            case GONE:
                mTvTextBtn.setVisibility(View.GONE);
                break;
            default:
                mTvTextBtn.setVisibility(View.GONE);
        }
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
                if (mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickMenu();
                break;
            case R.id.iv_back: // 뒤로가기
                if (mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickBack();
                break;
            case R.id.iv_search: // 검색
                if (mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickSearch();
                break;
            case R.id.tv_text_btn: // 오른쪽 글자 버튼
                if (mIOBaseTitleBarListener != null) mIOBaseTitleBarListener.OnClickRightTextBtn();
                break;
        }
    }

    public ImageView getmIvMenu() {
        return mIvMenu;
    }

    public ImageView getmIvBack() {
        return mIvBack;
    }

    public TextView getmTvTitle() {
        return mTvTitle;
    }

    public ImageView getmIvSearch() {
        return mIvSearch;
    }

    public TextView getmTvTextBtn() {
        return mTvTextBtn;
    }
}
