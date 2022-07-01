package Exceptions;

public class PermissionException extends Exception{
    @Override
    public String getMessage() {
        return "You are not allowed to do this action.";
    }
}
