package RespondingCommand;

import Exceptions.userOrPasswordInvalidException;
import Interfaces.Authentication;
import Server.Data;
import Services.AuthenticationImpl;
import org.json.JSONObject;

import java.net.Socket;

public class LogInRespond extends Respond {
    private final Data data = Data.getInstance();
    private Authentication loginAuthentication;

    public LogInRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() {
        try {
            loginAuthentication = new AuthenticationImpl();
            loginAuthentication.logIn(null,null);

        } catch (userOrPasswordInvalidException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
