package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.util.List;

public class MathesAdapter extends RecyclerView.Adapter<MathesViewHolders> {

    private List<MathesObject> mathesList;
    private Context context;

    public MathesAdapter(List<MathesObject> mathesList, Context context){
        this.mathesList = mathesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MathesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layuotView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mathes, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layuotView.setLayoutParams(lp);
        MathesViewHolders rov = new MathesViewHolders(layuotView);


        return rov;
    }

    @Override
    public void onBindViewHolder(@NonNull MathesViewHolders holder, int position) {
        holder.mMathId.setText(mathesList.get(position).getUserId());
        holder.mMathName.setText(mathesList.get(position).getName());
        Glide.with(context).load(mathesList.get(position).getProfileImageUri()).into(holder.mMathImage);
    }

    @Override
    public int getItemCount() {
        return this.mathesList.size();
    }
}