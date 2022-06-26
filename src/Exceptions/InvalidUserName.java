package Exceptions;

public class InvalidUserName extends Exception{
    @Override
    public String getMessage() {
        return "Invalid user name. User name most be unique and contains at least 6 words or numbers.";
    }

}
