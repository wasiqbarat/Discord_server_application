package Exceptions;

public class userExistException extends Exception{
    @Override
    public String getMessage() {
        return "user already exists";
    }
}
