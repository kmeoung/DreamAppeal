package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.image.FragmentCamera;
import com.truevalue.dreamappeal.fragment.image.FragmentGallery;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityGalleryCamera extends BaseActivity implements LifecycleOwner {

    public static final String VIEW_TYPE_ADD_ACTION_POST = "VIEW_TYPE_ADD_ACTION_POST";
    private final int REQUEST_IMAGE_CAPTURE = 1004;
    @BindView(R.id.v_status)
    View mVStatus;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.sp_title)
    Spinner mSpTitle;
    @BindView(R.id.tv_text_btn)
    TextView mTvTextBtn;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.vp_viewpager)
    ViewPager mVpViewpager;
    @BindView(R.id.base_container)
    FrameLayout mBaseContainer;
    @BindView(R.id.tv_gallery)
    TextView mTvGallery;
    @BindView(R.id.tv_camera)
    TextView mTvCamera;
    @BindView(R.id.tl_tab)
    TabLayout mTlTab;

    private ArrayList<String> mTabList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);
        ButterKnife.bind(this);
        // 상태 창 투명화
        updateStatusbarTranslate(mLlTitle);
        replaceFragment(R.id.base_container, new FragmentGallery(), false);
        initView();
//        initTab();
//        initAdapter();
    }

    private void initView() {
        if (getIntent().getStringExtra(VIEW_TYPE_ADD_ACTION_POST) != null) {
            mTvTextBtn.setText("다음");

            mTvTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityGalleryCamera.this, ActivityAddActionPost.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }
    }


    public Spinner getTitleSpinner() {
        return mSpTitle;
    }


    /**
     * 사진찍어서 가져오기
     */
    public void onClickedCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private Uri ImageUriMyPhoto;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = null;
            try {
                extras = data.getExtras();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Uri uri = null;
            try {
                uri = data.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
//                mIvMyPhoto.setImageBitmap(photo);
            }
            if (uri != null) {
                ImageUriMyPhoto = data.getData();
//                Glide.with(this)
//                        .load(ImageUriMyPhoto)
//                        .into(mIvMyPhoto);
            }
        }
    }

//    private void httpPostBuyNameCard(final BaseProgressDialog dialog) {
//        TlogHashMap header = new TlogHashMap(ActivityBuyNameCard.this);
//
//        LinkedHashMap<String, File> fileBody = new LinkedHashMap<>();
//        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
//
//        if (type == 0) {
//            body.put("goodsType", "NORMAL");
//        } else {
//            body.put("goodsType", "PREMIUM");
//        }
//        body.put("isDefaultCard", "true");
//        // todo : 미리 등록해놓은 카드정보가 있을 경우
////        body.put("cardNumber", "");
////        body.put("cardExpireDate", "");
////        body.put("cardName", "");
////        body.put("cardContact", "");
//        body.put("mainDescription", mEtMainText.getText().toString());
//        body.put("subDescription", mEtMoreText.getText().toString());
//        body.put("selfIntroduction", mEtIntroduceText.getText().toString());
//        body.put("applyMemo", "unwelted");
//
//        if (TextUtils.isEmpty(mEtMainText.getText().toString())
//                || TextUtils.isEmpty(mEtMoreText.getText().toString())) {
//            Toast.makeText(this, "필수값을 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//            return;
//        }
//
//        Drawable nameCardDrawable = null;
//        Drawable myPhotoDrawable = null;
//        nameCardDrawable = mIvRealNameCard.getDrawable();
//        myPhotoDrawable = mIvMyPhoto.getDrawable();
//
//        File nameCardFile = null;
//        File myPhotoFile = null;
//        if (nameCardDrawable != null) {
//            Bitmap bitmap = ((BitmapDrawable) nameCardDrawable).getBitmap();
//
//            String filePath = Environment.getExternalStorageDirectory().getPath() + "/";
//            String fileName = "tempNameCard.jpeg";
//
//            SaveBitmapToFileCache(bitmap, filePath, fileName);
//
//            nameCardFile = new File(filePath + fileName);
//        }
//
//        if (myPhotoDrawable != null) {
//            Bitmap bitmap = ((BitmapDrawable) myPhotoDrawable).getBitmap();
//
//            String filePath = Environment.getExternalStorageDirectory().getPath() + "/";
//            String fileName = "tempMyPhoto.jpeg";
//
//            SaveBitmapToFileCache(bitmap, filePath, fileName);
//
//            myPhotoFile = new File(filePath + fileName);
//        }
//
//        if (nameCardFile != null && nameCardFile.exists()) {
//            fileBody.put("namecard1File", nameCardFile);
//        } else {
//            Toast.makeText(this, "필수값을 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//            return;
//        }
////        fileBody.put("namecard2File", null);
//
//        if (myPhotoFile != null && myPhotoFile.exists()) {
//            fileBody.put("photo1File", myPhotoFile);
//        } else {
////            fileBody.put("photo1File", null);
//        }
////        fileBody.put("photo2File", null);
//
//
//        final Handler handler = new Handler(getMainLooper());
//
//        TlogClient.getInstance().post(ActivityBuyNameCard.this,
//                Comm_Params.URL_USER_ID_BUY_NAMECARD, header, body, fileBody, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//                        final String strResponse = response.body().string();
//                        Log.d("ActivityBuyNameCard", strResponse);
//
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (response.code() == 201) {
//                                    dialog.dismiss();
//                                    finish();
//                                } else {
//                                    dialog.dismiss();
//                                    Toast.makeText(ActivityBuyNameCard.this, "이미 구매요청이 되었습니다", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                });
//    }

    /**
     * 어댑터 초기화
     */
    private void initAdapter() {
        mTlTab.setupWithViewPager(mVpViewpager);
        mVpViewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        for (int i = 0; i < mTabList.size(); i++) {
            mTlTab.getTabAt(i).setText(mTabList.get(i));
        }
        mVpViewpager.setOffscreenPageLimit(2);
    }

    /**
     * 탭 초기화
     */
    private void initTab() {
        mTabList = new ArrayList<>();
        mTabList.add("갤러리");
        mTabList.add("카메라");
    }

    @OnClick({R.id.iv_back, R.id.tv_text_btn, R.id.tv_gallery, R.id.tv_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_text_btn:
                break;
            case R.id.tv_gallery:
                break;
            case R.id.tv_camera:
                onClickedCamera();
                break;
        }
    }

    /**
     * ViewPager Adapter
     * TODO : 사진 찍고 난 후 설정이 필요 , 사진찍고 난 후 갤러리 refresh도 해줘야 함
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // 내 꿈 소개
                    return new FragmentGallery();
                case 1: // 실현 성과
                    return new FragmentCamera();
            }

            return null;
        }

        @Override
        public int getCount() {
            return mTabList.size(); // 페이지 2개 고정
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position);
        }
    }
}
