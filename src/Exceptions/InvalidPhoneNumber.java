package Exceptions;

public class InvalidPhoneNumber extends Exception{

    @Override
    public String getMessage() {
        return "Invalid phoneNumber. please enter numbers.";
    }
}
