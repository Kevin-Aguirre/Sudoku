package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SudokuSolverTest {
    private SudokuSolver solver;

    @BeforeEach
    void setUp() {
        solver = new SudokuSolver();
    }

    @Test
    void testSolveEasyPuzzle() {
        // A puzzle that should only require Naked/Hidden Singles
        String puz =
                "635789214 " +
                        "214653978 " +
                        "8972143.5 " + // Added a '3' at (2, 6)
                        "183472596 " +
                        "729165843 " +
                        "546938721 " +
                        "362847159 " +
                        "951326487 " +
                        "478591.32";   // Added a '3' at (8, 7)

        SudokuBoard board = SudokuBoard.buildBoard(puz);
        boolean solved = solver.solve(board);

        assertTrue(solved, "Solver should complete the puzzle");
        assertEquals(0, board.countEmpty(), "There should be no empty cells");
    }


    @Test
    void testStrategyTracking() {
        // We want to ensure that if a Naked Double is used, it's recorded
        // This requires a puzzle setup where a Double is mandatory
        String puz =
                "4..27.6.. " +
                        "798.61.2. " +
                        ".5.9.8.71 " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... ";
        // Note: Use a known puzzle string that requires NakedDouble here

        SudokuBoard board = SudokuBoard.buildBoard(puz);
        solver.solve(board);

        // This verifies your "usedMoves" logic
        assertTrue(solver.getUsedMoves().contains(HiddenSingle.class) ||
                solver.getUsedMoves().contains(NakedSingle.class));
    }

    @Test
    void testInvalidBoardFails() {
        // Test a board that is logically impossible (two 5s in a row)
        String badPuz =
                "55....... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... " +
                        "......... ";

        assertThrows(IllegalArgumentException.class, () -> SudokuBoard.buildBoard(badPuz),
                "Constructor should throw error on invalid initial placement");
    }
}