package org.example;

import java.util.List;

public class SudokuSolver {

    /*
    *
    * This must be implemented from most trivial to least.
    * */
    private final List<LegalMove> moves = List.of(
            new NakedSingle(),
            new HiddenSingle()

    );

    public boolean solve(SudokuBoard board) {
        board.initializeCandidates();

        boolean progress;
        do {
            progress = false;
            for (LegalMove move : moves) {
                if (move.apply(board)) {
                    progress = true;
                    break; // restart move list
                }
            }
        } while (progress);

        return board.countEmpty() == 0;
    }

    public boolean solveStep(SudokuBoard board) {
        board.initializeCandidates();

        for (LegalMove move : moves) {
            if (move.apply(board)) {
                return true;
            }
        }
        return false;
    }
}
