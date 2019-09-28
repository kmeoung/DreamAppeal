package com.truevalue.dreamappeal.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.truevalue.dreamappeal.R;

import java.util.ArrayList;

public class BasePagerAdapter<String> extends PagerAdapter {

    private ArrayList<String> mArray;
    private Context mContext;

    public BasePagerAdapter(Context context) {
        super();
        mContext = context;
        mArray = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mArray.size();
    }

    public void add(String item) {
        this.mArray.add(item);
    }

    public String get(int i) {
        return mArray.get(i);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        String url = mArray.get(position);
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(imageView);
        container.addView(imageView, 0);
        return imageView;
    }

}
