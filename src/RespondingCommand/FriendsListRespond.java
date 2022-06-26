package RespondingCommand;

import Classes.DiscordFriend;
import File.DataBase;
import Server.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;

public class FriendsListRespond extends Respond{
    private DataBase dataBase;
    private Data data = Data.getInstance();

    public FriendsListRespond(Socket socket, JSONObject info) {
        super(socket, info);
        dataBase = DataBase.getInstance();
    }

    @Override
    public void handle() throws Exception {
        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(info.getString("userName"));
        JSONArray friendsJsonArray = new JSONArray();

        for (DiscordFriend discordFriend : friends) {
            if (data.isOnline(discordFriend.getUserName())) {
                friendsJsonArray.put(discordFriend.getUserName() + "    " + "(Online)");
            }else {
                friendsJsonArray.put(discordFriend.getUserName() + "    " + "(Offline)");
            }
        }

        info.put("friends", friendsJsonArray);
        info.put("method", "loggedIn");
        info.put("process","friendsList");

        parseMessageToJsonAndSendToClient(info);
    }
}
