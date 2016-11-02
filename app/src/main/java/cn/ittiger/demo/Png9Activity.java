package cn.ittiger.demo;

import cn.ittiger.demo.adapter.BaseViewAdapter;
import cn.ittiger.demo.ui.Png9View;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class Png9Activity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_9_png);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new MyAdapter(this));
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context context) {

            mContext = context;
        }

        @Override
        public int getCount() {

            return 20;
        }

        @Override
        public String getItem(int position) {

            return position + "";
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if(convertView == null) {
                convertView = new Png9View(mContext);
                holder = new MyViewHolder((Png9View) convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    class MyViewHolder extends BaseViewAdapter.AbsViewHolder {
        Png9View mPng9View;

        public MyViewHolder(Png9View png9View) {

            mPng9View = png9View;
        }
    }
}
