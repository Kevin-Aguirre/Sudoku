package org.example;

public class Main {
    static void main() {
        SudokuGenerator.Difficulty diff = SudokuGenerator.Difficulty.EXTREME;
        SudokuGenerator gen = new SudokuGenerator(diff);
        SudokuBoard board = gen.generatePuzzle();

        System.out.println(board);
    }
}
