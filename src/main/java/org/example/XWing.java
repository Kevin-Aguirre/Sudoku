package org.example;

import java.util.*;

public class XWing implements LegalMove {

    @Override
    public MoveResult apply(SudokuBoard board) {

        for (int value = 1; value <= 9; value++) {
            MoveResult r = xWingRows(board, value);
            if (r != null) return r;

            r = xWingCols(board, value);
            if (r != null) return r;
        }

        return null;
    }

    @Override
    public String getName() {
        return "X-Wing";
    }

    private MoveResult xWing(SudokuBoard board, int value, boolean horizontal) {
        Map<Integer, Set<Integer>> primaryToSecondary = new HashMap<>();

        for (int p = 0; p < 9; p++) {
            Set<Integer> secondaries = new HashSet<>();
            for (int s = 0; s < 9; s++) {
                // Swap coordinates based on orientation
                Cell cell = horizontal ? board.getCell(p, s) : board.getCell(s, p);

                if (cell.isEmpty() && cell.getCandidates().contains(value)) {
                    secondaries.add(s);
                }
            }
            if (secondaries.size() == 2) {
                primaryToSecondary.put(p, secondaries);
            }
        }

        List<Integer> primaries = new ArrayList<>(primaryToSecondary.keySet());

        for (int i = 0; i < primaries.size(); i++) {
            for (int j = i + 1; j < primaries.size(); j++) {
                int p1 = primaries.get(i);
                int p2 = primaries.get(j);
                Set<Integer> secondaries = primaryToSecondary.get(p1);

                if (!secondaries.equals(primaryToSecondary.get(p2))) continue;

                // Eliminate candidates from other "primary" units in these secondary positions
                for (int s : secondaries) {
                    for (int p = 0; p < 9; p++) {
                        if (p == p1 || p == p2) continue;

                        Cell cell = horizontal ? board.getCell(p, s) : board.getCell(s, p);

                        if (cell.isEmpty() && cell.removeCandidate(value)) {
                            return MoveResult.candidateRemoved(getName(), cell, value);
                        }
                    }
                }
            }
        }
        return null;
    }

    private MoveResult xWingRows(SudokuBoard board, int value) {
        return xWing(board, value, true);
    }

    private MoveResult xWingCols(SudokuBoard board, int value) {
        return xWing(board, value, false);
    }
}
