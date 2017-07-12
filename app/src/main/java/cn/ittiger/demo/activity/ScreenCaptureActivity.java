package cn.ittiger.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.demo.R;
import cn.ittiger.demo.util.ScreenCapture;
import cn.ittiger.demo.util.UIUtil;

/**
 * @author: ylhu
 * @time: 17-7-12
 */

public class ScreenCaptureActivity extends AppCompatActivity {
    private final static int REQUEST_CODE = 1;
    private MediaProjectionManager mMediaProjectionManager;

    @BindView(R.id.image)
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_capture);
        ButterKnife.bind(this);

        mMediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @OnClick(R.id.button)
    public void onClick(View view) {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        } else {
            UIUtil.showToast(this, "版本太低，无法截屏，请使用5.0以上系统手机");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            ScreenCapture.with(this).capture(data, new ScreenCapture.CaptureListener() {
                @Override
                public void onSuccess(File file) {

                    UIUtil.showToast(ScreenCaptureActivity.this, "截屏成功");
                }

                @Override
                public void onFailure(String message) {

                    UIUtil.showToast(ScreenCaptureActivity.this, "截屏失败");
                }
            });
        }
    }
}
