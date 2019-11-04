package com.immomo.mls.fun.other;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by XiongFangyu on 2018/7/10.
 */
public class AllDividerItemDecoration extends RecyclerView.ItemDecoration {

    public int mSpacingWidth;
    public int mSpacingHeight;


    public AllDividerItemDecoration(int spacing) {
        this(spacing, spacing);
    }

    public AllDividerItemDecoration(int horizontalSpacing, int verticalSpacing) {
        this.mSpacingWidth = horizontalSpacing;
        this.mSpacingHeight = verticalSpacing;
    }

    /**
     * get total Span Count
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * get span index of given view in row
     *
     * @param view
     * @return
     */
    private int getSpanIndex(View view) {
        if (view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
            return ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            return ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        }
        return 0;
    }


    /**
     * get span size of given view in row
     *
     * @param view
     * @return
     */
    private int getSpanSize(View view) {
        if (view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
            return ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanSize();
        } else if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            return 1;
        }
        return 0;
    }

    /**
     * is given view align left of given row
     *
     * @param view
     * @return
     */
    private boolean isLeft(View view, int totalSpanCount) {
        return getSpanIndex(view) == 0 && getSpanSize(view) < totalSpanCount;
    }

    /**
     * is given view align right of given row
     *
     * @param view
     * @param totalSpanCount
     * @return
     */
    private boolean isRight(View view, int totalSpanCount) {
        return getSpanIndex(view) > 0 && (getSpanIndex(view) + getSpanSize(view) == totalSpanCount);
    }

    /**
     * is given view is full span
     *
     * @param view
     * @param totalSpanCount
     * @return
     */
    private boolean isFull(View view, int totalSpanCount) {
        return getSpanSize(view) == totalSpanCount;
    }

    /**
     * is position align top of recycler view
     * 从当前位置找起，只要找到一个跟自己的spanIndex一样的就说明不是第一行，或者已经查找过的spancount >= spanCount说明不是第一行
     *
     * @param parent
     * @param position
     * @param totalSpanCount
     * @return
     */
    private boolean isTop(RecyclerView parent, int position, int totalSpanCount) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup sizeLookup = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup();
            if (sizeLookup != null) {
                int currentSpanIndex = sizeLookup.getSpanIndex(position, totalSpanCount);
                int lookupedSpanCount = 0;
                for (int i = position - 1; i >= 0; i--) {
                    lookupedSpanCount = lookupedSpanCount + sizeLookup.getSpanSize(i);
                    if (lookupedSpanCount >= totalSpanCount) {
                        return false;
                    }
                    if (sizeLookup.getSpanIndex(i, totalSpanCount) == currentSpanIndex) {
                        return false;
                    }
                }
                return true;
            }
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            return position < totalSpanCount;//staggered 的每一列一个span
        }
        return false;
    }

    /**
     * is view align bottom of recycler view
     * 从当前位置向后找，如果找到一个spanIndex = 0的，说明不是最后一行
     *
     * @param parent
     * @param position
     * @param totalSpanCount
     * @return
     */
    private boolean isBottom(RecyclerView parent, int position, int totalSpanCount) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup sizeLookup = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup();
            if (sizeLookup != null && parent.getAdapter() != null) {
                int totalItemCount = parent.getAdapter().getItemCount();
                for (int i = position + 1; i < totalItemCount; i--) {
                    if (sizeLookup.getSpanIndex(i, totalSpanCount) == 0) {
                        return false;
                    }
                }
                return true;
            }
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            int totalItemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
            return position + (totalSpanCount - position % totalSpanCount) >= totalItemCount;//如果下一行的第一个不存在则说明是最后一行
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int totalSpanCount = getSpanCount(parent);
        final int itemPosition = parent.getChildPosition(view);

        final boolean isLeft = isLeft(view, totalSpanCount);
        final boolean isRight = isRight(view, totalSpanCount);
        final boolean isFull = isFull(view, totalSpanCount);
        final boolean isCenter = !isLeft && !isRight && !isFull;
        final boolean isTop = isTop(parent, itemPosition, totalSpanCount);
        final boolean isBottom = isBottom(parent, itemPosition, totalSpanCount);

//        LogUtil.d(itemPosition, "total:"+totalSpanCount, "index:"+getSpanIndex(view), "Size:"+getSpanSize(view), isLeft, isTop, isRight, isBottom, isFull, isCenter);

        final int hw = mSpacingWidth >> 1;
        final int hh = mSpacingHeight >> 1;
        final int left = (isRight || isCenter) ? hw : mSpacingWidth;
        final int right = (isLeft || isCenter) ? hw : mSpacingWidth;
        final int top = !isTop ? hh : mSpacingHeight;
        final int bottom = !isBottom ? hh : mSpacingHeight;

        outRect.set(left, top, right, bottom);
    }
}