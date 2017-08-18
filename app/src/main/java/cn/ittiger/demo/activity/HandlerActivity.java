package cn.ittiger.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.ittiger.demo.R;
import cn.ittiger.demo.util.UIUtil;

/**
 * @author: ylhu
 * @time: 17-7-28
 */

public class HandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        init();
    }

    private void init() {

        Log.d("HandlerActivity", "---Main---" + Thread.currentThread().toString());

        new Thread() {
            @Override
            public void run() {

                Looper.prepare();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                        super.handleMessage(msg);
                        UIUtil.showToast(HandlerActivity.this, Thread.currentThread().toString());
                        Log.d("HandlerActivity", "---1---" + Thread.currentThread().toString());
                    }
                };
                handler.sendEmptyMessage(1);
                Looper.loop();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {

                Handler handler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message msg) {

                        super.handleMessage(msg);
                        UIUtil.showToast(HandlerActivity.this, Thread.currentThread().toString());
                        Log.d("HandlerActivity", "---2---" + Thread.currentThread().toString());
                    }
                };
                handler.sendEmptyMessage(1);
            }
        }.start();
    }
}
