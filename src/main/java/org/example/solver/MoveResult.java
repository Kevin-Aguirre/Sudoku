package org.example.solver;

import org.example.model.Cell;

import java.util.List;
import java.util.Set;

public record MoveResult(
        String technique,
        Type type,
        Cell cell,
        Integer value,
        Set<Integer> values,
        String explanation
) {
    public String getTechnique() {
        return technique;
    }

    public enum Type { PLACEMENT, CANDIDATE_REMOVAL, CANDIDATE_RESTRICTION, COMPOSITE }

    public static MoveResult placement(String tech, Cell cell, int value) {
        return new MoveResult(tech, Type.PLACEMENT, cell, value, null,
                "Placed " + value + " at (" + cell.getRow() + "," + cell.getCol() + ")");
    }

    public static MoveResult restrictedTo(String tech, Cell cell, Set<Integer> allowed) {
        return new MoveResult(tech, Type.CANDIDATE_RESTRICTION, cell, null, allowed,
                "Restricted (" + cell.getRow() + "," + cell.getCol() + ") to " + allowed);
    }

    public static MoveResult composite(String tech, String headline, List<String> details) {
        return new MoveResult(tech, Type.COMPOSITE, null, null, null,
                headline + ": " + String.join(", ", details));
    }

    @Override
    public String toString() {
        return "[" + technique + "] " + explanation;
    }
}