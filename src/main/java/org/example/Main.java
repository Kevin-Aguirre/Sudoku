package org.example;

public class Main {
    static void main() {
        SudokuGenerator gen = new SudokuGenerator(SudokuGenerator.Difficulty.TEST);
        SudokuBoard board = gen.generatePuzzle();
        SudokuSolver solver = new SudokuSolver();

        do {
            System.out.println(board);
            board.printCandidates();
        } while (solver.solveStep(board));

    }

}
