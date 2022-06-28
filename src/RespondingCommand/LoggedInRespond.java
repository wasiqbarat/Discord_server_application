package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public class LoggedInRespond extends Respond{
    public LoggedInRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() throws Exception {

        info.put("process", "action");
        parseMessageToJsonAndSendToClient(info);
    }
}
