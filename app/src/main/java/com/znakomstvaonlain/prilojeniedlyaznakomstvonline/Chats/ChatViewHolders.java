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
    public LinearLayout container;
    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        message = itemView.findViewById(R.id.message);
        container = itemView.findViewById(R.id.container);


    }

    @Override
    public void onClick(View view) {
    }

}
