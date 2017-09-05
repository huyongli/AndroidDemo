package cn.ittiger.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.demo.R;
import cn.ittiger.demo.bean.TPhoto;
import cn.ittiger.demo.ui.CommonRecyclerView;
import cn.ittiger.demo.ui.HeaderAndFooterAdapter;
import cn.ittiger.demo.ui.ViewHolder;
import cn.ittiger.demo.util.DisplayUtil;
import cn.ittiger.demo.util.ImageLoaderManager;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerToInRecyclerViewActivity extends AppCompatActivity{
    @BindView(R.id.recyclerView)
    CommonRecyclerView mRecyclerView;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recyclerview_in_recyclerview);
        ButterKnife.bind(this);
        mActivity = this;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<TPhoto> photos = new ArrayList<>();
        photos.add(new TPhoto("第1个"));
        photos.add(new TPhoto("第2个"));
        photos.add(new TPhoto("第3个"));
        photos.add(new TPhoto("第4个"));

        mRecyclerView.setAdapter(new MyAdapter(photos));
    }

    class MyAdapter extends HeaderAndFooterAdapter<TPhoto> {

        public MyAdapter(List<TPhoto> list) {

            super(list);

        }

        @Override
        public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_grid_recycler_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position, TPhoto item) {

            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.mTextView.setText(item.getTitle());
            viewHolder.mTPhotoAdapter.reset(item.getPhotos());
        }
    }

    class MyViewHolder extends ViewHolder {
        @BindView(R.id.title)
        TextView mTextView;
        @BindView(R.id.recyclerView)
        CommonRecyclerView recyclerView;
        TPhotoAdapter mTPhotoAdapter;

        public MyViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
            mTPhotoAdapter = new TPhotoAdapter(new ArrayList<String>());
            recyclerView.setAdapter(mTPhotoAdapter);
        }
    }

    class TPhotoAdapter extends HeaderAndFooterAdapter<String> {
        int width;
        public TPhotoAdapter(List<String> list) {

            super(list);
            width = DisplayUtil.getWindowWidth(mActivity) / 3;
        }

        @Override
        public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mActivity).inflate(R.layout.image_view, parent, false);
            view.getLayoutParams().width = width;
            view.getLayoutParams().height = width;
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position, String item) {

            PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
            ImageLoaderManager.getInstance().displayImage(viewHolder.mImageView, item);
        }
    }

    class PhotoViewHolder extends ViewHolder {
        ImageView mImageView;

        public PhotoViewHolder(View itemView) {

            super(itemView);
            mImageView = (ImageView) itemView;
        }
    }
}
