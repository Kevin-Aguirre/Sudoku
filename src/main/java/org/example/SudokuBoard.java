package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuBoard {

    private final Cell[][] grid = new Cell[9][9];

    private final Set<Integer>[] rows = new HashSet[9];
    private final Set<Integer>[] cols = new HashSet[9];
    private final Set<Integer>[] boxes = new HashSet[9];

    public SudokuBoard(List<List<String>> input) {
        if (input.size() != 9) {
            throw new IllegalArgumentException("Input board must have 9 rows");
        }

        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for (int r = 0; r < 9; r++) {
            if (input.get(r).size() != 9) {
                throw new IllegalArgumentException("All rows must have 9 cells");
            }

            for (int c = 0; c < 9; c++) {
                String token = input.get(r).get(c);
                int value = token.equals(".") ? 0 : parseValue(token);

                Cell cell = new Cell(r, c, value);
                grid[r][c] = cell;

                if (value != 0) {
                    placeInitial(cell, value);
                }
            }
        }
    }

    public void initializeCandidates() {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                Cell cell = grid[row][col];
                cell.clearCandidates();
                if (cell.isEmpty()) {
                    for (int v = 1; v <= 9; ++v) {
                        if (isAllowed(cell, v)) {
                            cell.addCandidate(v);
                        }
                    }
                }
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Row and column must be in range 0–8");
        }
        return grid[row][col];
    }

    public int countEmpty() {
        int empty = 0;
        for (int r = 0; r < 9; ++r) {
            for (int c = 0; c < 9; ++c) {
                empty += getCell(r, c).getValue() == 0 ? 1 : 0;
            }
        }
        return empty;
    }

    public boolean isAllowed(Cell cell, int value) {
        return !rows[cell.getRow()].contains(value)
                && !cols[cell.getCol()].contains(value)
                && !boxes[cell.getBox()].contains(value);
    }

    public void placeValue(Cell cell, int value) {
        if (cell.isFixed()) {
            throw new IllegalStateException("Cannot modify fixed cell");
        }
        if (value < 1 || value > 9) {
            throw new IllegalArgumentException("Value must be 1–9");
        }
        if (!isAllowed(cell, value)) {
            throw new IllegalArgumentException("Move violates Sudoku rules");
        }

        setCellValue(cell, value);
    }

    public void clearValue(Cell cell) {
        if (cell.isFixed()) {
            throw new IllegalStateException("Cannot clear fixed cell");
        }
        setCellValue(cell, 0);
        initializeCandidates();
    }

    public static SudokuBoard emptyBoard() {
        List<List<String>> input = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            List<String> row = new ArrayList<>();
            for (int c = 0; c < 9; c++) {
                row.add(".");
            }
            input.add(row);
        }
        return new SudokuBoard(input);
    }

    private void placeInitial(Cell cell, int value) {
        if (!isAllowed(cell, value)) {
            throw new IllegalArgumentException(
                    "Invalid board: conflict at (" + cell.getRow() + "," + cell.getCol() + ")"
            );
        }
        addToTracking(cell, value);
    }

    private void setCellValue(Cell cell, int newValue) {
        if (!cell.isEmpty()) {
            removeFromTracking(cell, cell.getValue());
        }

        if (newValue != 0) {
            addToTracking(cell, newValue);
        }

        cell.setValue(newValue);

        if (newValue != 0) {
            updateCandidatesAfterPlacement(cell, newValue);
        }
    }

    private void updateCandidatesAfterPlacement(Cell placed, int value) {

        for (int i = 0; i < 9; i++) {
            grid[placed.getRow()][i].removeCandidate(value);
            grid[i][placed.getCol()].removeCandidate(value);
        }

        int br = (placed.getRow() / 3) * 3;
        int bc = (placed.getCol() / 3) * 3;

        for (int r = br; r < br + 3; r++) {
            for (int c = bc; c < bc + 3; c++) {
                grid[r][c].removeCandidate(value);
            }
        }
    }

    private void addToTracking(Cell cell, int value) {
        rows[cell.getRow()].add(value);
        cols[cell.getCol()].add(value);
        boxes[cell.getBox()].add(value);
    }

    private void removeFromTracking(Cell cell, int value) {
        rows[cell.getRow()].remove(value);
        cols[cell.getCol()].remove(value);
        boxes[cell.getBox()].remove(value);
    }

    private int parseValue(String s) {
        if (!s.matches("[1-9]")) {
            throw new IllegalArgumentException("Invalid symbol: " + s);
        }
        return Integer.parseInt(s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < 9; r++) {
            if (r % 3 == 0) {
                sb.append("+-------+-------+-------+\n");
            }

            for (int c = 0; c < 9; c++) {
                if (c % 3 == 0) sb.append("| ");

                int val = grid[r][c].getValue();
                sb.append(val == 0 ? ". " : val + " ");
            }
            sb.append("|\n");
        }
        sb.append("+-------+-------+-------+");

        return sb.toString();
    }

    public void printCandidates() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = grid[r][c];
                Set<Integer> candidates = cell.getCandidates();
                if (!candidates.isEmpty()) {
                    System.out.println("(" + r + ", " + c + ") -> " + candidates);
                }
            }
        }
    }


}
