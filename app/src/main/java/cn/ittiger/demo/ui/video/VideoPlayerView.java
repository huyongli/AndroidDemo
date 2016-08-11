package cn.ittiger.demo.ui.video;

import cn.ittiger.demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

/**
 * Created by baina on 16-7-29.
 */
public class VideoPlayerView extends RelativeLayout
        implements View.OnClickListener, TextureView.SurfaceTextureListener {

    private RelativeLayout mVideoContainer;
    private TextureView mTextureView;//视频播放容器
    private VideoPlayerControllerView mVideoPlayerControllerView;//视频底部的播放控制器
    private ImageView mVideoImage;//视频预览图
    private ImageView mPlayBtn;//播放屏幕上的暂停按钮
    private ProgressBar mProgressBar;//视频加载过程中的进度条
    private VideoPlayState mVideoPlayState = VideoPlayState.NONE;//视频播放的当前状态：播放，暂停

    private Surface mSurface = null;
    private MediaPlayer mPlayer;
    private String mVideoUrl;
    private int mId = -1;

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

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private void initView(Context context) {

        inflate(context, R.layout.video_player_view_layout, this);
        mVideoContainer = (RelativeLayout) findViewById(R.id.video_player_view_container);
        mVideoImage = (ImageView) findViewById(R.id.video_image);
        mPlayBtn = (ImageView) findViewById(R.id.play_image);
        mVideoPlayerControllerView = (VideoPlayerControllerView) findViewById(R.id.videoControllerView);
        mProgressBar = (ProgressBar) findViewById(R.id.video_loading);

        mPlayBtn.setOnClickListener(this);
        mVideoPlayerControllerView.setVideoControlListener(mVideoControlListener);
    }

    private void createVideoPlayView() {

        if(mTextureView == null) {
            mTextureView = new TextureView(getContext());
            mTextureView.setOnClickListener(this);
            mTextureView.setSurfaceTextureListener(this);
            RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(params);
            mVideoContainer.addView(mTextureView, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mTextureView) {
            mVideoPlayerControllerView.showOrHide();
        } else if(v.getId() == R.id.play_image) {
            if(mVideoPlayState == VideoPlayState.NONE) {
                EventBus.getDefault().post(new VideoStopEvent(mId));
                EventBus.getDefault().post(new VideoScrollEvent(this, mId));
            } else if(mVideoPlayState == VideoPlayState.FINISH) {
                mPlayer.seekTo(0);
                updatePlayProgress();
            }
            playOrPause();
        }
    }

    public void loadVideoUrl(String videoUrl, int id) {

        mVideoUrl = videoUrl;
        mId = id;
        createVideoPlayView();
    }

    /**
     * 播放或暂停播放控制
     */
    private void playOrPause() {

        if(loadVideoAndPlay(mVideoUrl)) {
            if(isCanPlay()) {//当前处于暂停播放状态
                startPlay();
            } else {
                pausePlay();
            }
            updatePlayBtnIcon();//切换播放时更新播放按钮的图标
        }
    }

    private boolean isCanPlay() {

        return mVideoPlayState == VideoPlayState.NONE || mVideoPlayState == VideoPlayState.PAUSE ||
                mVideoPlayState == VideoPlayState.FINISH;
    }

    private void hideVideoImageIfNeed() {

        if(mVideoImage.getVisibility() == VISIBLE) {
            mVideoImage.setVisibility(GONE);
        }
    }

    private void showVideoImageIfNeed() {

        if(mVideoImage.getVisibility() == GONE) {
            mVideoImage.setVisibility(VISIBLE);
        }
    }

    private void showControllerView() {

        if(mVideoPlayerControllerView.getVisibility() == GONE) {
            mVideoPlayerControllerView.setVisibility(VISIBLE);
        }
    }

    private void hidePlayButtonIfNeed() {

        if(mPlayBtn.getVisibility() == VISIBLE) {
            mPlayBtn.setVisibility(GONE);
        }
    }

    private void showPlayButtonIfNeed() {

        if(mPlayBtn.getVisibility() == GONE) {
            mPlayBtn.setVisibility(VISIBLE);
            updatePlayBtnIcon();
        }
    }

    /**
     * 更新播放按钮的图标
     */
    private void updatePlayBtnIcon() {

        int resId = R.drawable.ic_play;
        switch (mVideoPlayState) {
            case PLAY:
                resId = R.drawable.ic_bar_pause;
                break;
            case NONE:
            case PAUSE:
            case FINISH:
                resId = R.drawable.ic_play;
                break;
        }
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
    private boolean loadVideoAndPlay(String videoUrl) {

        if(mPlayer != null) {
            return true;
        }
        setVideoPlayState(VideoPlayState.LOADING);
        mPlayer = new MediaPlayer();
        playPrepare(videoUrl);
        return false;
    }

    private void setVideoPlayState(VideoPlayState state) {
        mVideoPlayState = state;
        switch (state) {
            case NONE:
                hideLoadingBarIfNeed();
                showPlayButtonIfNeed();
                showVideoImageIfNeed();
                break;
            case LOADING:
                showLoadingBarIfNeed();
                hidePlayButtonIfNeed();
                hideVideoImageIfNeed();
                break;
            case PAUSE:
                showPlayButtonIfNeed();
                break;
            case PLAY:
                hideVideoImageIfNeed();
                showControllerView();
                updatePlayProgress();
                hideLoadingBarIfNeed();
                break;
            case FINISH:
                showVideoImageIfNeed();
                showControllerView();
                updatePlayProgress();
                mVideoPlayerControllerView.show();
                break;
        }
    }

    /**
     * 播放准备
     * @param videoUrl
     */
    private void playPrepare(String videoUrl) {

        try {
            if(mPlayer != null) {
                mPlayer.setOnPreparedListener(mOnPreparedListener);
                mPlayer.setOnCompletionListener(mOnCompletionListener);
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

        createVideoPlayView();
        mPlayer.start();//开始播放
        setVideoPlayState(VideoPlayState.PLAY);
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {

        mPlayer.pause();
        setVideoPlayState(VideoPlayState.PAUSE);
    }

    public void finishPlay() {

        mPlayer.pause();
        mPlayer.seekTo(mPlayer.getDuration());
        setVideoPlayState(VideoPlayState.FINISH);
    }

    /**
     * 更新播放进度
     */
    private void updatePlayProgress() {

        if(mPlayer != null) {
            int duration = mPlayer.getDuration();
            int playTime = mPlayer.getCurrentPosition();
            mVideoPlayerControllerView.setVideoDuration(duration);
            mVideoPlayerControllerView.setVideoPlayTime(playTime);
            mVideoPlayerControllerView.setSecondaryProgress(mSecProgress);
        }
        if(mVideoPlayState == VideoPlayState.PLAY) {

            postDelayed(new Runnable() {

                @Override
                public void run() {

                    updatePlayProgress();
                }
            }, 1000);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

        mSurface = new Surface(surfaceTexture);
        if(mPlayer != null) {
            mPlayer.setSurface(mSurface);
        }
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

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            finishPlay();
        }
    };

    private VideoPlayerControllerView.VideoControlListener mVideoControlListener =
            new VideoPlayerControllerView.VideoControlListener() {

        @Override
        public void onProgressChanged(int seekTime) {

            mPlayer.seekTo(seekTime);
            updatePlayProgress();
        }

        @Override
        public void fullScreen() {

        }

        @Override
        public void exitFullScreen() {

        }

        @Override
        public void onControllerShow() {

            showPlayButtonIfNeed();
        }

        @Override
        public void onControllerHide() {

            if(mVideoPlayState == VideoPlayState.PLAY) {
                hidePlayButtonIfNeed();
            }
        }
    };

    public void onDestroy() {

        mVideoPlayerControllerView.onDestroy();
        setVideoPlayState(VideoPlayState.NONE);
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if(mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        mVideoContainer.removeView(mTextureView);
        mTextureView = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoStopEventReceive(VideoStopEvent event) {

        if(event.id != mId) {
            onDestroy();
        }
    }

    public static class VideoStopEvent {
        private int id;
        private VideoStopEvent(int id) {
            this.id = id;
        }
    }

    public static class VideoScrollEvent {
        private VideoPlayerView mVideoPlayerView;
        private int mPosition;

        public VideoScrollEvent(VideoPlayerView videoPlayerView, int position) {

            mVideoPlayerView = videoPlayerView;
            mPosition = position;
        }

        public VideoPlayerView getVideoPlayerView() {

            return mVideoPlayerView;
        }

        public int getPosition() {

            return mPosition;
        }
    }
}
