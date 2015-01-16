package com.lucasurbas.centeredverticalviewpager.library;

import android.view.View;

/**
 * Created by Lucas on 12/2/14.
 */
public class CenterPageTransformer implements VerticalViewPager.PageTransformer {

    private static final String TAG = CenterPageTransformer.class.getSimpleName();
    private final float minAlpha = VerticalViewPager.MIN_ALPHA;


    public void transformPage(int containerHeight, View view, float position) {
        int viewHeight = view.getHeight();


        //Log.v(TAG, "position: " + position);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the top.
            float margin = (containerHeight - viewHeight);
            view.setTranslationY(margin);
            view.setAlpha(minAlpha);

        } else if (position <= 0) { // [-1,1]

            float factor = 1 - position;
            float margin = (containerHeight - viewHeight) * factor / 2;
            view.setTranslationY(margin);

            // Fade the page relative to its size.
            view.setAlpha(minAlpha + ((1 - Math.abs(position)) * (1 - minAlpha)));

        } else if (position <= 1) {

            float factor = 1 - position;
            float margin = (containerHeight - viewHeight) * factor / 2;
            view.setTranslationY(margin);

            // Fade the page relative to its size.
            view.setAlpha(minAlpha + (factor * (1 - minAlpha)));
        } else { // (1,+Infinity]
            // This page is way off-screen to the bottom.

            view.setTranslationY(0);
            view.setAlpha(minAlpha);
        }
    }
}
