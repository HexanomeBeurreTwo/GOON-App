package com.h4115.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends ArrayAdapter<Channel> {

    protected ArrayList<Boolean> subscriptions = new ArrayList<>();

    public ChannelAdapter(Context context, List<Channel> channels, ArrayList<Boolean> subscriptions) {
        super(context, 0, channels);
        this.subscriptions = subscriptions;
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
            cvh.channelSwitch = (Switch) convertView.findViewById(R.id.channel_switch);
            cvh.channelSwitch.setTag(position);
            convertView.setTag(cvh);
        }

        Channel channel = getItem(position);
        final int index = position;

        cvh.channelName.setText(channel.getName());
        cvh.channelDescription.setText(channel.getDescription());
        cvh.channelSwitch.setChecked(subscriptions.get(index));
        cvh.channelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subscriptions.set(index, isChecked);
            }
        });
        return convertView;
    }

    private class ChannelViewHolder {
        public TextView channelName;
        public TextView channelDescription;
        public Switch channelSwitch;
    }
}