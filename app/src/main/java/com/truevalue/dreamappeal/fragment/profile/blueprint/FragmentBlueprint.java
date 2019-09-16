package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanAbilityOpportunityHeader;
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
import com.truevalue.dreamappeal.bean.BeanBlueprintObject;
import com.truevalue.dreamappeal.bean.BeanObjectHeader;
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
import java.util.ArrayList;
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

    @BindView(R.id.rv_blueprint)
    RecyclerView mRvBlueprint;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.tv_comment_size)
    TextView mTvCommentSize;
    @BindView(R.id.iv_profile)
    ImageView mIvProfile;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_commit_comment)
    Button mBtnCommitComment;


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
        String url = Comm_Param.URL_API_BLUEPRINT;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    mAdapter.clear();
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(body);
                    ArrayList<BeanBlueprintAbilityOpportunity> abilityList = new ArrayList<>();
                    ArrayList<BeanBlueprintAbilityOpportunity> opportunityList = new ArrayList<>();
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
                        }
                    }

                    mAdapter.add(new BeanObjectHeader()); // 실천목표 헤더

                    JSONArray objects = json.getJSONArray("objects");
                    for (int i = 0; i < objects.length(); i++) {
                        BeanBlueprintObject bean = gson.fromJson(objects.getJSONObject(i).toString(), BeanBlueprintObject.class);
                        mAdapter.add(bean);
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
                startActivity(intent);
                break;
            case R.id.btn_commit_comment:
                mEtComment.setText("");
                Toast.makeText(getContext(), "댓글이 입력되었습니다.", Toast.LENGTH_SHORT).show();
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if (getItemViewType(i) == LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER) {
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(new FragmentAbilityOpportunity(), true);
                }
            });
        }

        if (getItemViewType(i) == LISTITEM_TYPE_OBJECT_HEADER) {
            ImageView ivAddObject = h.getItemView(R.id.iv_add_object);

            ivAddObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddContents.newInstance("실천목표 등록하기"), true);
                }
            });

        } else if (getItemViewType(i) == LISTITEM_TYPE_ABILITY_OPPORTUNITY) {
            BeanBlueprintAbilityOpportunity bean = (BeanBlueprintAbilityOpportunity) mAdapter.get(i);
            TextView tvContents = h.getItemView(R.id.tv_contents);
            tvContents.setText(bean.getContents());

        } else if (getItemViewType(i) == LISTITEM_TYPE_OBJECT) {
            BeanBlueprintObject bean = (BeanBlueprintObject) mAdapter.get(i);
            TextView tvObjectTitle = h.getItemView(R.id.tv_object_title);
            TextView tvObjectCount = h.getItemView(R.id.tv_object_count);
            TextView tvComplete = h.getItemView(R.id.tv_complete);
            ImageView ivSize = h.getItemView(R.id.iv_size);
            tvObjectTitle.setText(bean.getObject_name());
            tvObjectCount.setText("총 인증\n\n" + bean.getTotal_action_post_count() + "회");
            Glide.with(this).load(R.drawable.ic_side_triangle_blue).into(ivSize);
            // 완료 여부
            if (bean.getComplete() == 0) {
                tvComplete.setVisibility(View.GONE);
            } else {
                tvComplete.setVisibility(View.VISIBLE);
            }

            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityMain) getActivity()).replaceFragmentRight(FragmentObjectStep.newInstance(bean.getIdx()), true);
                }
            });
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
        return 0;
    }
}
