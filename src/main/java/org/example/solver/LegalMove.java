package org.example.solver;

import org.example.model.SudokuBoard;

public interface LegalMove {
    MoveResult apply(SudokuBoard board);

    String getName();

}
