package cn.ittiger.demo.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author: ylhu
 * @time: 17-8-25
 */

public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";

    public MyIntentService() {

        super("MyIntentService");
    }

    public MyIntentService(String name) {

        super(name);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int index = intent.getIntExtra("index", -1);
        Log.d(TAG, "index task: " + index);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
