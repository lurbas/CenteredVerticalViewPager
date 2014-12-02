package com.lucasurbas.centeredverticalviewpager.library;

import android.view.View;

/**
 * Created by Lucas on 12/2/14.
 */
public class CenterPageTransformer implements VerticalViewPager.PageTransformer {

    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(int containerHeight, View view, float position) {
        int viewHeight = view.getHeight();


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the top.
            float margin = (containerHeight - viewHeight);
            view.setTranslationY(margin);
            view.setAlpha(MIN_ALPHA);

        } else if (position <= 1) { // [-1,1]

            float tempFactor = 1 - Math.abs(position);

            if (position < 0) {
                float factor = 1 - position;
                float margin = (containerHeight - viewHeight) * factor / 2;
                view.setTranslationY(margin);
            } else {
                float factor = tempFactor;
                float margin = (containerHeight - viewHeight) * factor / 2;
                view.setTranslationY(margin);
            }


            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (tempFactor  * (1 - MIN_ALPHA)));

        } else { // (1,+Infinity]
            // This page is way off-screen to the bottom.

            view.setTranslationY(0);
            view.setAlpha(MIN_ALPHA);
        }
    }
}
