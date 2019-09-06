package com.truevalue.dreamappeal.activity.profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseItemDecorationVertical;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanDreamList;
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

public class ActivityDreamList extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

    public static final String EXTRA_USER_INDEX = "EXTRA_USER_INDEX";
    public static final int REQUEST_ADD_PROFILES = 1010;

    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.rv_dream_list)
    RecyclerView mRvDreamList;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.iv_add_dream)
    ImageView mIvAddDream;
    @BindView(R.id.tv_add_dream)
    TextView mTvAddDream;
    @BindView(R.id.ll_add_dream)
    LinearLayout mLlAddDream;

    private BaseRecyclerViewAdapter mAdapter;
    private boolean isEdit = false;
    private int mUserIndex = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_list);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        // Init Adapter
        initAdapter();
        // Init Data
        initData();
        // Bind Temp Data
//        bindTempData();
    }
    // todo : 첫번째 꿈 두번쨰 꿈 설정해야 함
    private void initData() {
        mUserIndex = getIntent().getIntExtra(EXTRA_USER_INDEX, -1);
        httpGetDreamList();
    }

    /**
     * 내 꿈 목록 조회
     * TODO : 첫번째 꿈 / 두번째 꿈 등은 서버에서 적용이 되어야 테스트 및 적용을 할 수 있음
     */
    private void httpGetDreamList() {
        String url = Comm_Param.URL_API_PROFILES_INDEX_LIST;
        url = url.replace(Comm_Param.USER_INDEX, mUserIndex + "");
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamList.this);
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        mAdapter.clear();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityDreamList.this, message, Toast.LENGTH_SHORT).show();

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

        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamList.this);
        String token = prefs.getToken();
        String url = Comm_Param.URL_API_PROFILES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, profile_idx + "");

        HashMap header = Utils.getHttpHeader(token);

        BaseOkHttpClient client = new BaseOkHttpClient();

        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {

                Toast.makeText(ActivityDreamList.this, message, Toast.LENGTH_SHORT).show();
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
        mAdapter = new BaseRecyclerViewAdapter(ActivityDreamList.this, this);
        mRvDreamList.setAdapter(mAdapter);
        mRvDreamList.setLayoutManager(new LinearLayoutManager(ActivityDreamList.this));
        BaseItemDecorationVertical item = new BaseItemDecorationVertical(ActivityDreamList.this, 6);
        mRvDreamList.addItemDecoration(item);
        // Set Edit Mode
        isEditMode(false);
    }

    /**
     * Bind Temp Data
     */
    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
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
        finish();
    }

    /**
     * Right Text Button Clicked
     */
    @Override
    public void OnClickRightTextBtn() {
        onBackPressed();
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


        if (TextUtils.isEmpty(bean.getImage()))
            Glide.with(this).load(R.drawable.drawer_user).into(ivDreamProfile);
        else
            Glide.with(this).load(bean.getImage()).thumbnail(R.drawable.drawer_user).into(ivDreamProfile);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDreamList.this)
                            .setTitle("프로필 삭제")
                            .setMessage("프로필을 삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mAdapter.size() > 1) {
                                        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamList.this);
                                        if (bean.getIdx() == prefs.getProfileIndex()) {
                                            Toast.makeText(ActivityDreamList.this, "현재 사용중인 프로필은 삭제가 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            httpDeleteProfiles(bean.getIdx());
                                        }
                                    } else {
                                        Toast.makeText(ActivityDreamList.this, "최소 1개의 프로필이 있어야 합니다.", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDreamList.this)
                        .setTitle("프로필 선택")
                        .setMessage("프로필을 선택하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityDreamList.this);
                                if (bean.getIdx() == prefs.getProfileIndex()) {
                                    Toast.makeText(ActivityDreamList.this, "현재 사용중인 프로필입니다.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    return;
                                } else {
                                    prefs.setProfileIndex(bean.getIdx());
                                    Toast.makeText(ActivityDreamList.this, "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
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
                if (mAdapter.size() < 3) {
                    if (mLlAddDream.isEnabled()) {
                        Intent intent = new Intent(ActivityDreamList.this, ActivityDreamTitle.class);
                        intent.putExtra(ActivityDreamTitle.EXTRA_ADD_PROFILES,true);
                        startActivityForResult(intent,REQUEST_ADD_PROFILES);
                    }
                } else {
                    Toast.makeText(this, "프로필은 최대 3개까지만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit: // 수정 버튼
                isEdit = !isEdit;
                isEditMode(isEdit);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_ADD_PROFILES){
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
