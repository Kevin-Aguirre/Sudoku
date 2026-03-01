package org.example.solver.strategies;

import org.example.model.SudokuBoard;
import org.example.solver.MoveResult;
import org.example.solver.strategies.HiddenSingle;
import org.example.solver.TestBoards;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class HiddenSingleTest {
    @Test
    void testHiddenSingleWithProvidedBoard() {
        SudokuBoard board = TestBoards.hiddenSingleTest();
        board.initializeCandidates();
        HiddenSingle strategy = new HiddenSingle();
        MoveResult result = strategy.apply(board);
        assertNotNull(result, "Strategy should find at least one Hidden Single on this board");
    }
}