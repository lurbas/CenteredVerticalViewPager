package com.lucasurbas.centeredverticalviewpager.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lucasurbas.centeredverticalviewpager.library.ICenteredItem;

/**
 * Created by Lucas on 12/2/14.
 */
public class ItemView extends FrameLayout implements ICenteredItem {

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
