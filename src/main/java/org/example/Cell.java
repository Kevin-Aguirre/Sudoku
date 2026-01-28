package org.example;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// TODO - make solver track which changes were made
public class Cell {
    private final int row;
    private final int col;
    private final int box;
    private int value;
    private final boolean fixed;
    private final Set<Integer> candidates = new HashSet<>();

    public Cell(int row, int col, int value) {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            throw new IllegalArgumentException("Row and column must be in range 0–8");
        }
        this.row = row;
        this.col = col;
        this.box = (row / 3) * 3 + (col / 3);

        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Cell value must be within 0-9, with 0 denoting empty cell.");
        }
        this.value = value;
        this.fixed = value != 0;
    }

    /* CANDIDATES */
    public Set<Integer> getCandidates() {
        return Collections.unmodifiableSet(candidates);
    }

    public boolean addCandidate(int candidate) {
        if (candidate < 1 || candidate > 9) {
            throw new IllegalArgumentException("Candidate value must be 1–9");
        }
        return this.candidates.add(candidate);
    }

    public boolean removeCandidate(int candidate) {
        if (candidate < 1 || candidate > 9) {
            throw new IllegalArgumentException("Candidate value must be 1–9");
        }
        return this.candidates.remove(candidate);
    }

    public void clearCandidates() {
        this.candidates.clear();
    }

    /* RELATED TO VALUE */
    public boolean isEmpty() {
        return this.value == 0;
    }
    public int getValue() {
        return this.value;
    }

    public void setValue(int newValue) {
        validateValue(newValue);
        this.value = newValue;
    }

    public void clearValue() {
        this.value = 0;
    }

    /* RELATED TO LOCATION */
    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getBox() {
        return this.box;
    }

    /* VALIDATION BASED */
    public boolean isFixed() {
        return this.fixed;
    }

    public boolean restrictTo(Set<Integer> allowed) {
        boolean changed = false;

        // iterate over a copy to avoid concurrent modification
        for (Integer candidate : new HashSet<>(candidates)) {
            if (!allowed.contains(candidate)) {
                candidates.remove(candidate);
                changed = true;
            }
        }

        return changed;
    }

    private void validateValue(int v) {
        if (v < 0 || v > 9) {
            throw new IllegalArgumentException("Cell value must be 1–9");
        }
        if (fixed) {
            throw new IllegalStateException("Cannot modify fixed cell");
        }
    }


}
