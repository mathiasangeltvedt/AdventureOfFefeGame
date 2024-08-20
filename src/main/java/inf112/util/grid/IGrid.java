package inf112.util.grid;

public interface IGrid<E> extends Iterable<E> {

     /**
      * This method is used to retreive the number of rows on the grid
      * 
      * @return the number of rows on the grid
      */
     int getRows();

     /**
      * This method is used to retreive the number of cols on the grid
      * 
      * @return the number of cols on the grid
      */
     int getCols();

     /**
      * Retrieves a cell at a specific position in the grid
      * 
      * @param row row position of the cell
      * @param col column position of the cell
      * @return the value at that position
      */
     E getCell(int row, int col);

     /**
      * Sets a new value to a cell in the grid corresponding to a specific position
      * 
      * @param row   row position that should be set
      * @param col   column position that shuld be set
      * @param value the value that the position should be set to
      */
     void setCell(int row, int col, E value);
}
