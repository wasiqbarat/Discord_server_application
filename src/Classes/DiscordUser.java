package Classes;
import java.io.Serial;
import java.io.Serializable;

/**
 * DiscordUser is a discord user existent
 *
 * @author wasiq
 */
public class DiscordUser extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    private String password;

    public DiscordUser(String userName, String password, String email, String phoneNumber) {
        super(userName, email, phoneNumber);
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
