package com.truevalue.dreamappeal.fragment.profile;

import android.app.Activity;
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
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanLevelChoiceCategory;
import com.truevalue.dreamappeal.bean.BeanLevelChoiceHeader;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentLevelChoice extends BaseFragment implements IOBaseTitleBarListener {

    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;
    @BindView(R.id.rv_object)
    RecyclerView mRvObject;

    private BaseRecyclerViewAdapter mAdapterCategory;
    private BaseRecyclerViewAdapter mAdapterObject;

    private String mContents;
    private int mObjectSelected = -1;
    private int mCategorySelected = -1;

    // todo : 이미지 업로드 성공 시 변경
    public static FragmentLevelChoice newInstance(String contents) {
        FragmentLevelChoice fragment = new FragmentLevelChoice();
        fragment.mContents = contents;
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
        // init Adapter
        initAdapter();
        // init Data
        httpGetActionPostCategory();
    }

    @Override
    public void onResume() {
        super.onResume();
        initTitleBar();
    }

    private void initTitleBar() {
        ((ActivityAddActionPost) getActivity()).getmBtbBar().setIOBaseTitleBarListener(this);
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
                mCategorySelected = bean.getIdx();
                mObjectSelected = -1;
                mAdapterObject.clear();
                if (mCategorySelected != -1) httpGetActionPostCategoryObject(bean.getIdx());
                mAdapterCategory.notifyDataSetChanged();
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

    private IORecyclerViewListener mIOObjectListener = new IORecyclerViewListener() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseViewHolder.newInstance(R.layout.listitem_level_choice_item, parent, false);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

            BeanLevelChoiceObject bean = (BeanLevelChoiceObject) mAdapterObject.get(i);
            LinearLayout llItem = h.getItemView(R.id.ll_item);
            TextView tvTitle = h.getItemView(R.id.tv_title);
            tvTitle.setText(bean.getTitle());

            if (mObjectSelected == bean.getIdx()) {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else {
                llItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvTitle.setTextColor(getResources().getColor(R.color.black));
            }

            llItem.setOnClickListener(v -> {
                mObjectSelected = bean.getIdx();
                mAdapterObject.notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return mAdapterObject.size();
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

        mAdapterObject = new BaseRecyclerViewAdapter(getContext(), mIOObjectListener);
        mRvObject.setAdapter(mAdapterObject);
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
        DAHttpClient.getInstance()
                .Get(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            mAdapterCategory.clear();

                            mAdapterCategory.add(new BeanLevelChoiceCategory("임시 분류", -1));
                            JSONObject json = new JSONObject(body);
                            JSONArray objects = json.getJSONArray("objects");
                            Gson gson = new Gson();
                            for (int i = 0; i < objects.length(); i++) {
                                BeanLevelChoiceCategory bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanLevelChoiceCategory.class);
                                mAdapterCategory.add(bean);
                            }
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
        DAHttpClient.getInstance()
                .Get(url, header, null, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {

                            JSONObject json = new JSONObject(body);
                            JSONArray objects = json.getJSONArray("object_steps");
                            Gson gson = new Gson();
                            for (int i = 0; i < objects.length(); i++) {
                                BeanLevelChoiceObject bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanLevelChoiceObject.class);
                                mAdapterObject.add(bean);
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
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        // 내 실천인증 추가
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("content", mContents);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        body.put("register_date", sdf.format(new Date()));

        DAHttpClient.getInstance()
                .Post(url, header, body, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if(TextUtils.equals(code,SUCCESS)){
                            if(mCategorySelected != -1){
                                JSONObject json = new JSONObject(body);
                                JSONObject result = json.getJSONObject("result");
                                int insertId = result.getInt("insertId");
                                httpPatchActionPost(insertId);

                            }else{
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
    private void httpPatchActionPost(int action_post_index){
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACTIONPOSTS_INDEX;
        // 내 실천인증 수정
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.POST_INDEX,String.valueOf(action_post_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("object_idx",String.valueOf(mCategorySelected));
        if(mObjectSelected != -1) body.put("step_idx",String.valueOf(mObjectSelected));
        DAHttpClient.getInstance().Patch(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                if(TextUtils.equals(code,SUCCESS)){
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
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
        httpPostActionPost();
    }
}
