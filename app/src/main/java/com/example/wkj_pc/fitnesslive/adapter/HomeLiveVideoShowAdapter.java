package com.example.wkj_pc.fitnesslive.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wkj_pc.fitnesslive.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wkj_pc on 2017/8/21.
 */

public class HomeLiveVideoShowAdapter extends RecyclerView.Adapter<HomeLiveVideoShowAdapter.ViewHolder>{
    private List<String> lives;
    private Context context;
    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView liveUsersAmatar;
        ImageView liveUserBigImg;
        TextView homeUserLiveNumsShow;
        TextView liveUserNicknameShow;
        private final RecyclerView homeLiveUserTag;

        public ViewHolder(View itemView) {
            super(itemView);
            liveUsersAmatar = (CircleImageView) itemView.findViewById(R.id.home_live_user_logo);
            liveUserBigImg = (ImageView)itemView.findViewById(R.id.home_live_user_show_img);
            homeUserLiveNumsShow = (TextView) itemView.findViewById(R.id.home_user_live_num_show_text_view);
            liveUserNicknameShow = (TextView) itemView.findViewById(R.id.home_live_user_nickname_show_text_view);
            homeLiveUserTag = (RecyclerView) itemView.findViewById(R.id.home_live_user_tag_recyclerview);
        }
    }
    public HomeLiveVideoShowAdapter(List<String> homeUserLives) {
        lives=homeUserLives;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_user_live_show_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      /*  Glide.with(context).load(lives.get(position)).asBitmap().into(holder.liveUsersAmatar);

        holder.homeUserLiveNumsShow.setText(lives.get(position).getBytes());
        Glide.with(context).load("").asBitmap().into(holder.liveUserBigImg);
        holder.homeUserLiveNumsShow.setText(lives.get(position).getBytes());*/
      List<String> tags=new ArrayList<>();
      tags.add("清纯");
      tags.add("大方");
        tags.add("性感");
      initRecyelerView(holder.homeLiveUserTag,tags);
    }
    public void initRecyelerView(RecyclerView recyclerView,List<String> tags){
        LiveUserTagAdapter adapter = new LiveUserTagAdapter(tags);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public int getItemCount() {
        return lives.size();
    }
}
