package epam.intLab.lesson02.task01.exceptions;

public class IncorrectPostfixSequenceException extends RuntimeException {
    public IncorrectPostfixSequenceException(String message) {
        super(message);
    }
}
