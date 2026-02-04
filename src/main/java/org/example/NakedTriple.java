package org.example;

import java.util.*;

public class NakedTriple implements LegalMove {

    @Override
    public MoveResult apply(SudokuBoard board) {

        for (int unit = 0; unit < 9; unit++) {
            MoveResult r;

            r = applyToUnit(board.getRow(unit));
            if (r != null) return r;

            r = applyToUnit(board.getCol(unit));
            if (r != null) return r;

            r = applyToUnit(board.getBox(unit));
            if (r != null) return r;
        }

        return null;
    }

    public MoveResult applyToUnit(List<Cell> cells) {

        List<Cell> targets = new ArrayList<>();
        for (Cell c : cells) {
            if (c.isEmpty() && c.getCandidates().size() >= 2 && c.getCandidates().size() <= 3) {
                targets.add(c);
            }
        }

        int n = targets.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {

                    Set<Integer> union = new HashSet<>();
                    union.addAll(targets.get(i).getCandidates());
                    union.addAll(targets.get(j).getCandidates());
                    union.addAll(targets.get(k).getCandidates());

                    if (union.size() != 3) continue;
                    List<String> details = new ArrayList<>();

                    for (Cell other : cells) {
                        if (!other.isEmpty()) continue;
                        if (other == targets.get(i)
                                || other == targets.get(j)
                                || other == targets.get(k)) continue;

                        for (int v : union) {
                            if (other.removeCandidate(v)) {
                                details.add(
                                        "Removed " + v + " from ("
                                                + other.getRow() + ","
                                                + other.getCol() + ")"
                                );
                            }
                        }
                    }

                    if (!details.isEmpty()) {
                        return MoveResult.composite(
                                getName(),
                                "Naked triple eliminates candidates",
                                details
                        );
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "Naked Triple";
    }

}
