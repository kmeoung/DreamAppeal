package com.truevalue.dreamappeal.fragment.profile.dream_present;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseItemDecorationVertical;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanDreamList;
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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentDreamList extends BaseFragment implements IOBaseTitleBarListener, IORecyclerViewListener {


    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.iv_add_dream)
    ImageView mIvAddDream;
    @BindView(R.id.tv_add_dream)
    TextView mTvAddDream;
    @BindView(R.id.ll_add_dream)
    LinearLayout mLlAddDream;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.tv_level_info)
    TextView mTvLevelInfo;
    @BindView(R.id.rv_dream_list)
    RecyclerView mRvDreamList;
    @BindView(R.id.ll_edit_list)
    LinearLayout mLlEditList;

    private BaseRecyclerViewAdapter mAdapter;
    private boolean isEdit = false;
    private int mUserIndex = -1;

    public static FragmentDreamList newInstance(int user_index) {
        FragmentDreamList fragment = new FragmentDreamList();
        fragment.mUserIndex = user_index;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);
        mBtbBar.getmTvTextBtn().setSelected(true);
        // Init Adapter
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Init Data
        initData();
    }

    // todo : 첫번째 꿈 두번쨰 꿈 설정해야 함
    private void initData() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        if (prefs.getMyProfileIndex() == prefs.getProfileIndex()) {
            mLlEditList.setVisibility(View.VISIBLE);
        }else mLlEditList.setVisibility(View.GONE);

        httpGetDreamList();
        mTvLevelInfo.setText("< 경험치 획득 > 실천 등록 + 10 / 성과 등록 + 30");
    }

    /**
     * 내 꿈 목록 조회
     * TODO : 첫번째 꿈 / 두번째 꿈 등은 서버에서 적용이 되어야 테스트 및 적용을 할 수 있음
     */
    private void httpGetDreamList() {
        String url = Comm_Param.URL_API_PROFILES_INDEX_LIST;
        url = url.replace(Comm_Param.USER_INDEX, mUserIndex + "");
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(getContext());
        mAdapter.clear();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    JSONArray profiles = object.getJSONArray("profiles");
                    Gson gson = new Gson();
                    for (int i = 0; i < profiles.length(); i++) {
                        try {
                            JSONObject profile = null;
                            profile = profiles.getJSONObject(i);
                            BeanDreamList bean = gson.fromJson(profile.toString(), BeanDreamList.class);
                            mAdapter.add(bean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 내 꿈 목록 제거
     */
    private void httpDeleteProfiles(int profile_idx) {

        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String token = prefs.getToken();
        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, profile_idx + "");

        HashMap header = Utils.getHttpHeader(token);

        DAHttpClient client = DAHttpClient.getInstance(getContext());

        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {

                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetDreamList();
                }
            }
        });
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvDreamList.setAdapter(mAdapter);
        mRvDreamList.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseItemDecorationVertical item = new BaseItemDecorationVertical(getContext(), 6);
        mRvDreamList.addItemDecoration(item);
        // Set Edit Mode
        isEditMode(false);
    }

    /**
     * 수정 모드에 따라 설정이 변경
     *
     * @param isEdit
     */
    private void isEditMode(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) { // 수정 모드일 경우
            mLlAddDream.setEnabled(false);
            mTvAddDream.setEnabled(false);
            mIvAddDream.setEnabled(false);
            mBtnEdit.setText("확인");
        } else { // 수정 모드가 아닐 경우
            mLlAddDream.setEnabled(true);
            mTvAddDream.setEnabled(true);
            mIvAddDream.setEnabled(true);
            mBtnEdit.setText("편집");
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Back Clicked
     */
    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        getActivity().onBackPressed();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_dream_list, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        BeanDreamList bean = (BeanDreamList) mAdapter.get(i);
        LinearLayout llItemView = h.getItemView(R.id.ll_dream_list_item);
        ImageView ivDreamProfile = h.getItemView(R.id.iv_dream_profile);
        ImageView ivDelete = h.getItemView(R.id.iv_delete);
        TextView tvValueStyle = h.getItemView(R.id.tv_value_style);
        TextView tvJob = h.getItemView(R.id.tv_job);
        TextView tvDreamLevel = h.getItemView(R.id.tv_dream_level);
        TextView tvActionPostCount = h.getItemView(R.id.tv_action_post_count);
        TextView tvAchivementPostCount = h.getItemView(R.id.tv_achivement_post_count);
        ProgressBar pbExp = h.getItemView(R.id.pb_exp);
        TextView tvExp = h.getItemView(R.id.tv_exp);
        pbExp.setMax(bean.getMax_exp());
        pbExp.setProgress(bean.getExp());
        tvExp.setText(bean.getExp() + " / " + bean.getMax_exp());


        if (TextUtils.isEmpty(bean.getImage()))
            Glide.with(this).load(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(ivDreamProfile);
        else
            Glide.with(this).load(bean.getImage()).placeholder(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(ivDreamProfile);

        tvValueStyle.setText(bean.getValue_style());
        tvJob.setText(bean.getJob());
        tvDreamLevel.setText(String.format("LV. %02d", bean.getLevel()));
        tvActionPostCount.setText(String.format("%d회", bean.getAction_post_count()));
        tvAchivementPostCount.setText(String.format("%d회", bean.getAchievement_post_count()));

        if (isEdit) { // 수정 모드일 경우
            llItemView.setBackground(getResources().getDrawable(R.drawable.dream_list_box_black));
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setTitle("프로필 삭제")
                            .setMessage("프로필을 삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mAdapter.size() > 1) {
                                        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                                        if (bean.getIdx() == prefs.getProfileIndex()) {
                                            Toast.makeText(getContext().getApplicationContext(), "현재 사용중인 프로필은 삭제가 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            httpDeleteProfiles(bean.getIdx());
                                        }
                                    } else {
                                        Toast.makeText(getContext().getApplicationContext(), "최소 1개의 프로필이 있어야 합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else { // 수정 모드가 아닐 경우
            h.itemView.setOnClickListener(v -> {
                Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("프로필 선택")
                        .setMessage("프로필을 선택하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
                                if (bean.getIdx() == prefs.getProfileIndex()) {
                                    Toast.makeText(getContext().getApplicationContext(), "현재 사용중인 프로필입니다.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    return;
                                } else {
                                    boolean isMy = false;
                                    if (prefs.getProfileIndex() == prefs.getMyProfileIndex()) {
                                        isMy = true;
                                    }
                                    prefs.setProfileIndex(bean.getIdx(), isMy);
                                    Toast.makeText(getContext().getApplicationContext(), "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    ((ActivityMain) getActivity()).setmProfileOrder(bean.getProfile_order());
                                }
                                dialog.dismiss();
                                getActivity().onBackPressed();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
            llItemView.setBackground(getResources().getDrawable(R.drawable.dream_list_box_gray));
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

    @OnClick({R.id.ll_add_dream, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_dream: // 드림 리스트 추가 버튼
                // 버튼이 활성화 되어 있을 경우에만
                if (mAdapter.size() < 10) {
                    if (mLlAddDream.isEnabled()) {
                        ((ActivityMain) getActivity()).replaceFragmentRight(new FragmentDreamTitle(), true);
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "프로필은 최대 10개까지만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit: // 수정 버튼
                isEdit = !isEdit;
                isEditMode(isEdit);
                break;
        }
    }
}
