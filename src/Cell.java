/**
 * This class represents a cell on the board, which can be either occupied or empty.
 */
public class Cell
{

    private Tiles tile; // The tile on the cell, if any


    public Cell()
    {
        this.tile = null; // Indicates the cell is empty
    }


    public boolean isOccupied()
    {
        return tile != null;
    }


    public void placeTile(Tiles tile)
    {
        this.tile = tile;
    }


    public void removeTile()
    {
        this.tile = null;
    }


    public Tiles getTile()
    {
        return tile;
    }


    @Override
    public String toString()
    {
        return isOccupied() ? "  " + tile.getLetter() + "  " : "  -  ";
    }
}
