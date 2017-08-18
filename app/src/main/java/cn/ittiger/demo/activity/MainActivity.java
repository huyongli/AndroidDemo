package cn.ittiger.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ittiger.demo.behavior.BehaviorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: laohu on 2016/7/23
 * @site: http://ittiger.cn
 */
public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Demo");
//        startService(new Intent(this, RemoteService.class));
//        startService(new Intent(this, LocalService.class));
    }

    public List<String> getData() {

        List<String> list = new ArrayList<>(10);
        list.add("Retrofit");
        list.add("RecyclerView");
        list.add("RxJava");
        list.add("ViewDrawCache");
        list.add("9.Png");
        list.add("StyleTextView");
        list.add("VectorDrawable");
        list.add("Behavior");
        list.add("TextSwitcher");
        list.add("MediaProjection Capture");
        list.add("Handler");
        list.add("Window");
        list.add("FloatingActionButton");
        return list;
    }

    @Override
    public void onItemClick(int position, View itemView) {

        switch (position) {
            case 0://Retrofit
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case 1://RecyclerView
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case 2://RxJava
                startActivity(new Intent(this, RxJavaActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, ViewDrawCacheActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, Png9Activity.class));
                break;
            case 5:
                startActivity(new Intent(this, StyleTextViewActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, VectorDrawableActivity.class));
                break;
            case 7:
                startActivity(new Intent(this, BehaviorActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, TextSwitcherActivity.class));
                break;
            case 9:
                startActivity(new Intent(this, ScreenCaptureActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, HandlerActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, WindowActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, FloatingActionButtonActivity.class));
                break;
        }
    }
}
