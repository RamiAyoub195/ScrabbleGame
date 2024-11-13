/**
 * This class represents a cell on the board, which can be either occupied or empty.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 2.0
 * Date: Wednesday, November 6th, 2024
 */
public class Cell
{

    private Tiles tile; // The tile on the cell, if any


    public Cell()
    {
        this.tile = null; // Indicates the cell is empty
    }


    /**
     * Checks if is cell is occupied by a tile
     * @return true if occupied, otherwise false
     */
    public boolean isOccupied()
    {
        return tile != null;
    }


    /**
     * Place tile on cell
     * @param tile to be placed
     */
    public void placeTile(Tiles tile)
    {
        this.tile = tile;
    }

    /**
     * removing a tile from a cell making ut null again
     */
    public void removeTile()
    {
        this.tile = null;
    }

    /**
     * Gets a specific tile cell
     * @return the tile cell
     */
    public Tiles getTile()
    {
        return tile;
    }

    /**
     * Will represent how a cell looks like by overriding the two string method.
     * @return the to string of the cell
     */
    @Override
    public String toString()
    {
        return isOccupied() ? "  " + tile.getLetter() + "  " : "  -  ";
    }
}
