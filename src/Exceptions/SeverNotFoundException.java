package Exceptions;

public class SeverNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "Server not found.";
    }
}
