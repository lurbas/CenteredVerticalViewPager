package com.lucasurbas.centeredverticalviewpager.library;

import android.support.v4.view.PagerAdapter;

/**
 * Created by Lucas on 1/13/15.
 */
public abstract class CenteredPagerAdapter extends PagerAdapter {

    public int getItemPosition(Object object, int position) {
        return POSITION_UNCHANGED;
    }
}
