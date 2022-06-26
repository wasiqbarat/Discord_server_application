package Exceptions;

public class userOrPasswordInvalidException extends Exception{

    @Override
    public String getMessage() {
        return "password or username invalid";
    }

}
