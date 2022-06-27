package Classes;

import java.io.Serializable;

public class DiscordFriend extends Person implements Serializable {
    boolean isBlocked = false;

    public DiscordFriend(String userName, String email, String phoneNumber) {
        super(userName, email, phoneNumber);
    }

    public void block() {
        isBlocked = true;
    }

    public void unBlock() {
        isBlocked = false;
    }

    public boolean getBlockStatus() {
        return isBlocked;
    }

    public void setBlockStatus(boolean blockStatus) {
        isBlocked = blockStatus;
    }


}
