package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class HiddenSingleTest {
    @Test
    void testHiddenSingleWithProvidedBoard() {
        SudokuBoard board = TestBoards.hiddenSingleTest();
        board.initializeCandidates();
        HiddenSingle strategy = new HiddenSingle();
        boolean applied = strategy.apply(board);
        assertTrue(applied, "Strategy should find at least one Hidden Single on this board");
    }
}