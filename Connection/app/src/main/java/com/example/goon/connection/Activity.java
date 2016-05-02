package com.example.goon.connection;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniela on 29/04/2016.
 */
    public class Activity {
    private Integer idActivity;
    private String name;
    private String description;
    private String type;
    private ArrayList<String>tags;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean temporary;
    private Date date_start;
    private Date date_end;
    private Integer opening_hours;

    private ArrayList<Chanel> chanelArrayList ;

    public Activity(Integer idActivity, String name, String description, String type, ArrayList<String> tags, String address, Double latitude, Double longitude, Boolean temporary, Date date_start, Date date_end, Integer opening_hours, ArrayList<Chanel> chanelArrayList) {
        this.idActivity = idActivity;
        this.name = name;
        this.description = description;
        this.type = type;
        this.tags = tags;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temporary = temporary;
        this.date_start = date_start;
        this.date_end = date_end;
        this.opening_hours = opening_hours;
        this.chanelArrayList = chanelArrayList;
    }

    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Integer getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(Integer opening_hours) {
        this.opening_hours = opening_hours;
    }

    public ArrayList<Chanel> getChanelArrayList() {
        return chanelArrayList;
    }

    public void setChanelArrayList(ArrayList<Chanel> chanelArrayList) {
        this.chanelArrayList = chanelArrayList;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "idActivity=" + idActivity +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", tags=" + tags +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", temporary=" + temporary +
                ", date_start=" + date_start +
                ", date_end=" + date_end +
                ", opening_hours=" + opening_hours +
                ", chanelArrayList=" + chanelArrayList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;

        Activity activity = (Activity) o;

        if (!idActivity.equals(activity.idActivity)) return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (description != null ? !description.equals(activity.description) : activity.description != null)
            return false;
        if (type != null ? !type.equals(activity.type) : activity.type != null) return false;
        if (tags != null ? !tags.equals(activity.tags) : activity.tags != null) return false;
        if (address != null ? !address.equals(activity.address) : activity.address != null)
            return false;
        if (latitude != null ? !latitude.equals(activity.latitude) : activity.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(activity.longitude) : activity.longitude != null)
            return false;
        if (temporary != null ? !temporary.equals(activity.temporary) : activity.temporary != null)
            return false;
        if (date_start != null ? !date_start.equals(activity.date_start) : activity.date_start != null)
            return false;
        if (date_end != null ? !date_end.equals(activity.date_end) : activity.date_end != null)
            return false;
        if (opening_hours != null ? !opening_hours.equals(activity.opening_hours) : activity.opening_hours != null)
            return false;
        return !(chanelArrayList != null ? !chanelArrayList.equals(activity.chanelArrayList) : activity.chanelArrayList != null);

    }


}
