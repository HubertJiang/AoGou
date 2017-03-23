package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.kui.gou.R;
import com.kui.gou.adapter.ClassifyAdapter;
import com.kui.gou.entity.Classify;
import com.kui.gou.fragment.FindFragment;
import com.kui.gou.fragment.MainFragment;
import com.kui.gou.fragment.SettingFragment;
import com.kui.gou.util.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {
    private int currentItem = R.id.goods;
    private ClassifyAdapter adapter;
    private Spinner spinner;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        mainFragment = new MainFragment();
        fragments.add(mainFragment);
        fragments.add(new FindFragment());
        fragments.add(new SettingFragment());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

                switch (position) {
                    case 0:
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        spinner.setVisibility(View.VISIBLE);
                        break;
                    default:
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        spinner.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new ClassifyAdapter(this, R.layout.title_spinner);
        adapter.setDropDownViewResource(R.layout.item_classify);

        spinner.setAdapter(adapter); // set the adapter to provide layout of rows and content
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainFragment.refresh(adapter.getItem(position).getObjectId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.goods:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.find:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.me:
                                viewPager.setCurrentItem(2);
                                break;

                        }
                        return false;
                    }
                });

        spinner.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        get();
    }


    private void get() {
        BmobQuery<Classify> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(new FindListener<Classify>() {
            @Override
            public void done(List<Classify> list, BmobException e) {
                if (e == null) {
                    Classify classify = new Classify();
                    classify.name = getString(R.string.all);
                    list.add(0, classify);
                    adapter.setObjects(list);
                } else {
                    AoApplication.showToast(e.toString());
                }
            }
        });
    }


}
