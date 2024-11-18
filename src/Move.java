import java.util.ArrayList;

/**
 * This class represents a move that can be made by an AIPlayer, the move of the AI
 * that will be placed on the board. The move will have the tiles that the AIPlayer will place,
 * the row and column positions and the direction such as horizontal or vertical.
 *
 * Author(s):
 * Version:
 * Date:
 */

public class Move {
    private ArrayList<Tiles> tiles;
    private ArrayList<Integer> row;
    private ArrayList<Integer> col;

    public Move() {
        tiles = new ArrayList<>();
        row = new ArrayList<>();
        col = new ArrayList<>();
    }

    public void addToMove(Tiles tile, int row, int col) {
        tiles.add(tile);
        this.row.add(row);
        this.col.add(col);
    }



}
