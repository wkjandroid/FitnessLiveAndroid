package com.example.wkj_pc.fitnesslive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.po.UploadVideo;
import java.util.List;

import static io.vov.vitamio.provider.MediaStore.Video.Thumbnails.MICRO_KIND;

/**
 * Created by wkj on 2017/9/13.
 */

public class UploadNativeVideoAdapter extends RecyclerView.Adapter<UploadNativeVideoAdapter.ViewHolder>{

    private final List<UploadVideo> videos;
    private Context context;

    public UploadNativeVideoAdapter(List<UploadVideo> videos,Context context) {
        this.videos=videos;
        this.context=context;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoImageView;
        private CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.upload_native_video_show_check_box);
            videoImageView = (ImageView) itemView.findViewById(R.id.upload_native_video_show_image_view);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (UploadVideo video:videos){
                        video.setIsselected(false);
                    }
//                    videos.get(getAdapterPosition()).setIsselected(true);
                    notifyDataSetChanged();
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_native_video_show_items, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setChecked(videos.get(position).isselected());
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videos.get(position).getVideourl(),MICRO_KIND);
        holder.videoImageView.setImageBitmap(bitmap);
    }
    @Override
    public int getItemCount() {
        return videos.size();
    }
}
