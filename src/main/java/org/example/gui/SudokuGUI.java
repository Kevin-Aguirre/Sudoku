package org.example.gui;

import org.example.generator.SudokuGenerator;
import org.example.model.Cell;
import org.example.model.SudokuBoard;
import org.example.solver.SudokuSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import javax.swing.text.AbstractDocument;

public class SudokuGUI extends JFrame {
    private SudokuBoard board;
    private final JPanel[][] cellContainers = new JPanel[9][9];
    private final JTextField[][] inputFields = new JTextField[9][9];
    private final JLabel[][] candidateLabels = new JLabel[9][9];

    private final SudokuSolver solver = new SudokuSolver();
    private final JTextArea moveLog = new JTextArea();
    private JComboBox<SudokuGenerator.Difficulty> difficultySelector;
    private JCheckBox showCandidatesCheckbox;

    private final Color COLOR_CROSSHAIR = new Color(232, 241, 255);
    private final Color COLOR_MATCHING = new Color(190, 210, 255);
    private final Color COLOR_FOCUSED = new Color(150, 180, 255);

    public SudokuGUI() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cellContainers[r][c] = createCellComponent(r, c);
                gridPanel.add(cellContainers[r][c]);
            }
        }

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(280, 0));
        logPanel.setBorder(new TitledBorder("Solving Steps"));
        moveLog.setEditable(false);
        moveLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logPanel.add(new JScrollPane(moveLog), BorderLayout.CENTER);

        add(gridPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(createControlPanel(), BorderLayout.SOUTH);

        startNewGame(SudokuGenerator.Difficulty.MEDIUM);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createCellComponent(int r, int c) {
        JPanel container = new JPanel(new CardLayout());

        JTextField field = new JTextField();
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.BOLD, 22));
        field.setBorder(null);
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        JLabel label = getjLabel(field, container);

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(field, r, c);
                highlightContext(r, c);
            }
        });

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                CardLayout cl = (CardLayout) container.getLayout();
                cl.show(container, "INPUT");
                highlightContext(r, c);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getForeground().equals(Color.RED) && !field.getText().isEmpty()) {
                }

                else if (field.getText().isEmpty() && showCandidatesCheckbox.isSelected()) {
                    CardLayout cl = (CardLayout) container.getLayout();
                    cl.show(container, "CANDIDATES");
                    candidateLabels[r][c].setText(getCandidateHtml(board.getCell(r, c).getCandidates()));
                }

                resetHighlights();
            }
        });

        inputFields[r][c] = field;
        candidateLabels[r][c] = label;

        container.add(field, "INPUT");
        container.add(label, "CANDIDATES");

        int t = (r % 3 == 0) ? 2 : 1;
        int l = (c % 3 == 0) ? 2 : 1;
        int b = (r == 8) ? 2 : 1;
        int rr = (c == 8) ? 2 : 1;
        container.setBorder(new MatteBorder(t, l, b, rr, Color.BLACK));

        return container;
    }

    private static JLabel getjLabel(JTextField field, JPanel container) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                field.setText("");

                CardLayout cl = (CardLayout) container.getLayout();
                cl.show(container, "INPUT");

                field.requestFocusInWindow();
            }
        });
        return label;
    }

    private String getCandidateHtml(Set<Integer> candidates) {
        if (candidates == null || candidates.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<html><table style='font-size:7px; border-collapse:collapse;'>");
        for (int r = 0; r < 3; r++) {
            sb.append("<tr>");
            for (int c = 1; c <= 3; c++) {
                int val = r * 3 + c;
                sb.append("<td style='width:12px; height:10px; text-align:center;'>");
                sb.append(candidates.contains(val) ? val : "&nbsp;");
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        return sb.append("</table></html>").toString();
    }

    private void highlightContext(int activeRow, int activeCol) {
        resetHighlights();
        int activeValue = board.getCell(activeRow, activeCol).getValue();
        int activeBox = (activeRow / 3) * 3 + (activeCol / 3);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int currentBox = (r / 3) * 3 + (c / 3);

                Color bg = Color.WHITE;
                if (r == activeRow || c == activeCol || currentBox == activeBox) bg = COLOR_CROSSHAIR;
                if (activeValue != 0 && board.getCell(r, c).getValue() == activeValue) bg = COLOR_MATCHING;
                if (r == activeRow && c == activeCol) bg = COLOR_FOCUSED;

                inputFields[r][c].setBackground(bg);
                candidateLabels[r][c].setBackground(bg);
            }
        }
    }

    private void resetHighlights() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                Color bg = cell.isFixed() ? new Color(235, 235, 235) : Color.WHITE;
                inputFields[r][c].setBackground(bg);
                candidateLabels[r][c].setBackground(bg);
            }
        }
    }

    private void updateGridFromBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                JTextField field = inputFields[r][c];
                CardLayout cl = (CardLayout) cellContainers[r][c].getLayout();

                if (field.getForeground().equals(Color.RED) && !field.getText().isEmpty()) {
                    cl.show(cellContainers[r][c], "INPUT");
                    continue;
                }

                if (cell.getValue() != 0) {
                    field.setText(String.valueOf(cell.getValue()));
                    field.setForeground(cell.isFixed() ? Color.BLACK : Color.BLUE);
                    cl.show(cellContainers[r][c], "INPUT");
                } else if (showCandidatesCheckbox.isSelected()) {
                    candidateLabels[r][c].setText(getCandidateHtml(cell.getCandidates()));
                    field.setText("");
                    cl.show(cellContainers[r][c], "CANDIDATES");
                } else {
                    field.setText("");
                    cl.show(cellContainers[r][c], "INPUT");
                }
            }
        }
    }

    private void handleInput(JTextField field, int r, int c) {
        Cell cell = board.getCell(r, c);
        if (cell.isFixed()) {
            return;
        }
        String text = field.getText().trim();
        board.clearValue(cell);
        field.setForeground(Color.BLUE);

        if (text.isEmpty()) {

        } else {
            try {
                int val = Integer.parseInt(text);

                if (board.isAllowed(cell, val)) {
                    board.placeValue(cell, val);
                    field.setForeground(Color.BLUE);
                } else {
                    field.setForeground(Color.RED);
                }
            } catch (NumberFormatException e) {
                field.setText("");
            }
        }

        board.initializeCandidates();

        updateGridFromBoardExcept(r, c);

        if (board.countEmpty() == 0) {
            triggerWin();
        }
    };
    private void updateGridFromBoardExcept(int excludeR, int excludeC) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (r == excludeR && c == excludeC) continue; // Skip the active cell

                Cell cell = board.getCell(r, c);
                JTextField field = inputFields[r][c];
                CardLayout cl = (CardLayout) cellContainers[r][c].getLayout();

                if (cell.getValue() != 0) {
                    field.setText(String.valueOf(cell.getValue()));
                    field.setForeground(cell.isFixed() ? Color.BLACK : Color.BLUE);
                    cl.show(cellContainers[r][c], "INPUT");
                } else if (showCandidatesCheckbox.isSelected()) {
                    candidateLabels[r][c].setText(getCandidateHtml(cell.getCandidates()));
                    cl.show(cellContainers[r][c], "CANDIDATES");
                } else {
                    field.setText("");
                    cl.show(cellContainers[r][c], "INPUT");
                }
            }
        }
    }
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        difficultySelector = new JComboBox<>(SudokuGenerator.Difficulty.values());
        showCandidatesCheckbox = new JCheckBox("Show Candidates", true);
        showCandidatesCheckbox.addActionListener(e -> updateGridFromBoard());

        JButton newGameBtn = new JButton("New Game");
        JButton solveBtn = new JButton("Solve Step");
        JButton solveAllBtn = new JButton("Solve All");
        JButton clearMovesBtn = new JButton("Clear Moves");
        JButton clearLogBtn = new JButton("Clear Log");

        newGameBtn.addActionListener(e -> startNewGame((SudokuGenerator.Difficulty) difficultySelector.getSelectedItem()));

        solveBtn.addActionListener(e -> {
            if (solver.solveStep(board)) {
                updateGridFromBoard();
                logMove(solver.getAppliedMoves().getLast().toString());
            } else {
                logMove("No more steps found!");
            }
        });

        solveAllBtn.addActionListener(e -> {
            if (solver.solve(board)) {
                board.initializeCandidates(); // Important for the final view
                updateGridFromBoard();
                logMove("Puzzle solved by AI!");
            } else {
                logMove("Solver could not complete this puzzle.");
            }
        });

        clearMovesBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear your progress?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clearUserEntries();
            }
        });

        clearLogBtn.addActionListener(e -> moveLog.setText(""));

        panel.add(new JLabel("Difficulty:"));
        panel.add(difficultySelector);
        panel.add(showCandidatesCheckbox);
        panel.add(newGameBtn);
        panel.add(solveBtn);
        panel.add(solveAllBtn);
        panel.add(clearMovesBtn);
        panel.add(clearLogBtn);
        return panel;
    }

    private void clearUserEntries() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isFixed()) {
                    board.clearValue(cell);
                    // Reset the visual state of the input field
                    inputFields[r][c].setForeground(Color.BLUE);
                    inputFields[r][c].setText("");
                }
            }
        }
        board.initializeCandidates();
        updateGridFromBoard();
        logMove("User moves cleared.");
    }

    private void startNewGame(SudokuGenerator.Difficulty diff) {
        moveLog.setText("");
        board = new SudokuGenerator(diff).generatePuzzle();
        board.initializeCandidates();
        updateGridFromBoard();
        logMove("New " + diff + " game started.");
    }

    private void logMove(String message) {
        moveLog.append("> " + message + "\n");
    }

    private void triggerWin() {
        logMove("****** YOU WIN! ******");
        JOptionPane.showMessageDialog(this, "Puzzle Solved!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }
}

