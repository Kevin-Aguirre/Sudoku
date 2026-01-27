package org.example;

public class Main {
    static void main() {
        SudokuBoard board = TestBoards.oneMissing();
        System.out.println("before");
        System.out.println(board);
        SudokuSolver solver = new SudokuSolver();

        solver.solveStep(board);
        System.out.println("after 1");
        System.out.println(board);

        solver.solveStep(board);
        System.out.println("after 2");
        System.out.println(board);

        solver.solveStep(board);
        System.out.println("after 3");
        System.out.println(board);

    }
}
