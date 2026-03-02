package org.example.solver;

import org.example.model.SudokuBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SudokuSolverTest {
    private SudokuSolver solver;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        solver = new SudokuSolver();
        board = new SudokuBoard();
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

    @Test
    void testSolveStep_TriggersNakedSingle() {
        /* Set up a "Naked Single" scenario:
           In a row, fill numbers 1-8. The last cell MUST be 9.
        */
        for (int c = 0; c < 8; c++) {
            board.placeValue(board.getCell(0, c), c + 1);
        }
        board.initializeCandidates();

        // The cell at (0, 8) now only has one possible candidate: 9
        boolean result = solver.solveStep(board);

        assertTrue(result, "Solver should find a move");
        assertEquals(9, board.getCell(0, 8).getValue(), "Naked Single should have placed 9");

        // Verify move was logged
        List<MoveResult> history = solver.getAppliedMoves();
        assertEquals(1, history.size(), "There should be exactly one move in the history");
        assertTrue(history.get(0).toString().contains("Naked Single"), "The move type should be logged");
    }

    @Test
    void testGetAppliedMoves_IsImmutable() {
        // Setup a move
        board.placeValue(board.getCell(0, 0), 1);
        board.initializeCandidates();
        solver.solveStep(board);

        List<MoveResult> moves = solver.getAppliedMoves();

        // Attempt to modify the list (should fail because of List.copyOf)
        assertThrows(UnsupportedOperationException.class, moves::clear, "The returned list should be an immutable copy");
    }

    @Test
    void testSolveStep_ReturnsFalseWhenStuck() {
        // A blank board with no candidates initialized or no obvious moves
        // If the solver can't find any of the strategies in its list:
        board.initializeCandidates();

        // If we fill a board randomly such that no singles/doubles exist
        // (For simplicity, an empty board usually returns false if no progress can be made)
        boolean result = solver.solveStep(board);

        assertFalse(result, "Solver should return false if no legal moves are found");
    }


}