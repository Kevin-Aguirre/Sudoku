package org.example;

import java.util.List;
import java.util.Set;

public class MoveResult {

    public enum Type {
        PLACEMENT,
        CANDIDATE_REMOVAL,
        CANDIDATE_RESTRICTION
    }

    private final String technique;
    private final Type type;
    private final Cell cell;
    private final Integer value;          // for placements / removals
    private final Set<Integer> values;    // for restriction
    private final String explanation;

    private MoveResult(
            String technique,
            Type type,
            Cell cell,
            Integer value,
            Set<Integer> values,
            String explanation
    ) {
        this.technique = technique;
        this.type = type;
        this.cell = cell;
        this.value = value;
        this.values = values;
        this.explanation = explanation;
    }

    /* --------- Factory helpers --------- */

    public static MoveResult placement(String tech, Cell cell, int value) {
        return new MoveResult(
                tech,
                Type.PLACEMENT,
                cell,
                value,
                null,
                "Placed " + value + " at (" + cell.getRow() + "," + cell.getCol() + ")"
        );
    }

    public static MoveResult composite(
            String technique,
            String headline,
            List<String> details
    ) {
        return new MoveResult(
                technique,
                Type.CANDIDATE_REMOVAL,
                null,
                null,
                null,
                headline + ": " + String.join(", ", details)
        );
    }


    public static MoveResult candidateRemoved(String tech, Cell cell, int value) {
        return new MoveResult(
                tech,
                Type.CANDIDATE_REMOVAL,
                cell,
                value,
                null,
                "Removed " + value + " from (" + cell.getRow() + "," + cell.getCol() + ")"
        );
    }

    public static MoveResult restrictedTo(String tech, Cell cell, Set<Integer> allowed) {
        return new MoveResult(
                tech,
                Type.CANDIDATE_RESTRICTION,
                cell,
                null,
                allowed,
                "Restricted (" + cell.getRow() + "," + cell.getCol() + ") to " + allowed
        );
    }

    @Override
    public String toString() {
        return "[" + technique + "] " + explanation;
    }
}
