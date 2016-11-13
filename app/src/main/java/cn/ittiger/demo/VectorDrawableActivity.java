package cn.ittiger.demo;

import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;

public class VectorDrawableActivity extends AppCompatActivity {
    static {
        /**
         * 此方法必须必须引用appcompat-v7:23.4.0
         *
         * Button类控件使用vector必须使用selector进行包装才会起作用，不然会crash
         * 并且使用selector时必须调用下面的方法进行设置，否则也会crash
         * */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawable);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.rebot_animate);
        imageView.setImageDrawable(animatedVectorDrawableCompat);
        if (animatedVectorDrawableCompat != null) {
            animatedVectorDrawableCompat.start();
        }

        ImageView star = (ImageView) findViewById(R.id.starImageView);
        AnimatedVectorDrawableCompat starDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.five_star_animate);
        star.setImageDrawable(starDrawable);
        if (starDrawable != null) {
            starDrawable.start();
        }

        ImageView load = (ImageView) findViewById(R.id.loadingImageView);
//        AnimatedVectorDrawable loadDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.loading_animate);
        AnimatedVectorDrawableCompat loadDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.loading_animate);
        load.setImageDrawable(loadDrawable);
        if (loadDrawable != null) {
            loadDrawable.start();
        }
    }
}
