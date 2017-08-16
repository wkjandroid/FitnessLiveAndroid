package com.example.wkj_pc.fitnesslive.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.fragment.BottomSheetDialogFrag;
import com.example.wkj_pc.fitnesslive.fragment.DynamicFragment;
import com.example.wkj_pc.fitnesslive.tools.BitmapUtils;
import com.example.wkj_pc.fitnesslive.tools.GsonUtils;
import com.example.wkj_pc.fitnesslive.tools.LoginUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawable_view)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_nav_view)
    NavigationView homeNavView;
    @BindView(R.id.main_message_btn)
    ImageView messageShowBtn;
    //底部导航栏
    @BindView(R.id.home_main_change_tablayout)
    TabLayout homeMainChangeTablayout;
    @BindView(R.id.home_main_content_fragment)
    LinearLayout homeMainContentFragment;

    private DynamicFragment dynamicFragment;
    private HomePageFragment homePgaeFragment;
    private View headerView;
    private ImageView amatarView;
    private TextView username;
    private TextView tishiView;
    private String quitLoginUrl;
    private FragmentManager fragmentManager;
    private int position = 0;   //底部导航栏选中角标
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initMessageReceiver();
        setNavigationView();
        fragmentManager = getFragmentManager();
        /* 将滑出页面的菜单中加入点击事件 */
        homeMainChangeTablayout.setSelectedTabIndicatorColor(Color.WHITE);
        initTabLayout();
        quitLoginUrl = getResources().getString(R.string.app_destroy_url);

    }
    //设置底部导航栏
    private void initTabLayout() {

        homeMainChangeTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    position = 0;
                    toolbar.setVisibility(View.VISIBLE);
                    if (null==homePgaeFragment)
                        homePgaeFragment = new HomePageFragment();
                    FragmentTransaction transaction =fragmentManager.beginTransaction();
                    transaction.replace(R.id.home_main_content_fragment, homePgaeFragment);
                    transaction.commit();
                } else if (tab.getPosition() == 1) {
                    //滑出直播和拍摄选项
                      if (null == MainApplication.loginUser)
                      {
                          startActivity(new Intent(MainActivity.this,LoginActivity.class));
                          return;
                      }
                      new BottomSheetDialogFrag().show(getSupportFragmentManager(), "dialog");
                      homeMainChangeTablayout.getTabAt(position).select();
                } else {
                    position = 2;
                    toolbar.setVisibility(View.GONE);
                    if (null==dynamicFragment)
                        dynamicFragment = new DynamicFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.home_main_content_fragment,dynamicFragment);
                    transaction.commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    if (null == MainApplication.loginUser)
                        return;
                    new BottomSheetDialogFrag().show(getSupportFragmentManager(), "dialog");
                }
            }
        });
    }

    /**
     * 有系统消息来到后，显示在toolbar中
     */
    private void initMessageReceiver() {
        int count = 3;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_action_message_receiver)
                .copy(Bitmap.Config.ARGB_8888, true);
        if (count < 1) {
            messageShowBtn.setImageBitmap(bitmap);
        } else {
            Bitmap showBitmap = BitmapUtils.decorateBitmapWithNums(bitmap, this, count);
            messageShowBtn.setImageBitmap(showBitmap);
        }
        /** 跳转去处理系统的通知消息*/
        messageShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SysMessageActivity.class));
            }
        });
    }

    /**
     * 处于活动界面的时候，显示登录信息
     */
    @Override
    protected void onResume() {
        //显示登录用户信息
        if (null != MainApplication.loginUser) {
            if (null!=MainApplication.loginUser.getAmatar()){
                Glide.with(this).load(MainApplication.loginUser.getAmatar()).asBitmap().into(amatarView);
            }
            tishiView.setText(MainApplication.loginUser.getNickname());
        }
        //设置底部导航栏，弹出选则框退出后设置首页为选中项。
        homeMainChangeTablayout.getTabAt(position).select();
        super.onResume();
    }

    private void setNavigationView() {
        /* 设置头部信息*/
        headerView = homeNavView.getHeaderView(0);
        amatarView = (ImageView) headerView.findViewById(R.id.icon_amatar_image);
        tishiView = (TextView) headerView.findViewById(R.id.tishi);
        amatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        username = (TextView) headerView.findViewById(R.id.username);
        /* 将菜单图标显示出来 */
        homeNavView.setItemIconTintList(null);
        /* 将滑出页面的菜单加入点击事件 */
        homeNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_user_settings:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.home_user_register:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        break;
                    case R.id.home_user_destroy:
                        //用户注销
                        drawerLayout.closeDrawers();
                        quitLogin();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 注销 退出登录
     */
    public void quitLogin() {
        if (null == MainApplication.loginUser)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = GsonUtils.getGson().toJson(MainApplication.loginUser);
                LoginUtils.toRequestQuitLogin(quitLoginUrl, content, MainApplication.cookie);
            }
        }).start();

        amatarView = (ImageView) headerView.findViewById(R.id.icon_amatar_image);
        amatarView.setImageResource(R.drawable.head_img);
        tishiView.setText("点击头像登录");
        MainApplication.loginUser = null;
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);//设置toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //将左上角图标展示
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_main_menu_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*设置菜单*/
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);//打开滑动菜单
                break;
        }
        return true;
    }

}
