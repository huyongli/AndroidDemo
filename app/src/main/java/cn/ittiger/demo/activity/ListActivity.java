package cn.ittiger.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import java.util.List;

import cn.ittiger.demo.R;
import cn.ittiger.demo.adapter.StringListAdapter;
import cn.ittiger.demo.decoration.SpacesItemDecoration;
import cn.ittiger.demo.ui.CommonRecyclerView;

public abstract class ListActivity extends AppCompatActivity implements CommonRecyclerView.OnItemClickListener {
    protected CommonRecyclerView mRecyclerView;
    protected StringListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected LinearLayout mContainer;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_list);
        mRecyclerView = (CommonRecyclerView) findViewById(R.id.recyclerView);

        mContainer = (LinearLayout) findViewById(R.id.container);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnItemClickListener(this);

        mAdapter = new StringListAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
    }

    public abstract List<String> getData();
}
