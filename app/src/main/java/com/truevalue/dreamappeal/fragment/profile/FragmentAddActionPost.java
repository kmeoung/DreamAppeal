package com.truevalue.dreamappeal.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.activity.profile.ActivityAddAchivement;
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

    private BaseRecyclerViewAdapter mAdapter;
    private int mPostIndex = -1;
    private BeanActionPostDetail mBean = null;
    private int mType = ActivityAddActionPost.TYPE_ADD_ACTION_POST;

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
        initAdapter();
        // Data init
        initData();
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
        }
    }

    private void initTitleBar() {
        BaseTitleBar btbBar = ((ActivityAddActionPost) getActivity()).getmBtbBar();
        btbBar.setIOBaseTitleBarListener(this);
        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            btbBar.setTitle("새 실천인증");
            btbBar.getmTvTextBtn().setText("다음");
        } else if (mType == ActivityAddActionPost.TYPE_EDIT_ACTION_POST) {
            btbBar.setTitle("실천목표 수정");
            btbBar.getmTvTextBtn().setText("완료");
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
        Glide.with(getContext()).load(file).placeholder(R.drawable.ic_image_black_24dp).into(ivImage);
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
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        String comment = mEtInputComment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(getContext().getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mAdapter.size() < 1){
            Toast.makeText(getContext().getApplicationContext(), "이미지를 추가해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            ((ActivityAddActionPost) getActivity()).replaceFragmentRight(FragmentLevelChoice.newInstance(comment,mAdapter.getAll()), true);
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
                Intent intent = new Intent(getContext(), ActivityGalleryCamera.class);
                intent.putExtra(ActivityGalleryCamera.VIEW_TYPE_ADD_ACTION_POST, FragmentMain.REQUEST_ADD_ACHIVEMENT);
                startActivityForResult(intent, REQUEST_GET_IMAGE);
                break;
            case R.id.btn_edit:
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
            }
        }
    }
}
