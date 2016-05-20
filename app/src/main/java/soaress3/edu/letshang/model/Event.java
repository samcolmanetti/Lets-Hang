package soaress3.edu.letshang.model;

public class Event {
    String name;
    String eventDate;
    Double lat;
    Double lng;
    Boolean publicEvent;
    String userId;
    String description;

    public Event() {
    }

    public Event(String name, String event_date, Double lat, Double lng,
                 Boolean isPubicEvent, String user_id, String description) {
        this.name = name;
        this.eventDate = event_date;
        this.lat = lat;
        this.lng = lng;
        this.publicEvent = isPubicEvent;
        this.userId = user_id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getEventDate() {
        return eventDate;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Boolean getPublicEvent() {
        return publicEvent;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }
}
