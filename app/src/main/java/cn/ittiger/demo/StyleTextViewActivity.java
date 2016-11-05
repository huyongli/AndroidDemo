package cn.ittiger.demo;

import cn.ittiger.demo.util.UIUtil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class StyleTextViewActivity extends AppCompatActivity {
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_text_view);
        mTextView = (TextView) findViewById(R.id.textView);
        setTextView();
    }

    void setTextView() {

        SpannableStringBuilder builder = new SpannableStringBuilder("普通文本");

        int start = builder.length();
        builder.append("文本高亮");
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("URL连接");
        end = builder.length();
        builder.setSpan(new URLSpan("tel:4155551212") {
            @Override
            public void onClick(View widget) {

                UIUtil.showToast(StyleTextViewActivity.this, "URLSpan");
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("背景高亮");
        end = builder.length();
        builder.setSpan(new BackgroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("下划线文本");
        end = builder.length();
        builder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        builder.append("\n");
        start = builder.length();
        builder.append("删除线文本");
        end = builder.length();
        builder.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("图片替换");
        end = builder.length();
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, 50, 50);
        builder.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("部分粗体");
        end = builder.length();
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {//将任意文本设置为可点击
            @Override
            public void onClick(View widget) {
                UIUtil.showToast(StyleTextViewActivity.this, "ClickableSpan");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //默认实现会出现下划线效果
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        start = builder.length();
        builder.append("组合使用");
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mTextView.setText(builder);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
