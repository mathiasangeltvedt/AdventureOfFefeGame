package inf112.util.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CellPositionTest {

    // This test ensures that a newly created cell has the correct row, column, and
    // value as expected
    @Test
    void sanityTest() {
        Cell<String> cellPosition = new Cell<>(2, 3, "test");
        assertEquals(2, cellPosition.row());
        assertEquals(3, cellPosition.col());
        assertEquals("test", cellPosition.value());
    }

    // Ensures that cells with identical properties are considered equal and have
    // the same hash code
    @Test
    void testEquals() {
        Cell<String> x = new Cell<>(2, 3, "test");
        Cell<String> y = new Cell<>(2, 3, "test");

        assertFalse(x == y); // Different objects should not be the same instance
        assertTrue(x.equals(y)); // The cells should be equal
        assertTrue(y.equals(x)); // The cells should be equal
        assertEquals(x.hashCode(), y.hashCode()); // The hash codes should be the same
    }

    // Ensures that cells with different properties are not considered equal
    @Test
    void testNotEquals() {
        Cell<String> x = new Cell<>(2, 3, "test");
        Cell<String> y = new Cell<>(3, 2, "test2");

        assertFalse(x == y); // Different objects should not be the same instance
        assertFalse(x.equals(y)); // The cells should not be equal
        assertFalse(y.equals(x)); // The cells should not be equal

    }

    // Ensures that the hash code of cells with different properties are not the
    // same
    @Test
    void hashCodeTest() {
        Cell<String> x = new Cell<>(2, 3, "test");
        Cell<String> y = new Cell<>(2, 3, "test");
        Cell<String> z = new Cell<>(3, 2, "test2");

        assertEquals(x.hashCode(), y.hashCode()); // The hash codes should be the same
        assertNotEquals(x.hashCode(), z.hashCode()); // The hash codes should not be the same
    }

}
