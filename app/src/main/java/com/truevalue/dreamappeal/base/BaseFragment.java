package com.truevalue.dreamappeal.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.view.View;

import com.truevalue.dreamappeal.R;

public class BaseFragment extends Fragment {

    public void replaceFragment(int container, Fragment fragment, boolean addToStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (addToStack) {
            ft.addToBackStack(null);
        }
        ft.add(container, fragment);
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
        FragmentManager fm = getFragmentManager();
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
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(container, fragment);
        if (addToStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public View findViewById(int resid) {
        return getActivity().findViewById(resid);
    }


    public void onViewPaged(boolean isView){

    }
}
