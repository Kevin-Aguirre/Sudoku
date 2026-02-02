package org.example;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SudokuSolver {
    private final Set<Class<? extends LegalMove>> usedMoves = new HashSet<>();

    /*
    * This must be implemented from most trivial to least.
    * */
    private final List<LegalMove> moves = List.of(
            new NakedSingle(),
            new HiddenSingle(),
            new NakedDouble(),
            new HiddenDouble(),
            new NakedTriple(),
            new HiddenTriple()
    );

    public boolean solve(SudokuBoard board) {
        usedMoves.clear();
        board.initializeCandidates();

        boolean progress;
        do {
            progress = false;
            for (LegalMove move : moves) {
                if (move.apply(board)) {
                    usedMoves.add(move.getClass());
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

    public Set<Class<? extends LegalMove>> getUsedMoves() {
        return Set.copyOf(usedMoves);
    }

}
