package org.example;

import java.util.ArrayList;
import java.util.List;

public class HiddenSingle implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        // rows
        for (int r = 0; r < 9; r++) {
            if (checkUnit(board, getRow(board, r))) {
                System.out.println("Applying " + getName());
                return true;
            };
        }

        // columns
        for (int c = 0; c < 9; c++) {
            if (checkUnit(board, getCol(board, c))) {
                System.out.println("Applying " + getName());
                return true;
            };
        }

        // boxes
        for (int b = 0; b < 9; b++) {
            if (checkUnit(board, getBox(board, b))) {
                System.out.println("Applying " + getName());
                return true;
            };
        }

        return false;
    }

    @Override
    public String getName() {
        return "Hidden Single";
    }

    @Override
    public String getMessage() {
        return "";
    }

    /* ---------------- Core logic ---------------- */

    private boolean checkUnit(SudokuBoard board, List<Cell> unit) {

        for (int value = 1; value <= 9; value++) {

            Cell onlySpot = null;

            for (Cell cell : unit) {
                if (cell.isEmpty() && cell.getCandidates().contains(value)) {

                    if (onlySpot != null) {
                        onlySpot = null; // more than one → not hidden single
                        break;
                    }
                    onlySpot = cell;
                }
            }

            if (onlySpot != null) {
                board.placeValue(onlySpot, value);
                return true;
            }
        }

        return false;
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

        for (int r = br; r < br + 3; r++) {
            for (int c = bc; c < bc + 3; c++) {
                box.add(board.getCell(r, c));
            }
        }
        return box;
    }
}
