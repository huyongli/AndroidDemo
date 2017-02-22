package cn.ittiger.demo.behavior;

import cn.ittiger.demo.R;
import cn.ittiger.demo.adapter.StringListAdapter;
import cn.ittiger.demo.decoration.SpacesItemDecoration;
import cn.ittiger.demo.ui.CommonRecyclerView;
import cn.ittiger.demo.util.AnimatorUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylhu on 17-2-22.
 */
public class BackTopBehaviorActivity extends AppCompatActivity {
    CommonRecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backtop_behavior);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        initRecyclerView();
    }

    private void initRecyclerView() {

        mRecyclerView = (CommonRecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i ++) {
            data.add("数据" + (i + 1));
        }

        StringListAdapter mAdapter = new StringListAdapter(this, data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private boolean isInitializeFAB = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInitializeFAB) {
            isInitializeFAB = true;
//            hideFAB();
//            mFloatingActionButton.hide();
        }
    }

    private void hideFAB() {
        mFloatingActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimatorUtil.scaleHide(mFloatingActionButton, new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }
                    @Override
                    public void onAnimationEnd(View view) {

                        view.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                });
            }
        }, 500);
    }
}
