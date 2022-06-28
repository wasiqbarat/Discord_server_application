package Services;

import Classes.DiscordUser;
import File.DiscordFile;
import Interfaces.Observer;

public class ObserverImpl implements Observer {
    private final DiscordFile discordFile = DiscordFile.getInstance();

    @Override
    public void addFriend(DiscordUser discordUser) {

    }

    @Override
    public void removeFriend(String userName) {

    }

}
