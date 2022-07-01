package Exceptions;

public class InvalidChannelName extends Exception{
    @Override
    public String getMessage() {
        return "Channel name invalid loggedInException";
    }
}
