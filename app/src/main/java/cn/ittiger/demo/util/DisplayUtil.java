package cn.ittiger.demo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DisplayUtil {
    /**
     * 将px值转换为dp值
     */
    public static int px2dp(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dp值转换为px值
     */
    public static int dp2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     */
    public static int px2sp(Context context, float pxValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取 显示信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 返回屏幕宽度Px
     *
     * @param context
     * @return int
     */
    public static int getWindowWidth(Context context) {

        int screenWidthPixels = 0;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Point outPoint = new Point();
            display.getRealSize(outPoint);
            screenWidthPixels = outPoint.x;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point outPoint = new Point();
            display.getSize(outPoint);
            screenWidthPixels = outPoint.x;
        } else {
            screenWidthPixels = display.getWidth();
        }
        return screenWidthPixels;
    }

    /**
     * 返回屏幕高度
     *
     * @param context
     * @return int
     */
    public static int getWindowHeight(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取一个视图的高度
     *
     * @param view
     * @return
     */
    public static int getViewHeight(View view) {

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(0, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHight(Activity context) {

        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 获得状态栏高度
     */
    public static int getStatusBarHeightForAtt(Context context) {

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
