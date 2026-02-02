package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    public enum Difficulty {
        EASY, MEDIUM, HARD, TEST
    }

    private final Difficulty difficulty;
    private final Random random = new Random();
    private final SudokuSolutionCounter counter = new SudokuSolutionCounter();

    public SudokuGenerator(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public SudokuBoard generatePuzzle() {
        SudokuBoard fullBoard = generateFullBoard();
        removeCellsWithUniqueness(fullBoard, getCellsToRemove());
        return fullBoard;
    }

    private SudokuBoard generateFullBoard() {
        SudokuBoard board = SudokuBoard.emptyBoard();
        if (!fillBoard(board, 0, 0)) {
            throw new IllegalStateException("Failed to generate a full board");
        }
        return board;
    }

    private boolean fillBoard(SudokuBoard board, int row, int col) {
        if (row == 9) return true;

        int nextRow = (col == 8) ? row + 1: row;
        int nextCol = (col == 8) ? 0 : col + 1;
        List<Integer> values = new ArrayList<>();
        for (int v = 1; v <= 9; v++) {
            values.add(v);
        }

        Collections.shuffle(values, random);
        for (int value : values) {
            Cell cell = board.getCell(row, col);
            if (board.isAllowed(cell, value)) {
                board.placeValue(cell, value);
                if (fillBoard(board, nextRow, nextCol)) {
                    return true;
                }
                board.clearValue(cell);
            }
        }
        return false;
    }

    private int getCellsToRemove() {
        return switch (difficulty) {
            case EASY -> 20;
            case MEDIUM -> 30;
            case HARD -> 40;
            case TEST -> 60;
        };
    }

    private void removeCells(SudokuBoard sudokuBoard, int cellsToRemove) {
        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; ++c) {
                positions.add(new int[]{r, c});
            }
        }

        Collections.shuffle(positions, random);
        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= cellsToRemove) break;
            int row = pos[0];
            int col = pos[1];

            Cell cell = sudokuBoard.getCell(row, col);
            if (!cell.isEmpty()) {
                sudokuBoard.clearValue(cell);
                removed++;
            }
        }
    }

    private void removeCellsWithUniqueness(SudokuBoard board, int targetRemovals) {

        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                positions.add(new int[]{r, c});

        Collections.shuffle(positions, random);

        int removed = 0;

        for (int[] pos : positions) {
            if (removed >= targetRemovals) break;

            int r = pos[0];
            int c = pos[1];
            Cell cell = board.getCell(r, c);

            if (cell.isEmpty()) continue;

            int backup = cell.getValue();
            board.clearValue(cell);

            // Count solutions (stop at 2)
            int solutions = counter.countSolutions(board, 2);

            if (solutions != 1) {
                // Revert removal
                board.placeValue(cell, backup);
            } else {
                removed++;
            }
        }
    }
}
