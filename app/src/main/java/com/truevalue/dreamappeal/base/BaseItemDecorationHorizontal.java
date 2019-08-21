package com.truevalue.dreamappeal.base;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.utils.Utils;

public class BaseItemDecorationHorizontal extends RecyclerView.ItemDecoration {

    private final int divWidth;

    public BaseItemDecorationHorizontal(Context context, int divDp) {
        int pixel = Utils.DpToPixel(context, divDp);
        this.divWidth = pixel;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = divWidth;

        // 마지막 뷰는 오른쪽에도 공백 추가
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = divWidth;
        }
    }
}
