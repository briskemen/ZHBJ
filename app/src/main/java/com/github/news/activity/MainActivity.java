package com.github.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.github.news.TabLayoutActivity;
import com.github.news.base.BaseActivity;
import com.github.news.base.BasePager;
import com.github.news.base.impl.NewsCenterPager;
import com.github.news.base.impl.PicturePager;
import com.github.news.base.impl.VRPager;
import com.github.news.base.impl.VideoPager;
import com.github.news.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.rg_group)
    RadioGroup rgGroup;

    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout   mDrawerLayout;
    
    @BindView(R.id.nav_view)
    NavigationView mNavigationMenu;

    private ActionBarDrawerToggle mToggle;

    private List<BasePager> mPagerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initActionBarToggle();
        initNavigationMenu();
        initData();
    }

    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 让导航按钮显示
            actionBar.setHomeAsUpIndicator(R.mipmap.img_menu);// 设置导航按钮图标
        }
    }

    private void initActionBarToggle() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R
                .string.close);
        mToggle.syncState();// 同步状态的方法
        mDrawerLayout.addDrawerListener(mToggle); //设置mDrawerLayout拖动的监听
    }

    private void initNavigationMenu() {
        mNavigationMenu.setCheckedItem(R.id.nav_news);// 将news设为默认选中
        mNavigationMenu.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_recommend:
                        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_women:
                       /* Intent intent3 = new Intent(MainActivity.this, ImageActivity.class);
                        startActivity(intent3);*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        Intent intent2 = new Intent(MainActivity.this, com.github.news.activity.VRActivity.class);
                        startActivity(intent2);
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void initData() {
        rgGroup.check(R.id.rb_news);// 默认勾选首页

        // 初始化4个子页面
        mPagerList = new ArrayList();
        mPagerList.add(new NewsCenterPager(this));
        mPagerList.add(new VRPager(this));
        mPagerList.add(new PicturePager(this));
        mPagerList.add(new VideoPager(this));

        mViewPager.setAdapter(new ContentAdapter());

        // 监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        // mViewPager.setCurrentItem(0);// 设置当前页面
                        mViewPager.setCurrentItem(0, false);// 去掉切换页面的动画
                        break;
                    case R.id.rb_vr:
                        mViewPager.setCurrentItem(1, false);// 设置当前页面
                        break;
                    case R.id.rb_pic:
                        mViewPager.setCurrentItem(2, false);// 设置当前页面
                        break;
                    case R.id.rb_video:
                        mViewPager.setCurrentItem(3, false);// 设置当前页面
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                mPagerList.get(pos).initData();// 获取当前被选中的页面, 初始化该页面数据
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        mPagerList.get(0).initData();// 初始化首页数据
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            // pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}