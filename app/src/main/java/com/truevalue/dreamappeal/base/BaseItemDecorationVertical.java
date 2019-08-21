package com.truevalue.dreamappeal.base;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.utils.Utils;

public class BaseItemDecorationVertical extends RecyclerView.ItemDecoration {

    private final int divHeight;

    public BaseItemDecorationVertical(Context context, int divDp) {
        int pixel = Utils.DpToPixel(context, divDp);
        this.divHeight = pixel;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 마지막 뷰는 공백 추가 안함
//        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.top = divHeight;
//        }
        outRect.top = divHeight;
    }
}
