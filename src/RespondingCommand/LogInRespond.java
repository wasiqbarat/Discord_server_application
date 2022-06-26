package RespondingCommand;

import Exceptions.userOrPasswordInvalidException;
import Interfaces.Authentication;
import Server.Data;
import Services.AuthenticationImpl;
import org.json.JSONObject;

import java.net.Socket;

public class LogInRespond extends Respond {
    private final Data data = Data.getInstance();

    public LogInRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() {
        String userName = info.getString("userName");
        String password = info.getString("password");
        Authentication loginAuthentication = new AuthenticationImpl();

        try {
            loginAuthentication.logIn(userName,password);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exception", false);
            jsonObject.put("method", "loggedIn");
            jsonObject.put("process", "loggedIn");
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);

            parseMessageToJsonAndSendToClient(jsonObject);
        } catch (userOrPasswordInvalidException e) {
            parseErrorToJsonAndSendToClient(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
