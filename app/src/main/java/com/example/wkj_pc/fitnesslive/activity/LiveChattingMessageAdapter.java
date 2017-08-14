package com.example.wkj_pc.fitnesslive.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.po.LiveChattingMessage;

import java.util.List;

/**
 * Created by wkj_pc on 2017/8/13.
 */

class LiveChattingMessageAdapter extends RecyclerView.Adapter<LiveChattingMessageAdapter.ViewHolder>{
    private List<LiveChattingMessage> messages;

    public LiveChattingMessageAdapter(List<LiveChattingMessage> livechattingmessages) {
        messages=livechattingmessages;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView accountTextView;
        private TextView chattingMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            accountTextView = (TextView) itemView.findViewById(R.id.live_chatting_message_account);
            chattingMessage = (TextView) itemView.findViewById(R.id.live_chatting_message_text_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_message_show_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.accountTextView.setText(messages.get(position).getFrom());
        holder.chattingMessage.setText(messages.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
