package com.h4115.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class HappeningAdapter extends ArrayAdapter<MainActivity.Happening> {

    public HappeningAdapter(Context context, List<MainActivity.Happening> happenings) {
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
            hvh.happeningLocation = (TextView) convertView.findViewById(R.id.happening_location);
            hvh.happeningImage = (ImageView) convertView.findViewById(R.id.happening_image);
            convertView.setTag(hvh);
        }

        MainActivity.Happening happening = getItem(position);

        hvh.happeningName.setText(happening.getName());
        hvh.happeningLocation.setText(happening.getLocation());
        hvh.happeningImage.setImageDrawable(new ColorDrawable(happening.getImageResource()));

        return convertView;
    }

    private class HappeningViewHolder{
        public TextView happeningName;
        public TextView happeningLocation;
        public ImageView happeningImage;
    }
}
