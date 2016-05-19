package soaress3.edu.letshang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;

/**
 * Created by luizr on 30/04/2016.
 */
public class Event {
    String name;
    String event_date;
    Double location_lat;
    Double location_long;
    Boolean public_event;
    String user_id;
    String description;

    public Event() {
    }

    public Event(String name, String event_date, Double location_lat, Double location_long, Boolean public_event, String user_id, String description) {
        this.name = name;
        this.event_date = event_date;
        this.location_lat = location_lat;
        this.location_long = location_long;
        this.public_event = public_event;
        this.user_id = user_id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public Double getLocation_lat() {
        return location_lat;
    }

    public Double getLocation_long() {
        return location_long;
    }

    public Boolean getPublic_event() {
        return public_event;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }
}
