package epam.intLab.lesson02.task01;

import epam.intLab.lesson02.task01.exceptions.IncorrectInfixSequenceException;
import epam.intLab.lesson02.task01.exceptions.IncorrectPostfixSequenceException;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import static java.lang.Double.parseDouble;

public class ExpressionCalculator {
    private static final String OPERATORS = "+-/*";

    private ExpressionCalculator() {
    }

    /**
     * Convert infix sequence to deque with postfix sequence
     * ** the shunting-yard algorithm **
     * supported operations "+","-","*","/","unary -" and "(",")"
     *
     * @param inputString input queue sequence
     * @return calculate of conversation to reverse polish notation
     * @throws IncorrectInfixSequenceException if input is incorrect
     */
    public static Queue<String> convertToRPN(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            throw new IncorrectInfixSequenceException("input is empty");
        }

        Deque<String> stack = new LinkedList<>();
        Deque<String> outputQueue = new LinkedList<>();
        boolean previousIsNumber = false;
        int pointer = 0;

        while (pointer < inputString.length()) {
            String currentToken = String.valueOf(inputString.charAt(pointer));
            pointer++;

            // if current token is number
            if (isNumber(currentToken)) {
                if (previousIsNumber) {
                    String previousSymbol = outputQueue.removeLast();
                    currentToken = previousSymbol + currentToken;
                }
                previousIsNumber = true;
                outputQueue.add(currentToken);
                continue;
            }

            // if current token is operator
            if (OPERATORS.contains(currentToken)) {
                // current token is potential unary minus?
                if ("-".equals(currentToken)
                        && (pointer == 1 || "(".equals(String.valueOf(inputString.charAt(pointer - 2))))) {
                    previousIsNumber = true;
                    outputQueue.add("-");
                    continue;
                }

                if (!previousIsNumber) {
                    throw new IncorrectInfixSequenceException("too many operations together");
                }

                previousIsNumber = false;

                while (tryPop(stack)) {
                    String tempToken = stack.pop();
                    if (priority(currentToken) <= priority(tempToken)) {
                        outputQueue.add(tempToken);
                    } else {
                        //if current token priority > stack token priority -> add token back into stack
                        stack.push(tempToken);
                        break;
                    }
                }
                stack.push(currentToken);
                continue;
            }

            // if current token is '('
            if (isOpenBracket(currentToken)) {
                stack.push(currentToken);
                continue;
            }

            // if current token is ')'
            if (isCloseBracket(currentToken)) {
                boolean isOpenBracketFound = false;
                while (tryPop(stack)) {
                    String tempToken = stack.pop();
                    if (isOpenBracket(tempToken)) {
                        isOpenBracketFound = true;
                        break;
                    } else {
                        outputQueue.add(tempToken);
                    }
                }
                // stack is empty but '(' not found
                if (!isOpenBracketFound) {
                    throw new IncorrectInfixSequenceException("too many ')'");
                }
                continue;
            }
            throw new IncorrectInfixSequenceException("not supported symbol '" + currentToken + "'");
        }

        // after last token copy stack into output queue
        while (tryPop(stack)) {
            String temp = stack.pop();
            if (temp.equals("(")) {
                throw new IncorrectInfixSequenceException("too many '('");
            }
            outputQueue.add(temp);
        }
        return outputQueue;
    }

    /**
     * Returns result of postfix sequence calculation
     * supported operations "+","-","*","/","unary -" and "(",")"
     *
     * @param input input sequence
     * @return result of calculation
     * @throws IncorrectPostfixSequenceException if postfix sequence is incorrect
     * @throws ArithmeticException               if divide by 0
     */
    public static double calculate(String input) {
        input = removeSpaces(input);
        Queue<String> inputQueue = convertToRPN(input);
        Deque<String> stack = new LinkedList<>();

        while (!inputQueue.isEmpty()) {
            String currentToken = inputQueue.remove();

            if (isNumber(currentToken)) {
                stack.push(currentToken);
            } else if (OPERATORS.contains(currentToken)) {
                try {
                    String secondOp = stack.pop();
                    String firstOp = stack.pop();
                    stack.push(calc(firstOp, secondOp, currentToken).toString());
                } catch (Exception e) {
                    throw new IncorrectPostfixSequenceException("not enough operands");
                }
            } else {
                throw new IncorrectPostfixSequenceException("not supported symbol '" + currentToken + "'");
            }
        }
        return parseDouble(stack.pop());
    }

    // throws ArithmeticException if divide by 0
    private static Double calc(String first, String second, String operation) {
        double firstOp = parseDouble(first);
        double secondOp = parseDouble(second);

        switch (operation) {
            case "+":
                return firstOp + secondOp;
            case "-":
                return firstOp - secondOp;
            case "*":
                return firstOp * secondOp;
            default:
                if (secondOp == 0) {
                    throw new ArithmeticException("Divide by 0: " + first + "/0");
                }
                return parseDouble(first) / secondOp;
        }
    }

    private static boolean tryPop(Deque<String> stack) {
        String temp;
        try {
            temp = stack.pop();
        } catch (NoSuchElementException e) {
            return false;
        }
        stack.push(temp);
        return true;
    }

    private static int priority(String token) {
        switch (token) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    private static boolean isOpenBracket(String token) {
        return "(".equals(token);
    }

    private static boolean isCloseBracket(String token) {
        return ")".equals(token);
    }

    private static boolean isNumber(String token) {
        try {
            parseDouble(token);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static String removeSpaces(String input) {
        return input.replaceAll(" ", "");
    }
}