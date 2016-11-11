package cn.ittiger.demo;

import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class VectorDrawableActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawable);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        AnimatedVectorDrawableCompat morphing = (AnimatedVectorDrawableCompat) getResources().getDrawable(R.drawable.bot_animate);
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.rebot_animate);
        imageView.setImageDrawable(animatedVectorDrawableCompat);
        if (animatedVectorDrawableCompat != null) {
            animatedVectorDrawableCompat.start();
        }

        ImageView star = (ImageView) findViewById(R.id.starImageView);
//        AnimatedVectorDrawableCompat morphing = (AnimatedVectorDrawableCompat) getResources().getDrawable(R.drawable.bot_animate);
        AnimatedVectorDrawableCompat starDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.five_star_animate);
        star.setImageDrawable(starDrawable);
        if (starDrawable != null) {
            starDrawable.start();
        }
    }
}
