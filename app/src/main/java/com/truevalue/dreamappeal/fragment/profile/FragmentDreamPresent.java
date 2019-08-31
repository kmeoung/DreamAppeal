package com.truevalue.dreamappeal.fragment.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamDescription;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamList;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamTitle;
import com.truevalue.dreamappeal.activity.profile.ActivityMeritAndMotive;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Comm_Prefs_Param;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentDreamPresent extends BaseFragment implements IORecyclerViewListener {

    BaseRecyclerViewAdapter mAdapter;
    @BindView(R.id.tv_dream_name)
    TextView mTvDreamName;
    @BindView(R.id.tv_dream_level)
    TextView mTvDreamLevel;
    @BindView(R.id.ll_dreams)
    LinearLayout mLlDreams;
    @BindView(R.id.tv_follwer)
    TextView mTvFollwer;
    @BindView(R.id.ll_follower)
    LinearLayout mLlFollower;
    @BindView(R.id.tv_achivement_post_count)
    TextView mTvAchivementPostCount;
    @BindView(R.id.tv_action_post_count)
    TextView mTvActionPostCount;
    @BindView(R.id.iv_dream_profile)
    ImageView mIvDreamProfile;
    @BindView(R.id.tv_value_style)
    TextView mTvValueStyle;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.ll_dream_title)
    LinearLayout mLlDreamTitle;
    @BindView(R.id.tv_dream_description)
    TextView mTvDreamDescription;
    @BindView(R.id.rv_dream_description)
    RecyclerView mRvDreamDescription;
    @BindView(R.id.btn_dream_description_more)
    Button mBtnDreamDescriptionMore;
    @BindView(R.id.tv_merit_and_motive)
    TextView mTvMeritAndMotive;
    @BindView(R.id.btn_merit_and_motive_more)
    Button mBtnMeritAndMotiveMore;
    @BindView(R.id.tv_cheering)
    TextView mTvCheering;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_cheering)
    LinearLayout mLlCheering;

    private boolean isMyDreamMore = false;
    private boolean isMyDreamReason = false;

    Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_present, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        bindTempData();
        initView();

    }

    private void initView() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        if (prefs.getProfileIndex() < 0) {
            httpPostProfiles(prefs.getToken());
        }
    }

    private void httpPostProfiles(String token) {
        BaseOkHttpClient client = new BaseOkHttpClient();

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);

        HashMap<String, String> body = new HashMap<>();
        body.put("image", "");
        body.put("value_style", "");
        body.put("job", "");
        body.put("description", "");
        body.put("description_spec", new JSONObject().toString());
        body.put("meritNmotive", "");

        client.Post(Comm_Param.URL_API_PROFILES, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        // todo : D/SERVER POST RESPONSE: {"code":"SUCCESS","message":"프로필 등록 성공","result":{"fieldCount":0,"affectedRows":1,"insertId":1,"info":"","serverStatus":2,"warningStatus":1}} 형식 설정 필요
                    }
                });
            }
        });
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvDreamDescription.setAdapter(mAdapter);
        mRvDreamDescription.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        for (int i = 0; i < 3; i++) {
            mAdapter.add("");
        }
        mTvMeritAndMotive.setMaxLines(3);

        mBtnDreamDescriptionMore.setOnClickListener(v -> {
            if (isMyDreamMore) {
                isMyDreamMore = false;
                mAdapter.clear();
                for (int i = 0; i < 3; i++) {
                    mAdapter.add("");
                }
            } else {
                isMyDreamMore = true;
                mAdapter.clear();
                for (int i = 0; i < 10; i++) {
                    mAdapter.add("");
                }
            }
        });

        mBtnMeritAndMotiveMore.setOnClickListener(v -> {
            if (isMyDreamReason) {
                isMyDreamReason = false;
                mTvMeritAndMotive.setMaxLines(3);
            } else {
                isMyDreamReason = true;
                mTvMeritAndMotive.setMaxLines(1000);
            }
        });
    }

    @OnClick({R.id.rv_dream_description, R.id.ll_dreams, R.id.ll_follower, R.id.ll_merit_and_motive,
            R.id.iv_dream_profile, R.id.btn_dream_description_more,
            R.id.btn_merit_and_motive_more, R.id.ll_comment,
            R.id.ll_cheering, R.id.ll_dream_title, R.id.ll_dream_description})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dreams: // 내 꿈 레벨 (꿈 선택)
                intent = new Intent(getContext(), ActivityDreamList.class);
                startActivity(intent);
                break;
            case R.id.ll_follower: // 팔로워
                break;
            case R.id.iv_dream_profile: // 내 꿈 프로필 이미지
                intent = new Intent(getContext(), ActivityGalleryCamera.class);
                startActivity(intent);
                break;
            case R.id.btn_dream_description_more: // 내 꿈 더보기
                break;
            case R.id.btn_merit_and_motive_more: // 내 꿈 이유 더보기
                break;
            case R.id.ll_comment: // 댓글
                intent = new Intent(getContext(), ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering: // 불꽃(좋아요)
                break;
            case R.id.ll_dream_title: // 내 꿈 명칭 정하기 페이지 이동
                intent = new Intent(getContext(), ActivityDreamTitle.class);
                startActivity(intent);
                break;
            case R.id.ll_dream_description: // 핵심 문장으로 내 꿈 설명하기 페이지 이동
                intent = new Intent(getContext(), ActivityDreamDescription.class);
                startActivity(intent);
                break;
            case R.id.ll_merit_and_motive: // 이유 페이지 이동
                intent = new Intent(getContext(), ActivityMeritAndMotive.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * RecyclerView Interface
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_dream_description, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }
}
