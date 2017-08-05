package com.example.wkj_pc.fitnesslive.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.example.wkj_pc.fitnesslive.R;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wkj_pc on 2017/7/31.
 */


public class AttentionUserAdapter extends RecyclerView.Adapter<AttentionUserAdapter.ViewHolder>{

    private List<Integer> amatarItems;
    private Context context;

    public AttentionUserAdapter(List<Integer> amatarLists){
        amatarItems=amatarLists;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView amatar;
        public ViewHolder(View itemView) {
            super(itemView);
            amatar= (CircleImageView) itemView.findViewById(R.id.circle_image_view);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attention_user_show_reyclerview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amatar.setImageResource(amatarItems.get(position));
    }

    @Override
    public int getItemCount() {
        return amatarItems.size();
    }
}
