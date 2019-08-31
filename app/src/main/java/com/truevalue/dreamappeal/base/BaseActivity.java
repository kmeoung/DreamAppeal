package com.truevalue.dreamappeal.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.truevalue.dreamappeal.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    /***
     * 4.4 이상 킷캣부터 지원
     * 상태바 투명화
     */
    public void updateStatusbarTranslate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
        }
    }



    /***
     * 4.4 이상 킷캣부터 지원
     * 상태바 투명화 & 투명 배경 추가
     */
    public void updateStatusbarTranslate(View titleBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusHeight = getResources().getDimensionPixelSize(id);
            Log.d("status Height",statusHeight + "");
//            params.height = statusHeight;
            titleBar.setPadding(0,statusHeight,0,0);
            Log.d("padding top ",titleBar.getPaddingTop() + "");
        }
    }

    /***
     * 4.4 이상 킷캣부터 지원
     * 상태바 투명 + 색 지정
     */
    public void updateStatusbarTranslateColor(View vStatusBar, int colorValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = vStatusBar.getLayoutParams();

            int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusHeight = getResources().getDimensionPixelSize(id);

            params.height = statusHeight;
            vStatusBar.setLayoutParams(params);
            vStatusBar.setBackgroundColor(colorValue);

            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
        }
    }

    /***
     * 4.4 이상 킷캣부터 지원
     * 네비게이션 바 투명화
     */
    public void updateNavigationBarTranslate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            );
        }
    }

    /***
     * @param color 4.4 이상 킷캣부터 지원
     *              상태바가 color 값 지정
     */
    public void setStatusbarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    /***
     * @param color 4.4 이상 킷캣부터 지원
     *              네비게이션바 color 값 지정
     */
    public void setNavigationbarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    public void replaceFragment(int container, Fragment fragment, boolean addToStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(container, fragment);
        if (addToStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    /**
     * with animation
     *
     * @param container
     * @param fragment
     * @param addToStack
     */
    public void replaceFragmentLeft(int container, Fragment fragment, boolean addToStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(container, fragment);
        if (addToStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    /**
     * with animation
     *
     * @param container
     * @param fragment
     * @param addToStack
     */

    public void replaceFragmentRight(int container, Fragment fragment, boolean addToStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(container, fragment);
        if (addToStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
