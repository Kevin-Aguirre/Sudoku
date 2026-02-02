package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* * *
* Challenge im facing, in order to develop pwd
*
*
* * */
public class Main {
    public static void main(String[] args) {
        SudokuGenerator gen = new SudokuGenerator(SudokuGenerator.Difficulty.HARD);
        SudokuSolver solver = new SudokuSolver();
        SudokuBoard board = gen.generatePuzzle();

        while (solver.solveStep(board)) {
            System.out.println(board);
        }
    }
}
