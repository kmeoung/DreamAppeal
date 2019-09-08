package com.truevalue.dreamappeal.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamDescription;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamList;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamTitle;
import com.truevalue.dreamappeal.activity.profile.ActivityMeritAndMotive;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanProfiles;
import com.truevalue.dreamappeal.bean.BeanProfilesIndex;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentDreamPresent extends BaseFragment implements IORecyclerViewListener {

    public static final int REQUEST_ACTIVITY_DREAM_TITLE = 1000;
    public static final int REQUEST_ACTIVITY_DESCRIPTION = 1001;
    public static final int REQUEST_ACTIVITY_MERIT_AND_MOTIVE = 1002;
    public static final int REQUEST_ACTIVITY_DREAM_LIST = 1003;


    public static final String EXTRA_ACTIVITY_DREAM_LIST = "EXTRA_ACTIVITY_DREAM_LIST";

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
    @BindView(R.id.tv_init_dream_title)
    TextView mTvInitDreamTitle;
    @BindView(R.id.ll_dream_description)
    LinearLayout mLlDreamDescription;
    @BindView(R.id.ll_merit_and_motive)
    LinearLayout mLlMeritAndMotive;
    @BindView(R.id.iv_cheering)
    ImageView mIvCheering;
    @BindView(R.id.tv_init_dream_description)
    TextView mTvInitDreamDescription;
    @BindView(R.id.tv_init_merit_and_motive)
    TextView mTvInitMeritAndMotive;

    private boolean isMyDreamMore = false;
    private boolean isMyDreamReason = false;

    private int mUserIndex = -1;

    private boolean isViewCreated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_present, container, false);
        ButterKnife.bind(this, view);
        isViewCreated = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAdapter == null) initAdapter();
        initView();
    }

    @Override
    public void onViewPaged(boolean isView) {
        if (isViewCreated) {
            if (isView) {
                // 초기화
                initView();
            }
        }
    }

    private void initView() {
        // 회원정보가 존재할 시
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String token = prefs.getToken();
        if (prefs.getProfileIndex() < 0) {
            httpPostProfiles(token);
        } else { // 회원정보가 존재하지 않을 시
            int profiles_index = prefs.getProfileIndex();
            httpGetProfilesIndex(profiles_index, token);
        }
    }

    /**
     * 회원을 처음 등록했을 때 초기화
     *
     * @param token
     */
    private void httpPostProfiles(String token) {
        BaseOkHttpClient client = new BaseOkHttpClient();

        HashMap header = Utils.getHttpHeader(token);

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
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    JSONObject result = object.getJSONObject("result");
                    Gson gson = new Gson();
                    BeanProfiles bean = gson.fromJson(result.toString(), BeanProfiles.class);
                    Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                    prefs.setProfileIndex(bean.getInsertId());

                    // 내 꿈 조회 호출
                    httpGetProfilesIndex(bean.getInsertId(), token);
                }
            }
        });
    }

    /**
     * 내 꿈 소개 조회
     *
     * @param profiles_index
     * @param token
     */
    private void httpGetProfilesIndex(int profiles_index, String token) {

        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(profiles_index));

        mAdapter.clear();

        HashMap header = Utils.getHttpHeader(token);
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {

                    Gson gson = new Gson();
                    JSONObject object = new JSONObject(body);
                    JSONObject profile = object.getJSONObject("profile");
                    BeanProfilesIndex bean = gson.fromJson(profile.toString(), BeanProfilesIndex.class);

                    try {
                        JSONArray jsonArray = profile.getJSONArray("description_spec");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject content = jsonArray.getJSONObject(i);
                            String strContent = content.getString("content");
                            mAdapter.add(strContent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // todo : 팔로워아직 서버에 없음
                    mTvDreamLevel.setText(String.format("LV.%02d", bean.getLevel()));
                    mTvAchivementPostCount.setText(String.format("%d", bean.getAchievement_post_count()));
                    mTvActionPostCount.setText(String.format("%d", bean.getAction_post_count()));
                    mTvValueStyle.setText(bean.getValue_style());
                    mTvJob.setText(bean.getJob());
                    mTvMeritAndMotive.setText(bean.getMeritNmotive());
                    mTvDreamDescription.setText(bean.getDescription());
                    mUserIndex = bean.getUser_idx();
                    // 내 이미지가 비어있을 경우
                    if (TextUtils.isEmpty(bean.getImage()))
                        Glide.with(getContext()).load(R.drawable.drawer_user).into(mIvDreamProfile);
                    else
                        Glide.with(getContext()).
                                load(bean.getImage()).
                                thumbnail(R.drawable.drawer_user).
                                into(mIvDreamProfile);

                    // 꿈에 대한 설명이 비어있을 경우
                    if (TextUtils.isEmpty(bean.getDescription()))
                        mTvInitDreamDescription.setVisibility(View.VISIBLE);
                    else mTvInitDreamDescription.setVisibility(View.GONE);

                    // 꿈 명칭이 비어있을 경우
                    if (TextUtils.isEmpty(bean.getValue_style()) && TextUtils.isEmpty(bean.getJob()))
                        mTvInitDreamTitle.setVisibility(View.VISIBLE);
                    else mTvInitDreamTitle.setVisibility(View.GONE);

                    // 꿈 이유가 비어있을 경우
                    if (TextUtils.isEmpty(bean.getMeritNmotive()))
                        mTvInitMeritAndMotive.setVisibility(View.VISIBLE);
                    else mTvInitMeritAndMotive.setVisibility(View.GONE);

                }
            }
        });

    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvDreamDescription.setAdapter(mAdapter);
        mRvDreamDescription.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick({R.id.rv_dream_description, R.id.ll_dreams, R.id.ll_follower, R.id.ll_merit_and_motive,
            R.id.iv_dream_profile, R.id.btn_dream_description_more,
            R.id.btn_merit_and_motive_more, R.id.ll_comment,
            R.id.ll_cheering, R.id.ll_dream_title, R.id.ll_dream_description, R.id.tv_init_dream_title,
            R.id.tv_init_dream_description, R.id.tv_init_merit_and_motive})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dreams: // 내 꿈 레벨 (꿈 선택)
                // todo : 서버가 연동이 되어 있을 시에만
                if (mUserIndex != -1) {
                    intent = new Intent(getContext(), ActivityDreamList.class);
                    intent.putExtra(ActivityDreamList.EXTRA_USER_INDEX, mUserIndex);
                    startActivityForResult(intent, REQUEST_ACTIVITY_DREAM_LIST);
                }
                break;
            case R.id.ll_follower: // 팔로워
                break;
            case R.id.iv_dream_profile: // 내 꿈 프로필 이미지
                intent = new Intent(getContext(), ActivityGalleryCamera.class);
                startActivity(intent);
                break;
            case R.id.btn_dream_description_more: // 내 꿈 더보기
                if (isMyDreamMore) {
                    isMyDreamMore = false;
                } else {
                    isMyDreamMore = true;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_merit_and_motive_more: // 내 꿈 이유 더보기
                if (isMyDreamReason) {
                    isMyDreamReason = false;
                    mTvMeritAndMotive.setMaxLines(3);
                } else {
                    isMyDreamReason = true;
                    mTvMeritAndMotive.setMaxLines(1000);
                }
                break;
            case R.id.ll_comment: // 댓글
                intent = new Intent(getContext(), ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_cheering: // 불꽃(좋아요)
                break;
            case R.id.ll_dream_title: // 내 꿈 명칭 정하기 페이지 이동
            case R.id.tv_init_dream_title:
                intent = new Intent(getContext(), ActivityDreamTitle.class);
                String valueStyle = mTvValueStyle.getText().toString();
                String job = mTvJob.getText().toString();
                if (!TextUtils.isEmpty(valueStyle) &&
                        !TextUtils.isEmpty(job)) {
                    String dreamTitle = valueStyle + ActivityDreamTitle.EXTRA_DREAM_TITLE_DIVIDER + job;
                    intent.putExtra(ActivityDreamTitle.EXTRA_DREAM_TITLE, dreamTitle);
                }
                startActivityForResult(intent, REQUEST_ACTIVITY_DREAM_TITLE);
                break;
            case R.id.ll_dream_description: // 핵심 문장으로 내 꿈 설명하기 페이지 이동
            case R.id.tv_init_dream_description:
                intent = new Intent(getContext(), ActivityDreamDescription.class);

                if (!TextUtils.isEmpty(mTvDreamDescription.getText().toString())) {
                    String description = mTvDreamDescription.getText().toString();
                    for (int i = 0; i < mAdapter.size(); i++) {
                        description += ActivityDreamDescription.EXTRA_DESCRIPTION_DIVIDER;
                        description += mAdapter.get(i);
                    }
                    intent.putExtra(ActivityDreamDescription.EXTRA_DREAM_DESCRIPTION, description);
                }
                startActivityForResult(intent, REQUEST_ACTIVITY_DESCRIPTION);
                break;
            case R.id.ll_merit_and_motive: // 이유 페이지 이동
            case R.id.tv_init_merit_and_motive:
                intent = new Intent(getContext(), ActivityMeritAndMotive.class);
                String meritAndMotive = mTvMeritAndMotive.getText().toString();
                if (!TextUtils.isEmpty(meritAndMotive))
                    intent.putExtra(ActivityMeritAndMotive.EXTRA_MERIT_AND_MOTIVE, meritAndMotive);
                startActivityForResult(intent, REQUEST_ACTIVITY_MERIT_AND_MOTIVE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACTIVITY_DREAM_LIST: // 내 꿈 목록
                case REQUEST_ACTIVITY_DREAM_TITLE: // 내 꿈 명칭
                case REQUEST_ACTIVITY_DESCRIPTION: // 내 꿈 설명
                case REQUEST_ACTIVITY_MERIT_AND_MOTIVE: // 내 꿈 매력
                    Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                    // 페이지 리로드
                    httpGetProfilesIndex(prefs.getProfileIndex(), prefs.getToken());
                    isMyDreamMore = false;
                    isMyDreamReason = false;
                    break;
            }
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
        String strContents = (String) mAdapter.get(i);
        TextView tvDreamContents = h.getItemView(R.id.tv_contents);
        tvDreamContents.setText(strContents);
        if (isMyDreamMore) {
            tvDreamContents.setMaxLines(1000);
        } else {
            tvDreamContents.setMaxLines(1);
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
}
