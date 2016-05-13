package soaress3.edu.letshang.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by luizr on 30/04/2016.
 */
public class Profile {
    String name;
    String email;
    String birthday;
    String gender;
    String picture;

    public Profile() {
    }

    public Profile(String name, String email, String birthday, String gender, String picture) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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
}
