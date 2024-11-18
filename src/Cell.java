/**
 * This class represents a cell on the board, which can be either occupied or empty.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */
public class Cell
{

    private Tiles tile; // The tile on the cell
    private String specialType; //will indicate if the cell is a premium square or not such as


    public Cell()
    {
        this.tile = null; // Indicates the cell is empty
        this.specialType = null; //Initializes it as null
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
     * Gets the type of special square
     * @return the special type
     */
    public String getSpecialType() {
        return specialType;
    }

    /**
     * Sets the special square type
     * @param specialType the type of the special square
     */
    public void setSpecialType(String specialType) {
        this.specialType = specialType;
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

}
