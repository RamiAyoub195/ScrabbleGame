public class Pair<R, C> {
    private R row;
    private C col;

    // Constructor
    public Pair(R row, C col) {
        this.row = row;
        this.col = col;
    }

    // Getter methods
    public R getKey() {
        return row;
    }

    public C getValue() {
        return col;
    }

    // Setter methods
    public void setKey(R row) {
        this.row = row;
    }

    public void setValue(C value) {
        this.col = col;
    }

    // Optional: Override toString for easy display
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
