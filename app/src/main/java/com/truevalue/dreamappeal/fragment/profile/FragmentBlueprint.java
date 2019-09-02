package com.truevalue.dreamappeal.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
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

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.profile.ActivityAbilityOpportunity;
import com.truevalue.dreamappeal.activity.profile.ActivityAddContents;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityObjectStep;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanAbilityOpportunityHeader;
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
import com.truevalue.dreamappeal.bean.BeanBlueprintObject;
import com.truevalue.dreamappeal.bean.BeanObjectHeader;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blueprint, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapter();

        bindTempData();

        httpGetBluePrint();
    }

    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvBlueprint.setAdapter(mAdapter);
        mRvBlueprint.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        mAdapter.add(new BeanAbilityOpportunityHeader()); // 능력 / 기회 헤더
        for (int i = 0; i < 3; i++) {
            mAdapter.add(new BeanBlueprintAbilityOpportunity());
        }
        mAdapter.add(new BeanObjectHeader()); // 실천목표 헤더
        for (int i = 0; i < 20; i++) {
            mAdapter.add(new BeanBlueprintObject());
        }
    }

    private void httpGetBluePrint(){
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_BLUEPRINT;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX,String.valueOf(prefs.getProfileIndex()));
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_comment,R.id.btn_commit_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_comment:
                Intent intent = new Intent(getContext(), ActivityCommentDetail.class);
                startActivity(intent);
                break;
            case R.id.btn_commit_comment:
                mEtComment.setText("");
                Toast.makeText(getContext(),"댓글이 입력되었습니다.",Toast.LENGTH_SHORT).show();
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
        if(getItemViewType(i) == LISTITEM_TYPE_ABILITY_OPPORTUNITY_HEADER){
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityAbilityOpportunity.class);
                    startActivity(intent);
                }
            });
        }

        if(getItemViewType(i) == LISTITEM_TYPE_OBJECT_HEADER){
            ImageView ivAddObject = h.getItemView(R.id.iv_add_object);

            ivAddObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityAddContents.class);
                    intent.putExtra(ActivityAddContents.STR_TITLE, "꿈에 맞는 실천 목표를 세워보세요");
                    startActivity(intent);
                    // TODO : Activity 애니메이션 없애기
//                    getActivity().overridePendingTransition(0, 0);
                }
            });
        }

       else if(getItemViewType(i) == LISTITEM_TYPE_OBJECT){
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityObjectStep.class);
                    startActivity(intent);
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
