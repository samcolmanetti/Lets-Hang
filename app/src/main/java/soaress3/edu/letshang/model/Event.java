package soaress3.edu.letshang.model;

/**
 * Created by luizr on 30/04/2016.
 */
public class Event {
    long event_id;
    String create_date;
    String event_date;
    Double location_lat;
    Double location_long;
    Boolean public_event;
    String user_id;
    String description;
    long chat_id;

    public Event() {
    }

    public Event(long event_id, String create_date, String event_date, Double location_lat, Double location_long, Boolean public_event, String user_id, String description, long chat_id) {
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

    public String getCreate_date() {
        return create_date;
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

    public long getChat_id() {
        return chat_id;
    }
}
