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

}
