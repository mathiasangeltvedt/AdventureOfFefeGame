package inf112.util.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class GridTest {

    // Test to ensure that the grid has the correct number of rows and columns
    @Test
    void gridGetRowsCols() {
        IGrid<Integer> grid = new Grid<>(3, 2);
        assertEquals(3, grid.getRows());
        assertEquals(2, grid.getCols());
    }

    // Test grid's default cell value, ability to set values, and retrieve them
    @Test
    void gridSanityTest() {
        IGrid<Integer> grid = new Grid<>(3, 2);

        assertEquals(3, grid.getRows());
        assertEquals(2, grid.getCols());
        assertEquals(null, grid.getCell(0, 0)); // null is default for Cell

        grid.setCell(0, 0, 1); // Set a cell value
        grid.setCell(1, 1, 2); // Set another cell value

        assertEquals(1, grid.getCell(0, 0)); // Cell (0,0) should contain the value 1
        assertEquals(null, grid.getCell(0, 1)); // Cell (0,1) should remain null as not set
        assertEquals(2, grid.getCell(1, 1)); // Cell (1,1) should contain the value 2

    }

    // Test grid's ability to iterate over all cells
    @Test
    void gridIteratorTest() {
        IGrid<Integer> grid = new Grid<>(3, 2);

        grid.setCell(0, 0, 1);
        grid.setCell(2, 1, 2);

        ArrayList<Integer> test = new ArrayList<>();
        for (Integer num : grid) {
            test.add(num);
        }
        assertEquals(2, test.size()); // Only two cells have been set
    }

    // Tests that attempting to access a grid cell outside of its defined bounds
    // throws an exception
    @Test
    void coordinateOnGridTest() {
        IGrid<Double> grid = new Grid<>(3, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            grid.getCell(3, 2);
        });

    }

}
