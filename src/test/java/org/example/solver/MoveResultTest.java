package org.example.solver;

import org.example.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MoveResultTest {

    @Test
    void testRestrictedTo_StoresAllowedCandidates() {
        // Arrange
        Cell cell = new Cell(2, 3, 0);
        Set<Integer> allowed = Set.of(1, 5);
        String technique = "Naked Double";

        // Act
        MoveResult result = MoveResult.restrictedTo(technique, cell, allowed);

        // Assert
        assertEquals(MoveResult.Type.CANDIDATE_RESTRICTION, result.type());
        assertEquals(allowed, result.values(), "The set of allowed values should match");
        assertEquals(cell, result.cell());
        assertTrue(result.explanation().contains("Restricted (2,3) to [1, 5]") ||
                result.explanation().contains("5, 1"), "Explanation should contain coordinates and values");
    }

    @Test
    void testComposite_AggregatesDetails() {
        // Arrange
        String technique = "Locked Candidates";
        String headline = "Claiming in Box 1";
        List<String> details = List.of("Removed 3 from (0,4)", "Removed 3 from (0,5)");

        // Act
        MoveResult result = MoveResult.composite(technique, headline, details);

        // Assert
        assertEquals(MoveResult.Type.COMPOSITE, result.type());
        assertNull(result.cell(), "Composite moves usually target multiple cells, so the primary cell field can be null");

        String expectedExplanation = "Claiming in Box 1: Removed 3 from (0,4), Removed 3 from (0,5)";
        assertEquals(expectedExplanation, result.explanation());
    }

    @Test
    void testToString_Formatting() {
        // Arrange
        Cell cell = new Cell(0, 0, 0);
        MoveResult result = MoveResult.placement("Naked Single", cell, 9);

        // Act
        String output = result.toString();

        // Assert
        // Should look like: [Naked Single] Placed 9 at (0,0)
        assertEquals("[Naked Single] Placed 9 at (0,0)", output);
    }
}