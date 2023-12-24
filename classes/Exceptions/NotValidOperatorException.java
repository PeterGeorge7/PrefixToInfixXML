package Exceptions;

public class NotValidOperatorException extends Exception {
    public NotValidOperatorException() {
        super("Not Valid Operator Exists .. only (+, -, *, /, %)");
    }
}
