package Interfaces;

import Classes.DiscordUser;

public interface Authentication {
    void signUp (DiscordUser discordUser) throws Exception;
    void logIn(String userName, String password) throws Exception;
}
