/**
 * This class is the cell class which represents each board square. A cell is composed of
 * a tile and a cell can be empty or occupied on the square of a board.
 * Author(s): Andrew Tawfik
 * Version: 2.0
 * Date: Wednesday, November 6th, 2024
 */

public class Cell
{

    private Tiles tile; // The tile for the cell


    public Cell()
    {
        this.tile = null; // Initializes the cell is empty
    }

    /**
     * This function checks to see if the cell tile is occupied with a tile or not.
     *
     * @return true if occupied and false otherwise
     */
    public boolean isOccupied()
    {
        return tile != null;
    }

    /**
     * This function places a tile as a cell.
     * @param tile the tile to be the cell
     */
    public void placeTile(Tiles tile)
    {
        this.tile = tile;
    }

    /**
     * Removes the tile from the cell.
     */
    public void removeTile()
    {
        this.tile = null;
    }

    /**
     * gets the tile from the cell
     * @return the tile in cell.
     */

    public Tiles getTile()
    {
        return tile;
    }

    /**
     * Represents how the cell will look like having a tile in there.
     * @return a string representation of tile.
     */
    @Override
    public String toString()
    {
        return isOccupied() ? "  " + tile.getLetter() + "  " : "  -  ";
    }
}
