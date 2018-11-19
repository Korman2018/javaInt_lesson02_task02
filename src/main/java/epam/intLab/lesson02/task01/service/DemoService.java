package epam.intLab.lesson02.task01.service;

import epam.intLab.lesson02.task01.ExpressionCalculator;

public class DemoService {
    public void start() {
        String expression = "-100+(3* (55 - 45))/(25-10)";
        System.out.println(expression + " = " + ExpressionCalculator.calculate(expression));

        expression = "1-5*(10-(-100+ (-50-50)) + 100)/(100-90)";
        System.out.println(expression + " = " + ExpressionCalculator.calculate(expression));

        expression = "-19 + (10 * 8 + 1)-100/(-10)";
        System.out.println(expression + " = " + ExpressionCalculator.calculate(expression));
    }
}
