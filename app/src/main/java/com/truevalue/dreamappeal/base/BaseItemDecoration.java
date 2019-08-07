package com.truevalue.dreamappeal.base;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;

    public BaseItemDecoration(int div_height) {
        this.divHeight = div_height;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 마지막 뷰는 공백 추가 안함
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = divHeight;
        }
    }
}
