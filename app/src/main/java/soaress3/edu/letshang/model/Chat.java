package soaress3.edu.letshang.model;

/**
 * Created by luizr on 30/04/2016.
 */
public class Chat {
    String user_id;
    String message;
    long event_id;

    public Chat() {
    }

    public Chat(String user_id, String message, long event_id) {
        this.user_id = user_id;
        this.message = message;
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }

    public long getEvent_id() {
        return event_id;
    }
}
