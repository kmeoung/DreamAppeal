package com.truevalue.dreamappeal.fragment.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.truevalue.dreamappeal.activity.profile.ActivityDreamPresentComment;
import com.truevalue.dreamappeal.activity.profile.ActivityDreamTitle;
import com.truevalue.dreamappeal.activity.profile.ActivityMeritAndMotive;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        // todo : glide 테스트
        Glide.with(getContext()).load("https://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg").placeholder(R.drawable.drawer_user).into(mIvDreamProfile);

        // TODO : 임시 권한 설정
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        getGallery();
    }

    private void getGallery() {
        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

// content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

// Make the query.

        Cursor cur = getContext().getContentResolver().query(images, projection, null, null, null);

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);

                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket
                        + "  date_taken=" + date);
            } while (cur.moveToNext());

        }
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
                intent = new Intent(getContext(), ActivityDreamPresentComment.class);
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
