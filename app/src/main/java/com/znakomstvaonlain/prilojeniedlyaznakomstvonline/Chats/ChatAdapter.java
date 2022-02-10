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
        holder.message.setText(chatList.get(position).getMessage());
        if (chatList.get(position).getCurrentUser()){
            holder.message.setGravity(Gravity.END);
            holder.message.setTextColor(Color.parseColor("#404040"));
            holder.container.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else {
            holder.message.setGravity(Gravity.START);
            holder.message.setTextColor(Color.parseColor("#FFFFFF"));
            holder.container.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}