package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanAbilityOpportunityHeader;
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
import com.truevalue.dreamappeal.bean.BeanBlueprintObject;
import com.truevalue.dreamappeal.bean.BeanObjectHeader;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentBlueprint extends BaseFragment implements IORecyclerViewListener {

    private final int LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER = 0;
    private final int LISTITEM_TYPE_ABILITY_OPPORTUNITY = 1;
    private final int LISTITEM_TYPE_OBJECT_HEADER = 2;
    private final int LISTITEM_TYPE_OBJECT = 3;
    private final int LISTITEM_DEFAULT_ABILITY_OPPORTUNITY = 4;
    private final int LISTITEM_DEFAULT_OBJECT = 5;
    @BindView(R.id.iv_profile)
    ImageView mIvProfile;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.rl_comment)
    RelativeLayout mRlComment;

    private String TYPE_DEFAULT_ABILITY_OPPORTUNITY = "TYPE_DEFAULT_ABILITY_OPPORTUNITY";
    private String TYPE_DEFAULT_OBJECT = "TYPE_DEFAULT_OBJECT";

    @BindView(R.id.rv_blueprint)
    RecyclerView mRvBlueprint;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_commit_comment)
    ImageButton mBtnCommitComment;

    private BaseRecyclerViewAdapter mAdapter;
    private boolean isViewCreated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blueprint, container, false);
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

    private void initView() {
        mBtnCommitComment.setSelected(true);
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtComment.getText().length() > 0) {
                    mBtnCommitComment.setVisibility(View.VISIBLE);
                    mRlComment.setVisibility(View.GONE);
                } else {
                    mBtnCommitComment.setVisibility(View.GONE);
                    mRlComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onViewPaged(boolean isView) {
        if (isViewCreated) {
            if (isView) {
                httpGetBluePrint();
            }
        }
        super.onViewPaged(isView);
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvBlueprint.setAdapter(mAdapter);
        mRvBlueprint.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * 발전 계획 가져오기
     */
    private void httpGetBluePrint() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILE_BLUEPRINT;
        url = url.replace(Comm_Param.MY_PROFILES_INDEX, String.valueOf(prefs.getMyProfileIndex()));
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                if (!TextUtils.equals(code, SUCCESS) || Comm_Param.REAL)
                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    mAdapter.clear();
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(body);
                    ArrayList<BeanBlueprintAbilityOpportunity> abilityList = new ArrayList<>();
                    ArrayList<BeanBlueprintAbilityOpportunity> opportunityList = new ArrayList<>();
                    try {
                        int commentCount = json.getInt("comment_count");
                        if (commentCount < 1000) {
                            mTvComment.setText(commentCount + "");
                        } else {
                            int k = (commentCount / 1000);
                            if (k < 1000) {
                                mTvComment.setText(k + "K");
                            } else {
                                int m = (k / 1000);
                                mTvComment.setText(m + "M");
                            }
                        }

                        String image = json.getString("user_image");
                        if (TextUtils.isEmpty(image))
                            Glide.with(getContext()).load(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(mIvProfile);
                        else
                            Glide.with(getContext()).load(image).placeholder(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(mIvProfile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray abilities = json.getJSONArray("abilities");
                        for (int i = 0; i < abilities.length(); i++) {
                            JSONObject ability = abilities.getJSONObject(i);
                            int idx = ability.getInt("idx");
                            int profile_idx = ability.getInt("profile_idx");
                            String strAbility = ability.getString("ability");
                            abilityList.add(new BeanBlueprintAbilityOpportunity(profile_idx, idx, strAbility));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray opportunities = json.getJSONArray("opportunities");
                        for (int i = 0; i < opportunities.length(); i++) {
                            JSONObject opportunity = opportunities.getJSONObject(i);
                            int idx = opportunity.getInt("idx");
                            int profile_idx = opportunity.getInt("profile_idx");
                            String strOpportunity = opportunity.getString("opportunity");
                            opportunityList.add(new BeanBlueprintAbilityOpportunity(profile_idx, idx, strOpportunity));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.add(new BeanAbilityOpportunityHeader()); // 능력 / 기회 헤더
                    if (abilityList.size() > 0 && opportunityList.size() > 0) { // 능력 및 기회가 둘다 1개이상 있을 시
                        if (abilityList.size() > 1) { // 능력이 1개이상 있을 시
                            for (int i = 0; i < 2; i++) {
                                mAdapter.add(abilityList.get(i));
                            }

                            for (int i = 0; i < 1; i++) {
                                mAdapter.add(opportunityList.get(i));
                            }
                        } else { // 능력이 1개일 시
                            if (opportunityList.size() > 1) { // 기회가 1개 이상일 시
                                for (int i = 0; i < 1; i++) {
                                    mAdapter.add(abilityList.get(i));
                                }

                                for (int i = 0; i < 2; i++) {
                                    mAdapter.add(opportunityList.get(i));
                                }
                            } else { // 능력 및 기회가 1개일 시
                                for (int i = 0; i < abilityList.size(); i++) {
                                    mAdapter.add(abilityList.get(i));
                                }

                                for (int i = 0; i < opportunityList.size(); i++) {
                                    mAdapter.add(opportunityList.get(i));
                                }
                            }
                        }
                    } else {
                        if (abilityList.size() > 0) { // 능력만 있을 시

                            int max = 0;

                            if (abilityList.size() > 2) {
                                max = 3;
                            } else max = abilityList.size();

                            for (int i = 0; i < max; i++) {
                                mAdapter.add(abilityList.get(i));
                            }

                        } else if (opportunityList.size() > 0) { // 기회만 있을 시

                            int max = 0;

                            if (opportunityList.size() > 2) {
                                max = 3;
                            } else max = opportunityList.size();

                            for (int i = 0; i < max; i++) {
                                mAdapter.add(opportunityList.get(i));
                            }
                        } else { // 둘다 없을 시
                            mAdapter.add(TYPE_DEFAULT_ABILITY_OPPORTUNITY);
                        }
                    }

                    mAdapter.add(new BeanObjectHeader()); // 실천목표 헤더
                    JSONArray objects = null;
                    try {
                        objects = json.getJSONArray("objects");
                        if (objects == null || objects.length() < 1)
                            mAdapter.add(TYPE_DEFAULT_OBJECT);
                        for (int i = 0; i < objects.length(); i++) {
                            BeanBlueprintObject bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanBlueprintObject.class);
                            mAdapter.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_comment, R.id.btn_commit_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_comment:
                Intent intent = new Intent(getContext(), ActivityCommentDetail.class);
                intent.putExtra(ActivityCommentDetail.EXTRA_COMMENT_TYPE, ActivityCommentDetail.TYPE_BLUEPRINT);
                intent.putExtra(ActivityCommentDetail.EXTRA_OFF_KEYBOARD, "OFF");
                startActivityForResult(intent, FragmentMain.REQUEST_BLUEPRINT_COMMENT);
                break;
            case R.id.btn_commit_comment:
                httpPostComment();
                mEtComment.setText("");
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER)
            return BaseViewHolder.newInstance(R.layout.listitem_blueprint_ability_opportunity_header, parent, false);
        else if (viewType == LISTITEM_TYPE_ABILITY_OPPORTUNITY)
            return BaseViewHolder.newInstance(R.layout.listitem_blueprint_ability_opportunity, parent, false);
        else if (viewType == LISTITEM_TYPE_OBJECT_HEADER)
            return BaseViewHolder.newInstance(R.layout.listitem_blueprint_object_header, parent, false);
        else if (viewType == LISTITEM_TYPE_OBJECT)
            return BaseViewHolder.newInstance(R.layout.listitem_object, parent, false);
        else if (viewType == LISTITEM_DEFAULT_ABILITY_OPPORTUNITY || viewType == LISTITEM_DEFAULT_OBJECT)
            return BaseViewHolder.newInstance(R.layout.listitem_deafult_text, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());

        if (getItemViewType(i) == LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER) {
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(new FragmentAbilityOpportunity(), true);
                }
            });
        } else if (getItemViewType(i) == LISTITEM_TYPE_OBJECT_HEADER) {
            ImageView ivAddObject = h.getItemView(R.id.iv_add_object);
            // todo : 추후 처리
            Button btn = h.getItemView(R.id.btn_abliity_opportunity);

            if (prefs.getProfileIndex() == prefs.getMyProfileIndex()) {
                ivAddObject.setVisibility(View.VISIBLE);
            } else {
                ivAddObject.setVisibility(View.GONE);
            }

            ivAddObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddContents.newInstance("실천목표 등록하기"), true);
                }
            });

        } else if (getItemViewType(i) == LISTITEM_TYPE_ABILITY_OPPORTUNITY) {
            BeanBlueprintAbilityOpportunity bean = (BeanBlueprintAbilityOpportunity) mAdapter.get(i);
            TextView tvContents = h.getItemView(R.id.tv_contents);
            LinearLayout llItem = h.getItemView(R.id.ll_item);
            tvContents.setText(bean.getContents());
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(new FragmentAbilityOpportunity(), true);
                }
            });
        } else if (getItemViewType(i) == LISTITEM_TYPE_OBJECT) {
            BeanBlueprintObject bean = (BeanBlueprintObject) mAdapter.get(i);
            TextView tvObjectTitle = h.getItemView(R.id.tv_object_title);
            TextView tvObjectCount = h.getItemView(R.id.tv_object_count);
            LinearLayout llComplete = h.getItemView(R.id.ll_complete);
            ImageView ivSize = h.getItemView(R.id.iv_size);
            ImageView ivObject = h.getItemView(R.id.iv_object);
            tvObjectTitle.setText(bean.getObject_name());
            tvObjectCount.setText("총 인증\n\n" + bean.getTotal_action_post_count() + "회");
            Glide.with(this).load(R.drawable.ic_side_triangle_blue).into(ivSize);

            if (TextUtils.isEmpty(bean.getThumbnail_image()))
                Glide.with(this).load(R.drawable.ic_image_black_24dp).into(ivObject);
            else
                Glide.with(this).load(bean.getThumbnail_image()).apply(new RequestOptions().centerCrop()).placeholder(R.drawable.ic_image_black_24dp).into(ivObject);
            // 완료 여부
            if (bean.getComplete() == 0) {
                llComplete.setVisibility(View.GONE);
            } else {
                llComplete.setVisibility(View.VISIBLE);
            }

            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(FragmentObjectStep.newInstance(bean.getIdx()), true);
                }
            });
        } else if (getItemViewType(i) == LISTITEM_DEFAULT_ABILITY_OPPORTUNITY) {
            TextView tvView = h.getItemView(R.id.tv_default_text);
            String merit = "꿈에 필요한 능력과";
            String motive = "기회 정리하기";
            SpannableStringBuilder spannableMerit = Utils.replaceTextColor(getContext(), merit, "능력");
            SpannableStringBuilder spannableMotive = Utils.replaceTextColor(getContext(), motive, "기회");
            tvView.setText(TextUtils.concat(spannableMerit, " ", spannableMotive));

            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(new FragmentAbilityOpportunity(), true);
                }
            });
        } else if (getItemViewType(i) == LISTITEM_DEFAULT_OBJECT) {
            TextView tvView = h.getItemView(R.id.tv_default_text);
            String str = "능력/기회를 위한 실천목표를 등록해주세요";
            SpannableStringBuilder ssb = Utils.replaceTextColor(getContext(), str, "실천목표");
            tvView.setText(ssb);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (mAdapter.get(i) instanceof BeanAbilityOpportunityHeader)
            return LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER;
        else if (mAdapter.get(i) instanceof BeanObjectHeader)
            return LISTITEM_TYPE_OBJECT_HEADER;
        else if (mAdapter.get(i) instanceof BeanBlueprintAbilityOpportunity)
            return LISTITEM_TYPE_ABILITY_OPPORTUNITY;
        else if (mAdapter.get(i) instanceof BeanBlueprintObject)
            return LISTITEM_TYPE_OBJECT;
        else if (mAdapter.get(i) instanceof String) {
            if (TextUtils.equals((String) mAdapter.get(i), TYPE_DEFAULT_ABILITY_OPPORTUNITY)) {
                return LISTITEM_DEFAULT_ABILITY_OPPORTUNITY;
            } else return LISTITEM_DEFAULT_OBJECT;
        }
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FragmentMain.REQUEST_BLUEPRINT_COMMENT) {
            httpGetBluePrint();
        }
    }

    /**
     * http Post
     * 댓글 등록
     */
    private void httpPostComment() {
        if (TextUtils.isEmpty(mEtComment.getText().toString())) {
            Toast.makeText(getContext().getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS
                .replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("writer_idx", String.valueOf(prefs.getMyProfileIndex()));
        body.put("content", mEtComment.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        body.put("register_date", sdf.format(new Date()));
        DAHttpClient.getInstance(getContext())
                .Post(url, header, body, new IOServerCallback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (TextUtils.equals(code, SUCCESS)) {
                            httpGetBluePrint();
                            mEtComment.setText("");
                        }
                    }
                });
    }
}
