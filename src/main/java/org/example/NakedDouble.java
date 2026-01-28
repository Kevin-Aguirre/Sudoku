package org.example;

import java.util.*;

public class NakedDouble implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {

        // rows
        for (int r = 0; r < 9; r++) {
            if (applyToUnit(getRow(board, r))) return true;
        }

        // columns
        for (int c = 0; c < 9; c++) {
            if (applyToUnit(getCol(board, c))) return true;
        }

        // boxes
        for (int b = 0; b < 9; b++) {
            if (applyToUnit(getBox(board, b))) return true;
        }

        return false;
    }

    private boolean applyToUnit(List<Cell> cells) {
        Map<Set<Integer>, List<Cell>> groups = new HashMap<>();

        for (Cell cell : cells) {
            if (cell.isEmpty() && cell.getCandidates().size() == 2) {
                Set<Integer> key = new HashSet<>(cell.getCandidates());
                groups.computeIfAbsent(key, k -> new ArrayList<>()).add(cell);
            }
        }

        for (var entry : groups.entrySet()) {
            Set<Integer> pair = entry.getKey();
            List<Cell> matched = entry.getValue();

            if (matched.size() == 2) { // naked double found
                for (Cell other : cells) {
                    if (!matched.contains(other) && other.isEmpty()) {
                        for (int v : pair) {
                            if (other.removeCandidate(v)) {
                                System.out.println("Applying " + getName());
                                return true;
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
        return "Naked Double";
    }

    @Override
    public String getMessage() {
        return "Removing candidates using naked double";
    }
}
