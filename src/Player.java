import java.util.*;
/**
 * The player class of the game, each player has a name, a set of tiles and
 * a score. Each player can play tile(s), can swap tile(s) or skip their turn.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 2.0
 * Date: Wednesday, November 6, 2024
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
     * A list of the players tiles
     * @return player tiles
     */
    public ArrayList<Tiles> getTiles() {
        return tiles;
    }

    /**
     * Returns the name of a player
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the score of the player
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Displays a player including their name, their tiles and current score.
     */
    public void displayPlayer(){
        System.out.print(name + "'s " + "Tiles: ");
        for(Tiles t : tiles){
            System.out.print(t.toString());
        }
        System.out.println(name + "'s score: " + score);
    }

    /**
     * Displays the tiles of a player
     */
    public void displayPlayerTiles(){
        for(Tiles t : tiles){
            System.out.print(t.toString());
        }
    }

    /**
     * Adds the score of a player with their current score
     * @param score the score to be added to the current score
     */
    public void addScore(int score) {
        this.score += score;
    }

}
