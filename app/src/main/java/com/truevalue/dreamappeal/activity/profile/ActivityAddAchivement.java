package com.truevalue.dreamappeal.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.base.BaseItemDecorationHorizontal;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityAddAchivement extends BaseActivity implements IOBaseTitleBarListener, IORecyclerViewListener {

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

    private BaseRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achivement);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mBtbBar);
        // 상단바 연동
        mBtbBar.setIOBaseTitleBarListener(this);

        // Adapter 초기화
        initAdapter();
        // 임시 데이터
        bindTempData();
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
     * Bind Temp Data
     */
    private void bindTempData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
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
        // TODO : 완료하면 상세 페이지 띄워줘야함
        httpPostAchivementPost();
    }

    /**
     * 실현 성과 등록
     */
    private void httpPostAchivementPost(){
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityAddAchivement.this);
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS;
        url = url.replace(Comm_Param.PROFILES_INDEX,String.valueOf(prefs.getProfileIndex()));

        if(TextUtils.isEmpty(mEtTitle.getText().toString()) || TextUtils.isEmpty(mEtInputComment.getText().toString())){
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = prefs.getToken();
        HashMap header = Utils.getHttpHeader(token);
        HashMap<String,String> body = new HashMap();

        body.put("title",mEtTitle.getText().toString());
        body.put("content",mEtInputComment.getText().toString());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        body.put("thumbnail_image","");
        body.put("register_date",strDate);
        body.put("tags","");

        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Post(url, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(ActivityAddAchivement.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_achivement_list, parent, false);
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


    @OnClick({R.id.iv_add_img, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_img: // 이미지 추가 버튼
                Intent intent = new Intent(ActivityAddAchivement.this, ActivityGalleryCamera.class);
                startActivity(intent);
                break;
            case R.id.btn_edit: // 수정 버튼
                break;
        }
    }
}
