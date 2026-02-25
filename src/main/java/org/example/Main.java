package org.example;

import javax.swing.SwingUtilities;

/* * *
* Challenge im facing, in order to develop pwd
*
*
* * */

public class Main {

    public static void main(String[] args) {
        // This ensures the UI is created on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new SudokuGUI().setVisible(true));
    }
}



