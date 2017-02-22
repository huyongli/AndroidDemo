package cn.ittiger.demo.util;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * Created by ylhu on 17-2-22.
 */
public class AnimatorUtil {

    // 显示view
    public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {

        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(800)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    // 隐藏view
    public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {

        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(800)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(viewPropertyAnimatorListener)
                .start();
    }
}
