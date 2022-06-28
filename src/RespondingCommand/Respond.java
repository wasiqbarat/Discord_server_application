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

    public abstract void handle() throws Exception;

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

    protected void parseMessageToJsonAndSendToClient(JSONObject object) {
        try {
            System.out.println("__________________this is sending to client____");
            System.out.println(object);
            System.out.println("_______________________________________________");


            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(object.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
