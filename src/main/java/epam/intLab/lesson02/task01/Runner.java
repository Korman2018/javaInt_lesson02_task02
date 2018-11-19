package epam.intLab.lesson02.task01;

import epam.intLab.lesson02.task01.service.DemoService;

public class Runner {
    public static void main(String[] args) {
        DemoService demoService = new DemoService();
        demoService.start();
    }
}
