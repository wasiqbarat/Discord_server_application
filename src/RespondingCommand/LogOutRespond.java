package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public class LogOutRespond extends Respond{
    public LogOutRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() throws Exception {
        info.remove("userName");
        info.remove("password");
        info.remove("process");

        parseMessageToJsonAndSendToClient(info);
    }
}
