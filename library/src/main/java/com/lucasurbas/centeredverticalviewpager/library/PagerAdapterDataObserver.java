package com.lucasurbas.centeredverticalviewpager.library;

/**
 * Created by Lucas on 1/13/15.
 */

/**
 * Observer base class for watching changes to an {@link CenteredPagerAdapter}.
 * See {@link CenteredPagerAdapter#registerPagerAdapterDataObserver(PagerAdapterDataObserver)}.
 */
public abstract class PagerAdapterDataObserver {

    public abstract void onChanged();

    public abstract void onItemRangeChanged(int positionStart, int itemCount);

    public abstract void onItemRangeInserted(int positionStart, int itemCount);

    public abstract void onItemRangeRemoved(int positionStart, int itemCount);
}
