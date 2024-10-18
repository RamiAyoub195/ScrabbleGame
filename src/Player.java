import java.util.*;
/**
 *
 *
 * Author:
 * Student Number:
 * Date:
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

    public void displayPlayer(){
        System.out.print(name + "'s " + "Tiles: ");
        for(Tiles t : tiles){
            System.out.print(t.toString());
        }
        System.out.println(name + "'s score: " + score);
    }

    public void displayTiles(){
        System.out.print(name + "'s " + "Tiles: ");
        for(Tiles t : tiles){
            System.out.print(t.toString());
        }
    }

    public int getScore() {
        return score;
    }
}
