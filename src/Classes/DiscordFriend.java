package Classes;

import java.io.Serial;
import java.io.Serializable;

/**
 * DiscordFriend indicates a discord social network existent
 *
 * @author wasiq
 * @see DiscordUser
 */
public class DiscordFriend extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    boolean isBlocked = false;

    public DiscordFriend(String userName, String email, String phoneNumber) {
        super(userName, email, phoneNumber);
    }

    public boolean getBlockStatus() {
        return isBlocked;
    }

    public void setBlockStatus(boolean blockStatus) {
        isBlocked = blockStatus;
    }


}
