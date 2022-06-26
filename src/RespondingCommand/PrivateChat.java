package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public class PrivateChat extends Respond{
    public PrivateChat(Socket socket, JSONObject info) {
        super(socket, info);
    }


    @Override
    public void handle() throws Exception {

    }
}
