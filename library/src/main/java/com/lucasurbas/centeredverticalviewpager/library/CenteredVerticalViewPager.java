package com.lucasurbas.centeredverticalviewpager.library;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

/**
 * Created by Lucas on 11/28/14.
 */
public class CenteredVerticalViewPager extends VerticalViewPager {

    private static final String TAG = CenteredVerticalViewPager.class.getSimpleName();

    private int mPadding = 200;

    public CenteredVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare();
    }

    private void prepare() {
        setOffscreenPageLimit(2);
//        setInternalPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                //Log.v(TAG, "onPageScrolled: pos: "  + position + ", posOffset: " + positionOffset);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        setPageTransformer(false, new ItemTransformer());
    }

    protected int getClientHeight() {
        return getMeasuredHeight() - mPadding * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, our internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view.  We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        final int measuredHeight = getMeasuredHeight();
        final int maxGutterSize = measuredHeight / 10;
        mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int childHeightSize = measuredHeight - mPadding * 2;

        /*
         * Make sure all children have been properly measured. Decor views first.
         * Right now we cheat and make this less complicated by assuming decor
         * views won't intersect. We will pin to edges based on gravity.
         */
        int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    final int hgrav = lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int vgrav = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;
                    int widthMode = MeasureSpec.AT_MOST;
                    int heightMode = MeasureSpec.AT_MOST;
                    boolean consumeVertical = vgrav == Gravity.TOP || vgrav == Gravity.BOTTOM;
                    boolean consumeHorizontal = hgrav == Gravity.LEFT || hgrav == Gravity.RIGHT;

                    if (consumeVertical) {
                        heightMode = MeasureSpec.EXACTLY;
                    } else if (consumeHorizontal) {
                        widthMode = MeasureSpec.EXACTLY;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != LayoutParams.WRAP_CONTENT) {
                        widthMode = MeasureSpec.EXACTLY;
                        if (lp.width != LayoutParams.MATCH_PARENT) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != LayoutParams.WRAP_CONTENT) {
                        heightMode = MeasureSpec.EXACTLY;
                        if (lp.height != LayoutParams.MATCH_PARENT) {
                            heightSize = lp.height;
                        }
                    }
                    final int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    final int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);

                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        // Make sure we have created all fragments that we need to have shown.
        mInLayout = true;
        populate();
        mInLayout = false;

        // Page views next.
        size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                if (DEBUG) Log.v(TAG, "Measuring #" + i + " " + child
                        + ": " + mChildWidthMeasureSpec);

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    final int heightSpec = MeasureSpec.makeMeasureSpec(
                            (int) (childHeightSize * lp.heightFactor), MeasureSpec.AT_MOST);
                    child.measure(mChildWidthMeasureSpec, heightSpec);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = mPadding;
        int paddingRight = getPaddingRight();
        int paddingBottom = mPadding;
        final int scrollY = getScrollY();

        int decorCount = 0;

        // First pass - decor views. We need to do this in two passes so that
        // we have the proper offsets for non-decor views later.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft = 0;
                int childTop = 0;
                if (lp.isDecor) {
                    final int hgrav = lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int vgrav = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;
                    switch (hgrav) {
                        default:
                            childLeft = paddingLeft;
                            break;
                        case Gravity.LEFT:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case Gravity.CENTER_HORIZONTAL:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2,
                                    paddingLeft);
                            break;
                        case Gravity.RIGHT:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                    }
                    switch (vgrav) {
                        default:
                            childTop = paddingTop;
                            break;
                        case Gravity.TOP:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case Gravity.CENTER_VERTICAL:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2,
                                    paddingTop);
                            break;
                        case Gravity.BOTTOM:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                    }

                    childTop += scrollY;
                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                    decorCount++;
                }
            }
        }

        final int childHeight = height - paddingTop - paddingBottom;
        // Page views. Do this once we have the right padding offsets from above.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ItemInfo ii;
                if (!lp.isDecor && (ii = infoForChild(child)) != null) {
                    int toff = (int) (childHeight * ii.offset);
                    int childLeft = paddingLeft;
                    int childTop = paddingTop + toff;
                    if (lp.needsMeasure) {
                        // This was added during layout and needs measurement.
                        // Do it now that we know what we're working with.
                        lp.needsMeasure = false;
                        final int widthSpec = MeasureSpec.makeMeasureSpec(
                                (int) (width - paddingLeft - paddingRight),
                                MeasureSpec.EXACTLY);
                        final int heightSpec = MeasureSpec.makeMeasureSpec(
                                (int) (childHeight * lp.heightFactor),
                                MeasureSpec.EXACTLY);
                        child.measure(widthSpec, heightSpec);
                    }
                    if (DEBUG) Log.v(TAG, "Positioning #" + i + " " + child + " f=" + ii.object
                            + ":" + childLeft + "," + childTop + " " + child.getMeasuredWidth()
                            + "x" + child.getMeasuredHeight());

                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                }
            }
        }
        mLeftPageBounds = paddingLeft;
        mRightPageBounds = width - paddingRight;
        mDecorChildCount = decorCount;

        if (mFirstLayout) {
            scrollToItem(mCurItem, false, 0, false);
        }
        mFirstLayout = false;
    }

    @Override
    protected void drawMargins(Canvas canvas){

        // Draw the margin drawable between pages if needed.
        if (mPageMargin > 0 && mMarginDrawable != null && mItems.size() > 0 && mAdapter != null) {
            final int scrollY = getScrollY();
            final int height = getHeight() - mPadding * 2;

            final float marginOffset = (float) mPageMargin / height;
            final float paddingOffset = (float) mPadding / height;
            int itemIndex = 0;
            ItemInfo ii = mItems.get(0);
            float offset = ii.offset;
            final int itemCount = mItems.size();
            final int firstPos = ii.position;
            final int lastPos = mItems.get(itemCount - 1).position;
            for (int pos = firstPos; pos < lastPos; pos++) {
                while (pos > ii.position && itemIndex < itemCount) {
                    ii = mItems.get(++itemIndex);
                }

                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.heightFactor + paddingOffset) * height;
                    offset = ii.offset + ii.heightFactor + marginOffset;
                } else {
                    float heightFactor = mAdapter.getPageWidth(pos);
                    drawAt = (offset + heightFactor + paddingOffset) * height;
                    offset += heightFactor + marginOffset;
                }

                if (drawAt + mPageMargin > scrollY) {
                    mMarginDrawable.setBounds(mLeftPageBounds, (int) drawAt,
                            mRightPageBounds, (int) (drawAt + mPageMargin + 0.5f));
                    mMarginDrawable.draw(canvas);
                }

                if (drawAt > scrollY + height) {
                    break; // No more visible, no sense in continuing
                }
            }
        }
    }

    @Override
    public void drawOverscroll(Canvas canvas) {
        boolean needsInvalidate = false;

        final int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode == ViewCompat.OVER_SCROLL_ALWAYS ||
                (overScrollMode == ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS &&
                        mAdapter != null && mAdapter.getCount() > 1)) {
            if (!mTopEdge.isFinished()) {

                final int restoreCount = canvas.save();
                final int height = getClientHeight();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();

                canvas.translate(getPaddingLeft(), mFirstOffset * height);
                mTopEdge.setSize(width, height);
                needsInvalidate |= mTopEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!mBottomEdge.isFinished()) {

                final int restoreCount = canvas.save();
                final int height = getClientHeight();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();

                canvas.rotate(180);
                canvas.translate(-width - getPaddingLeft(), -((mLastOffset + 1) * height) - 2 * mPadding);
                mBottomEdge.setSize(width, height);
                needsInvalidate |= mBottomEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            mTopEdge.finish();
            mBottomEdge.finish();
        }

        if (needsInvalidate) {
            // Keep animating
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        // Offset any decor views if needed - keep them on-screen at all times.
        if (mDecorChildCount > 0) {
            final int scrollY = getScrollY();
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            final int height = getHeight();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) continue;

                final int vgrav = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;
                int childTop = 0;
                switch (vgrav) {
                    default:
                        childTop = paddingTop;
                        break;
                    case Gravity.TOP:
                        childTop = paddingTop;
                        paddingTop += child.getHeight();
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = Math.max((height - child.getMeasuredHeight()) / 2,
                                paddingTop);
                        break;
                    case Gravity.BOTTOM:
                        childTop = height - paddingBottom - child.getMeasuredHeight();
                        paddingBottom += child.getMeasuredHeight();
                        break;
                }
                childTop += scrollY;

                final int childOffset = childTop - child.getTop();
                if (childOffset != 0) {
                    child.offsetTopAndBottom(childOffset);
                }
            }
        }

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if (mPageTransformer != null) {
            final int scrollY = getScrollY();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.isDecor) continue;

                final float transformPos = (float) (child.getTop() - mPadding - scrollY) / (getClientHeight());
                Log.v(TAG, "item: " + i + ", transformPos: "  + transformPos);
                mPageTransformer.transformPage(getClientHeight(), child, transformPos);
            }
        }

        mCalledSuper = true;
    }
}
