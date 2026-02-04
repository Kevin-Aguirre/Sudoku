package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

    private JTextField createCellField(int r, int c) {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.BOLD, 20));
        field.setPreferredSize(new Dimension(50, 50));

        // Grid Borders
        int top = (r % 3 == 0) ? 2 : 1;
        int left = (c % 3 == 0) ? 2 : 1;
        int bottom = (r == 8) ? 2 : 1;
        int right = (c == 8) ? 2 : 1;
        field.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

        // THE FIX: Listen for key releases to trigger validation
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(field, r, c);
            }
        });

        return field;
    }

    private void handleInput(JTextField field, int r, int c) {
        String text = field.getText().trim();
        Cell cell = board.getCell(r, c);

        if (text.isEmpty()) {
            board.clearValue(cell);
            field.setBackground(Color.WHITE);
            return;
        }

        try {
            // Get last character
            String lastChar = text.substring(text.length() - 1);
            int val = Integer.parseInt(lastChar);
            field.setText(String.valueOf(val));

            if (val >= 1 && val <= 9) {
                // Logic check
                if (board.isAllowed(cell, val)) {
                    board.placeValue(cell, val);
                    field.setBackground(Color.WHITE);
                    field.setForeground(Color.BLUE);
                } else {
                    // Feedback: WRONG MOVE
                    field.setBackground(new Color(255, 200, 200));
                    field.setForeground(Color.RED);
                    logMove("Rule violation: " + val + " at (" + (r+1) + "," + (c+1) + ")");
                }
            } else {
                field.setText("");
            }
        } catch (NumberFormatException ex) {
            field.setText("");
            field.setBackground(Color.WHITE);
        }
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        difficultySelector = new JComboBox<>(SudokuGenerator.Difficulty.values());
        difficultySelector.setSelectedItem(SudokuGenerator.Difficulty.MEDIUM);

        JButton newGameBtn = new JButton("New Game");
        JButton solveBtn = new JButton("Solve Step");
        JButton fullBtn = new JButton("Solve All");

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

                // If the cell was part of the initial puzzle (fixed)
                boolean isFixed = !cell.isEmpty() && val != 0;
                // Note: Your SudokuBoard might need a way to track initial cells vs user cells

                cells[r][c].setEditable(val == 0);
                cells[r][c].setBackground(Color.WHITE);
                cells[r][c].setForeground(val == 0 ? Color.BLUE : Color.BLACK);
            }
        }
    }

    private void logMove(String message) {
        moveLog.append("> " + message + "\n");
        moveLog.setCaretPosition(moveLog.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGUI().setVisible(true));
    }
}