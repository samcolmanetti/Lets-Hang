package soaress3.edu.letshang.model;

import java.util.Date;

/**
 * Created by luizr on 30/04/2016.
 */
public class Profile {
    long profile_id;
    String user_id;
    String birthday;
    String gender;
    String picture;
    String status;

    public Profile() {
    }

    public Profile(long profile_id, String user_id, String birthday, String gender, String picture, String status) {
        this.profile_id = profile_id;
        this.user_id = user_id;
        this.birthday = birthday;
        this.gender = gender;
        this.picture = picture;
        this.status = status;
    }

    public long getProfile_id() {
        return profile_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getPicture() {
        return picture;
    }

    public String getStatus() {
        return status;
    }
}
