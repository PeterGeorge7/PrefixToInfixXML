package Exceptions;

public class MissingOperator extends Exception {
    public MissingOperator() {
        super("(Error in Prefix Expression) , there is missing operator .. can't calculate it");
    }
}
