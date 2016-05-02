package com.example.goon.connection;

import java.util.ArrayList;

/**
 * Created by Daniela on 29/04/2016.
 */
public class Chanel {
    private Integer idChanel;
    private String name;
    private String description;
    private String tags;
    private ArrayList<Activity> activityArrayList;

    public Chanel(Integer idChanel, String name, String description, String tags,  ArrayList<Activity> activityArrayList) {
        this.idChanel = idChanel;

        this.name = name;
        this.description = description;
        this.tags = tags;
        this.activityArrayList = activityArrayList;
    }

    public Integer getIdChanel() {
        return idChanel;
    }

    public void setIdChanel(Integer idChanel) {
        this.idChanel = idChanel;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ArrayList<Activity> getActivityArrayList() {
        return activityArrayList;
    }

    public void setActivityArrayList(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    @Override
    public String toString() {
        return "Chanel{" +
                "idChanel=" + idChanel +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", activityArrayList=" + activityArrayList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chanel)) return false;

        Chanel chanel = (Chanel) o;

        if (!getIdChanel().equals(chanel.getIdChanel())) return false;
        if (getName() != null ? !getName().equals(chanel.getName()) : chanel.getName() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(chanel.getDescription()) : chanel.getDescription() != null)
            return false;
        if (getTags() != null ? !getTags().equals(chanel.getTags()) : chanel.getTags() != null)
            return false;

        return getActivityArrayList().equals(chanel.getActivityArrayList());

    }

}
