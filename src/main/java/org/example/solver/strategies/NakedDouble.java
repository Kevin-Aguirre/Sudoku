    package org.example.solver.strategies;

    import org.example.model.Cell;
    import org.example.solver.LegalMove;
    import org.example.solver.MoveResult;
    import org.example.model.SudokuBoard;

    import java.util.*;

    public class NakedDouble implements LegalMove {

        @Override
        public MoveResult apply(SudokuBoard board) {
            for (int unit = 0; unit < 9; unit++) {
                MoveResult r;

                r = checkUnit(board.getRow(unit), "row " + unit);
                if (r != null) return r;

                r = checkUnit(board.getCol(unit), "col " + unit);
                if (r != null) return r;

                r = checkUnit(board.getBox(unit), "box " + unit);
                if (r != null) return r;

            }

            return null;
        }

        private MoveResult checkUnit(List<Cell> unit, String unitName) {

            Map<Set<Integer>, List<Cell>> pairs = new HashMap<>();

            for (Cell cell : unit) {
                if (!cell.isEmpty()) continue;

                Set<Integer> cands = cell.getCandidates();
                if (cands.size() == 2) {
                    pairs.computeIfAbsent(
                            Set.copyOf(cands),
                            k -> new ArrayList<>()
                    ).add(cell);
                }
            }

            for (Map.Entry<Set<Integer>, List<Cell>> entry : pairs.entrySet()) {
                Set<Integer> pair = entry.getKey();
                List<Cell> pairCells = entry.getValue();

                if (pairCells.size() != 2) continue;

                List<String> eliminations = new ArrayList<>();
                boolean changed = false;

                for (Cell cell : unit) {
                    if (pairCells.contains(cell)) continue;
                    if (!cell.isEmpty()) continue;

                    for (int value : pair) {
                        if (cell.removeCandidate(value)) {
                            eliminations.add(
                                    "removed " + value +
                                            " from (" + cell.getRow() + "," + cell.getCol() + ")"
                            );
                            changed = true;
                        }
                    }
                }

                if (changed) {
                    return MoveResult.composite(
                            getName(),
                            "Naked pair " + pair + " in " + unitName +
                                    " eliminates candidates",
                            eliminations
                    );
                }
            }

            return null;
        }
        @Override
        public String getName() {
            return "Naked Double";
        }

    }
