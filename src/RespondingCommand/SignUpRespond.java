package RespondingCommand;

import Classes.DiscordUser;
import Exceptions.InvalidEmail;
import Exceptions.InvalidPhoneNumber;
import Exceptions.InvalidUserName;
import Services.AuthenticationImpl;
import org.json.JSONObject;
import java.net.Socket;


public class SignUpRespond extends Respond{

    public SignUpRespond(Socket socket, JSONObject info) {
        super(socket, info);
    }

    @Override
    public void handle() {
        String userName = info.getString("userName");
        String password = info.getString("password");
        String email = info.getString("email");
        String phoneNumber = info.getString("phone");

        try {
            if (!email.matches("(.*)*(@)(.*)*(\\.)(.*)*")) {
                throw new InvalidEmail();
            }

            if(!userName.matches("[0-9 a-z A-Z]{6,}")) {
                throw new InvalidUserName();
            }

        }catch (Exception e) {
            parseErrorToJsonAndSendToClient(e);
            return;
        }


        DiscordUser discordUser = new DiscordUser(userName, password, email, phoneNumber);
        AuthenticationImpl authentication = new AuthenticationImpl();

        try {
            authentication.signUp(discordUser);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exception", false);
            jsonObject.put("method", "signUp");
            jsonObject.put("process", "loggedIn");
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);

            parseMessageToJsonAndSendToClient(jsonObject);
        } catch (Exception e) {
            parseErrorToJsonAndSendToClient(e);
            e.printStackTrace();
        }

    }
}

