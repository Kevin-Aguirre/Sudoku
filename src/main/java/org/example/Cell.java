package org.example;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final int row;
    private final int col;
    private final int box;
    private int value;
    private final boolean fixed;

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

    public boolean isEmpty() {
        return this.value == 0;
    }
    public int getValue() {
        return this.value;
    }
    public boolean isFixed() { return this.fixed; }

    public void setValue(int newValue) {
        validateValue(newValue);
        this.value = newValue;
    }

    public void clearValue() {
        this.value = 0;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getBox() {
        return this.box;
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
