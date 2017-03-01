package cn.ittiger.demo.nestedscroll;

import cn.ittiger.demo.R;
import cn.ittiger.demo.adapter.ContentFragmentAdapter;
import cn.ittiger.demo.fragment.ContentFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylhu on 17-3-1.
 */
public class StickyNavigationActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_navigation);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

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
}
