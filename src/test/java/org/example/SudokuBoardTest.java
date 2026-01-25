package org.example;

import org.example.SudokuBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardTest {


    @Test
    void constructor_invalidRowCount() {
        List<List<String>> emptyInput = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(emptyInput));
    }

    @Test
    void constructor_invalidColCount() {
        List<List<String>> invalidColCount = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            invalidColCount.add(new ArrayList<>());
        }
        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(invalidColCount));
    }

    @Test
    void constructor_canCreateEmptyGrid() {
        List<List<String>> emptyBoard = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            List<String> newEntry = new ArrayList<>();
            for (int j = 0; j < 9; ++j) {
                newEntry.add(".");
            }
            emptyBoard.add(newEntry);
        }

        assertDoesNotThrow(() -> new SudokuBoard(emptyBoard));
    }

    @Test
    void constructor_triggerBoardConflict() {
        List<List<String>> duplicateDigitBoard = new ArrayList<>();

        List<String> firstRow = new ArrayList<>();
        firstRow.add("5");
        firstRow.add("5");
        for (int j = 0; j < 7; j++) {
            firstRow.add(".");
        }

        duplicateDigitBoard.add(firstRow);

        for (int i = 0; i < 8; ++i) {
            List<String> newEntry = new ArrayList<>();
            for (int j = 0; j < 9; ++j) {
                newEntry.add(".");
            }
            duplicateDigitBoard.add(newEntry);
        }

        assertThrows(IllegalArgumentException.class, () -> new SudokuBoard(duplicateDigitBoard));
    }

}
