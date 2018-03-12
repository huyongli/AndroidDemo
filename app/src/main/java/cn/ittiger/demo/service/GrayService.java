package cn.ittiger.demo.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import cn.ittiger.demo.receiver.TestBroadcastReceiver;

/**
 * @author: ylhu
 * @time: 2018/2/23
 */

public class GrayService extends Service {

    private final static int GRAY_SERVICE_ID = 1001;
    TestBroadcastReceiver receiver;

    @Override
    public void onCreate() {

        super.onCreate();
        receiver = new TestBroadcastReceiver();
        IntentFilter filter = new IntentFilter(TestBroadcastReceiver.ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
            sendBroadcastReceiver();
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {

            return null;
        }
    }

    TimerTask mTask = new TimerTask() {
        @Override
        public void run() {

            /*if(mCount == 30) {
                mTimer.cancel();
                return;
            }
            mCount ++;
            Intent intent = new Intent();
            intent.setAction(TestBroadcastReceiver.ACTION);
            intent.putExtra("data","from GrayService");
            sendBroadcast(intent);*/
            stopSelf();
        }
    };

    int mCount = 0;
    Timer mTimer = new Timer("timer");

    private void sendBroadcastReceiver() {
//        mTimer.schedule(mTask, 10000, 10000);
        mTimer.schedule(mTask, 10000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mTimer.cancel();
        unregisterReceiver(receiver);
        Log.d("Receiver", "destroy GrayService");
    }
}
