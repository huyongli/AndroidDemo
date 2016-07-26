package cn.ittiger.demo;

import cn.ittiger.demo.retrofit.RetrofitManager;
import cn.ittiger.demo.util.ImageLoaderManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RetrofitActivity extends ListActivity {

    protected LinearLayout mProgressLayout;
    protected ProgressBar mProgressBar;
    protected TextView mTextViewPercent;
    protected TextView mTextViewProgress;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("RetrofitDemo");

        LayoutInflater.from(this).inflate(R.layout.retrofit_layout, mContainer, true);
        mProgressLayout = (LinearLayout) findViewById(R.id.ll_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTextViewPercent = (TextView) findViewById(R.id.tv_percent);
        mTextViewProgress = (TextView) findViewById(R.id.tv_progress);
        mImageView = (ImageView) findViewById(R.id.image);
    }

    @Override
    public List<String> getData() {

        List<String> list = new ArrayList<>();
        list.add("Get Request model sync");
        list.add("Get Request model async");
        list.add("Get Request string async");
        list.add("Post Request upload file");
        list.add("Post Request string async");
        list.add("Post Request model async by map");
        list.add("download file progress by interceptor");
        return list;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
                long total = msg.getData().getLong("total");
                long progress = msg.getData().getLong("progress");
                int percent = (int) (progress * 1.0 / total * 100);
                if(percent > mProgressBar.getProgress()) {
                    mTextViewPercent.setText(percent + "%");
                    mTextViewProgress.setText(progress + "/" + total);
                    mProgressBar.setProgress(percent);
                }
                break;
            case 2:
                mProgressLayout.setVisibility(View.GONE);
                break;
            case 3:
                String path = msg.getData().getString("file");
                ImageLoaderManager.getInstance().displayImage(mImageView, "file://" + path);
                break;
            }
        }
    };

    @Override
    public void onItemClick(int position, View itemView) {

        switch(position) {
            case 0://Get Request model sync
                RetrofitManager.getInstance().syncGetRequestModel(this);
                break;
            case 1://Get Request model async
                RetrofitManager.getInstance().asyncGetRequestModel(this);
                break;
            case 2://Get Request string async
                RetrofitManager.getInstance().asyncGetRequestString(this);
                break;
            case 3://Post Request upload file
                mProgressLayout.setVisibility(View.VISIBLE);
                RetrofitManager.getInstance().uploadFile(this, mHandler);
                break;
            case 4://Post Request string sync
                RetrofitManager.getInstance().syncPostRequestString(this);
                break;
            case 5://Post Request model async by map
                RetrofitManager.getInstance().asyncPostRequestModelMap(this);
                break;
            case 6://download file progress by interceptor
                mProgressLayout.setVisibility(View.VISIBLE);
                RetrofitManager.getInstance().asyncDownloadFile(this, mHandler);
                break;
        }
    }
}
