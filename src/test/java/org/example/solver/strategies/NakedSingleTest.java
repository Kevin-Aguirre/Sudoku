package org.example.solver.strategies;

import org.example.model.SudokuBoard;
import org.example.solver.MoveResult;
import org.example.solver.TestBoards;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NakedSingleTest {
    @Test
    void testNakedSingleWithFixedBoard() {
        SudokuBoard board = TestBoards.nakedSingleTest();
        board.initializeCandidates();

        Set<Integer> candidates = board.getCell(0, 8).getCandidates();
        assertEquals(1, candidates.size(), "Cell (0, 8) should have exactly one candidate");
        assertTrue(candidates.contains(2), "The candidate should be 2");

        // Apply the strategy
        NakedSingle strategy = new NakedSingle();
        MoveResult result = strategy.apply(board); // Change boolean to MoveResult

        // Assertions
        assertNotNull(result, "Naked Single strategy should return a MoveResult, not null");
        assertEquals("Naked Single", result.getTechnique());
        assertEquals(2, board.getCell(0, 8).getValue(), "The value 2 should be placed at (0, 8)");
    }
}