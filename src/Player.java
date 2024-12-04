import java.util.*;
/**
 * The player class of the game, each player has a name, a set of tiles and
 * a score. Each player can play tile(s), can swap tile(s) or skip their turn.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 5.0
 * Date: Tuesday, December 3, 2024
 *
 */

public class Player {

    private String name; //player name
    private ArrayList<Tiles> tiles; //player tiles
    private int score; //player score

    /**
     * Initializes a player with a name, empty set of tiles and a score of 0
     * @param name name of the player
     */
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Sets the tiles of the player.
     * @param tiles the array list of player tiles to be set
     */
    public void setTiles(ArrayList<Tiles> tiles) {
        this.tiles = tiles;
    }

    /**
     * Returns the list of the players tiles.
     * @return player tiles
     */
    public ArrayList<Tiles> getTiles() {
        return tiles;
    }

    /**
     * Returns the name of a player.
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the score of a player.
     * @param score the score being set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the score of the player.
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds the score of a player with their current score.
     * @param score the score to be added to the current score
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Converts the Player object into its XML representation.
     *
     * @return A string representing the Player object in XML format.
     * The XML includes the player's name, score, and their tiles.
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder("<Player>"); //creates the player tag
        xml.append("<Name>").append(name).append("</Name>") //creates tha name tag with the players name
                .append("<Score>").append(score).append("</Score>") //creates the score tag with the players score
                .append("<Tiles>"); //creates the tag for the players tile
        for (Tiles tile : tiles) { //travreses through the players tiles
            xml.append(tile.toXML()); //calls the xml method for the tile and append it
        }
        xml.append("</Tiles>") //closes the tag for the tiles
                .append("</Player>"); //closes the tag for the players
        return xml.toString(); //returns the XMl string
    }

    /**
     * Creates a Player object from its XML representation.
     *
     * @param xml The XML string representing the Player object.
     *            The XML must contain the player's name, score, and their tiles.
     * @return A Player object initialized with the data parsed from the XML.
     */
    public static Player fromXML(String xml) {
        String name = xml.substring(xml.indexOf("<Name>") + 6, xml.indexOf("</Name>")); //gets the name tag with the playres name
        int score = Integer.parseInt(xml.substring(xml.indexOf("<Score>") + 7, xml.indexOf("</Score>"))); //gets the score tag with th players score

        String tilesXML = xml.substring(xml.indexOf("<Tiles>") + 7, xml.indexOf("</Tiles>")); //gets the tiles tag and the playres tag
        ArrayList<Tiles> tiles = new ArrayList<>(); //creates an arraylist of tiles
        while (tilesXML.contains("<Tile>")) { //traverses through the tag of tiles
            int start = tilesXML.indexOf("<Tile>"); //the start tag of the tile
            int end = tilesXML.indexOf("</Tile>") + 7; //end tag of the tile
            tiles.add(Tiles.fromXML(tilesXML.substring(start, end))); //calls the xml method in the tiles class then add it to the array list
            tilesXML = tilesXML.substring(end); //moves to the next tag
        }

        Player player = new Player(name); //creates the player
        player.addScore(score); //adds their score
        player.tiles.addAll(tiles); //adds the tiles of the player
        return player; //returns the player
    }

}
