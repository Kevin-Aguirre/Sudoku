package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SudokuSolver {

    private final List<MoveResult> appliedMoves = new ArrayList<>();

    private final List<LegalMove> moves = List.of(
            new NakedSingle(),
            new HiddenSingle(),
            new NakedDouble(),
            new HiddenDouble(),
            new NakedTriple(),
            new HiddenTriple(),
            new XWing()
    );

    public boolean solve(SudokuBoard board) {
        appliedMoves.clear();
        board.initializeCandidates();

        boolean progress;
        do {
            progress = false;
            for (LegalMove move : moves) {
                MoveResult result = move.apply(board);
                if (result != null) {
                    appliedMoves.add(result);
                    progress = true;
                    break; // restart move list after a successful move
                }
            }
        } while (progress);

        return board.countEmpty() == 0;
    }

    public boolean solveStep(SudokuBoard board) {
        System.out.println(board);
        board.printDetailedGrid();
        for (LegalMove move : moves) {
            MoveResult result = move.apply(board);
            if (result != null) {
                appliedMoves.add(result);
                return true;
            }
        }
        return false;
    }

    public List<MoveResult> getAppliedMoves() {
        return List.copyOf(appliedMoves);
    }

    public Set<Class<? extends MoveResult>> getUsedTechniques() {
        return appliedMoves.stream()
                .map(MoveResult::getClass)
                .collect(Collectors.toSet());
    }
}
