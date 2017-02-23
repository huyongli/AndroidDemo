package cn.ittiger.demo.behavior;


import cn.ittiger.demo.R;
import cn.ittiger.demo.behavior.adapter.ContentFragmentAdapter;
import cn.ittiger.demo.behavior.fragment.ContentFragment;
import cn.ittiger.demo.behavior.uc.UCViewHeaderBehavior;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UCMainViewActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private UCViewHeaderBehavior mUCViewHeaderBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uc_main_view_layout);
        mTabLayout = (TabLayout) findViewById(R.id.news_view_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.news_view_content_layout);

        mUCViewHeaderBehavior = (UCViewHeaderBehavior) ((CoordinatorLayout.LayoutParams)findViewById(R.id.news_view_header_layout).getLayoutParams()).getBehavior();
        initViewData();
    }

    private void initViewData() {

        List<ContentFragment> fragments = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            fragments.add(ContentFragment.newInstance(i));
        }
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        ContentFragmentAdapter adapter = new ContentFragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {

        if(mUCViewHeaderBehavior.isClosed()) {
            mUCViewHeaderBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }
}
