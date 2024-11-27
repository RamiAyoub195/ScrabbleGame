import java.util.*;
/**
 * The player class of the game, each player has a name, a set of tiles and
 * a score. Each player can play tile(s), can swap tile(s) or skip their turn.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
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
        StringBuilder xml = new StringBuilder("<Player>");
        xml.append("<Name>").append(name).append("</Name>")
                .append("<Score>").append(score).append("</Score>")
                .append("<Tiles>");
        for (Tiles tile : tiles) {
            xml.append(tile.toXML());
        }
        xml.append("</Tiles>")
                .append("</Player>");
        return xml.toString();
    }

    /**
     * Creates a Player object from its XML representation.
     *
     * @param xml The XML string representing the Player object.
     *            The XML must contain the player's name, score, and their tiles.
     * @return A Player object initialized with the data parsed from the XML.
     */
    public static Player fromXML(String xml) {
        String name = xml.substring(xml.indexOf("<Name>") + 6, xml.indexOf("</Name>"));
        int score = Integer.parseInt(xml.substring(xml.indexOf("<Score>") + 7, xml.indexOf("</Score>")));

        String tilesXML = xml.substring(xml.indexOf("<Tiles>") + 7, xml.indexOf("</Tiles>"));
        ArrayList<Tiles> tiles = new ArrayList<>();
        while (tilesXML.contains("<Tile>")) {
            int start = tilesXML.indexOf("<Tile>");
            int end = tilesXML.indexOf("</Tile>") + 7;
            tiles.add(Tiles.fromXML(tilesXML.substring(start, end)));
            tilesXML = tilesXML.substring(end);
        }

        Player player = new Player(name);
        player.addScore(score);
        player.tiles.addAll(tiles);
        return player;
    }




}
