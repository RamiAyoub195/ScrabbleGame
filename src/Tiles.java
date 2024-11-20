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
}
