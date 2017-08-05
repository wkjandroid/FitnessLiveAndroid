package com.example.wkj_pc.fitnesslive.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.fragment.BottomSheetDialogFrag;
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
    @BindView(R.id.navigation_home_tablayout)
    TabLayout navigationHomeTablayout;
    @BindView(R.id.main_message_btn)
    ImageView messageShowBtn;
    private View headerView;
    private ImageView amatarView;
    private TextView username;
    private TextView tishiView;
    private String quitLoginUrl;
    private BottomSheetDialogFrag bottomSheetDialogFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initMessageReceiver();
        bottomSheetDialogFrag = new BottomSheetDialogFrag();
        setNavigationView();/* 将滑出页面的菜单中加入点击事件 */
        initNavigationTabLayout();
        quitLoginUrl=getResources().getString(R.string.app_destroy_url);
    }

/**  有系统消息来到后，显示在toolbar中*/
    private void initMessageReceiver() {
        int count=3;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_action_message_receiver)
                .copy(Bitmap.Config.ARGB_8888,true);
        if (count<1){
            messageShowBtn.setImageBitmap(bitmap);
        }else {
            Bitmap showBitmap = BitmapUtils.decorateBitmapWithNums(bitmap,this,count);
            messageShowBtn.setImageBitmap(showBitmap);
        }
        /** 跳转去处理系统的通知消息*/
        messageShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VideoPlayerActivity.class));
            }
        });
        
    }

    /** 设置首页下面的导航栏 */
    private void initNavigationTabLayout() {

        navigationHomeTablayout.setTabTextColors(getResources().getColor(R.color.colorBeginText),
                getResources().getColor(R.color.colorEndText));
        navigationHomeTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==1)
                    bottomSheetDialogFrag.show(getSupportFragmentManager(), "dialog");
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition()==1)
                   bottomSheetDialogFrag.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    /** 处于活动界面的时候，显示登录信息 */
    @Override
    protected void onResume() {
        if (null!=MainApplication.loginUser){
            Glide.with(this).load(MainApplication.loginUser.getAmatar()).asBitmap().into(amatarView);
            tishiView.setText(MainApplication.loginUser.getNickname());
        }
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
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        username = (TextView) headerView.findViewById(R.id.username);
        /* 将菜单图标显示出来 */
        homeNavView.setItemIconTintList(null);
        /* 将滑出页面的菜单加入点击事件 */
        homeNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_settings:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        break;
                    case R.id.home_register:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                        break;
                    case R.id.home_destroy:
                        quitLogin();
                        break;
                }
                return false;
            }
        });
    }
    /** 注销 退出登录*/
    public void quitLogin(){
        if (null==MainApplication.loginUser)
        {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = GsonUtils.getGson().toJson(MainApplication.loginUser);
                LoginUtils.toRequestQuitLogin(quitLoginUrl,content,MainApplication.cookie);
            }
        }).start();
        amatarView = (ImageView) headerView.findViewById(R.id.icon_amatar_image);
        tishiView.setText("点击头像登录");
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
