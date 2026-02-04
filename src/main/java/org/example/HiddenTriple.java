package org.example;

import java.util.*;

public class HiddenTriple implements LegalMove {

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
        return "Hidden Triple";
    }

    /* ---------------- Core logic ---------------- */

    private MoveResult checkUnit(List<Cell> unit) {

        // value -> cells it can go in
        Map<Integer, List<Cell>> positions = new HashMap<>();

        for (int v = 1; v <= 9; v++) {
            List<Cell> cells = new ArrayList<>();
            for (Cell cell : unit) {
                if (cell.isEmpty() && cell.getCandidates().contains(v)) {
                    cells.add(cell);
                }
            }
            // Hidden triple candidates must appear in 2–3 cells
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

                    // exactly three cells → hidden triple
                    if (union.size() != 3) continue;

                    Set<Integer> allowed = Set.of(v1, v2, v3);

                    for (Cell cell : union) {
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
        }

        return null;
    }
}
