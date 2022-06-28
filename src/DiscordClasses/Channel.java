package DiscordClasses;

import Classes.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class Channel {
    private String userName;
    private String pinnedMessage;
    private ArrayList<Person> users;
    private HashMap<Message, ArrayList<Reaction>> reactions;

    public Channel(String userName) {
        this.userName = userName;
        users = new ArrayList<>();
        reactions = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Person> getUsers() {
        return users;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPinnedMessage() {
        return pinnedMessage;
    }

    public void setPinnedMessage(String pinnedMessage) {
        this.pinnedMessage = pinnedMessage;
    }
}
