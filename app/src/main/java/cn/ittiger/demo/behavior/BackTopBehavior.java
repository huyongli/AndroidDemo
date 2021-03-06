package cn.ittiger.demo.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ylhu on 17-2-22.
 */
public class BackTopBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = "BackTopBehavior";

    public BackTopBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;//垂直方向滑动
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        if (dyConsumed > 0 && dyUnconsumed == 0) {
            Log.d(TAG, "上滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            Log.d(TAG, "到边界了还在上滑。。。");
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            Log.d(TAG, "下滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            Log.d(TAG, "到边界了，还在下滑。。。");
        }

        if((dyConsumed > 0 && dyUnconsumed == 0) || (dyConsumed == 0 && dyUnconsumed > 0) && child.getVisibility() != View.VISIBLE) {
            child.show();
        } else if((dyConsumed < 0 && dyUnconsumed == 0) || (dyConsumed == 0 && dyUnconsumed < 0) && child.getVisibility() != View.GONE) {
            child.hide();
        }
    }
}
