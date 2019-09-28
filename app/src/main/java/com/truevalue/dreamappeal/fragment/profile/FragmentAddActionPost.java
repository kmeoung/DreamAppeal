package com.truevalue.dreamappeal.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseItemDecorationHorizontal;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.fragment.FragmentMain;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class FragmentAddActionPost extends BaseFragment implements IORecyclerViewListener, IOBaseTitleBarListener {
    public static final int REQUEST_GET_IMAGE = 1009;
    @BindView(R.id.rv_action_post_img)
    RecyclerView mRvActionPostImg;
    @BindView(R.id.et_input_comment)
    EditText mEtInputComment;
    @BindView(R.id.iv_add_img)
    ImageView mIvAddImg;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.ll_btns)
    LinearLayout mLlBtns;

    private BaseRecyclerViewAdapter mAdapter;
    private int mPostIndex = -1;
    private BeanActionPostDetail mBean = null;
    private int mType = ActivityAddActionPost.TYPE_ADD_ACTION_POST;
    private boolean isEdit = false;

    public static FragmentAddActionPost newInstance(int post_index, BeanActionPostDetail bean) {
        FragmentAddActionPost fragment = new FragmentAddActionPost();
        fragment.mPostIndex = post_index;
        fragment.mBean = bean;
        fragment.mType = ActivityAddActionPost.TYPE_EDIT_ACTION_POST;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_action_post, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init Adapter
        if (mAdapter == null)
            initAdapter();
        // Data init
        initData();

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // init Title Bar
        initTitleBar();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvActionPostImg.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        mRvActionPostImg.setLayoutManager(llm);
        mRvActionPostImg.addItemDecoration(new BaseItemDecorationHorizontal(getContext(), 10));
    }

    private void initData() {
        if (mBean != null) {
            mEtInputComment.setText(mBean.getContent());
            initRightBtn();
        }
    }

    private void initView() {
        if (mType == ActivityAddActionPost.TYPE_EDIT_ACTION_POST) {
            // todo : 임시
            mLlBtns.setVisibility(View.GONE);
            mRvActionPostImg.setVisibility(View.GONE);
        } else {
            mLlBtns.setVisibility(View.VISIBLE);
            mRvActionPostImg.setVisibility(View.VISIBLE);
        }

        mEtInputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initRightBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initTitleBar() {
        BaseTitleBar btbBar = ((ActivityAddActionPost) getActivity()).getmBtbBar();
        btbBar.setIOBaseTitleBarListener(this);
        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            btbBar.setTitle("새 실천인증");
            btbBar.getmIvBack().setVisibility(View.GONE);
            btbBar.getmIvClose().setVisibility(View.VISIBLE);
            btbBar.getmTvTextBtn().setText("다음");

        } else if (mType == ActivityAddActionPost.TYPE_EDIT_ACTION_POST) {
            btbBar.setTitle("실천목표 수정");
            btbBar.getmTvTextBtn().setText("완료");
            btbBar.getmIvBack().setVisibility(View.GONE);
            btbBar.getmIvClose().setVisibility(View.VISIBLE);
        }
    }

    /**
     * Bind Temp Data
     */
    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_achivement_list, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        File file = (File) mAdapter.get(i);
        ImageView ivImage = h.getItemView(R.id.iv_achivement);
        ImageView ivDelete = h.getItemView(R.id.iv_delete);
        Glide.with(getContext()).load(file).placeholder(R.drawable.ic_image_black_24dp).into(ivImage);

        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.remove(i);
                    mAdapter.notifyDataSetChanged();
                    initRightBtn();
                }
            });
        } else {
            ivDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public void OnClickBack() {

    }

    @Override
    public void OnClickClose() {
        getActivity().onBackPressed();
    }

    private void initRightBtn() {
        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            if (mAdapter.size() > 0 && mEtInputComment.getText().length() > 0) {
                ((ActivityAddActionPost) getActivity()).getmBtbBar().getmTvTextBtn().setSelected(true);
            } else {
                ((ActivityAddActionPost) getActivity()).getmBtbBar().getmTvTextBtn().setSelected(false);
            }
        } else {
            if (mEtInputComment.getText().length() > 0) {
                ((ActivityAddActionPost) getActivity()).getmBtbBar().getmTvTextBtn().setSelected(true);
            } else {
                ((ActivityAddActionPost) getActivity()).getmBtbBar().getmTvTextBtn().setSelected(false);
            }
        }
    }

    @Override
    public void OnClickRightTextBtn() {
        isEdit = false;
        String comment = mEtInputComment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            if (mAdapter.size() < 1) {
                Toast.makeText(getContext().getApplicationContext(), "이미지를 추가해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ActivityAddActionPost) getActivity()).replaceFragmentRight(FragmentLevelChoice.newInstance(comment, mAdapter.getAll()), true);
        } else if (mType == ActivityAddActionPost.TYPE_EDIT_ACTION_POST) {
            httpPatchActionPost();
        }
    }

    /**
     * http Patch
     * 실천 인증 수정
     */
    private void httpPatchActionPost() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_INDEX;
        // 내 실천인증 수정
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(mPostIndex));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("content", mEtInputComment.getText().toString());
        DAHttpClient.getInstance(getContext()).Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                }
            }
        });
    }

    @OnClick({R.id.iv_add_img, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_img:
                if (!isEdit) {
                    Intent intent = new Intent(getContext(), ActivityGalleryCamera.class);
                    intent.putExtra(ActivityGalleryCamera.VIEW_TYPE_ADD_ACTION_POST, FragmentMain.REQUEST_ADD_ACHIVEMENT);
                    startActivityForResult(intent, REQUEST_GET_IMAGE);
                }
                break;
            case R.id.btn_edit:
                isEdit = !isEdit;
                mIvAddImg.setActivated(isEdit);
                mAdapter.notifyDataSetChanged();
                if (isEdit) mBtnEdit.setText("완료");
                else mBtnEdit.setText("삭제");
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GET_IMAGE) {
                File file = (File) data.getSerializableExtra(ActivityGalleryCamera.REQEUST_IMAGE_FILE);
                mAdapter.add(file);
                initRightBtn();
            }
        }
    }
}
