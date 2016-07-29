package cn.ittiger.demo;

import cn.ittiger.demo.ui.TextureVideoView;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private ListView mListView;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mActivity = this;

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new MyAdapter());
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
            final ViewHolder viewHolder;
            if(convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.activity_video_list_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mVideoView.setTag(getItem(position));
            viewHolder.btn.setText("play video:" + (position + 1));
            viewHolder.btn.setTag(getItem(position));
            final TextureListener listener = new TextureListener();
            viewHolder.mVideoView.setSurfaceTextureListener(listener);
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    listener.play((String) viewHolder.btn.getTag());
                }
            });

            return convertView;
        }
    }

    public class ViewHolder {
        public Button btn;
        public TextureVideoView mVideoView;
        public ProgressBar mProgressBar;

        public ViewHolder(View view) {
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            btn = (Button) view.findViewById(R.id.btn_play);
            mVideoView = (TextureVideoView) view.findViewById(R.id.videoview);
        }
    }

    public class TextureListener implements TextureView.SurfaceTextureListener {
        private MediaPlayer mMediaPlayer;
        private Surface surface;


        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            System.out.println("onSurfaceTextureAvailable");
            surface = new Surface(surfaceTexture);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            System.out.println("onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

            System.out.println("onSurfaceTextureDestroyed");
            surface = null;
            if(mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }

        public void play(final String url) {

            new Thread(){
                @Override
                public void run() {

                    try {
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setDataSource(url);
                        mMediaPlayer.setSurface(surface);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mMediaPlayer.start();

                                Log.i("Q_M:", mMediaPlayer.getVideoWidth() + "-------" + mMediaPlayer.getVideoHeight());
                            }
                        });
                        mMediaPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
