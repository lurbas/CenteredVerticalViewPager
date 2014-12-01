package com.lucasurbas.centeredverticalviewpager.library;

import android.support.v4.view.PagerAdapter;

/**
 * Created by Lucas on 11/28/14.
 */
public abstract class CenteredPagerAdapter extends PagerAdapter {

    @Override
    public float getPageWidth(int position) {
        return (0.7f);
    }
}
