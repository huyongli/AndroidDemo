package cn.ittiger.demo.service;

import cn.ittiger.demo.util.UIUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: laohu on 2016/10/10
 * @site: http://ittiger.cn
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "RemoteService";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, RemoteService.class));
        Log.d(TAG, "receive:AlarmReceiver");
        UIUtil.showToast(context, "AlarmReceiver");
    }
}
