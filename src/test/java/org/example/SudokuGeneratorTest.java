package org.example;

import org.junit.jupiter.api.Test;
import org.example.SudokuGenerator.Difficulty;
import static org.junit.jupiter.api.Assertions.*;

class SudokuGeneratorTest {

    @Test
    void testBoardGeneration_Easy() {
        Difficulty diff = Difficulty.EASY;
        SudokuGenerator gen = new SudokuGenerator(diff);
        SudokuBoard board = gen.generatePuzzle();

        assertEquals(20, board.countEmpty());
    }

    @Test
    void testBoardGeneration_Medium() {
        Difficulty diff = Difficulty.MEDIUM;
        SudokuGenerator gen = new SudokuGenerator(diff);
        SudokuBoard board = gen.generatePuzzle();
        assertEquals(30, board.countEmpty());
    }

    @Test
    void testBoardGeneration_Hard() {
        Difficulty diff = Difficulty.HARD;
        SudokuGenerator gen = new SudokuGenerator(diff);
        SudokuBoard board = gen.generatePuzzle();
        assertEquals(40, board.countEmpty());
    }
}