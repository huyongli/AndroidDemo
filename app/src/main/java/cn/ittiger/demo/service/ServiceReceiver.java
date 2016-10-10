package cn.ittiger.demo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: laohu on 2016/10/10
 * @site: http://ittiger.cn
 */
public class ServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "RemoteService";
    private static final String ACTION_REMOTE_SERVICE = "cn.ittiger.remoteservice";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(ACTION_REMOTE_SERVICE.equals(intent.getAction())) {
            context.startService(new Intent(context, RemoteService.class));
            Log.d(TAG, "receive:cn.ittiger.remoteservice");
        } else if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, RemoteService.class));
            Log.d(TAG, "receive:ACTION_BOOT_COMPLETED");
        } else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            context.startService(new Intent(context, RemoteService.class));
            Log.d(TAG, "receive:ACTION_USER_PRESENT");
        } else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            context.startService(new Intent(context, RemoteService.class));
            Log.d(TAG, "receive:ACTION_SCREEN_OFF");
        } else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            context.startService(new Intent(context, RemoteService.class));
            Log.d(TAG, "receive:ACTION_SCREEN_ON");
        }
    }
}
