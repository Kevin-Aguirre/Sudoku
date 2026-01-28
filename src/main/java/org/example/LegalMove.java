package org.example;

public interface LegalMove {
    boolean apply(SudokuBoard board);

    String getName();

    String getMessage();
}
