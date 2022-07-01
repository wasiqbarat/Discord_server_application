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
        if ( !info.getString("process").equals("serverPanel")) {
            info.put("process", "action");
        }

        parseMessageToJsonAndSendToClient(info);
    }
}
