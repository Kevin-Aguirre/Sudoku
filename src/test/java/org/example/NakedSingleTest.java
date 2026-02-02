package org.example;

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
        boolean applied = strategy.apply(board);

        // Assertions
        assertTrue(applied, "Naked Single strategy should return true");
        assertEquals(2, board.getCell(0, 8).getValue(), "The value 2 should be placed at (0, 8)");
    }
}