package cn.ittiger.demo;

import cn.ittiger.demo.service.RemoteService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        startService(new Intent(this, RemoteService.class));
    }

    public List<String> getData() {

        List<String> list = new ArrayList<>(10);
        list.add("Retrofit");
        list.add("RecyclerView");
        list.add("RxJava");
        list.add("ViewDrawCache");
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
        }
    }
}
