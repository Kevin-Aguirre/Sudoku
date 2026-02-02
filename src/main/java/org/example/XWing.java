package org.example;

import java.util.*;

public class XWing implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {
        // Try row-based X-Wing
        for (int value = 1; value <= 9; value++) {
            if (xWingRows(board, value)) {
                System.out.println("Applying " + getName() + " (row-based) on " + value);
                return true;
            }
        }

        // Try column-based X-Wing
        for (int value = 1; value <= 9; value++) {
            if (xWingCols(board, value)) {
                System.out.println("Applying " + getName() + " (col-based) on " + value);
                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "X-Wing";
    }

    @Override
    public String getMessage() {
        return "Eliminating candidates using X-Wing";
    }

    /* ================= ROW-BASED ================= */

    private boolean xWingRows(SudokuBoard board, int value) {
        Map<Integer, Set<Integer>> rowToCols = new HashMap<>();

        // Step 1: map rows -> columns where value appears
        for (int r = 0; r < 9; r++) {
            Set<Integer> cols = new HashSet<>();
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isEmpty() && cell.getCandidates().contains(value)) {
                    cols.add(c);
                }
            }
            if (cols.size() == 2) {
                rowToCols.put(r, cols);
            }
        }

        List<Integer> rows = new ArrayList<>(rowToCols.keySet());
        boolean changed = false;

        // Find matching row pairs
        for (int i = 0; i < rows.size(); i++) {
            for (int j = i + 1; j < rows.size(); j++) {
                int r1 = rows.get(i);
                int r2 = rows.get(j);
                Set<Integer> c1c2 = rowToCols.get(r1);

                if (!c1c2.equals(rowToCols.get(r2))) continue;

                // Eliminate candidate from all other rows in those columns
                for (int c : c1c2) {
                    for (int r = 0; r < 9; r++) {
                        if (r == r1 || r == r2) continue;
                        Cell cell = board.getCell(r, c);
                        if (cell.isEmpty()) {
                            changed |= cell.removeCandidate(value);
                        }
                    }
                }

                if (changed) return true;
            }
        }
        return false;
    }

    /* ================= COLUMN-BASED ================= */

    private boolean xWingCols(SudokuBoard board, int value) {
        Map<Integer, Set<Integer>> colToRows = new HashMap<>();

        // Map columns -> rows where candidate appears
        for (int c = 0; c < 9; c++) {
            Set<Integer> rows = new HashSet<>();
            for (int r = 0; r < 9; r++) {
                Cell cell = board.getCell(r, c);
                if (cell.isEmpty() && cell.getCandidates().contains(value)) {
                    rows.add(r);
                }
            }
            if (rows.size() == 2) {  // candidate appears exactly twice in column
                colToRows.put(c, rows);
            }
        }

        List<Integer> cols = new ArrayList<>(colToRows.keySet());
        boolean changed = false;

        // Find matching column pairs
        for (int i = 0; i < cols.size(); i++) {
            for (int j = i + 1; j < cols.size(); j++) {
                int c1 = cols.get(i);
                int c2 = cols.get(j);
                Set<Integer> r1r2 = colToRows.get(c1);

                if (!r1r2.equals(colToRows.get(c2))) continue;

                // Eliminate candidate from all other columns in those rows
                for (int r : r1r2) {
                    for (int c = 0; c < 9; c++) {
                        if (c == c1 || c == c2) continue;
                        Cell cell = board.getCell(r, c);
                        if (cell.isEmpty()) {
                            changed |= cell.removeCandidate(value);
                        }
                    }
                }

                if (changed) return true;
            }
        }

        return false;
    }
}
