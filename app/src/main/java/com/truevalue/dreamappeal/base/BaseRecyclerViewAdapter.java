package com.truevalue.dreamappeal.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private IORecyclerViewListener mListener;

    private ArrayList<T> mList = new ArrayList<>();

    public LayoutInflater mInflater;
    private Context context;

    public BaseRecyclerViewAdapter(Context context, IORecyclerViewListener listener) {
        super();
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mListener != null)
            return mListener.onCreateViewHolder(parent, viewType);
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mListener != null)
            mListener.onBindViewHolder((BaseViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        if (mListener != null)
            return mListener.getItemCount();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mListener.getItemViewType(position);
    }

    public void add(T t) {
        boolean ok = mList.add(t);
        if(ok) notifyDataSetChanged();
    }

    public T get(int i) {
        return mList.get(i);
    }

    public void remove(int i) {
        mList.remove(i);
        notifyDataSetChanged();
    }

    public ArrayList<T> getAll() {
        return mList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public T set(int index, T object){
        T model = mList.set(index,object);
        notifyDataSetChanged();
        return model;
    }

    public int size() {
        return mList.size();
    }
}
