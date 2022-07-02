package RespondingCommand;

import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Respond class responds and provide data to client
 *
 * info is commands that received from client
 */
public abstract class Respond {
    protected Socket socket;
    protected JSONObject info;

    public Respond(Socket socket, JSONObject info) {
        this.socket = socket;
        this.info = info;
    }

    public abstract void handle() throws Exception;

    /**
     * if exception throws by methods this method send exception to client
     * @param e is that exception which throws by methods
     */
    protected void parseErrorToJsonAndSendToClient(Exception e) {
        info.put("exception", true);
        info.put("cause", e.getMessage());

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(info.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * this method send final data to client
     * @param object is the final data that provides from responders
     */
    protected void parseMessageToJsonAndSendToClient(JSONObject object) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(object.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
