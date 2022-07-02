package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public class LoggedInRespond extends Respond{
    public LoggedInRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() throws Exception {

        //if user want to go to server panel after login
        if (info.getString("process").equals("serverPanel")) {
            parseMessageToJsonAndSendToClient(info);
            return;
        }
        if ( info.getString("process").equals("channelPanel")) {
            parseMessageToJsonAndSendToClient(info);
            return;
        }

        info.put("process", "action");

        parseMessageToJsonAndSendToClient(info);
    }

}
