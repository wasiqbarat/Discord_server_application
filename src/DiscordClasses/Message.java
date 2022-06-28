package DiscordClasses;

import Classes.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime dateTime;
    private boolean readStatus;

    public Message(String content, String sender, String receiver, LocalDateTime dateTime) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        readStatus = false;
    }

    public void setReadStatus(boolean b) {
        readStatus = b;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isReadStatus() {
        return readStatus;
    }
}