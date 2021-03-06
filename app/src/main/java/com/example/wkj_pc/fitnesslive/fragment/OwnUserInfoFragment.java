package com.example.wkj_pc.fitnesslive.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.activity.LoginActivity;
import com.example.wkj_pc.fitnesslive.activity.MainActivity;
import com.example.wkj_pc.fitnesslive.activity.OwnUploadVideoActivity;
import com.example.wkj_pc.fitnesslive.activity.SysMessageActivity;
import com.example.wkj_pc.fitnesslive.activity.UserInfoEditActivity;
import com.example.wkj_pc.fitnesslive.service.LiveService;
import com.example.wkj_pc.fitnesslive.service.LoginService;
import com.example.wkj_pc.fitnesslive.tools.AlertDialogTools;
import com.example.wkj_pc.fitnesslive.tools.BitmapUtils;
import com.example.wkj_pc.fitnesslive.tools.GsonUtils;
import com.example.wkj_pc.fitnesslive.tools.LoginUtils;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class OwnUserInfoFragment extends Fragment implements View.OnClickListener{

    private CircleImageView amatarView; //头像
    private TextView ownNickname;
    private TextView ownAccount;
    private TextView ownUserInfoMyAttention;
    private TextView ownUserRank;
    private TextView ownUserInfoGrade;
    private LinearLayout ownUserVideoLinearLayout;
    private LinearLayout ownUserInfoAboutUsLinearlayout;
    private LinearLayout ownUserInfoDestroyLinearLayout;
    private ImageView ownMessageReceiverBtn;
    private String loginQuitUrl;
    private SharedPreferences cookieSp;
    private LinearLayout ownUserInfoAmatartAccountLinearLayout;
    private TextView ownFansNum;
    private FragmentTransaction tran;
    private FragmentManager manager;
    private FragmentTransaction tran1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginQuitUrl=getResources().getString(R.string.app_destroy_url);
        cookieSp = getActivity().getSharedPreferences("cookie", MODE_PRIVATE);
        manager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_user_info, container, false);
        ImageView ownLiveVideoImageView = (ImageView) view.findViewById(R.id.own_live_video_image_view);
        ownLiveVideoImageView.setOnClickListener(this);
        TextView ownMainPageTextView = (TextView) view.findViewById(R.id.own_main_page_text_view);
        ownMainPageTextView.setOnClickListener(this);
        ownMessageReceiverBtn = (ImageView) view.findViewById(R.id.own_message_receive_btn);
        ownMessageReceiverBtn.setOnClickListener(this);
        amatarView = (CircleImageView) view.findViewById(R.id.icon_amatar_image);
        ownNickname = (TextView) view.findViewById(R.id.own_nickname);
        ownAccount = (TextView) view.findViewById(R.id.own_account);
        ownUserInfoMyAttention = (TextView) view.findViewById(R.id.own_user_info_my_attention);
        ownFansNum = (TextView) view.findViewById(R.id.own_user_fansnum);
        ownUserRank = (TextView) view.findViewById(R.id.icon_own_user_rank);
        ownUserInfoGrade = (TextView) view.findViewById(R.id.own_user_info_grade);
        ownUserVideoLinearLayout = (LinearLayout) view.findViewById(R.id.own_user_video_linearlayout);
        ownUserVideoLinearLayout.setOnClickListener(this);
        ownUserInfoAboutUsLinearlayout = (LinearLayout) view.findViewById(R.id.own_user_info_about_us_linearlayout);
        ownUserInfoAboutUsLinearlayout.setOnClickListener(this);
        ownUserInfoDestroyLinearLayout = (LinearLayout) view.findViewById(R.id.own_user_info_destroy_linearlayout);
        ownUserInfoDestroyLinearLayout.setOnClickListener(this);
        ownUserInfoAmatartAccountLinearLayout = (LinearLayout) view.findViewById(R.id.own_user_info_amatar_account_linearlayout);
        ownUserInfoAmatartAccountLinearLayout.setOnClickListener(this);
        return view;
    }
    /*设置系统消息*/
    private void initMessageReceiver() {
        int count = 3;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_action_message_receiver)
                .copy(Bitmap.Config.ARGB_8888, true);
        if (count < 1) {
            ownMessageReceiverBtn.setImageBitmap(bitmap);
        } else {
            Bitmap showBitmap = BitmapUtils.decorateBitmapWithNums(bitmap, getActivity(), count);
            ownMessageReceiverBtn.setImageBitmap(showBitmap);
        }
    }
    /*设置系统消息显示*/
    private void setSysMessageShow() {

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.own_live_video_image_view:
                //滑出直播和拍摄选项
                if (null == MainApplication.loginUser)
                {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else {
                    new BottomSheetDialogFrag().show(MainActivity.manager,"dialog");
                }
                break;
            case R.id.own_main_page_text_view:   //切换到主页fragment
                tran = manager.beginTransaction();
                tran.replace(R.id.home_main_content_fragment,new MainPageFragment());
                tran.commit();
                break;
            case R.id.own_message_receive_btn:  // 跳转去处理系统的通知消息
                startActivity(new Intent(getActivity(), SysMessageActivity.class));
                break;
            case R.id.own_user_info_destroy_linearlayout:   //退出登录
                if (null==MainApplication.loginUser)
                    return;
                AlertDialogTools.showDialog(getActivity(), R.mipmap.icon_user_destroy_login, true, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LoginUtils.toRequestQuitLogin(loginQuitUrl, GsonUtils.getGson().toJson(MainApplication.loginUser),
                                        MainApplication.cookie);
                            }
                        }).start();
                        getActivity().stopService(new Intent(getActivity(), LiveService.class));
                        getActivity().stopService(new Intent(getActivity(), LoginService.class));
                        amatarView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.head_img));
                        MainApplication.loginUser=null;
                        ownNickname.setText("昵称：小灰灰");
                        ownAccount.setText("账号：000000");
                        ownUserInfoGrade.setText("0");
                        ownUserInfoMyAttention.setText("0");
                        ownFansNum.setText("0");
                    }
                },"取消",null,"提醒","您将退出登录!");
                break;
            case R.id.own_user_video_linearlayout:   //个人视频
                if (null==MainApplication.loginUser){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(),OwnUploadVideoActivity.class));
                }
                break;
            case R.id.own_user_info_amatar_account_linearlayout:
                if (null==MainApplication.loginUser){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(),UserInfoEditActivity.class));
                }
                break;
            case R.id.own_user_info_about_us_linearlayout:   //关于我们
                tran = manager.beginTransaction();
                tran.replace(R.id.home_main_content_fragment,new AboutUsFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;
        }
    }
    /**
     *   /处于活动界面的时候，显示登录信息
     *   有系统消息来到后，显示在toolbar中
     */
    @Override
    public void onResume() {
        super.onResume();
        //显示登录用户信息
        if (null != MainApplication.loginUser) {
            if (!TextUtils.isEmpty(MainApplication.loginUser.getAmatar())){
                Glide.with(this).load(MainApplication.loginUser.getAmatar())
                        .asBitmap().into(amatarView);
            }
            ownNickname.setText("昵称："+MainApplication.loginUser.getNickname());
            ownAccount.setText("账号："+MainApplication.loginUser.getAccount());
            ownUserInfoGrade.setText((null==MainApplication.loginUser.getGrade())?"0":
                    MainApplication.loginUser.getGrade().toString());
            ownUserInfoMyAttention.setText((null==MainApplication.loginUser.getAttentionnum())?"0"
                    :MainApplication.loginUser.getAttentionnum()+"");
            ownFansNum.setText((null==MainApplication.loginUser.getFansnum())?"0":
                    MainApplication.loginUser.getFansnum()+"");
        }
        setSysMessageShow();
        initMessageReceiver();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

