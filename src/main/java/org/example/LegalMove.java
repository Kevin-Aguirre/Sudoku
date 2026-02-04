package org.example;

public interface LegalMove {
    MoveResult apply(SudokuBoard board);

    String getName();

}
