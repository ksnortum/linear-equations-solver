package solver.test;

import solver.main.control.Executor;

public class TestExecutor {
    private String[] args;
    private Executor executor;

    public static void main(String[] args) {
        new TestExecutor().run();
    }

    private void run() {
        setup();
        executor.run(args);
    }

    private void setup() {
        args = new String[4];
        args[0] = "-in";
        args[1] = "in.txt";
        args[2] = "-out";
        args[3] = "out.txt";

        executor = new Executor();
    }
}