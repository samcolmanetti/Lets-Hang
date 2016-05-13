package soaress3.edu.letshang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;

/**
 * Created by luizr on 30/04/2016.
 */
public class Event {
    long event_id;
    String name;
    HashMap<String, Object> create_date;
    HashMap<String, Object> event_date;
    Double location_lat;
    Double location_long;
    Boolean public_event;
    String user_id;
    String description;
    long chat_id;

    public Event() {
    }

    public Event(String name, long event_id, HashMap<String, Object> create_date, HashMap<String, Object> event_date, Double location_lat, Double location_long, Boolean public_event, String user_id, String description, long chat_id) {
        this.name = name;
        this.event_id = event_id;
        this.create_date = create_date;
        this.event_date = event_date;
        this.location_lat = location_lat;
        this.location_long = location_long;
        this.public_event = public_event;
        this.user_id = user_id;
        this.description = description;
        this.chat_id = chat_id;
    }

    public long getEvent_id() {
        return event_id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> getCreate_date() {
        if (create_date != null) {
            return create_date;
        }

        HashMap<String, Object> dateCreatedObj = new HashMap<String, Object>();
        dateCreatedObj.put("date", ServerValue.TIMESTAMP);
        return dateCreatedObj;
    }

    public HashMap<String, Object> getEvent_date() {
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

    public long getChat_id() {
        return chat_id;
    }

    @JsonIgnore
    public long getCreate_dateLong() {
        return (long)create_date.get("date");
    }

    @JsonIgnore
    public long getEvent_dateLong() {
        return (long)event_date.get("date");
    }
}
