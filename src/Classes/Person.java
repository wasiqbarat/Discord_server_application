package Classes;

import java.io.Serializable;

/**
 * Person a general class to show a person existent
 *
 * @author wasiq
 *
 * @see DiscordUser
 * @see DiscordFriend
 */

public abstract class Person implements Serializable {
    protected String userName;
    protected String email;
    protected String phoneNumber;

    public Person(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
