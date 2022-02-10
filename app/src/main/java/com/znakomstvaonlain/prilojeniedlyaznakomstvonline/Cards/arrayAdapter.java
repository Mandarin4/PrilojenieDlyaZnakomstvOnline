package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId,items);
    }
    public View getView(int position , View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        name.setText(card_item.getName());
        switch (card_item.getProfileImageUri()){
            case "defaultFemale":
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(R.drawable.default_female).into(image);
                break;
            case "defaultMale":
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(R.drawable.default_male).into(image);
                break;

            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUri()).into(image);
                break;
        }
        return  convertView;
    }
}
