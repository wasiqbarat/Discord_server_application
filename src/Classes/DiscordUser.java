package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscordUser extends Person implements Serializable {
    private final String password;
    private final ArrayList<Person> friends;

    public DiscordUser(String userName, String password, String email, String phoneNumber) {
        super(userName, email, phoneNumber);
        this.password = password;
        friends = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    public ArrayList<Person> getFriends() {
        return friends;
    }
}
