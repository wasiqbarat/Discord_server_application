package RespondingCommand;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public abstract class Respond {
    protected Socket socket;
    protected JSONObject info;

    public Respond(Socket socket, JSONObject info) {
        this.socket = socket;
        this.info = info;
    }

    public abstract void handle();

    protected void parseErrorToJson(Exception e) {
        JSONObject object = new JSONObject();
        object.put("exception", true);
        object.put("cause", e.getMessage());
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(object.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void parseMessageToJson(JSONObject object) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(object.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
