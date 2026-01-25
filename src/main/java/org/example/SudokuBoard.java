package org.example;
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

        for (int i = 0; i < 9; ++i) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for (int r = 0; r < 9; ++r) {
            if (input.get(r).size() != 9) {
                throw new IllegalArgumentException("All rows in input board must have 9 cells.");
            }

            for (int c = 0; c < 9; ++c) {
                String token = input.get(r).get(c);
                int value = token.equals(".") ? 0 : parseValue(token);

                Cell cell = new Cell(r, c, value);
                grid[r][c] = cell;

                if (!cell.isEmpty()) {
                    placeInitial(cell, value);
                }

            }
        }
    }

    public void placeInitial(Cell cell, int value) {
        if (!isAllowed(cell, value)) {
            throw new IllegalArgumentException(
                    "Invalid board: conflict at (" + cell.getRow() + "," + cell.getCol() + ")"
            );
        }

        rows[cell.getRow()].add(value);
        cols[cell.getCol()].add(value);
        boxes[cell.getBox()].add(value);
    }

    public boolean isAllowed(Cell cell, int value) {
        return !rows[cell.getRow()].contains(value)
                && !cols[cell.getCol()].contains(value)
                && !boxes[cell.getBox()].contains(value);
    }

    private int parseValue(String s) {
        if (!s.matches("[1-9]"))
            throw new IllegalArgumentException("Invalid symbol: " + s);
        return Integer.parseInt(s);
    }

}

