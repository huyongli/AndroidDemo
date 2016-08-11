package cn.ittiger.demo.adapter;

import cn.ittiger.demo.R;
import cn.ittiger.demo.ui.video.VideoPlayerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: laohu on 2016/8/11
 * @site: http://ittiger.cn
 */
public class VideoAdapter extends BaseAdapter {

    private List<String> mList;
    private Activity mActivity;

    public VideoAdapter(Activity activity) {
        mActivity = activity;

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
            viewHolder.mVideoPlayView = (VideoPlayerView) convertView.findViewById(R.id.video_player_view);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.video_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mVideoPlayView.loadVideoUrl(getItem(position), position);
        viewHolder.mTextView.setText("video：" + (position + 1));
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private class ViewHolder {
        public VideoPlayerView mVideoPlayView;
        public TextView mTextView;
    }

}
