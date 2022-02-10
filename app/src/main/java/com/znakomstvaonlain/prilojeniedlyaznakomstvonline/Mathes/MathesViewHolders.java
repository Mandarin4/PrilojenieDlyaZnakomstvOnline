package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats.ChatActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

public class MathesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMathId, mMathName;
    public ImageView mMathImage;

    public MathesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMathId = itemView.findViewById(R.id.MathId);
        mMathName = itemView.findViewById(R.id.MathName);
        mMathImage = itemView.findViewById(R.id.MathImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMathId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);

    }

}
