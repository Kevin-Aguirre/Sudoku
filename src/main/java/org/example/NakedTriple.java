package org.example;

import java.util.*;

public class NakedTriple implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        for (int unit = 0; unit < 9; ++unit) {
            if (applyToUnit(board.getRow(unit))) return true;
            if (applyToUnit(board.getCol(unit))) return true;
            if (applyToUnit(board.getBox(unit))) return true;
        }
        return false;
    }

    public boolean applyToUnit(List<Cell> cells) {

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

                    if (union.size() == 3) { // naked triple found
                        for (Cell other : cells) {
                            if (other.isEmpty() &&
                                    other != targets.get(i) &&
                                    other != targets.get(j) &&
                                    other != targets.get(k)) {

                                for (int v : union) {
                                    if (other.removeCandidate(v)) {
                                        System.out.println("Applying " + getName());
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Naked Triple";
    }

    @Override
    public String getMessage() {
        return "Removing candidates using naked triple";
    }
}
