package com.lucasurbas.centeredverticalviewpager.sample;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Lucas on 12/2/14.
 */
public class Utils {

    public static int dpToPixels(Context context, int dpValue) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
        return px;
    }
}
