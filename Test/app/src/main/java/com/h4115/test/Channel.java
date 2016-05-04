package com.h4115.test;

import java.util.ArrayList;

public class Channel {
    private Integer idChannel;
    private String name;
    private String description;
    private String tags;

    public Channel(Integer idChanel, String name, String description, String tags) {
        this.idChannel = idChanel;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public Channel(Integer idChannel, String name, String description) {
        this.idChannel = idChannel;
        this.name = name;
        this.description = description;
        this.tags = "";

    }

    public Integer getIdChannel() {
        return this.idChannel;
    }

    public void setIdChannel(Integer idChannel) {
        this.idChannel = idChannel;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "idChanel=" + idChannel +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;

        Channel channel = (Channel) o;

        if (!getIdChannel().equals(channel.getIdChannel())) return false;
        if (getName() != null ? !getName().equals(channel.getName()) : channel.getName() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(channel.getDescription()) : channel.getDescription() != null)
            return false;
        if (getTags() != null ? !getTags().equals(channel.getTags()) : channel.getTags() != null)
            return false;

        return true;
    }

}
