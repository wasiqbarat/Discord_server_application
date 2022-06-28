package Exceptions;

public class BlockedUserException extends Exception{
    @Override
    public String getMessage() {
        return "You are blocked by this user loggedInException";
    }
}
