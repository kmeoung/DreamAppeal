package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;
import com.truevalue.dreamappeal.fragment.FragmentMain;
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
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityGalleryCamera extends BaseActivity implements LifecycleOwner {

    public static final String VIEW_TYPE_ADD_ACTION_POST = "VIEW_TYPE_ADD_ACTION_POST";
    public static final String REQEUST_IMAGE_FILE = "REQEUST_IMAGE_FILE";
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
    private File mImageFile;
    private int mViewType = -1;

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

    public File getmImageFile() {
        return mImageFile;
    }

    public void setmImageFile(File mImageFile) {
        this.mImageFile = mImageFile;
    }

    private void initView() {
        mViewType = getIntent().getIntExtra(VIEW_TYPE_ADD_ACTION_POST, -1);
        if (mViewType != -1) {

            switch (mViewType) {
                case FragmentMain.REQUEST_ADD_POST:
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
                    break;
                case FragmentMain.REQUEST_ADD_ACHIVEMENT:
                    mTvTextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File sendFile = getmImageFile();
                            Intent intent = new Intent();
                            intent.putExtra(REQEUST_IMAGE_FILE, sendFile);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                    break;
            }

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
     * Image Upload
     */
    private void httpPostProfilesImage() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(ActivityGalleryCamera.this);

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        DAHttpClient client = DAHttpClient.getInstance(ActivityGalleryCamera.this);
        File sendFile = getmImageFile();
        Log.d("FILE", sendFile.getName());
        HashMap<String, String> body = new HashMap<>();
        String[] fileInfo = sendFile.getName().split("\\.");
        body.put("key", fileInfo[0]);
        body.put("type", fileInfo[1]);

        client.Post(Comm_Param.URL_API_PROFILE_INDEX_IMAGE.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex())), header, body, new IOServerCallback() {
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
                    httpPostUploadImage(sendFile, fileUrl, url);
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
            File file = null;
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                String filePath = Environment.getExternalStorageDirectory().getPath() + "/";
                String fileName;
                fileName = new Date().getTime() + ".jpeg";
                Utils.SaveBitmapToFileCache(photo, filePath, fileName);
                file = new File(filePath + fileName);
            }
            if (uri != null) {
                uri = data.getData();
                file = new File(Utils.getRealPathFromURI(ActivityGalleryCamera.this, uri));
            }

            if (mViewType == FragmentMain.REQUEST_ADD_ACHIVEMENT) {
                Intent intent = new Intent();
                intent.putExtra(REQEUST_IMAGE_FILE, file);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                httpPostProfilesImage();
            }

        } else if (requestCode == REQUEST_ADD_ACTION_POST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

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
