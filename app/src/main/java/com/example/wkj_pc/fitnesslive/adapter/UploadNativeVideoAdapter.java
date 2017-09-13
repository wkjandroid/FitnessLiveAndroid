package com.example.wkj_pc.fitnesslive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.wkj_pc.fitnesslive.po.UploadVideo;

import java.util.List;

/**
 * Created by wkj on 2017/9/13.
 */

public class UploadNativeVideoAdapter extends RecyclerView.Adapter<UploadNativeVideoAdapter.ViewHolder>{

    private final List<UploadVideo> videos;
    public UploadNativeVideoAdapter(List<UploadVideo> videos) {
        this.videos=videos;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return videos.size();
    }
}
