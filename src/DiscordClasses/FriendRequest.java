package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class demonstrates a friend request
 *
 * @author wasiq
 */
public class FriendRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final String sender;
    private final LocalDateTime dateTime;

    public FriendRequest(String sender, String receiver, LocalDateTime dateTime) {
        this.sender = sender;
        this.dateTime = dateTime;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

}
