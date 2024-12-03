/**
 * This class represents a cell on the board, which can be either occupied or empty by a tile.
 * A cell can also have a special type meaning it's a TWS, DWS, etc.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */
public class Cell
{

    private Tiles tile; // The tile on the cell
    private String specialType; //will indicate if the cell is a premium square or not such as TWS, DLS, etc.

    /**
     * The constructor of the class. A cell can be occupied by a tile and can be a special square.
     */
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

    /**
     * Converts the Cell object into its XML representation.
     *
     * @return A string representing the Cell object in XML format.
     * The XML includes the tile (if any) and the special type of the cell (if any).
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder("<Cell>"); //creates the cell tag
        if (tile != null) { //if the cell is occupied
            xml.append(tile.toXML()); //calls and appends the toXMl from the tile class
        }
        if (specialType != null) { //if it's a special square
            xml.append("<SpecialType>").append(specialType).append("</SpecialType>"); //appends the special type of the cell
        }
        xml.append("</Cell>"); //closes of the cell tag
        return xml.toString(); //returns the XML string
    }

    /**
     * Creates a Cell object from its XML representation.
     *
     * @param xml The XML string representing the Cell object.
     *            The XML must include the tile (if any) and the special type (if any).
     * @return A Cell object initialized with the data parsed from the XML.
     */
    public static Cell fromXML(String xml) {
        Cell cell = new Cell(); //creates a new cell

        if (xml.contains("<Tile>")) { //if there is a tile
            String tileXML = xml.substring(xml.indexOf("<Tile>"), xml.indexOf("</Tile>") + 7); //creates the tile tag with the tile values
            cell.placeTile(Tiles.fromXML(tileXML)); //calls the XML method of the tile and places that tile
        }
        if (xml.contains("<SpecialType>")) { //if there is a special type
            String specialType = xml.substring(xml.indexOf("<SpecialType>") + 13, xml.indexOf("</SpecialType>")); //creates the tile tag with the special type
            cell.setSpecialType(specialType); //sets the special type of the cell
        }
        return cell; //returns the created cell
    }

}
