package cn.ittiger.demo.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public class LocalService extends Service {
    private static final String TAG = "RemoteService";
    MyConnection mConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

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
        bindService(new Intent(this, RemoteService.class), mConnection, Context.BIND_IMPORTANT);
        Log.d(TAG, "LocalService create");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "LocalService destroy");
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "RemoteService connected to LocalService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "RemoteService disconnected to LocalService");
            startService(new Intent(LocalService.this, RemoteService.class));
        }
    }

    class MyBinder extends ServiceAIDL.Stub {

        @Override
        public String getName() throws RemoteException {

            return "LocalService";
        }
    }
}
