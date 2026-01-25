package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void constructor_validEmptyCell_doesNotThrow() {
        assertDoesNotThrow(() -> new Cell(0, 0, 0));
    }
    @Test
    void constructor_invalidValue_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cell(0, 0, 12)
        );
    }
    @Test
    void constructor_invalidRow_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cell(9, 0, 3)
        );
    }
    @Test
    void constructor_negativeCol_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cell(0, -1, 3)
        );
    }

    @Test
    void isEmpty() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertTrue(emptyCell.isEmpty(), "Cell initialized to 0 must be empty.");
    }

    @Test
    void isNotEmpty() {
        Cell validCell = new Cell(1, 1, 5);
        assertFalse(validCell.isEmpty(), "Cell initialized to value from 1-9 is not empty.");
    }

    @Test
    void getValue() {
        Cell validCell = new Cell(1, 1, 5);
        assertEquals(5, validCell.getValue(), "Cell must return 5.");
    }

    @Test
    void isFixed() {
        Cell validCell = new Cell(1, 1, 5);
        assertTrue(validCell.isFixed(), "Cell initialized to value from 1-9 is fixed.");
    }

    @Test
    void isNotFixed() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertFalse(emptyCell.isFixed(), "Cell initialized to 0 must not be fixed.");
    }

    @Test()
    void setValue_withValid() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertDoesNotThrow(() -> emptyCell.setValue(5));
        assertEquals(5, emptyCell.getValue());
    }

    @Test()
    void setValue_withFixed() {
        Cell emptyCell = new Cell(1, 1, 9);
        assertThrows(IllegalStateException.class, () -> emptyCell.setValue(5));
    }

    @Test()
    void setValue_withInvalid() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertThrows(IllegalArgumentException.class, () -> emptyCell.setValue(51290));
    }

    @Test
    void getRow() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertEquals(1, emptyCell.getRow(), "Cell row should return 1.");
    }

    @Test
    void getCol() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertEquals(1, emptyCell.getCol(), "Cell col should return 1.");
    }

    @Test
    void getBox() {
        Cell emptyCell = new Cell(1, 1, 0);
        assertEquals(0, emptyCell.getBox(), "Cell box should return 0.");
    }

}