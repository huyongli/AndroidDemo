package cn.ittiger.demo;

import cn.ittiger.demo.adapter.VideoAdapter;
import cn.ittiger.demo.ui.VideoPlayView;
import cn.ittiger.demo.ui.video.VideoPlayerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private ListView mListView;
    private Activity mActivity;
    private VideoAdapter mMyAdapter;
    private VideoPlayerView mVideoPlayerView;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        EventBus.getDefault().register(this);
        mActivity = this;

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(mPosition < firstVisibleItem || mPosition >= firstVisibleItem + visibleItemCount) {
                    if(mVideoPlayerView != null) {
                        mVideoPlayerView.onDestroy();
                    }
                    mVideoPlayerView = null;
                    mPosition = -1;
                }
            }
        });
        mMyAdapter = new VideoAdapter(mActivity);
        mListView.setAdapter(mMyAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("VideoPlayerView", "OnItemClick");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoScrollEventReceive(VideoPlayerView.VideoScrollEvent event) {

        mVideoPlayerView = event.getVideoPlayerView();
        mPosition = event.getPosition();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
