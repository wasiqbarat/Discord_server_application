package RespondingCommand;

import org.json.JSONObject;

import java.net.Socket;

public abstract class Respond {
    protected Socket socket;
    protected JSONObject info;

    public Respond(Socket socket, JSONObject info) {
        this.socket = socket;
        this.info = info;
    }

    public abstract void handle();
}
