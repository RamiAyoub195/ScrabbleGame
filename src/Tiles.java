/**
 * This class is the tiles class which is placed on the board of the scrabble game. Each tile has a letter and
 * an associated value. When a word is formed, the values of the letter are added up to give a score to the player.
 *
 * Author: Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */

public class Tiles{

    private String letter; //The letter of the tile
    private int value; //The associated value of the letter

    /**
     * This is the constructor of the class that forms a tile. Each tile has a
     * letter and a value.
     *
     * @param letter the letter of the tile
     * @param value the value of the tile
     */
    public Tiles(String letter, int value) {
        this.letter = letter;
        this.value = value;
    }

    /**
     * Sets the letter on a tile. Is used to set the letter on a blank tile or reset the tile back
     * to a blank tile.
     * @param letter to set on the tile.
     */
    public void setLetter(String letter) {
        this.letter = letter;
    }

    /**
     * Returns the letter of a tile.
     * @return letter tile
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Returns the value of a tile.
     * @return value tile
     */
    public int getNumber() {
        return value;
    }

    /**
     * Will return a string representation of the letter and its associated value for a tile
     */
    @Override
    public String toString() {
        return this.letter + ":" + this.value + " ";
    }

    /**
     * Converts the Tiles object into its XML representation.
     *
     * @return A string representing the Tiles object in XML format.
     * The XML includes the tile's letter and value.
     */
    public String toXML()
    {
        return "<Tile><Letter>" + letter + "</Letter><Value>" + value + "</Value></Tile>"; //the XMl with the players letter and value
    }

    /**
     * Creates a Tiles object from its XML representation.
     *
     * @param xml The XML string representing the Tiles object.
     *            The XML must contain the tile's letter and value.
     * @return A Tiles object initialized with the data parsed from the XML.
     */
    public static Tiles fromXML(String xml)
    {
        String letter = xml.substring(xml.indexOf("<Letter>") + 8, xml.indexOf("</Letter>")); //gets the letter between the tags
        int value = Integer.parseInt(xml.substring(xml.indexOf("<Value>") + 7, xml.indexOf("</Value>"))); //gets the value beween the tags
        return new Tiles(letter, value); //returns a new tile created
    }
}
