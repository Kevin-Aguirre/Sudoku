package org.example;

import java.util.*;

public class HiddenTriple implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        for (int unit = 0; unit < 9; unit++) {
            if (checkUnit(board, board.getRow(unit))) return true;
            if (checkUnit(board, board.getCol(unit))) return true;
            if (checkUnit(board, board.getBox(unit))) return true;
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
            if (!cells.isEmpty()) {
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
                        boolean changed = false;

                        for (Cell cell : union) {
                            // restrict candidates to just the triple
                            if (cell.restrictTo(allowed)) {
                                changed = true;
                            }
                        }

                        if (changed) {
                            System.out.println("Applying " + getName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
