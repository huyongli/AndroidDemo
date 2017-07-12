package cn.ittiger.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.demo.R;

/**
 * @author: ylhu
 * @time: 17-7-12
 */

public class TextSwitcherActivity extends AppCompatActivity {
    @BindView(R.id.switcher)
    TextSwitcher mTextSwitcher;
    @BindView(R.id.viewSwitcher)
    ViewSwitcher mViewSwitcher;

    int mCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_switcher);
        ButterKnife.bind(this);

        //设置显示Text文本到View创建工厂
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView t = new TextView(TextSwitcherActivity.this);
                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                t.setTextAppearance(TextSwitcherActivity.this, android.R.style.TextAppearance_Large);
                return t;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mTextSwitcher.setInAnimation(in);//设置文本出现动画
        mTextSwitcher.setOutAnimation(out);//设置文本消失动画
        mTextSwitcher.setCurrentText(String.valueOf(mCount));//设置初始值，初始值不显示动画

        Animation slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        mViewSwitcher.setInAnimation(slide_in_left);//设置文本出现动画
        mViewSwitcher.setOutAnimation(slide_out_right);//设置文本消失动画
    }

    @OnClick(R.id.button)
    public void onTextButtonClick(View view) {
        mCount++;
        mTextSwitcher.setText(String.valueOf(mCount));//更新文本显示值，会出现动画
    }

    @OnClick(R.id.button2)
    public void onViewuttonClick(View view) {

        mViewSwitcher.showNext();
    }
}
