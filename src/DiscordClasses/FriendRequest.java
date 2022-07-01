package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class FriendRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final String sender;
    private final String receiver;
    private final LocalDateTime dateTime;

    public FriendRequest(String sender, String receiver, LocalDateTime dateTime) {
        this.sender = sender;
        this.dateTime = dateTime;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

}
