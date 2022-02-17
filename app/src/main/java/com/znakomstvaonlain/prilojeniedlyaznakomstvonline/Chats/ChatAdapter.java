package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {

    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layuotView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layuotView.setLayoutParams(lp);
        ChatViewHolders rov = new ChatViewHolders(layuotView);


        return rov;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        if (chatList.get(position).getCurrentUser()){
            holder.container_my.setVisibility(View.VISIBLE);
            holder.container_oppo.setVisibility(View.GONE);
            holder.message_my.setText(chatList.get(position).getMessage());
            holder.date_my.setText(chatList.get(position).getDate());
        }else {
            holder.container_my.setVisibility(View.GONE);
            holder.container_oppo.setVisibility(View.VISIBLE);
            holder.message_oppo.setText(chatList.get(position).getMessage());
            holder.date_oppo.setText(chatList.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}