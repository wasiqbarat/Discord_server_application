package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscordUser implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private ArrayList<DiscordUser> discordFriends;

    public DiscordUser(String userName, String password, String email, String phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        discordFriends = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<DiscordUser> getDiscordFriends() {
        return discordFriends;
    }

    public void setDiscordFriends(ArrayList<DiscordUser> discordFriends) {
        this.discordFriends = discordFriends;
    }

}
