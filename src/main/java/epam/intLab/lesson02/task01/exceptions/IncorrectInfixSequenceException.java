package epam.intLab.lesson02.task01.exceptions;

public class IncorrectInfixSequenceException extends RuntimeException {
    public IncorrectInfixSequenceException(String message) {
        super(message);
    }
}
