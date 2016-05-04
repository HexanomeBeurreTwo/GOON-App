package com.h4115.test;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Romain on 03/05/2016.
 */
public class Happening {

    protected int id;
    protected String name;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected String tags;
    protected boolean temporary;

    public Happening(int id, String name, String description, Double latitude, Double longitude, Boolean temporary, String tags) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.name = name;
        this.description = description;
        this.temporary = temporary;
        this.tags = tags;
    }

    public int getId() { return this.id; }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public String getTags(){ return this.tags; }

    public boolean getTemporary(){ return this.temporary; }
}
