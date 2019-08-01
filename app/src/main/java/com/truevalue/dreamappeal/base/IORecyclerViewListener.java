package com.truevalue.dreamappeal.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

public interface IORecyclerViewListener {

    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull BaseViewHolder h, int i);

    int getItemCount();

    int getItemViewType(int i);
}
