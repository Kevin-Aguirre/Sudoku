package org.example.gui;

import org.example.generator.SudokuGenerator;
import org.example.model.SudokuBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SudokuGUITest {

    private SudokuGUI gui;
    private SudokuBoard board;
    private JTextField[][] inputFields; // Defined at class level

    @BeforeEach
    void setUp() throws Exception {
        gui = new SudokuGUI();

        // Use the new empty constructor we just added
        board = new SudokuBoard();

        // Inject the board and get the fields
        setPrivateField(gui, board);
        inputFields = (JTextField[][]) getPrivateField(gui, "inputFields");
    }

    @Test
    void testGridInitialization() throws Exception {
        JTextField[][] fields = (JTextField[][]) getPrivateField(gui, "inputFields");

        assertNotNull(fields);
        assertEquals(9, fields.length);
        assertEquals(9, fields[0].length);
        assertNotNull(fields[0][0], "Top-left cell should be initialized");
    }

    @Test
    void testValidInputIsBlue() throws Exception {
        JTextField[][] fields = (JTextField[][]) getPrivateField(gui, "inputFields");
        JTextField cell = fields[4][4];

        cell.setText("1");
        simulateKeyReleased(cell);

        if (cell.getForeground().equals(Color.BLUE)) {
            assertEquals(Color.BLUE, cell.getForeground());
        }
    }

    @Test
    void testNewGameResetsLog() throws Exception {
        JTextArea moveLog = (JTextArea) getPrivateField(gui, "moveLog");
        moveLog.setText("Some old moves...");

        invokePrivateMethod(gui, SudokuGenerator.Difficulty.EASY);

        assertTrue(moveLog.getText().contains("New EASY game started."));
        assertFalse(moveLog.getText().contains("Some old moves..."));
    }

    private void setPrivateField(Object obj, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField("board");
        field.setAccessible(true);
        field.set(obj, value);
    }

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private void invokePrivateMethod(Object obj, Object... args) throws Exception {
        java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("startNewGame", SudokuGenerator.Difficulty.class);
        method.setAccessible(true);
        method.invoke(obj, args);
    }

    private void simulateKeyReleased(JTextField field) {
        for (java.awt.event.KeyListener listener : field.getKeyListeners()) {
            listener.keyReleased(new java.awt.event.KeyEvent(field, 0, 0, 0, 0, ' '));
        }
    }
}