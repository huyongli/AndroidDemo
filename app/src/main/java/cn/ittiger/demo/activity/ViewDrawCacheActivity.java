package cn.ittiger.demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ittiger.demo.R;
import cn.ittiger.demo.util.Util;

public class ViewDrawCacheActivity extends AppCompatActivity {

    TextView mTextView;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_draw_cache);
        mTextView = (TextView) findViewById(R.id.textView);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void onClick(View view) {

        mTextView.setDrawingCacheEnabled(true);
//        mTextView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        mTextView.layout(0, 0, mTextView.getMeasuredWidth(), mTextView.getMeasuredHeight());
//        mTextView.buildDrawingCache();
        Bitmap bitmap = mTextView.getDrawingCache();
        mImageView.setImageBitmap(bitmap);

        saveBitmap(bitmap);
    }

    private void saveBitmap(Bitmap bitmap) {

        File dirPath = new File(Util.getSdCardPath());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(new Date());
        File image = new File(dirPath, time + ".jpg");
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(image));
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
