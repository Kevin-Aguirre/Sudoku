package org.example;
import java.util.HashSet;
import java.util.Set;
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
        Set<Integer> solvedValues = new HashSet<>();
        for (Cell cell : unit) {
            if (!cell.isEmpty()) solvedValues.add(cell.getValue());
        }
        for (int value = 1; value <= 9; value++) {
            if (solvedValues.contains(value)) continue;
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
                return MoveResult.placement(getName(), onlySpot, value);
            }
        }

        return null;
    }
}
