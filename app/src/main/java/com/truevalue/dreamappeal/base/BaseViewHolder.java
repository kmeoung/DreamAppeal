package com.truevalue.dreamappeal.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseViewHolder extends RecyclerView.ViewHolder {

    public static BaseViewHolder newInstance(int layoutid, ViewGroup viewGroup, boolean attachToRoot) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutid,viewGroup,attachToRoot);
        BaseViewHolder fragment = new BaseViewHolder(view);
        return fragment;
    }

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public <T extends View> T getItemView(int resid){
        return itemView.findViewById(resid);
    }

}
