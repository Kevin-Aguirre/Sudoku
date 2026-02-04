package org.example;

import java.util.*;

public class HiddenDouble implements LegalMove {

    @Override
    public MoveResult apply(SudokuBoard board) {

        for (int unit = 0; unit < 9; unit++) {
            MoveResult r;

            r = checkUnit(board.getRow(unit));
            if (r != null) return r;

            r = checkUnit(board.getCol(unit));
            if (r != null) return r;

            r = checkUnit(board.getBox(unit));
            if (r != null) return r;
        }

        return null;
    }

    @Override
    public String getName() {
        return "Hidden Double";
    }

    /* ---------------- Core logic ---------------- */

    private MoveResult checkUnit(List<Cell> unit) {

        // value -> cells it appears in
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

                if (!sameCells(a, b)) continue;

                Set<Integer> allowed = Set.of(v1, v2);

                for (Cell cell : a) {
                    if (cell.restrictTo(allowed)) {
                        return MoveResult.restrictedTo(
                                getName(),
                                cell,
                                allowed
                        );
                    }
                }
            }
        }

        return null;
    }

    private boolean sameCells(List<Cell> a, List<Cell> b) {
        return a.size() == b.size() && a.containsAll(b);
    }
}
