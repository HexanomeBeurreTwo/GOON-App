package com.h4115.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class HappeningAdapter extends ArrayAdapter<Happening> {

    public HappeningAdapter(Context context, List<Happening> happenings) {
        super(context, 0, happenings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_happening_layout,parent, false);
        }

        HappeningViewHolder hvh = (HappeningViewHolder) convertView.getTag();
        if(hvh == null){
            hvh = new HappeningViewHolder();
            hvh.happeningName = (TextView) convertView.findViewById(R.id.happening_name);
            hvh.happeningTags = (TextView) convertView.findViewById(R.id.happening_tags);
            hvh.happeningImage = (ImageView) convertView.findViewById(R.id.happening_image);
            convertView.setTag(hvh);
        }

        Happening happening = getItem(position);

        String tags = happening.getTags();
        tags = tags.replace("[", "");
        tags = tags.replace("]", "");
        tags = tags.replace("\"", "");
        tags = tags.replace(",", " ");
        tags = tags.replace(";", " ");

        hvh.happeningName.setText(happening.getName());
        hvh.happeningTags.setText(tags);
        hvh.happeningImage.setImageDrawable(new ColorDrawable(happening.getId()));

        return convertView;
    }

    private class HappeningViewHolder{
        public TextView happeningName;
        public TextView happeningTags;
        public ImageView happeningImage;
    }
}
