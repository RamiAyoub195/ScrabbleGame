/**
 * Class to hold a coordinate of a letter on the grid. Coordinate
 * consists of two integers, one representing row number and the other
 * representing the column number.
 *
 * Author(s): Liam Bennet
 * Version: 2.0
 * Date: Wednesday, November 6, 2024
 */

public class Pair<R, C> {
    private R row;
    private C col;

    public Pair(R row, C col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get row
     * @return row number
     */
    public R getKey() {
        return row;
    }

    /**
     * Get column
     * @return column number
     */
    public C getValue() {
        return col;
    }

    /**
     * Set row
     * @param row row number
     */
    public void setKey(R row) {
        this.row = row;
    }

    /**
     * Set column
     * @param col column number
     */
    public void setValue(C col) {
        this.col = col;
    }

    /**
     * to String
     * @return string of row and col
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
