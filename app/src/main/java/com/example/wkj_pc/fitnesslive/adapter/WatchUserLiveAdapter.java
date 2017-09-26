package com.example.wkj_pc.fitnesslive.adapter;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.po.User;
import com.example.wkj_pc.fitnesslive.tools.ToastUtils;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wkj_pc on 2017/7/31.
 */


public class WatchUserLiveAdapter extends RecyclerView.Adapter<WatchUserLiveAdapter.ViewHolder>{

    private List<User> watchUsers;
    private Context context;
    public WatchUserLiveAdapter(List<User> users,Context context){
        watchUsers=users;
        this.context=context;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView amatar;
        public ViewHolder(View itemView) {
            super(itemView);
            amatar= (CircleImageView) itemView.findViewById(R.id.circle_image_view);
            amatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(context,"dianji", Toast.LENGTH_SHORT);
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_user_show_reyclerview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null!=watchUsers.get(position).getAmatar()){
            Glide.with(context).load(watchUsers.get(position).getAmatar()).asBitmap().into(holder.amatar);
        }else {
            holder.amatar.setImageResource(R.drawable.head_img);
        }
    }
    @Override
    public int getItemCount() {
        return watchUsers.size();
    }
}
