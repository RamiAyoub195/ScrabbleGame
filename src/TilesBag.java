import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains all tiles in a game, there are 100 tiles in a typical scrabble game according to Hasbro's online guide.
 * Each letter tile has a value associated with it which can also be found on Hasbro's online guide. This bag class will contain all 100 tiles
 * each having a letter and an associated value.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 *
 */

public class TilesBag {

    private HashMap<String, Integer> scrabbleTiles; //This will store how many letter tiles we have in the game
    private ArrayList<Tiles> tilesBag; //will store each letter tile and its associated value

    /**
     * This is the constructor of the class that will be adding all the tiles in the game to a hashtable, where each key is
     * a letter and the value is how many of those letters we have. Each letter will be assigned its associated value and
     * added to an ArrayList.
     */
    public TilesBag() {
        scrabbleTiles = new HashMap();
        tilesBag = new ArrayList();

        scrabbleTiles.put("A", 9); //All of these can be found according to Hasbro online, we have 9 A's in a game, 2 B's in a game and so on...
        scrabbleTiles.put("B", 2);
        scrabbleTiles.put("C", 2);
        scrabbleTiles.put("D", 4);
        scrabbleTiles.put("E", 12);
        scrabbleTiles.put("F", 2);
        scrabbleTiles.put("G", 3);
        scrabbleTiles.put("H", 2);
        scrabbleTiles.put("I", 9);
        scrabbleTiles.put("J", 1);
        scrabbleTiles.put("K", 1);
        scrabbleTiles.put("L", 4);
        scrabbleTiles.put("M", 2);
        scrabbleTiles.put("N", 6);
        scrabbleTiles.put("O", 8);
        scrabbleTiles.put("P", 2);
        scrabbleTiles.put("Q", 1);
        scrabbleTiles.put("R", 6);
        scrabbleTiles.put("S", 4);
        scrabbleTiles.put("T", 6);
        scrabbleTiles.put("U", 4);
        scrabbleTiles.put("V", 2);
        scrabbleTiles.put("W", 2);
        scrabbleTiles.put("X", 1);
        scrabbleTiles.put("Y", 2);
        scrabbleTiles.put("Z", 1);
        scrabbleTiles.put(" ", 2);

        //Assigns each letter their associated value based on information online (Hasbro's online guide for scrabble)
        for(HashMap.Entry<String, Integer> s: scrabbleTiles.entrySet()){
            if (s.getKey().equals(" ")) {
                for (int i = 0; i < s.getValue(); i++) {
                    tilesBag.add(new Tiles(s.getKey(), 0));
                }
            }
            if (s.getKey().equals("A") || s.getKey().equals("E") || s.getKey().equals("I") || s.getKey().equals("O") ||
                    s.getKey().equals("U") || s.getKey().equals("L") || s.getKey().equals("N")|| s.getKey().equals("S") || s.getKey().equals("T") ||
                    s.getKey().equals("R")){
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 1));
                }
            }
            else if (s.getKey().equals("D") || s.getKey().equals("G")) {
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 2));
                }
            }
            else if (s.getKey().equals("B") || s.getKey().equals("C") || s.getKey().equals("M") || s.getKey().equals("P")) {
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 3));
                }
            }
            else if (s.getKey().equals("F") || s.getKey().equals("H") || s.getKey().equals("V") ||
                    s.getKey().equals("W") || s.getKey().equals("Y")) {
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 4));
                }
            }
            else if (s.getKey().equals("K")) {
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 5));
                }
            }
            else if (s.getKey().equals("J") || s.getKey().equals("X")) {
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 8));
                }
            }
            else{
                for (int i = 0; i < s.getValue(); i++){
                    tilesBag.add(new Tiles(s.getKey(), 10));
                }
            }
        }
    }

    /**
     * Returns the array list of tiles that we formed in the constructor
     * @return Arraylist of tiles
     */
    public ArrayList<Tiles> bagArraylist(){
        return tilesBag;
    }

    /**
     * Checks to see if the bag of tiles is empty.
     * @return true if bag is empty, false otherwise.
     */
    public boolean bagOfTileIsEmpty(){
        return tilesBag.isEmpty();
    }

}

