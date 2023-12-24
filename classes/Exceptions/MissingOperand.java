package Exceptions;

public class MissingOperand extends Exception {
    public MissingOperand() {
        super("(Undefined number) Not Valid Prefix Expression (missing operand)");
        // System.exit(0);
    }
}
