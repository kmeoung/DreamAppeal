package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityGalleryCamera extends BaseActivity implements LifecycleOwner {

    public static final String VIEW_TYPE_ADD_ACTION_POST = "VIEW_TYPE_ADD_ACTION_POST";
    private final int REQUEST_IMAGE_CAPTURE = 1004;
    private final int REQUEST_ADD_ACTION_POST = 1040;

    private final int REQUEST_CODE_PERMISSION = 3000;
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
    private String mPath;

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

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    private void initView() {
        if (getIntent().getStringExtra(VIEW_TYPE_ADD_ACTION_POST) != null) {
            mTvTextBtn.setText("다음");

            mTvTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityGalleryCamera.this, ActivityAddActionPost.class);
                    intent.putExtra(ActivityAddActionPost.EXTRA_ACTION_POST_TYPE, ActivityAddActionPost.TYPE_ADD_ACTION_POST);
                    startActivityForResult(intent, REQUEST_ADD_ACTION_POST);
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            mTvTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo : 이미지 업로드 테스트
                    httpPostProfilesImage();
                }
            });
        }
    }

    /**
     * 회원을 처음 등록했을 때 초기화
     */
    private void httpPostProfilesImage() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityGalleryCamera.this);

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityGalleryCamera.this);

        File file = new File(getmPath());
        Log.d("FILE", file.getName());
        HashMap<String, String> body = new HashMap<>();
        String[] fileInfo = file.getName().split("\\.");
        body.put("key", fileInfo[0]);
        body.put("type", fileInfo[1]);

        client.Post(Comm_Param.URL_API_PROFILE_INDEX_IMAGE.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex())), header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject json = new JSONObject(body);
                    JSONObject jsonUrl = json.getJSONObject("url");
                    String fileUrl = jsonUrl.getString("fileURL");
                    String url = jsonUrl.getString("uploadURL");
                    httpPostUploadImage(file, fileUrl, url);
                }
            }
        });
    }

    private void httpPostUploadImage(File file, String fileUrl, String uploadUrl) {
        // Create the connection and use it to upload the new object using the pre-signed URL.

        DAHttpClient.getInstance(ActivityGalleryCamera.this).Put(uploadUrl, file, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    profileUpdate(fileUrl);
                }
            }
        });
    }

    private void profileUpdate(String fileUrl) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityGalleryCamera.this);

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityGalleryCamera.this);

        HashMap<String, String> body = new HashMap<>();
        body.put("image", fileUrl);

        client.Patch(Comm_Param.URL_API_PROFILES_INDEX.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex())), header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                // 성공일 시
                if (TextUtils.equals(code, SUCCESS)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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
        } else if (requestCode == REQUEST_ADD_ACTION_POST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
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
//            CustomToast.makeText(this, "필수값을 제대로 입력해주세요", CustomToast.LENGTH_SHORT).show();
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
//            CustomToast.makeText(this, "필수값을 제대로 입력해주세요", CustomToast.LENGTH_SHORT).show();
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
//                                    CustomToast.makeText(ActivityBuyNameCard.this, "이미 구매요청이 되었습니다", CustomToast.LENGTH_SHORT).show();
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
