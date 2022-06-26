package DiscordClasses;

import Classes.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FriendRequest implements Serializable {
    private String sender;
    private String receiver;
    private LocalDateTime dateTime;

    public FriendRequest(String sender, String receiver, LocalDateTime dateTime) {
        this.sender = sender;
        this.dateTime = dateTime;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

