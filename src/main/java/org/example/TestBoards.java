package org.example;

import java.util.List;

public class TestBoards {

    public static SudokuBoard oneMissing() {
        List<List<String>> input = List.of(
                List.of("5","3","4","6","7","8","9","1","."),
                List.of("6","7","2","1","9","5","3","4","8"),
                List.of("1","9","8",".","4","2","5","6","7"),
                List.of("8","5","9","7","6","1","4","2","3"),
                List.of("4","2","6","8","5","3","7","9","1"),
                List.of("7","1","3","9","2","4","8","5","6"),
                List.of("9","6","1","5","3","7","2","8","4"),
                List.of("2",".","7","4","1","9","6","3","5"),
                List.of("3","4","5","2","8","6","1","7","9")
        );
        return new SudokuBoard(input);
    }
}
