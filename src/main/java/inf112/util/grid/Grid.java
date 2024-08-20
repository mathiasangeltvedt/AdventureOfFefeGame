package inf112.util.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the Grid of the game
 */
public class Grid<E> implements IGrid<E> {
    private int rows, cols;
    private final E DEFAULT_VAL = null;
    private boolean mutatedSinceLast = true;
    List<E> iteratorArr;

    // Lagrer verdiene i en 2D liste
    private List<List<E>> grid = new ArrayList<>();

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(DEFAULT_VAL);
            }
            grid.add(row);
        }
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public E getCell(int row, int col) {
        if (!onGrid(row, col))
            throw new IndexOutOfBoundsException();
        return grid.get(row).get(col);
    }

    @Override
    public void setCell(int row, int col, E val) {
        if (!onGrid(row, col))
            throw new IndexOutOfBoundsException();
        mutatedSinceLast = true;
        grid.get(row).set(col, val);
    }

    @Override
    public Iterator<E> iterator() {
        if (!mutatedSinceLast)
            return iteratorArr.iterator();
        iteratorArr = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = getCell(i, j);
                if (val != null)
                    iteratorArr.add(val);
            }
        }
        mutatedSinceLast = false;
        return iteratorArr.iterator();
    }

    private boolean onGrid(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

}
