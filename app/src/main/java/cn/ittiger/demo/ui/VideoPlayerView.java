package cn.ittiger.demo.ui;

import cn.ittiger.demo.R;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by baina on 16-7-29.
 */
public class VideoPlayerView extends RelativeLayout implements View.OnClickListener,
        VideoPlayerControllerView.VideoControlListener, TextureView.SurfaceTextureListener {

    private TextureView mTextureView;//视频播放容器
    private VideoPlayerControllerView mVideoPlayerControllerView;//视频底部的播放控制器
    private ImageView mVideoImage;//视频预览图
    private ImageView mPlayBtn;//播放屏幕上的暂停按钮
    private ProgressBar mProgressBar;//视频加载过程中的进度条
    private VideoPlayState mVideoPlayState = VideoPlayState.PLAY;//视频播放的当前状态：播放，暂停

    private Surface mSurface = null;
    private MediaPlayer mPlayer;
    private String mVideoUrl = "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4";

    private int mSecProgress = 0;

    public VideoPlayerView(Context context) {

        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        inflate(context, R.layout.video_player_view_layout, this);
        mTextureView = (TextureView) findViewById(R.id.video_view);
        mVideoImage = (ImageView) findViewById(R.id.video_image);
        mPlayBtn = (ImageView) findViewById(R.id.play_image);
        mVideoPlayerControllerView = (VideoPlayerControllerView) findViewById(R.id.videoControllerView);
        mProgressBar = (ProgressBar) findViewById(R.id.video_loading);

        mTextureView.setOnClickListener(this);
        mTextureView.setSurfaceTextureListener(this);
        mPlayBtn.setOnClickListener(this);
        mVideoPlayerControllerView.setVideoControlListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_view:
                mVideoPlayerControllerView.showOrHide();
                break;
            case R.id.play_image:
                playOrPause();
                break;
        }
    }

    /**
     * 播放或暂停播放控制
     */
    private void playOrPause() {

        if(loadAndPlay(mVideoUrl)) {
            if(mVideoPlayState == VideoPlayState.PAUSE) {//当前处于暂停播放状态
                startPlay();
            } else {
                pausePlay();
            }
            updatePlayBtnIcon();//切换播放时更新播放按钮的图标
        }
    }

    /**
     * 显示或者隐藏播放暂停按钮
     */
    private void showOrHidePlayBtn() {

        if(mPlayBtn.getVisibility() == VISIBLE) {
            mPlayBtn.setVisibility(GONE);
        } else {
            mPlayBtn.setVisibility(VISIBLE);
            updatePlayBtnIcon();
        }
    }

    /**
     * 更新播放按钮的图标
     */
    private void updatePlayBtnIcon() {

        int resId = mVideoPlayState == VideoPlayState.PAUSE ? R.drawable.ic_play : R.drawable.ic_bar_pause;
        mPlayBtn.setImageResource(resId);
    }

    /**
     * 显示加载进度条
     */
    private void showLoadingBarIfNeed() {

        if(mProgressBar.getVisibility() == GONE) {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏加载加载进度条
     */
    private void hideLoadingBarIfNeed() {

        if(mProgressBar.getVisibility() == VISIBLE) {
            mProgressBar.setVisibility(GONE);
        }
    }

    /**
     * 加载播放指定URL的视频
     * @param videoUrl
     * @return
     */
    public boolean loadAndPlay(String videoUrl) {

        if(mPlayer != null) {
            return true;
        }
        showOrHidePlayBtn();
        showLoadingBarIfNeed();
        mPlayer = new MediaPlayer();
        playPrepare(videoUrl);
        return false;
    }

    /**
     * 播放准备
     * @param videoUrl
     */
    private void playPrepare(String videoUrl) {

        try {
            if(mPlayer != null) {
//            mPlayer.setOnCompletionListener(mOnCompletionListener);//播放完成的监听
                mPlayer.setOnPreparedListener(mOnPreparedListener);
//            mPlayer.setOnInfoListener(mInfoListener);
                mPlayer.setOnErrorListener(mErrorListener);
                mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);//缓冲监听
                mPlayer.setSurface(mSurface);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setScreenOnWhilePlaying(true);
                mPlayer.setDataSource(videoUrl);
                mPlayer.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void startPlay() {

        mPlayer.start();//开始播放
        if(mVideoImage.getVisibility() == VISIBLE) {//播放时隐藏视频图片
            mVideoImage.setVisibility(GONE);
        }
        mVideoPlayState = VideoPlayState.PLAY;//更新当前状态为：播放
        hideLoadingBarIfNeed();
    }

    /**
     * 更新播放进度
     */
    private void updatePlayProgress() {

        if(mVideoPlayState == VideoPlayState.PLAY) {
            int duration = mPlayer.getDuration();
            int playTime = mPlayer.getCurrentPosition();
            mVideoPlayerControllerView.setVideoDuration(duration);
            mVideoPlayerControllerView.setVideoPlayTime(playTime);
            mVideoPlayerControllerView.setSecondaryProgress(mSecProgress);
        }
        postDelayed(new Runnable() {

            @Override
            public void run() {

                updatePlayProgress();
            }
        }, 1000);
    }

    /**
     * 停止播放
     */
    public void pausePlay() {

        mPlayer.pause();
        mVideoPlayState = VideoPlayState.PAUSE;//更新当前状态为：暂停
    }

    /**
     * 拖动播放进度条时的响应
     *
     * @param seekTime
     */
    @Override
    public void onProgressChanged(int seekTime) {

        mPlayer.seekTo(seekTime);
    }

    /**
     * 底部播放控制器上的全屏按钮回调
     */
    @Override
    public void fullScreen() {

        Toast.makeText(getContext(), "点击全屏", Toast.LENGTH_SHORT).show();
    }

    /**
     * 底部播放控制器隐藏时的回调
     */
    @Override
    public void onControllerHide() {

        if(mPlayBtn.getVisibility() == VISIBLE) {
            mPlayBtn.setVisibility(GONE);
        }
    }

    /**
     * 底部播放控制器显示时的回调
     */
    @Override
    public void onControllerShow() {

        if(mPlayBtn.getVisibility() == GONE) {
            mPlayBtn.setVisibility(VISIBLE);
        }
        updatePlayBtnIcon();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

        mSurface = new Surface(surfaceTexture);
        playPrepare(mVideoUrl);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            //视频初始完成，即将播放，初始化完成后显示播放控制其
            mVideoPlayerControllerView.showOrHide();
            updatePlayProgress();
            startPlay();
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (getWindowToken() != null) {
                String message = "播放错误";

                Log.e("mErrorListener", message);
            }
            return true;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

            mSecProgress = percent;
        }
    };


    public enum VideoPlayState {

        PLAY, PAUSE
    }
}
