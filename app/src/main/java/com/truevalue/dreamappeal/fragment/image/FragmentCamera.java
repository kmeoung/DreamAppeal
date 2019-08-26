package com.truevalue.dreamappeal.fragment.image;

import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityGalleryCamera;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentCamera extends BaseFragment implements LifecycleOwner{

    @BindView(R.id.ibtn_camera)
    ImageButton mIbtnCamera;
    @BindView(R.id.ttv_texture)
    TextureView mTtvTexture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startCamera();
        mTtvTexture.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateTransform();
            }
        });
    }

    private void startCamera() {
        // 미리보기를 만들기 위한 환경설정 객체를 만듦
        PreviewConfig.Builder builder = new PreviewConfig.Builder();
        builder.setTargetAspectRatio(new Rational(1, 1));
        // 화면 사이즈 가져오기
        Point size = Utils.getDisplaySize(getActivity());

        int displayWidth = size.x;

        int viewWidth = displayWidth;

        int viewHeight = displayWidth;

        // View 사이즈 재설정
        Utils.setResizeView(mTtvTexture,viewWidth,viewHeight);
        builder.setTargetResolution(new Size(viewWidth, viewHeight));

        // 미리보기 객체를 만듦
        Preview preview = new Preview(builder.build());

        //사진을 찍기 위한 설정을 위해 ImageCaptureConfig를 생성합니다.
        ImageCaptureConfig.Builder imageCaptureBuilder = new ImageCaptureConfig.Builder();
        imageCaptureBuilder.setTargetAspectRatio(new Rational(1, 1));
        imageCaptureBuilder.setTargetResolution(new Size(viewWidth, viewHeight));

        // ImageCapture 객체를 이용해 버튼이 클릭되었을때 사진을 찍도록 합니다.
        ImageCapture imageCapture = new ImageCapture(imageCaptureBuilder.build());

        mIbtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getActivity().getExternalMediaDirs().toString(),"${System.currentTimeMillis()}.jpg");
                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "사진 경로 : ${file.absolutePath}";
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Photo capture failed: $message";
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.e("imageCaptureError : ",message);
                    }
                });
            }
        });
        //사진찍기 설정 끝

        // 뷰파인더가 갱신될 때 마다 레이아웃을 다시 설정
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                mTtvTexture.setSurfaceTexture(output.getSurfaceTexture());
                // SurfaceTexture를 넘겨줌으로써 카메라영상이 뷰파인더로 연결됨
                updateTransform();
            }
        });
        // 생명주기에 카메라 바인딩
        CameraX.bindToLifecycle(this,preview,imageCapture);
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();

        // 뷰파인더에 중심점을 계산합니다.
        float centerX = mTtvTexture.getWidth() / 2.0f;
        float centerY = mTtvTexture.getHeight() / 2.0f;

        // 화면 회전을 위한 회전각도 출력
        float rotationDegree;

        switch (mTtvTexture.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegree = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegree = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegree = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegree = 270;
                break;
            default:
                return;
        }

        matrix.postRotate(-rotationDegree, centerX, centerY);
        // 뷰파인더 방향에 맞게 나타냄
        mTtvTexture.setTransform(matrix);
    }
}
