package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public class SignUpRespond extends Respond{
    public SignUpRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() {

    }
}
