package RespondingCommand;

import Classes.DiscordFriend;
import File.DataBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;

public class BlockUserRespond extends Respond {
    DataBase dataBase;

    public BlockUserRespond(Socket socket, JSONObject info) {
        super(socket, info);
        dataBase = DataBase.getInstance();
    }

    @Override
    public void handle() throws Exception {
        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(info.getString("userName"));

        switch (info.getString("process")) {

            case "blocking" -> {
                JSONArray friendsList = new JSONArray();

                for (DiscordFriend discordFriend : friends) {
                    friendsList.put(discordFriend.getUserName() + " " + discordFriend.getBlockStatus());
                }

                info.put("friendsList", friendsList);
                info.put("method", "loggedIn");
                info.put("process", "blockUser");
            }

            case "applyBlocking" -> {
                JSONArray applyBlocking = info.getJSONArray("applyBlocking");

                for (Object object : applyBlocking) {
                    String userName = object.toString().split(" ")[0];

                    String blockStatus = object.toString().split(" ")[1];

                    boolean blockStatus1 = false;

                    if (blockStatus.equals("true")) {
                        blockStatus1 = true;
                    }

                    for (DiscordFriend discordFriend : friends) {

                        if (discordFriend.getUserName().equals(userName)) {
                            discordFriend.setBlockStatus(blockStatus1);
                        }

                    }
                }

                dataBase.updateFriendsMap(info.getString("userName"), friends);

                info.remove("applyBlocking");
                info.put("method", "loggedIn");
                info.put("process", "action");
            }

        }

        parseMessageToJsonAndSendToClient(info);
    }
}
