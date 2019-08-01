package com.truevalue.dreamappeal.base;

import com.truevalue.dreamappeal.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {


    public void replaceFragment(int container, Fragment fragment, boolean addToStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(container, fragment);
        if(addToStack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    /**
     * with animation
     * @param container
     * @param fragment
     * @param addToStack
     */
    public void replaceFragmentLeft(int container, Fragment fragment, boolean addToStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(container, fragment);
        if(addToStack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    /**
     * with animation
     * @param container
     * @param fragment
     * @param addToStack
     */

    public void replaceFragmentRight(int container, Fragment fragment, boolean addToStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(container, fragment);
        if(addToStack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
