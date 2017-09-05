package cn.ittiger.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ittiger.demo.R;
import cn.ittiger.demo.service.MyIntentService;

/**
 * @author: ylhu
 * @time: 17-8-25
 */

public class IntentServiceActivity extends AppCompatActivity {
    int mCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        startService(new Intent(this, MyIntentService.class));
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("index", mCount++);
        startService(intent);
    }

    public void onStop(View view) {

        stopService(new Intent(this, MyIntentService.class));
    }
}
