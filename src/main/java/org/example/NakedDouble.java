    package org.example;

    import java.util.*;

    public class NakedDouble implements LegalMove {

        @Override
        public boolean apply(SudokuBoard board) {
            for (int unit = 0; unit < 9; unit++) {
                if (applyToUnit(board.getRow(unit))) return true;
                if (applyToUnit(board.getCol(unit))) return true;
                if (applyToUnit(board.getBox(unit))) return true;
            }

            return false;
        }

        private boolean applyToUnit(List<Cell> cells) {
            Map<Set<Integer>, List<Cell>> groups = new HashMap<>();
            boolean changed = false;

            for (Cell cell : cells) {
                if (cell.isEmpty() && cell.getCandidates().size() == 2) {
                    groups.computeIfAbsent(new HashSet<>(cell.getCandidates()), k -> new ArrayList<>()).add(cell);
                }
            }

            for (var entry : groups.entrySet()) {
                Set<Integer> pair = entry.getKey();
                List<Cell> matched = entry.getValue();

                if (matched.size() == 2) {
                    for (Cell other : cells) {
                        if (!matched.contains(other) && other.isEmpty()) {
                            for (int v : pair) {
                                if (other.removeCandidate(v)) {
                                    changed = true;
                                }
                            }
                        }
                    }
                    if (changed) return true;
                }
            }
            return false;
        }

        @Override
        public String getName() {
            return "Naked Double";
        }

        @Override
        public String getMessage() {
            return "Removing candidates using naked double";
        }
    }
