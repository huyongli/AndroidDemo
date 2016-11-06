package cn.ittiger.demo.ui;

import cn.ittiger.demo.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author: laohu on 2016/11/2
 * @site: http://ittiger.cn
 */
public class Png9View extends FrameLayout {

    private LinearLayout mContainer;
    public Png9View(Context context) {

        this(context, null);
    }

    public Png9View(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public Png9View(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.png_9_view, this, true);

        mContainer = (LinearLayout) findViewById(R.id.root);

        setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        mContainer.setBackgroundResource(R.drawable.bg);
//        Drawable.createFromResourceStream(context, null, inStream, null);
    }
}
