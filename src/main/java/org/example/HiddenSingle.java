package org.example;

import java.util.ArrayList;
import java.util.List;

public class HiddenSingle implements LegalMove {

    @Override
    public boolean apply(SudokuBoard board) {
        for (int unit = 0; unit < 9; unit++) {
            if (checkUnit(board, board.getRow(unit))) return true;
            if (checkUnit(board, board.getCol(unit))) return true;
            if (checkUnit(board, board.getBox(unit))) return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Hidden Single";
    }

    @Override
    public String getMessage() {
        return "";
    }

    private boolean checkUnit(SudokuBoard board, List<Cell> unit) {

        for (int value = 1; value <= 9; value++) {

            Cell onlySpot = null;

            for (Cell cell : unit) {
                if (cell.isEmpty() && cell.getCandidates().contains(value)) {

                    if (onlySpot != null) {
                        onlySpot = null;
                        break;
                    }
                    onlySpot = cell;
                }
            }

            if (onlySpot != null) {
                board.placeValue(onlySpot, value);
                onlySpot.clearCandidates();
                return true;
            }
        }

        return false;
    }

}
