package RespondingCommand;

import Classes.DiscordUser;
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
        DiscordUser discordUser = new DiscordUser(userName, password, email, phoneNumber);
        AuthenticationImpl authentication = new AuthenticationImpl();

        try {
            authentication.signUp(discordUser);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exception", false);
            jsonObject.put("method", "signUp");
            jsonObject.put("process", "logIn");
            parseMessageToJson(jsonObject);

        } catch (Exception e) {
            parseErrorToJson(e);
            e.printStackTrace();
        }
    }
}

