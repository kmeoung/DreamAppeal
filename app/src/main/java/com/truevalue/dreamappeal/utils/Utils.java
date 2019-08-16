package com.truevalue.dreamappeal.utils;

import android.content.Context;
import android.util.TypedValue;

public class Utils {

//    DpToPixel 코드

     public static int DpToPixel(Context context, float DP) {
     float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources()
     .getDisplayMetrics());
     return (int) px;
     }
}
