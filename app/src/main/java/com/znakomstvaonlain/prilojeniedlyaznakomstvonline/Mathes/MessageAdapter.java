package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats.ChatActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats.ChatViewHolders;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.SettingsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<MessageList> messageLists;
    private Context context;

    public MessageAdapter(List<MessageList> messageLists, Context context){
        this.messageLists = messageLists;
        this.context = context;
    }


    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MessageList list2 = messageLists.get(position);


        switch (list2.getProfilePic()){
            case "defaultFemale":
                Glide.with(context).load(R.drawable.default_female).into(holder.profilePic);
                break;
            case "defaultMale":
                Glide.with(context).load(R.drawable.default_male).into(holder.profilePic);
                break;
            default:
                Glide.with(context).load(list2.getProfilePic()).into(holder.profilePic);
                break;
        }

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());

        if (list2.getUnseenMEssage() == 0){
            holder.unseenMessage.setVisibility(View.GONE);
        }else{
            holder.unseenMessage.setVisibility(View.VISIBLE);
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", list2.getName());
                intent.putExtra("profilePic", list2.getProfilePic());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.messageLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView name, lastMessage, unseenMessage;
        private LinearLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessage = itemView.findViewById(R.id.unseenMessage);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }
}