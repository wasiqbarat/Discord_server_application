package RespondingCommand;

import Server.Data;
import org.json.JSONObject;

import java.net.Socket;

public class LogInRespond extends Respond {
    private Data data = Data.getInstance();
    public LogInRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() {

    }

}
