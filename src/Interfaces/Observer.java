package Interfaces;

import Classes.DiscordUser;

public interface Observer {
    void addFriend(DiscordUser discordUser);
    void removeFriend(String userName);
}
