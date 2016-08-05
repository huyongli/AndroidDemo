package cn.ittiger.demo;

import cn.ittiger.demo.ui.VideoPlayView;

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
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements VideoPlayView.MediaPlayerImpl, AbsListView.OnScrollListener {

    private ListView mListView;
    private Activity mActivity;
    private VideoPlayView playView;
    private MyAdapter mMyAdapter;
    private View currentItemView;
    private int currentPosition = -1;
    private int scrollDistance;// 记录切换到横屏时滑动的距离
    private List<String> path;
    private boolean isPlaying;
    private int firstVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mActivity = this;

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnScrollListener(this);
        mMyAdapter = new MyAdapter();
        mListView.setAdapter(mMyAdapter);
    }

    public void setPlayView(VideoPlayView playView) {
        this.playView = playView;
        playView.setMediaPlayerListenr(this);
        Log.i("XX", currentPosition + "");
        int[] curr = new int[2];
        currentItemView.getLocationOnScreen(curr);
        Log.i("TAG", curr[1] + "");
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("EE", "当前" + currentPosition);
        Log.i("EE", "可见的第一个" + mListView.getFirstVisiblePosition());
        if ((currentPosition < mListView.getFirstVisiblePosition() || currentPosition > mListView
                .getLastVisiblePosition()) && isPlaying && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            closeVideo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playView != null) {
            playView.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playView != null) {
            playView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playView != null) {
            playView.stop();
        }
    }

    private void closeVideo() {
        currentPosition = -1;
        isPlaying = false;
        playView.stop();
        mMyAdapter.notifyDataSetChanged();
        playView = null;
        currentItemView = null;
    }

    /**
     * 在 manifest 中设置当前 activity, 当横竖屏切换时会执行该方法, 否则会 finish 重新执行一遍生命周期
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playView != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // scrollDistance = currentItemView.getTop();

            //获取状态栏高度(如果设置沉浸式状态栏了就不需要获取)
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            currentItemView.setLayoutParams(new AbsListView.
                    LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                    getWindowManager().getDefaultDisplay().getHeight() - rect.top));
            //设置横屏后要显示的当前的 itemView
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    //一定要对添加这句话,否则无效,因为界面初始化完成后 listView 失去了焦点
                    mListView.requestFocusFromTouch();
                    mListView.setSelection(currentPosition);
                }
            });
            Log.i("XX", "横屏");
        } else if (playView != null && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏时的设置会影响返回竖屏后的效果, 这里设置高度与 xml 文件中的高度相同
            Log.i("MM", currentPosition + "竖屏");
            currentItemView.setLayoutParams(new AbsListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT, getResources()
                    .getDimensionPixelOffset(R.dimen.d_240)));
            //本来想切换到竖屏后恢复到初始位置,但是上部出现空白
//            mListView.scrollBy(0, -(scrollDistance));
            //通过该方法恢复位置,不过还是有点小问题
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.requestFocusFromTouch();
                    mListView.setSelection(firstVisiblePosition);
                }
            });
            Log.i("XX", "竖屏");
        }
    }

    /**
     * 当窗口处于横屏时,点击返回键退出横屏
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (playView != null) {
                    playView.setExpendBtn(false);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 由于视频在 listView 中,横屏后仍然后滑动
     * 此处判断当处于横屏时将滑动事件消耗掉
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onError() {
        closeVideo();
    }

    @Override
    public void onExpend() {
        firstVisiblePosition = mListView.getFirstVisiblePosition();
        //强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onShrik() {
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public class MyAdapter extends BaseAdapter {

        private List<String> mList;

        public MyAdapter() {

            mList = new ArrayList<>();
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
            mList.add("http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
        }

        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public String getItem(int position) {

            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.video_play_items, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.play_btn = (ImageView) convertView.findViewById(R.id.play_btn);
                viewHolder.show_layout = (FrameLayout) convertView.findViewById(R.id.show_layout);
                viewHolder.play_view = (VideoPlayView) convertView.findViewById(R.id.video_play_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.play_btn.setOnClickListener(new MyClick(position,
                    viewHolder.play_view, viewHolder.show_layout, convertView));

            if (currentPosition == position) {
                viewHolder.play_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.play_view.setVisibility(View.GONE);
                viewHolder.show_layout.setVisibility(View.VISIBLE);
                viewHolder.play_view.stop();
            }
            return convertView;
        }
    }

    private  class ViewHolder {
        private ImageView play_btn;//播放按钮
        private FrameLayout show_layout;
        private VideoPlayView play_view;

    }

    class MyClick implements View.OnClickListener {
        private int position;
        private VideoPlayView playView;
        private FrameLayout show_layout;
        private View convertView;

        public MyClick(int position, VideoPlayView playView, FrameLayout show_layout, View convertView) {
            this.position = position;
            this.show_layout = show_layout;
            this.playView = playView;
            this.convertView = convertView;
        }

        @Override
        public void onClick(View v) {
            isPlaying = true;
            currentPosition = position;
            show_layout.setVisibility(View.GONE);
            playView.setUrl(mMyAdapter.getItem(position));
            currentItemView = convertView;
            setPlayView(playView);
            playView.openVideo();
            mMyAdapter.notifyDataSetChanged();
        }
    }
}
