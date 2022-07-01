package Exceptions;

public class InvalidServerName extends Exception {
    @Override
    public String getMessage() {
        return "Invalid server name loggedInException";
    }
}
