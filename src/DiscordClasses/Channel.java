package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String pinnedMessage;
    private HashMap<Message, ArrayList<Reaction>> reactions;

    public Channel(String userName) {
        this.name = userName;
        reactions = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public void addReaction(Message message, Reaction reaction) {
        if (reactions.get(message) == null) {
            reactions.put(message, new ArrayList<>());
        }
    }

    public String getPinnedMessage() {
        return pinnedMessage;
    }

    public void setPinnedMessage(String pinnedMessage) {
        this.pinnedMessage = pinnedMessage;
    }

    //if two channel names be equal, means these channels are equal
    public boolean isEqual(Channel channel) {
        return channel.name.equals(name);
    }

}
