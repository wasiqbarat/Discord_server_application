package RespondingCommand;

import Classes.DiscordUser;
import File.DataBase;
import org.json.JSONObject;

import java.net.Socket;

public class ChangePasswordRespond extends Respond{
    DataBase dataBase = DataBase.getInstance();

    public ChangePasswordRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() throws Exception {
        try {
            DiscordUser person = (DiscordUser) dataBase.getUser(info.getString("userName"));
            person.setPassword(info.getString("newPassword"));
            dataBase.updateUser(person);
            info.put("password", info.getString("newPassword"));
            info.remove("newPassword");

            info.put("method", "loggedIn");
            info.put("process", "action");

            parseMessageToJsonAndSendToClient(info);
        }catch (Exception e) {
            parseErrorToJsonAndSendToClient(e);
        }

    }
}
