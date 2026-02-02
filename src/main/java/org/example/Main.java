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

    static void main(String[] args) {

        SudokuGenerator generator =
                new SudokuGenerator(SudokuGenerator.Difficulty.HARD);

        SudokuSolver solver = new SudokuSolver();

        int attempts = 0;

        while (true) {
            System.out.println('s');
            attempts++;

            // generate puzzle
            SudokuBoard original = generator.generatePuzzle();

            // minimal copy for solving

            boolean solved = solver.solve(original);

            if (!solved) {
                System.out.println("Solver failed after " + attempts + " attempts\n");

                System.out.println("=== Original puzzle ===");
                System.out.println(original);

                System.out.println("\n=== After solver exhaustion ===");

                System.out.println("\nTechniques used:");
                solver.getUsedMoves()
                        .forEach(c -> System.out.println(" - " + c.getSimpleName()));

                break;
            }
        }
    }
}
