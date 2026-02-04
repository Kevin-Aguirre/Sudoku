package org.example;

import java.util.ArrayList;
import java.util.List;

public class HiddenSingle implements LegalMove {


    @Override
    public MoveResult apply(SudokuBoard board) {

        for (int unit = 0; unit < 9; unit++) {
            MoveResult r;

            r = checkUnit(board, board.getRow(unit));
            if (r != null) return r;

            r = checkUnit(board, board.getCol(unit));
            if (r != null) return r;

            r = checkUnit(board, board.getBox(unit));
            if (r != null) return r;
        }

        return null;
    }


    @Override
    public String getName() {
        return "Hidden Single";
    }

    private MoveResult checkUnit(SudokuBoard board, List<Cell> unit) {

        for (int value = 1; value <= 9; value++) {

            Cell onlySpot = null;

            for (Cell cell : unit) {
                if (cell.isEmpty() && cell.getCandidates().contains(value)) {

                    if (onlySpot != null) {
                        onlySpot = null;
                        break; // value appears in multiple places
                    }
                    onlySpot = cell;
                }
            }

            if (onlySpot != null) {
                board.placeValue(onlySpot, value);
                onlySpot.clearCandidates();

                return MoveResult.placement(
                        getName(),
                        onlySpot,
                        value
                );
            }
        }

        return null;
    }
}
