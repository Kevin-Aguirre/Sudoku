package org.example.solver.strategies;

import org.example.model.Cell;
import org.example.solver.LegalMove;
import org.example.solver.MoveResult;
import org.example.model.SudokuBoard;

public class NakedSingle implements LegalMove {

    @Override
    public MoveResult apply(SudokuBoard board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);

                if (cell.isEmpty() && cell.getCandidates().size() == 1) {
                    int value = cell.getCandidates().iterator().next();
                    board.placeValue(cell, value);
                    return MoveResult.placement(getName(), cell, value);
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "Naked Single";
    }

}
