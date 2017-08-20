package com.example.wkj_pc.fitnesslive.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wkj_pc.fitnesslive.MainApplication;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.activity.LoginActivity;
import com.example.wkj_pc.fitnesslive.activity.MainActivity;

public class MainPageFragment extends Fragment implements View.OnClickListener{
    private FragmentManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getFragmentManager();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ImageView homeMessageReceiverBtn = (ImageView) view.findViewById(R.id.home_message_receive_btn);
        homeMessageReceiverBtn.setOnClickListener(this);
        TextView ownInfo= (TextView) view.findViewById(R.id.home_personinfo_image_view);
        ownInfo.setOnClickListener(this);
        ImageView homeLiveVideoImageView  = (ImageView) view.findViewById(R.id.home_live_video_image_view);
        homeLiveVideoImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_live_video_image_view:   //弹出拍摄或者直播图标
                //滑出直播和拍摄选项
                if (null == MainApplication.loginUser)
                {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    return;
                }
                new BottomSheetDialogFrag().show(MainActivity.manager,"dialog");
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

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}