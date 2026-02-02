package org.example;

public class SudokuSolutionCounter {

    private int solutions;

    public int countSolutions(SudokuBoard board, int limit) {
        solutions = 0;
        solve(board, limit);
        return solutions;
    }

    private void solve(SudokuBoard board, int limit) {
        if (solutions >= limit) return;

        int row = -1, col = -1;
        boolean found = false;

        for (int r = 0; r < 9 && !found; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getCell(r, c).isEmpty()) {
                    row = r;
                    col = c;
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            solutions++;
            return;
        }

        Cell cell = board.getCell(row, col);

        for (int v = 1; v <= 9; v++) {
            if (board.isAllowed(cell, v)) {
                board.placeValue(cell, v);
                solve(board, limit);
                board.clearValue(cell);

                if (solutions >= limit) return;
            }
        }
    }
}
