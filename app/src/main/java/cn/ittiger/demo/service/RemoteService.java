package cn.ittiger.demo.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author: laohu on 2016/10/10
 * @site: http://ittiger.cn
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    private final static int GRAY_SERVICE_ID = 1001;
    MyConnection mConnection;

    @Override
    public void onCreate() {

        super.onCreate();
        if(mConnection == null) {
            mConnection = new MyConnection();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

//        runServiceForeground();
//        scheduleAlarm();

        bindService(new Intent(this, LocalService.class), mConnection, Context.BIND_IMPORTANT);
        Log.d(TAG, "RemoteService create");
        return START_STICKY;
    }

    private void runServiceForeground() {

        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, RemoteInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
    }

    private void scheduleAlarm() {
        /* Request the AlarmManager object */
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Create the PendingIntent that will launch the BroadcastReceiver */
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0);

        /* Schedule Alarm with and authorize to WakeUp the device during sleep */
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20 * 1000, pending);
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class RemoteInnerService extends Service {

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

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "RemoteService destroy");
//        sendBroadcast(new Intent("cn.ittiger.remoteservice"));
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "LocalService connected to RemoteService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "LocalService disconnected to RemoteService");
            startService(new Intent(RemoteService.this, LocalService.class));
        }
    }

    class MyBinder extends ServiceAIDL.Stub {

        @Override
        public String getName() throws RemoteException {

            return "RemoteService";
        }
    }
}
