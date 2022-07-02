package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private final String content;
    private final String sender;
    private final String receiver;
    private final LocalDateTime dateTime;
    private boolean readStatus;
    private ArrayList<Reaction> reactions;

    public Message(String content, String sender, String receiver, LocalDateTime dateTime) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        reactions = new ArrayList<>();
        readStatus = false;
    }

    public ArrayList<Reaction> getReactions() {
        return reactions;
    }

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
    }

    public String reactionsToString() {
        int numberOfLikes = 0;
        int numberOfDisLikes = 0;
        int numberOfHaha = 0;

        if (reactions == null) {
            reactions = new ArrayList<>();
        }
        for (Reaction reaction : reactions) {
            if (reaction.equals(Reaction.LIKE)) {
                numberOfLikes++;
            }
            if (reaction.equals(Reaction.DISLIKE)) {
                numberOfDisLikes++;
            }
            if (reaction.equals(Reaction.HAHA)) {
                numberOfHaha++;
            }
        }

        return "Likes: " + numberOfLikes + "   DisLikes: " + numberOfDisLikes + "   HaHa: " + numberOfHaha;
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