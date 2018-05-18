package rnf.taxiad.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import rnf.taxiad.activities.HomeActivity;

/**
 * Created by php-android02 on 2/9/16.
 */
public class ScrollViewPager extends AutoScrollViewPager {
    public ScrollViewPager(Context paramContext) {
        super(paramContext);
    }

    public ScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void scrollOnce() {
        super.scrollOnce();
        PagerAdapter adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0 || adapter.getCount() > 1)
            return;
        Context context = getContext();
        if (context instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) context;
            activity.onScroll();
        }
    }
}
