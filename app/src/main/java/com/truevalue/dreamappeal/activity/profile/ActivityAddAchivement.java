package com.truevalue.dreamappeal.activity.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseItemDecorationHorizontal;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanAchivementPostMain;
import com.truevalue.dreamappeal.bean.BeanPostDetail;
import com.truevalue.dreamappeal.fragment.FragmentMain;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityAddAchivement extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    public static final String EXTRA_EDIT_ACHIVEMENT_POST = "EXTRA_EDIT_ACHIVEMENT_POST";
    public static final int REQUEST_GET_IMAGE = 1004;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.iv_add_img)
    ImageView mIvAddImg;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.rv_achivement_img)
    RecyclerView mRvAchivementImg;
    @BindView(R.id.et_input_comment)
    EditText mEtInputComment;
    @BindView(R.id.ll_btns)
    LinearLayout mLlBtns;

    private BaseRecyclerViewAdapter mAdapter;
    private BeanAchivementPostMain mBean = null;
    private BeanPostDetail mBeanDetail = null;
    private boolean isEdit = false;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achivement);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        mBtbBar.getmIvClose().setVisibility(View.VISIBLE);
        // Adapter 초기화
        initAdapter();

        initData();
    }

    private void initData() {
        if (getIntent().getSerializableExtra(EXTRA_EDIT_ACHIVEMENT_POST) != null) {
            if (getIntent().getSerializableExtra(EXTRA_EDIT_ACHIVEMENT_POST) instanceof BeanPostDetail) {
                mBeanDetail = (BeanPostDetail) getIntent().getSerializableExtra(EXTRA_EDIT_ACHIVEMENT_POST);
                mEtTitle.setText(mBeanDetail.getTitle());
                mEtInputComment.setText(mBeanDetail.getContent());
            } else {
                mBean = (BeanAchivementPostMain) getIntent().getSerializableExtra(EXTRA_EDIT_ACHIVEMENT_POST);
                mEtTitle.setText(mBean.getTitle());
                mEtInputComment.setText(mBean.getContent());
                // todo : 사진이 추가될 시 여기에도 추가 바람
            }
            // todo : 임시
            mLlBtns.setVisibility(View.GONE);
            mRvAchivementImg.setVisibility(View.GONE);
            initRightText();
        }else{
            mLlBtns.setVisibility(View.VISIBLE);
            mRvAchivementImg.setVisibility(View.VISIBLE);
        }


        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initRightText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtInputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initRightText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDialog = new ProgressDialog(ActivityAddAchivement.this);
        mDialog.setCancelable(false);
    }

    private void initRightText() {
        if (TextUtils.isEmpty(mEtTitle.getText().toString()) || TextUtils.isEmpty(mEtInputComment.getText().toString())) {
            mBtbBar.getmTvTextBtn().setSelected(false);
        } else mBtbBar.getmTvTextBtn().setSelected(true);
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(ActivityAddAchivement.this, this);
        mRvAchivementImg.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(ActivityAddAchivement.this);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        mRvAchivementImg.setLayoutManager(llm);
        mRvAchivementImg.addItemDecoration(new BaseItemDecorationHorizontal(ActivityAddAchivement.this, 10));
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        finish();
    }

    @Override
    public void OnClickClose() {
        finish();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        // TODO : 완료하면 상세 페이지 띄워줘야함
        if (mBean != null || mBeanDetail != null) { // 수정
            httpPatchAchivementPost();
        } else { // 추가
            httpPostAchivementPost();
        }
    }

    /**
     * 실현 성과 수정
     */
    private void httpPatchAchivementPost() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddAchivement.this);
        String url = Comm_Param.URL_API_ACHIVEMENT_POSTS_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        if (mBean != null)
            url = url.replace(Comm_Param.POST_INDEX, String.valueOf(mBean.getIdx()));
        else url = url.replace(Comm_Param.POST_INDEX, String.valueOf(mBeanDetail.getIdx()));

        if (TextUtils.isEmpty(mEtTitle.getText().toString()) || TextUtils.isEmpty(mEtInputComment.getText().toString())) {
            Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = prefs.getToken();
        HashMap header = Utils.getHttpHeader(token);
        HashMap<String, String> body = new HashMap();

        body.put("title", mEtTitle.getText().toString());
        body.put("content", mEtInputComment.getText().toString());

        DAHttpClient client = DAHttpClient.getInstance(ActivityAddAchivement.this);
        client.Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    /**
     * 실현 성과 등록
     */
    private void httpPostAchivementPost() {
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();

        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddAchivement.this);
        String url = Comm_Param.URL_API_ACHIVEMENT_POSTS;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        if (TextUtils.isEmpty(mEtTitle.getText().toString()) || TextUtils.isEmpty(mEtInputComment.getText().toString())) {
            Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAdapter.size() < 1) {
            Toast.makeText(getApplicationContext(), "이미지를 추가해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = prefs.getToken();
        HashMap header = Utils.getHttpHeader(token);
        HashMap<String, String> body = new HashMap();

        body.put("title", mEtTitle.getText().toString());
        body.put("content", mEtInputComment.getText().toString());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        body.put("thumbnail_image", "");
        body.put("register_date", strDate);
        body.put("tags", "");

        DAHttpClient client = DAHttpClient.getInstance(ActivityAddAchivement.this);
        client.Post(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
//                    setResult(RESULT_OK);
//                    finish();
                    JSONObject object = new JSONObject(body);
                    JSONObject result = object.getJSONObject("result");
                    int post_index = result.getInt("insertId");
                    httpPostProfilesImage(post_index);
                }
            }
        });
    }


    /**
     * Image Upload
     */
    private void httpPostProfilesImage(int posts_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddAchivement.this);

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityAddAchivement.this);
        HashMap<String, String> body = new HashMap<>();
        JSONArray jArray = new JSONArray();

        try {
            for (int i = 0; i < mAdapter.size(); i++) {
                File file = (File) mAdapter.get(i);
                JSONObject set = new JSONObject();
                String[] fileInfo = file.getName().split("\\.");
                set.put("key", fileInfo[0]);
                set.put("type", fileInfo[1]);
                jArray.put(set);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        body.put("set", jArray.toString());

        client.Post(Comm_Param.URL_API_ACHIVEMENT_POSTS_INDEX_IMAGES
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()))
                .replace(Comm_Param.POST_INDEX, String.valueOf(posts_index)), header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    mImageUploadCheck = new LinkedHashMap<>();
                    JSONArray jsonUrl = json.getJSONArray("urlList");
                    for (int i = 0; i < jsonUrl.length(); i++) {
                        JSONObject url = jsonUrl.getJSONObject(i);
                        String fileURL = url.getString("fileURL");
                        String uploadURL = url.getString("uploadURL");
                        httpPostUploadImage(i, posts_index, (File) mAdapter.get(i), fileURL, uploadURL);
                    }
                }
            }
        });
    }

    LinkedHashMap<Integer, String> mImageUploadCheck = null;

    private void httpPostUploadImage(int image_index, int posts_index, File file, String fileUrl, String uploadUrl) {
        // Create the connection and use it to upload the new object using the pre-signed URL.

        DAHttpClient.getInstance(ActivityAddAchivement.this).Put(uploadUrl, file, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    mImageUploadCheck.put(image_index, fileUrl);

                    if (mImageUploadCheck.size() == mAdapter.size()) {
                        profileUpdate(posts_index);
                    }

                }
            }
        });
    }

    private void profileUpdate(int post_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddAchivement.this);

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityAddAchivement.this);

        HashMap<String, String> body = new HashMap<>();
        JSONArray jArray = new JSONArray();

        if(mImageUploadCheck != null) {
            try {
                for (int i = 0; i < mImageUploadCheck.size(); i++) {
                    JSONObject jObject = new JSONObject();
                    String fileUrl = mImageUploadCheck.get(i);
                    jObject.put("url", fileUrl);
                    jArray.put(jObject);
                }
                body.put("image", jArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            body.put("thumbnail_image", mImageUploadCheck.get(0));
        }
        client.Patch(Comm_Param.URL_API_ACHIVEMENT_POSTS_INDEX
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()))
                .replace(Comm_Param.POST_INDEX, String.valueOf(post_index)), header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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
        Glide.with(ActivityAddAchivement.this).load(file).placeholder(R.drawable.ic_image_black_24dp).into(ivImage);

        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.remove(i);
                    mAdapter.notifyDataSetChanged();
                    initRightText();
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


    @OnClick({R.id.iv_add_img, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_img: // 이미지 추가 버튼
                if (!isEdit) {
                    Intent intent = new Intent(ActivityAddAchivement.this, ActivityGalleryCamera.class);
                    intent.putExtra(ActivityGalleryCamera.VIEW_TYPE_ADD_ACTION_POST, FragmentMain.REQUEST_ADD_ACHIVEMENT);
                    startActivityForResult(intent, REQUEST_GET_IMAGE);
                }
                break;
            case R.id.btn_edit: // 수정 버튼
                isEdit = !isEdit;
                mIvAddImg.setActivated(isEdit);
                mAdapter.notifyDataSetChanged();
                if (isEdit) mBtnEdit.setText("완료");
                else mBtnEdit.setText("삭제");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GET_IMAGE) {
                File file = (File) data.getSerializableExtra(ActivityGalleryCamera.REQEUST_IMAGE_FILE);
                mAdapter.add(file);
                initRightText();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        super.onDestroy();
    }
}
