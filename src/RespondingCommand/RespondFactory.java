package RespondingCommand;

import org.json.JSONObject;
import java.net.Socket;


public class RespondFactory {
    private static RespondFactory factory = null;

    private Respond respond;

    private RespondFactory() {

    }

    public static RespondFactory getInstance() {
        if (factory == null)
            factory = new RespondFactory();
        return factory;
    }

    public Respond getRespond(JSONObject json, Socket socket) {
        String method = json.getString("method");
        Respond respond = null;
        switch (method) {
            case "signUp" -> respond = new SignUpRespond(socket, json);
            case "logIn" -> respond = new LogInRespond(socket, json);
        }
        return respond;
    }
}
