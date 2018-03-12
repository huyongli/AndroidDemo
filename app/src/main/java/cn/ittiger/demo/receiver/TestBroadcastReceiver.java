package cn.ittiger.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: ylhu
 * @time: 2018/2/23
 */

public class TestBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = "cn.ittiger.demo.test.receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION.equals(intent.getAction())) {
            Log.d("Receiver", "receive broadcast:" + intent.getStringExtra("data"));
        }
    }
}
