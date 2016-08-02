package cn.ittiger.demo.ui;

import cn.ittiger.demo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by baina on 16-7-29.
 */
public class VideoPlayerControllerView extends RelativeLayout implements View.OnClickListener {

    private static final int HIDE_DELAY = 3000;
    private static final String UNKNOWN_SIZE = "00:00";
    private ImageView mFullScreenBtn;
    private TextView mPlayTimeText;
    private TextView mTotalTimeText;
    private SeekBar mSeekBar;
    private ProgressBar mViewOnlyProgressBar;
    private View mControllerBar;
    private int mVideoDuration = 0;
    private VideoControlListener mControlListener;
    private boolean mIsViewOnlySeekBarEnable = true;

    public VideoPlayerControllerView(Context context) {

        this(context, null);
    }

    public VideoPlayerControllerView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public VideoPlayerControllerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        inflate(context, R.layout.video_player_controller_view, this);
        mControllerBar = findViewById(R.id.controller_bar);
        mFullScreenBtn = (ImageView) findViewById(R.id.full_screen_btn);
        mPlayTimeText = (TextView) findViewById(R.id.play_time);
        mTotalTimeText = (TextView) findViewById(R.id.total_time);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mViewOnlyProgressBar = (ProgressBar) findViewById(R.id.view_only_progressbar);

        mFullScreenBtn.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    if (mIsViewOnlySeekBarEnable) {
                        mViewOnlyProgressBar.setProgress(progress);
                    }
                    int time = progress * mVideoDuration / 100 * 1000;
                    mControlListener.onProgressChanged(time);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.full_screen_btn:
                mControlListener.fullScreen();
                break;
        }
    }

    public void setVideoControlListener(VideoControlListener controlListener) {

        mControlListener = controlListener;
    }

    public void setVideoDuration(int videoDuration) {

        mVideoDuration = videoDuration / 1000;
        mTotalTimeText.setText(formatVideoTimeLength(mVideoDuration));
    }

    public void setIsViewOnlySeekBarEnable(boolean isViewOnlySeekBarEnable) {

        mIsViewOnlySeekBarEnable = isViewOnlySeekBarEnable;
    }

    public void setVideoPlayTime(int playTime) {

        playTime = playTime / 1000;
        mPlayTimeText.setText(formatVideoTimeLength(playTime));
        int progress = (int) (playTime * 1.0 / mVideoDuration * 100 + 0.5f);
        mSeekBar.setProgress(progress);
        if(mIsViewOnlySeekBarEnable) {
            mViewOnlyProgressBar.setProgress(progress);
        }
    }

    public void setSecondaryProgress(int progress) {

        mSeekBar.setSecondaryProgress(progress);
        if(mIsViewOnlySeekBarEnable) {
            mViewOnlyProgressBar.setSecondaryProgress(progress);
        }
    }

    public boolean isShown() {

        return mControllerBar.getVisibility() == VISIBLE;
    }

    public void showOrHide() {

        if (mControllerBar.getVisibility() == View.VISIBLE) {
            mControllerBar.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    R.anim.option_leave_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    super.onAnimationEnd(animation);
                    mControllerBar.setVisibility(View.GONE);
                    if(mIsViewOnlySeekBarEnable) {
                        mViewOnlyProgressBar.setVisibility(VISIBLE);
                    }
                    mControlListener.onControllerHide();
                }
            });
            mControllerBar.startAnimation(animation);
        } else {
            show();
            mControllerBar.postDelayed(mHideRunnable, HIDE_DELAY);
        }
    }

    public void show() {

        mControllerBar.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.option_entry_from_bottom);
        animation.setAnimationListener(new AnimationImp() {
            @Override
            public void onAnimationEnd(Animation animation) {

                super.onAnimationEnd(animation);
                mControllerBar.setVisibility(View.VISIBLE);
                if(mIsViewOnlySeekBarEnable) {
                    mViewOnlyProgressBar.setVisibility(GONE);
                }
                mControlListener.onControllerShow();
            }
        });
        mControllerBar.startAnimation(animation);
        mControllerBar.removeCallbacks(mHideRunnable);
    }

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            showOrHide();
        }
    };

    public static class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

    }


    /**
     * 转换视频时长(s)为时分秒的展示格式
     * @param seconds   视频总时长，单位秒
     * @return
     */
    public static String formatVideoTimeLength(long seconds) {

        String formatLength = "";
        if(seconds == 0) {
            formatLength = UNKNOWN_SIZE;
        } else if(seconds < 60) {//小于1分钟
            formatLength = "00:" + (seconds < 10 ? "0" + seconds : seconds);
        } else if(seconds < 60 * 60) {//小于1小时
            long sec = seconds % 60;
            long min = seconds / 60;
            formatLength = (min < 10 ? "0" + min : String.valueOf(min)) + ":" +
                    (sec < 10 ? "0" + sec : String.valueOf(sec));
        } else {
            long hour = seconds / 3600;
            long min = seconds % 3600 / 60;
            long sec = seconds % 3600 % 60;
            formatLength = (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" +
                    (min < 10 ? "0" + min : String.valueOf(min)) + ":" +
                    (sec < 10 ? "0" + sec : String.valueOf(sec));
        }
        return formatLength;
    }

    public interface VideoControlListener {

        void onProgressChanged(int seekTime);

        void fullScreen();

        void onControllerShow();

        void onControllerHide();
    }
}

