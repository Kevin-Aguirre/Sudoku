package org.example;

import java.util.*;

public class HiddenDouble implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        for (int r = 0; r < 9; r++)
            if (checkUnit(board, getRow(board, r))) {
                System.out.println("Applying " + getName());
                return true;
            }

        for (int c = 0; c < 9; c++)
            if (checkUnit(board, getCol(board, c))) {
                System.out.println("Applying " + getName());
                return true;
            }

        for (int b = 0; b < 9; b++)
            if (checkUnit(board, getBox(board, b))) {
                System.out.println("Applying " + getName());
                return true;
            }

        return false;
    }

    @Override
    public String getName() {
        return "Hidden Double";
    }

    @Override
    public String getMessage() {
        return "Restricting candidates using hidden double";
    }

    /* ---------------- Core logic ---------------- */

    private boolean checkUnit(SudokuBoard board, List<Cell> unit) {

        Map<Integer, List<Cell>> positions = new HashMap<>();

        for (int v = 1; v <= 9; v++) {
            List<Cell> cells = new ArrayList<>();
            for (Cell cell : unit) {
                if (cell.isEmpty() && cell.getCandidates().contains(v)) {
                    cells.add(cell);
                }
            }
            if (cells.size() == 2) {
                positions.put(v, cells);
            }
        }

        List<Integer> values = new ArrayList<>(positions.keySet());

        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {

                int v1 = values.get(i);
                int v2 = values.get(j);

                List<Cell> a = positions.get(v1);
                List<Cell> b = positions.get(v2);

                if (sameCells(a, b)) {
                    Set<Integer> allowed = Set.of(v1, v2);

                    for (Cell cell : a) {
                        if (cell.restrictTo(allowed)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean sameCells(List<Cell> a, List<Cell> b) {
        return a.containsAll(b) && b.containsAll(a);
    }

    /* ---------------- Unit helpers ---------------- */

    private List<Cell> getRow(SudokuBoard board, int r) {
        List<Cell> row = new ArrayList<>(9);
        for (int c = 0; c < 9; c++) row.add(board.getCell(r, c));
        return row;
    }

    private List<Cell> getCol(SudokuBoard board, int c) {
        List<Cell> col = new ArrayList<>(9);
        for (int r = 0; r < 9; r++) col.add(board.getCell(r, c));
        return col;
    }

    private List<Cell> getBox(SudokuBoard board, int b) {
        List<Cell> box = new ArrayList<>(9);
        int br = (b / 3) * 3;
        int bc = (b % 3) * 3;

        for (int r = br; r < br + 3; r++)
            for (int c = bc; c < bc + 3; c++)
                box.add(board.getCell(r, c));

        return box;
    }
}
