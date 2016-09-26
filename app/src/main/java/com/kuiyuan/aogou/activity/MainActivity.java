package com.kuiyuan.aogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuiyuan.aogou.R;
import com.kuiyuan.aogou.entity.User;
import com.kuiyuan.aogou.fragment.MainFragment;
import com.kuiyuan.aogou.fragment.SettingFragment;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int currentItem = R.id.goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getFragmentManager().beginTransaction().replace(R.id.frame_content, new MainFragment()).commit();
        getSupportActionBar().setTitle(R.string.goods);
        navigationView.setCheckedItem(R.id.goods);

        navigationView.setCheckedItem(0);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.name)).setText(BmobUser.getCurrentUser().getUsername());
        Glide.with(this).load(BmobUser.getCurrentUser(User.class).getAvatar().getUrl()).into((ImageView)navigationView.getHeaderView(0).findViewById(R.id.image_view));
        (navigationView.getHeaderView(0).findViewById(R.id.top_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InformationActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (currentItem != id) {
            if (id == R.id.goods) {
                getFragmentManager().beginTransaction().replace(R.id.frame_content, new MainFragment()).commit();
                getSupportActionBar().setTitle(R.string.goods);
                currentItem = id;
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.setting) {
                getFragmentManager().beginTransaction().replace(R.id.frame_content, new SettingFragment()).commit();
                getSupportActionBar().setTitle(R.string.setting);
                currentItem = id;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
