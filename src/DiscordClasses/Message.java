package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private final String content;
    private final String sender;
    private final String receiver;
    private final LocalDateTime dateTime;
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