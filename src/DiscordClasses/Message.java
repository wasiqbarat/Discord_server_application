package DiscordClasses;

import Classes.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String content;
    private Person sender;
    private Person receiver;
    private LocalDateTime dateTime;

    public Message(String content, Person sender, Person receiver, LocalDateTime dateTime) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
    }

}
