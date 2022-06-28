package Exceptions;

public class UserInvalid extends Exception{

    @Override
    public String getMessage() {
        return "UserInvalid";
    }
}
