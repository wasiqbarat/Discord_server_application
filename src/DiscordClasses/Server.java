package DiscordClasses;

import Classes.Person;

import java.io.Serializable;
import java.util.ArrayList;

public class Server implements Serializable {
    private String owner;
    private String name;
    private ArrayList<Person> users;
    private ArrayList<Channel> channels;

    public Server(String owner, String name) {
        this.owner = owner;
        this.name = name;
        users = new ArrayList<>();
        channels = new ArrayList<>();
    }

    public void addUser(Person person) {
        users.add(person);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
