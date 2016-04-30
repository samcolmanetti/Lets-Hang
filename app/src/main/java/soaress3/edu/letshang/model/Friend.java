package soaress3.edu.letshang.model;

/**
 * Created by luizr on 30/04/2016.
 */
public class Friend {
    String user_id_1;
    String user_id_2;
    long status;

    public Friend() {
    }

    public Friend(String user_id_1, String user_id_2, long status) {
        this.user_id_1 = user_id_1;
        this.user_id_2 = user_id_2;
        this.status = status;
    }

    public String getUser_id_1() {
        return user_id_1;
    }

    public String getUser_id_2() {
        return user_id_2;
    }

    public long getStatus() {
        return status;
    }
}
