package cn.ittiger.demo.ui;

import cn.ittiger.demo.R;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by baina on 16-7-29.
 */
public class VideoPlayerView extends RelativeLayout implements View.OnClickListener,
        VideoPlayerControllerView.VideoControlListener, TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private VideoPlayerControllerView mVideoPlayerControllerView;
    private ImageView mVideoImage;
    private ImageView mPlayImage;
    private ProgressBar mProgressBar;
    private VideoPlayerControllerView.VideoPlayState mVideoPlayState = VideoPlayerControllerView.VideoPlayState.PAUSE;

    private Surface mSurface = null;
    private MediaPlayer mPlayer;
    private String mVideoUrl = "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4";

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
        mPlayImage = (ImageView) findViewById(R.id.play_image);
        mVideoPlayerControllerView = (VideoPlayerControllerView) findViewById(R.id.videoControllerView);
        mProgressBar = (ProgressBar) findViewById(R.id.video_loading);

        mTextureView.setOnClickListener(this);
        mPlayImage.setOnClickListener(this);
        mVideoPlayerControllerView.setVideoControlListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_view:
                if(!mVideoPlayerControllerView.isShown()) {
                    showOrHidePlayBtn();
                }
                mVideoPlayerControllerView.showOrHide();
                break;
            case R.id.play_image:
                if(mVideoPlayState == VideoPlayerControllerView.VideoPlayState.PAUSE) {
                    startPlay();
                } else {
                    pausePlay();
                }
                setCurrentPlayImage();
                break;
        }
    }

    private void showOrHidePlayBtn() {

        if(mPlayImage.getVisibility() == VISIBLE) {
            mPlayImage.setVisibility(GONE);
        } else {
            mPlayImage.setVisibility(VISIBLE);
            setCurrentPlayImage();
        }
    }

    private void setCurrentPlayImage() {

        int resId = mVideoPlayState == VideoPlayerControllerView.VideoPlayState.PAUSE ? R.drawable.ic_play : R.drawable.ic_pause;
        mPlayImage.setImageResource(resId);
    }

    public void play(String videoUrl) {

        try {
            mPlayer = new MediaPlayer();
//            mPlayer.setOnCompletionListener(mOnCompletionListener);//播放完成的监听
            mPlayer.setOnPreparedListener(mOnPreparedListener);
//            mPlayer.setOnInfoListener(mInfoListener);
//            mPlayer.setOnErrorListener(mErrorListener);
//            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mPlayer.setSurface(mSurface);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setScreenOnWhilePlaying(true);
            mPlayer.setDataSource(videoUrl);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            startPlay();
        }
    };

    public void pausePlay() {

        mPlayer.pause();
        mVideoPlayerControllerView.setPlayState(VideoPlayerControllerView.VideoPlayState.PAUSE);
        mVideoPlayState = VideoPlayerControllerView.VideoPlayState.PAUSE;
    }

    public void startPlay() {

        if(mPlayer == null) {
            play(mVideoUrl);
        } else {
            mPlayer.start();
            mVideoPlayerControllerView.setPlayState(VideoPlayerControllerView.VideoPlayState.PLAY);
            mVideoPlayState = VideoPlayerControllerView.VideoPlayState.PLAY;
        }
    }

    @Override
    public void onProgressChanged(int time) {

    }

    @Override
    public void playSwitch() {


    }

    @Override
    public void fullScreen() {

    }

    @Override
    public void onHide() {

        mPlayImage.setVisibility(GONE);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

        mSurface = new Surface(surfaceTexture);
        play(mVideoUrl);
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
}
