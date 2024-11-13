public class Pair<R, C> {
    private R row;
    private C col;

    // Constructor
    public Pair(R row, C col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets row
     * @return row number
     */
    public R getKey() {
        return row;
    }

    /**
     * Gets column
     * @return column number
     */
    public C getValue() {
        return col;
    }

    /**
     * Sets row
      * @param row row number
     */
    public void setKey(R row) {
        this.row = row;
    }

    /**
     * Sets column
     * @param col column number
     */
    public void setValue(C col) {
        this.col = col;
    }

    /**
     * To string
     * @return string of row and column
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
