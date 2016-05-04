package com.h4115.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends ArrayAdapter<Channel> {

    protected ArrayList<Channel> dynamicChannels = new ArrayList<>();
    protected ArrayList<Channel> staticChannels = new ArrayList<>();

    public ChannelAdapter(Context context, List<Channel> channels, ArrayList<Channel> dynamicChannels) {
        super(context, 0, channels);

        for(Channel channel : dynamicChannels) this.dynamicChannels.add(channel);
        for(Channel channel : dynamicChannels) this.staticChannels.add(channel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_channel_layout, parent, false);
        }

        ChannelViewHolder cvh = (ChannelViewHolder) convertView.getTag();
        if (cvh == null) {
            cvh = new ChannelViewHolder();
            cvh.channelDescription = (TextView) convertView.findViewById(R.id.channel_description);
            cvh.channelName = (TextView) convertView.findViewById(R.id.channel_name);
            cvh.channelImage = (ImageView) convertView.findViewById(R.id.channel_image);
            cvh.channelSwitch = (Switch) convertView.findViewById(R.id.channel_switch);
            cvh.channelSwitch.setTag(position);
            convertView.setTag(cvh);
        }

        Channel channel = getItem(position);
        final int index = position;

        cvh.channelName.setText(channel.getName());
        cvh.channelDescription.setText(channel.getDescription());
        if(dynamicChannels.contains(channel)) cvh.channelSwitch.setChecked(true);
        cvh.channelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) dynamicChannels.add(getItem(index));
                else dynamicChannels.remove(getItem(index));
            }
        });

        switch(channel.getName()){
            case("Sport") : cvh.channelImage.setImageResource(R.drawable.sport); break;
            case("Romantisme") : cvh.channelImage.setImageResource(R.drawable.romantism); break;
            case("Culture") : cvh.channelImage.setImageResource(R.drawable.culture); break;
            case("Restaurant") : cvh.channelImage.setImageResource(R.drawable.restaurant); break;
            case("A voir") : cvh.channelImage.setImageResource(R.drawable.must_see); break;
            case("Soirées") : cvh.channelImage.setImageResource(R.drawable.evening); break;
            case("Famille") : cvh.channelImage.setImageResource(R.drawable.family); break;
            case("Tendance") : cvh.channelImage.setImageResource(R.drawable.trending); break;
            case("Musique") : cvh.channelImage.setImageResource(R.drawable.music); break;
        }


        return convertView;
    }

    private class ChannelViewHolder {
        public TextView channelName;
        public TextView channelDescription;
        public Switch channelSwitch;
        public ImageView channelImage;
    }
}