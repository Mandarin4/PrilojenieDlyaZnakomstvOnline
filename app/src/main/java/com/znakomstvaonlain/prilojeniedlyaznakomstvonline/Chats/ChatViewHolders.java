package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView message;
    public LinearLayout container_oppo;
    public LinearLayout container_my;
    public TextView message_my,date_my,date_oppo,message_oppo;
    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        message = itemView.findViewById(R.id.message);
        container_oppo = itemView.findViewById(R.id.container_oppo);
        container_my = itemView.findViewById(R.id.container_my);
        message_my = itemView.findViewById(R.id.message_my);
        date_my = itemView.findViewById(R.id.date_my);
        date_oppo = itemView.findViewById(R.id.date_oppo);
        message_oppo = itemView.findViewById(R.id.message_oppo);


    }

    @Override
    public void onClick(View view) {
    }

}
