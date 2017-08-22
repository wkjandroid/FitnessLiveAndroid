package com.example.wkj_pc.fitnesslive.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.activity.LoginActivity;
import com.example.wkj_pc.fitnesslive.activity.MainActivity;
import com.example.wkj_pc.fitnesslive.activity.SysMessageActivity;
import com.example.wkj_pc.fitnesslive.adapter.HomeLiveVideoShowAdapter;
import com.example.wkj_pc.fitnesslive.tools.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class MainPageFragment extends Fragment implements View.OnClickListener{
    private FragmentManager manager;
    private ImageView homeMessageReceiverBtn;
    private RecyclerView homeUserLiveShowRecyclerView;
    private LinearLayout bottomLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getFragmentManager();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        homeMessageReceiverBtn = (ImageView) view.findViewById(R.id.home_message_receive_btn);
        homeMessageReceiverBtn.setOnClickListener(this);
        TextView ownInfo= (TextView) view.findViewById(R.id.home_personinfo_image_view);
        ownInfo.setOnClickListener(this);
        ImageView homeLiveVideoImageView  = (ImageView) view.findViewById(R.id.home_live_video_image_view);
        homeLiveVideoImageView.setOnClickListener(this);
        homeUserLiveShowRecyclerView = (RecyclerView) view.findViewById(R.id.home_user_live_show_recycler_view);
        bottomLinearLayout = (LinearLayout) view.findViewById(R.id.bottom_linearlayout);
        ImageView homeUserSearchImgView= (ImageView) view.findViewById(R.id.home_user_search_img_view);
        homeUserSearchImgView.setOnClickListener(this);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        List <String> lists=new ArrayList<>();
        for (int i=0;i<50;i++){
            lists.add("你好");
        }
        LinearLayoutManager lMamager=new LinearLayoutManager(getActivity());
        homeUserLiveShowRecyclerView.setLayoutManager(lMamager);

        HomeLiveVideoShowAdapter adapter=new HomeLiveVideoShowAdapter(lists);
        homeUserLiveShowRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.home_live_video_image_view:   //弹出拍摄或者直播图标
                //滑出直播和拍摄选项
                if (null == MainApplication.loginUser)
                {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else {
                    new BottomSheetDialogFrag().show(MainActivity.manager,"dialog");
                }
                break;
            case R.id.home_message_receive_btn:
                startActivity(new Intent(getActivity(), SysMessageActivity.class));
                break;
            case R.id.home_user_search_img_view:
                break;
            case R.id.home_personinfo_image_view:   //切换到个人中心fragment
                FragmentTransaction tran = manager.beginTransaction();
                tran.replace(R.id.home_main_content_fragment,new OwnUserInfoFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        setSysMessageShow();
        initMessageReceiver();
    }
    /*设置系统消息显示*/
    private void setSysMessageShow() {

    }
    /*设置系统消息*/
    private void initMessageReceiver() {
        int count = 3;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_action_message_receiver)
                .copy(Bitmap.Config.ARGB_8888, true);
        if (count < 1) {
            homeMessageReceiverBtn.setImageBitmap(bitmap);
        } else {
            Bitmap showBitmap = BitmapUtils.decorateBitmapWithNums(bitmap, getActivity(), count);
            homeMessageReceiverBtn.setImageBitmap(showBitmap);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}