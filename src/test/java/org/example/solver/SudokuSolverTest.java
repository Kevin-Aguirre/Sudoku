package org.example.solver;

import org.example.model.SudokuBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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