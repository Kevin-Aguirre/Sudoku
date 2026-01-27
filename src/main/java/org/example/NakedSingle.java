package org.example;

public class NakedSingle implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);

                if (cell.isEmpty() && cell.getCandidates().size() == 1) {
                    int value = cell.getCandidates().iterator().next();
                    board.placeValue(cell, value);
                    return true; // exactly one logical move
                }
            }
        }
        return false;
    }
}
