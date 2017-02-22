package cn.ittiger.demo.behavior;

import cn.ittiger.demo.ListActivity;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylhu on 17-2-22.
 */
public class BehaviorActivity extends ListActivity {

    @Override
    public List<String> getData() {

        List<String> list = new ArrayList<>(10);
        list.add("滑动显示返回顶部按钮");
        return list;
    }

    @Override
    public void onItemClick(int position, View itemView) {

        switch (position) {
            case 0:
                startActivity(new Intent(this, BackTopBehaviorActivity.class));
                break;
        }
    }
}
