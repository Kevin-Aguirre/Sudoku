package org.example;

import java.util.*;

public class HiddenTriple implements LegalMove {

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
        return "Hidden Triple";
    }

    @Override
    public String getMessage() {
        return "Restricting candidates using hidden triple";
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
            if (cells.size() >= 2 && cells.size() <= 3) {
                positions.put(v, cells);
            }
        }

        List<Integer> values = new ArrayList<>(positions.keySet());

        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                for (int k = j + 1; k < values.size(); k++) {

                    int v1 = values.get(i);
                    int v2 = values.get(j);
                    int v3 = values.get(k);

                    Set<Cell> union = new HashSet<>();
                    union.addAll(positions.get(v1));
                    union.addAll(positions.get(v2));
                    union.addAll(positions.get(v3));

                    if (union.size() == 3) {
                        Set<Integer> allowed = Set.of(v1, v2, v3);

                        for (Cell cell : union) {
                            if (cell.restrictTo(allowed)) {
                                return true;
                            }
                        }
                    }
                }
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

        for (int r = br; r < br + 3; r++)
            for (int c = bc; c < bc + 3; c++)
                box.add(board.getCell(r, c));

        return box;
    }
}
