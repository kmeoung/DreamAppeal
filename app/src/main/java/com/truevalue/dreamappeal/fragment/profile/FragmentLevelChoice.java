package com.truevalue.dreamappeal.fragment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityAddActionPost;
import com.truevalue.dreamappeal.activity.profile.ActivityAddAchivement;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanActionPostDetail;
import com.truevalue.dreamappeal.bean.BeanLevelChoiceCategory;
import com.truevalue.dreamappeal.bean.BeanLevelChoiceObject;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentLevelChoice extends BaseFragment implements IOBaseTitleBarListener {

    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;
    @BindView(R.id.rv_object)
    RecyclerView mRvObject;

    private BaseRecyclerViewAdapter mAdapterCategory;
    private BaseRecyclerViewAdapter mAdapterStep;

    private String mContents = null;
    private int mPostIndex = -1;
    private int mStepSelected = -1;
    private int mCategorySelected = -1;
    private int mType = -1;
    private BeanActionPostDetail mBean = null;
    private ArrayList<File> mArrayImages = null;
    private ProgressDialog mDialog;

    public static FragmentLevelChoice newInstance(int post_index, BeanActionPostDetail bean) {
        FragmentLevelChoice fragment = new FragmentLevelChoice();
        fragment.mPostIndex = post_index;
        fragment.mType = ActivityAddActionPost.TYPE_RESET_LEVEL;
        fragment.mBean = bean;
        return fragment;
    }

    public static FragmentLevelChoice newInstance(String contents, ArrayList<File> image_list) {
        FragmentLevelChoice fragment = new FragmentLevelChoice();
        fragment.mContents = contents;
        fragment.mArrayImages = image_list;
        fragment.mType = ActivityAddActionPost.TYPE_ADD_ACTION_POST;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_choice, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        // init Adapter
        initAdapter();
        // init Data
        httpGetActionPostCategory();
    }

    private void initView() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initTitleBar();
    }

    private void initTitleBar() {
        BaseTitleBar btbBar = ((ActivityAddActionPost) getActivity()).getmBtbBar();
        btbBar.setTitle("해당 목표와 단계를 선택해주세요");
        btbBar.setIOBaseTitleBarListener(this);
        btbBar.getmTvTextBtn().setText("완료");
        btbBar.getmIvClose().setVisibility(View.GONE);
        btbBar.getmIvBack().setVisibility(View.VISIBLE);
        btbBar.getmTvTextBtn().setSelected(true);
    }

    private IORecyclerViewListener mIOCategoryListener = new IORecyclerViewListener() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseViewHolder.newInstance(R.layout.listitem_level_choice_item, parent, false);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
            BeanLevelChoiceCategory bean = (BeanLevelChoiceCategory) mAdapterCategory.get(i);
            LinearLayout llItem = h.getItemView(R.id.ll_item);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            tvTitle.setText(bean.getObject_name());

            if (mCategorySelected == bean.getIdx()) {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvTitle.setTextColor(getResources().getColor(R.color.black));
            }
            llItem.setOnClickListener(v -> {
                onClickedCategory(bean.getIdx());
            });
        }

        @Override
        public int getItemCount() {
            return mAdapterCategory.size();
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }
    };

    private void onClickedCategory(int index) {
        mCategorySelected = index;
        mStepSelected = -1;
        mAdapterStep.clear();
        if (mCategorySelected != -1) httpGetActionPostCategoryObject(index);
        mAdapterCategory.notifyDataSetChanged();
    }

    private IORecyclerViewListener mIOObjectListener = new IORecyclerViewListener() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseViewHolder.newInstance(R.layout.listitem_level_choice_item, parent, false);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

            BeanLevelChoiceObject bean = (BeanLevelChoiceObject) mAdapterStep.get(i);
            LinearLayout llItem = h.getItemView(R.id.ll_item);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            tvTitle.setText(bean.getTitle());

            if (mStepSelected == bean.getIdx()) {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvTitle.setTextColor(getResources().getColor(R.color.black));
            }

            llItem.setOnClickListener(v -> {
                mStepSelected = bean.getIdx();
                mAdapterStep.notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return mAdapterStep.size();
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }
    };


    private void initAdapter() {
        mAdapterCategory = new BaseRecyclerViewAdapter(getContext(), mIOCategoryListener);
        mRvCategory.setAdapter(mAdapterCategory);
        mRvCategory.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapterStep = new BaseRecyclerViewAdapter(getContext(), mIOObjectListener);
        mRvObject.setAdapter(mAdapterStep);
        mRvObject.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * http Get
     * 실천인증 카테고리 조회
     */
    private void httpGetActionPostCategory() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_CATEGORY;
        // 내 카테고리 조회
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext())
                .Get(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            mAdapterCategory.clear();

//                            mAdapterCategory.add(new BeanLevelChoiceCategory("임시 분류", -1));
                            JSONObject json = new JSONObject(body);
                            JSONArray objects = json.getJSONArray("objects");
                            Gson gson = new Gson();
                            for (int i = 0; i < objects.length(); i++) {
                                BeanLevelChoiceCategory bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanLevelChoiceCategory.class);
                                mAdapterCategory.add(bean);
                            }
                        }

                        if (mCategorySelected == -1 && mBean != null) {
                            mCategorySelected = mBean.getObject_idx();
                            onClickedCategory(mCategorySelected);
                        }
                    }
                });
    }

    /**
     * http Get
     * 실천인증 카테고리 세부사항 조회
     *
     * @param object_index
     */
    private void httpGetActionPostCategoryObject(int object_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_CATEGORY_INDEX;
        // 내 카테고리 조회
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.OBJECT_INDEX, String.valueOf(object_index));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient.getInstance(getContext())
                .Get(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {

                            JSONObject json = new JSONObject(body);
                            JSONArray objects = json.getJSONArray("object_steps");
                            Gson gson = new Gson();
                            for (int i = 0; i < objects.length(); i++) {
                                BeanLevelChoiceObject bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanLevelChoiceObject.class);
                                mAdapterStep.add(bean);
                            }

                            if (mStepSelected == -1 && mBean != null) {
                                mStepSelected = mBean.getStep_idx();
                                mBean.setStep_idx(-1);
                                mAdapterStep.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * http Post
     * 실천인증 추가
     */
    private void httpPostActionPost() {
        if (mCategorySelected == -1) {
            Toast.makeText(getContext().getApplicationContext(), "실천목표를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        // 내 실천인증 추가
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("content", mContents);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        body.put("register_date", sdf.format(new Date()));

        DAHttpClient.getInstance(getContext())
                .Post(url, header, body, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            if (mCategorySelected != -1) {
                                JSONObject json = new JSONObject(body);
                                JSONObject result = json.getJSONObject("result");
                                int insertId = result.getInt("insertId");
//                                httpPatchActionPost(insertId);
                                httpPostProfilesImage(insertId);
                            } else {
                                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                        }
                    }
                });
    }

    /**
     * http Patch
     * 실천 인증 수정
     */
    private void httpPatchActionPost(int action_post_index) {
        if (mCategorySelected == -1) {
            Toast.makeText(getContext().getApplicationContext(), "실천목표를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_INDEX;
        // 내 실천인증 수정
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX, String.valueOf(action_post_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        if (mCategorySelected != -1) body.put("object_idx", String.valueOf(mCategorySelected));
        if (mStepSelected != -1) body.put("step_idx", String.valueOf(mStepSelected));
        else body.put("step_idx","");

        JSONArray jArray = new JSONArray();
        try {
            if (mImageUploadCheck != null) {
                for (int i = 0; i < mImageUploadCheck.size(); i++) {
                    JSONObject jObject = new JSONObject();
                    String fileUrl = mImageUploadCheck.get(i);
                    jObject.put("url", fileUrl);
                    jArray.put(jObject);
                }
                body.put("image", jArray.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (mImageUploadCheck != null) {
            body.put("thumbnail_image", mImageUploadCheck.get(0));
        }
        DAHttpClient.getInstance(getContext()).Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                if (TextUtils.equals(code, SUCCESS)) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        });
    }

    /**
     * Image Upload
     */
    private void httpPostProfilesImage(int posts_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        HashMap<String, String> body = new HashMap<>();
        JSONArray jArray = new JSONArray();

        try {
            for (int i = 0; i < mArrayImages.size(); i++) {
                File file = (File) mArrayImages.get(i);
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

        client.Post(Comm_Param.URL_API_PROFILES_INDEX_INDEX_ACTIONPOSTS_INDEX_IMAGES
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()))
                .replace(Comm_Param.POST_INDEX, String.valueOf(posts_index)), header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    mImageUploadCheck = new LinkedHashMap<>();
                    JSONArray jsonUrl = json.getJSONArray("urlList");
                    for (int i = 0; i < jsonUrl.length(); i++) {
                        JSONObject url = jsonUrl.getJSONObject(i);
                        String fileURL = url.getString("fileURL");
                        String uploadURL = url.getString("uploadURL");
                        httpPostUploadImage(i, posts_index, (File) mArrayImages.get(i), fileURL, uploadURL);
                    }
                }
            }
        });
    }

    LinkedHashMap<Integer, String> mImageUploadCheck;

    private void httpPostUploadImage(int image_index, int posts_index, File file, String fileUrl, String uploadUrl) {
        // Create the connection and use it to upload the new object using the pre-signed URL.

        DAHttpClient.getInstance(getContext()).Put(uploadUrl, file, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    mImageUploadCheck.put(image_index, fileUrl);

                    if (mImageUploadCheck.size() == mArrayImages.size()) {
                        httpPatchActionPost(posts_index);
                    }
                }
            }
        });
    }


    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void OnClickRightTextBtn() {
        if (mType == ActivityAddActionPost.TYPE_ADD_ACTION_POST) {
            httpPostActionPost();
        } else if (mType == ActivityAddActionPost.TYPE_RESET_LEVEL) {
            httpPatchActionPost(mPostIndex);
        }
    }
}
