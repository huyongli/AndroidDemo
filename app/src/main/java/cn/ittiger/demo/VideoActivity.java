package cn.ittiger.demo;

import cn.ittiger.demo.ui.VideoPlayerView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VideoActivity extends AppCompatActivity {

    private String url = "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4";
    private VideoPlayerView mVideoPlayerView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoPlayerView = (VideoPlayerView) findViewById(R.id.videoPlayerView);
//        mButton = (Button) findViewById(R.id.btnPlay);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mVideoPlayerView.play(url);
//                mButton.setEnabled(false);
//            }
//        });
    }
}
