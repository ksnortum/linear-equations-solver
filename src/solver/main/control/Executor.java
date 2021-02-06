package solver.main.control;

import solver.main.logic.EquationSolver;
import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.MatrixRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

public class Executor {
    public enum SolutionState { SOLUTION, NO_SOLUTION, INFINITE_SOLUTIONS }

    private final EquationSolver equationSolver = new EquationSolver();
    private String inputFileName = null;
    private String outputFileName = null;
    private int numberOfVariables = 0;

    public void run(String[] args) {
        // sets numberOfVariables as a side-effect
        parseArgs(args);
        Optional<Matrix> matrixOptional = getMatrixFromInputFile();

        if (matrixOptional.isPresent()) {
            System.out.println("Start solving the equation.");
            System.out.println("Row manipulation:");
            Matrix matrix = matrixOptional.get();
            SolutionState state = equationSolver.solve(matrix, numberOfVariables);
            writeSolutionToOutputFile(matrix, state, numberOfVariables);
        }
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-in":
                    if (i < args.length - 1) {
                        i++;
                        inputFileName = args[i];
                    }
                    break;
                case "-out":
                    if (i < args.length - 1) {
                        i++;
                        outputFileName = args[i];
                    }
                    break;
                default:
                    System.err.println("Bad command line argument: " + args[i]);
            }
        }

        if (inputFileName == null || outputFileName == null) {
            System.err.println("input or output file name not set, aborting");
            System.exit(1);
        }
    }

    private Optional<Matrix> getMatrixFromInputFile() {
        Path path = Path.of(inputFileName);
        List<String> lines;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        // First line parsing
        String[] firstLine = lines.get(0).split("\\s+");

        if (firstLine.length < 2) {
            System.err.println("First line of input file has too few integers");
            return Optional.empty();
        }

        numberOfVariables = Integer.parseInt(firstLine[0]);
        int numberOfEquations = Integer.parseInt(firstLine[1]);
        Matrix matrix = new Matrix();

        // Build matrix
        for (int i = 1; i < numberOfEquations + 1; i++) {
            String line = lines.get(i);
            String[] parts = line.split("\\s+");
            MatrixRow matrixRow = new MatrixRow();

            for (String part : parts) {
                matrixRow.add(Complex.parse(part));
            }

            matrix.add(matrixRow);
        }

        return Optional.of(matrix);
    }

    private void writeSolutionToOutputFile(Matrix matrix, SolutionState state, int numberOfVariables) {
        Path path = Path.of(outputFileName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (state == SolutionState.NO_SOLUTION) {
            String line = "No solutions";
            System.out.println(line);
            writeLineToFile(path, line);
        } else if (state == SolutionState.INFINITE_SOLUTIONS) {
            String line = "Infinitely many solutions";
            System.out.println(line);
            writeLineToFile(path, line);
        } else {
            int constantColumn = matrix.getLineLength() - 1;
            System.out.println("Solution:");

            for (int row = 0; row < numberOfVariables; row++) {
                Complex constant = matrix.getCoefficient(row, constantColumn);
                System.out.printf("%s ", constant);
                String line = String.format("%s%n", constant);
                writeLineToFile(path, line);
            }
        }

        System.out.printf("%nSaved to file %s%n", outputFileName);
    }

    private void writeLineToFile(Path path, String line) {
        try {
            Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
