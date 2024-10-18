import java.util.*;


/**
 * This class is the board class which will display the board as the game goes along.
 * It will check to make sure that a tile can be put in a specific area of the board.
 * Will update the board when a tile has been added.
 *
 * Author: Rami Ayoub
 * Student Number: 101261583
 * Date: October 15th, 2024
 *
 */

public class Player {

    private String name;
    private ArrayList<Tiles> tiles;
    private int score;

    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Tiles> getTiles() {
        return tiles;
    }

    public void displayTiles(){
        System.out.println(name + "'s " + "Tiles: ");
        for(Tiles t : tiles){
            System.out.print(t.getLetter() + ":" + t.getNumber() + " ");
        }
    }

    public int getScore() {
        return score;
    }
}
