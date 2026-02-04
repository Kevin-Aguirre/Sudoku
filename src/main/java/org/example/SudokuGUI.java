package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class SudokuGUI extends JFrame {
    private SudokuBoard board;
    private final JTextField[][] cells = new JTextField[9][9];
    private final SudokuSolver solver = new SudokuSolver();
    private final JTextArea moveLog = new JTextArea();
    private JComboBox<SudokuGenerator.Difficulty> difficultySelector;

    public SudokuGUI() {
        setTitle("Sudoku Solver - Move Log");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c] = createCellField(r, c);
                gridPanel.add(cells[r][c]);
            }
        }

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(250, 0));
        logPanel.setBorder(new TitledBorder("Solving Steps"));
        moveLog.setEditable(false);
        moveLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        moveLog.setLineWrap(true);
        moveLog.setWrapStyleWord(true);
        logPanel.add(new JScrollPane(moveLog), BorderLayout.CENTER);

        add(gridPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(createControlPanel(), BorderLayout.SOUTH);

        startNewGame(SudokuGenerator.Difficulty.MEDIUM);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }
    private final Color HIGHLIGHT_COLOR = new Color(200, 220, 255); // Light Blue

    private JTextField createCellField(int r, int c) {
        JTextField field = getJTextField(r, c);
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        // Existing key listener for input
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(field, r, c);
                highlightValueFromField(field); // Re-highlight if value changed
            }
        });

        // NEW: Focus listener to highlight other cells with the same value
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                highlightValueFromField(field);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                resetHighlights();
            }
        });

        return field;
    }

    private static JTextField getJTextField(int r, int c) {
        JTextField field = new JTextField();
        // ... (Keep existing alignment, font, and border code)
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.BOLD, 20));
        field.setPreferredSize(new Dimension(50, 50));
        int top = (r % 3 == 0) ? 2 : 1;
        int left = (c % 3 == 0) ? 2 : 1;
        int bottom = (r == 8) ? 2 : 1;
        int right = (c == 8) ? 2 : 1;
        field.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
        return field;
    }

    private void highlightValueFromField(JTextField field) {
        resetHighlights(); // Clear old highlights first
        String text = field.getText().trim();
        if (!text.isEmpty()) {
            try {
                int val = Integer.parseInt(text);
                applyHighlight(val);
            } catch (NumberFormatException ignored) {}
        }
    }

    private void applyHighlight(int value) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell boardCell = board.getCell(r, c);
                // Highlight if the cell matches the value
                if (boardCell.getValue() == value) {
                    cells[r][c].setBackground(HIGHLIGHT_COLOR);
                }
            }
        }
    }

    private void resetHighlights() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                // Restore white for empty/user cells, or light gray for fixed cells
                if (!cells[r][c].isEditable()) {
                    cells[r][c].setBackground(new Color(240, 240, 240));
                } else {
                    // If there's an error (red), keep it red, otherwise white
                    if (cells[r][c].getForeground().equals(Color.RED)) {
                        cells[r][c].setBackground(new Color(255, 200, 200));
                    } else {
                        cells[r][c].setBackground(Color.WHITE);
                    }
                }
            }
        }
    }

    private void handleInput(JTextField field, int r, int c) {
        String text = field.getText().trim();
        Cell cell = board.getCell(r, c);

        if (text.isEmpty()) {
            board.clearValue(cell);
            field.setBackground(Color.WHITE);
            return;
        }

        int val = Integer.parseInt(text);

        if (board.isAllowed(cell, val)) {
            board.placeValue(cell, val);
            field.setBackground(Color.WHITE);
            field.setForeground(Color.BLUE);

            // Debugging: Log how many are left
            int emptyCount = board.countEmpty();
            logMove("Cells remaining: " + emptyCount);

            if (emptyCount == 0) {
                // Delay slightly so the UI paints the last number before the popup
                SwingUtilities.invokeLater(() -> triggerWin());
            }
        } else {
            field.setBackground(new Color(255, 200, 200));
            field.setForeground(Color.RED);
            logMove("Conflict at (" + (r + 1) + "," + (c + 1) + ")");
        }
        highlightValueFromField(field);
    }

    private void triggerWin() {
        logMove("***************************");
        logMove("CONGRATS! YOU WIN!");
        logMove("***************************");

        JOptionPane.showMessageDialog(this,
                "Congratulations! You've solved the puzzle!",
                "Victory",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        difficultySelector = new JComboBox<>(SudokuGenerator.Difficulty.values());
        difficultySelector.setSelectedItem(SudokuGenerator.Difficulty.MEDIUM);

        JButton newGameBtn = new JButton("New Game");
        JButton solveBtn = new JButton("Solve Step");
        JButton fullBtn = new JButton("Solve All");
        JButton clearBoardBtn = new JButton("Clear User Moves");

        clearBoardBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear all your moves?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                clearUserEntries();
            }
        });

        newGameBtn.addActionListener(e -> startNewGame((SudokuGenerator.Difficulty) difficultySelector.getSelectedItem()));

        solveBtn.addActionListener(e -> {
            if (solver.solveStep(board)) {
                updateGridFromBoard();
                logMove(solver.getAppliedMoves().getLast().toString());
            }
        });

        fullBtn.addActionListener(e -> {
            if (solver.solve(board)) {
                updateGridFromBoard();
                logMove("Board Solved!");
            }
        });

        panel.add(new JLabel("Difficulty:"));
        panel.add(difficultySelector);
        panel.add(newGameBtn);
        panel.add(solveBtn);
        panel.add(fullBtn);
        panel.add(clearBoardBtn);
        return panel;
    }

    private void startNewGame(SudokuGenerator.Difficulty diff) {
        moveLog.setText("");
        SudokuGenerator gen = new SudokuGenerator(diff);
        this.board = gen.generatePuzzle();
        updateGridFromBoard();
        logMove("New " + diff + " game started.");
    }

    private void updateGridFromBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                int val = cell.getValue();

                cells[r][c].setText(val == 0 ? "" : String.valueOf(val));

                // LOGIC: If the board says this cell is "Fixed", disable editing
                if (cell.isFixed()) {
                    cells[r][c].setEditable(false);
                    cells[r][c].setBackground(new Color(235, 235, 235)); // Light gray
                    cells[r][c].setForeground(Color.BLACK);
                } else {
                    cells[r][c].setEditable(true);
                    cells[r][c].setBackground(Color.WHITE);
                    cells[r][c].setForeground(Color.BLUE);
                }
            }
        }
    }

    private void logMove(String message) {
        moveLog.append("> " + message + "\n");
        moveLog.setCaretPosition(moveLog.getDocument().getLength());
    }

    private void clearUserEntries() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                // Check the GUI state - if it's editable, it's a user move
                if (cells[r][c].isEditable()) {
                    cells[r][c].setText("");
                    cells[r][c].setBackground(Color.WHITE);

                    // Also clear it in the underlying logic board
                    board.clearValue(board.getCell(r, c));
                }
            }
        }
        logMove("User moves cleared. Original puzzle preserved.");
    }
}



// This class filters input to only allow a single digit (1-9)
class NumericDocumentFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        // Combine current text with new text to see what the result would be
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        // Rule 1: Allow empty string (for deletions)
        if (nextText.isEmpty()) {
            super.replace(fb, offset, length, text, attrs);
            return;
        }

        // Rule 2: Only allow if the result is exactly one character and is 1-9
        if (nextText.length() == 1 && nextText.matches("[1-9]")) {
            super.replace(fb, offset, length, text, attrs);
        }
        // Otherwise, do nothing (reject the input)
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        // Redirect to replace logic
        replace(fb, offset, 0, string, attr);
    }
}