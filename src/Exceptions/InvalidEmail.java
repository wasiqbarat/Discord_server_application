package Exceptions;

public class InvalidEmail extends Exception{

    @Override
    public String getMessage() {
        return "Your email is invalid please enter correct email";
    }
}
