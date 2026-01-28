package org.example;

import java.util.*;

public class NakedTriple implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        for (int r = 0; r < 9; r++)
            if (applyToUnit(getRow(board, r))) return true;

        for (int c = 0; c < 9; c++)
            if (applyToUnit(getCol(board, c))) return true;

        for (int b = 0; b < 9; b++)
            if (applyToUnit(getBox(board, b))) return true;

        return false;
    }

    private boolean applyToUnit(List<Cell> cells) {

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

    private List<Cell> getRow(SudokuBoard b, int r) {
        List<Cell> list = new ArrayList<>();
        for (int c = 0; c < 9; c++) list.add(b.getCell(r, c));
        return list;
    }

    private List<Cell> getCol(SudokuBoard b, int c) {
        List<Cell> list = new ArrayList<>();
        for (int r = 0; r < 9; r++) list.add(b.getCell(r, c));
        return list;
    }

    private List<Cell> getBox(SudokuBoard b, int box) {
        List<Cell> list = new ArrayList<>();
        int sr = (box / 3) * 3;
        int sc = (box % 3) * 3;

        for (int r = sr; r < sr + 3; r++)
            for (int c = sc; c < sc + 3; c++)
                list.add(b.getCell(r, c));

        return list;
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
