package Services;

import Classes.DiscordUser;
import File.DiscordFile;
import Interfaces.Authentication;

public class AuthenticationImpl implements Authentication {
    DiscordFile discordFile = DiscordFile.getInstance();

    @Override
    public void signUp(DiscordUser discordUser) throws Exception {
        discordFile.signUpUser(discordUser);
    }

    @Override
    public void logIn(String userName, String password) throws Exception {
        discordFile.signIn(userName, password);
    }

}
