import java.util.*;

/**
 * This class contains all tiles in a game, there are 100 tiles in a typical scrabble game according to Hasbro's online guide.
 * Each letter tile has a value associated with it which can also be found on Hasbro's online guide. This bag class will contain all 100 tiles
 * each having a letter and an associated value, it will also contain the two blank spaces.
 *
 * How many tiles in the game and the quantity of each tile letter: https://hasbro-new.custhelp.com/app/answers/detail/a_id/19/~/how-many-of-each-letter-tile-are-included-in-a-scrabble-game%3F
 * The value of each tile letter: https://hasbro-new.custhelp.com/app/answers/detail/a_id/55/related/1/session/L2F2LzEvdGltZS8xNzMyMjk4ODc3L2dlbi8xNzMyMjk4ODc3L3NpZC9mVUtjZkRYRUUxS3AlN0VVTEU0Slhidm1TcE5OaWdIOGN5dFVxU2Q1X0tyWFFucFRjZ3ZXdjFmeXQlN0VTSkY0JTdFUUZMMEdoanhaVjU0SjdBRHhYZEZtXzFWN2tuY2M4JTdFcjZLUXZrNXhVX01wN0hGWVRDXzU0bGhjTFJQUSUyMSUyMQ==
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 5.0
 * Date: Tuesday, December 3, 2024
 *
 */

public class TilesBag {

    private static final String TILES_VALUE_1 = "AEIOULNRST"; //these are the letter tiles with the values of 1
    private static final String TILES_VALUE_2 = "DG"; //these are the letter tiles with the values of 2
    private static final String TILES_VALUE_3 = "BCMP"; //these are the letter tiles with the values of 3
    private static final String TILES_VALUE_4 = "FHVWY"; //these are the letter tiles with the values of 4
    private static final String TILES_VALUE_5 = "K"; //these are the letter tiles with the values of 5
    private static final String TILES_VALUE_8 = "JX"; //these are the letter tiles with the values of 8
    private static final String TILES_VALUE_10 = "QZ"; //these are the letter tiles with the values of 10
    private HashMap<String, Integer> scrabbleTileQuantities; //stores the quantity of the tile letter in the game
    private ArrayList<Tiles> tilesBag; //will store all tiles after they are created

    /**
     * This is the constructor of the class that will be adding all the tiles in the game to a hashtable, where each key is
     * a letter and the value is how many of those letters we have. Each letter will be assigned its associated value and
     * added to an ArrayList of tiles which will have all tiles in the game.
     */
    public TilesBag() {
        scrabbleTileQuantities = new HashMap(); //initializes the hashmap
        tilesBag = new ArrayList(); //initializes the arraylist

        scrabbleTileQuantities.put("A", 9); //All of these can be found according to Hasbro online, we have 9 A's tiles in a game, 2 B's tiles in a game and so on...
        scrabbleTileQuantities.put("B", 2);
        scrabbleTileQuantities.put("C", 2);
        scrabbleTileQuantities.put("D", 4);
        scrabbleTileQuantities.put("E", 12);
        scrabbleTileQuantities.put("F", 2);
        scrabbleTileQuantities.put("G", 3);
        scrabbleTileQuantities.put("H", 2);
        scrabbleTileQuantities.put("I", 9);
        scrabbleTileQuantities.put("J", 1);
        scrabbleTileQuantities.put("K", 1);
        scrabbleTileQuantities.put("L", 4);
        scrabbleTileQuantities.put("M", 2);
        scrabbleTileQuantities.put("N", 6);
        scrabbleTileQuantities.put("O", 8);
        scrabbleTileQuantities.put("P", 2);
        scrabbleTileQuantities.put("Q", 1);
        scrabbleTileQuantities.put("R", 6);
        scrabbleTileQuantities.put("S", 4);
        scrabbleTileQuantities.put("T", 6);
        scrabbleTileQuantities.put("U", 4);
        scrabbleTileQuantities.put("V", 2);
        scrabbleTileQuantities.put("W", 2);
        scrabbleTileQuantities.put("X", 1);
        scrabbleTileQuantities.put("Y", 2);
        scrabbleTileQuantities.put("Z", 1);
        scrabbleTileQuantities.put(" ", 2); //the blank tiles

        //Assigns each letter their associated value based on information online (Hasbro's online guide for scrabble).
        scrabbleTileQuantities.forEach((letter, quantity) -> { //does a for each loop for each key and value in the hashmap
            int value = getTileValue(letter); //gets the value associated to the letter
            for (int i = 0; i < quantity; i++) { //traverses through the quality of the letter that are in the game
                tilesBag.add(new Tiles(letter, value)); //creates a new tile with its letter and its associated value and adds it to the array list of tiles
            }
        });
    }

    /**
     * Another constructor that creates a next Tiles Bag after the previous one was already created this
     * ensures that the undo funtion works correctly.
     * @param tilesBag the bag of tiles
     */
    public TilesBag (ArrayList<Tiles> tilesBag) {
        this.tilesBag = new ArrayList<>(); //creates a new array list bag for the tiles bag

        for (Tiles tile : tilesBag) { //travreses through the previous tiles bag
            this.tilesBag.add(new Tiles(tile.getLetter(), tile.getNumber())); //stes the new tiles form the old tines into the tiles bag
        }
    }

    /**
     * Returns the value associated with the tile letter.
     */
    private int getTileValue(String tileLetter) {
        if(TILES_VALUE_1.contains(tileLetter)){return 1;} //returns 1 if the tile letter is in the tile 1 value
        else if(TILES_VALUE_2.contains(tileLetter)){return 2;} //returns 2 if the tile letter is in the tile 2 value
        else if(TILES_VALUE_3.contains(tileLetter)){return 3;} //returns 3 if the tile letter is in the tile 3 value
        else if(TILES_VALUE_4.contains(tileLetter)){return 4;} //returns 4 if the tile letter is in the tile 4 value
        else if(TILES_VALUE_5.contains(tileLetter)){return 5;} //returns 5 if the tile letter is in the tile 5 value
        else if(TILES_VALUE_8.contains(tileLetter)){return 8;} //returns 8 if the tile letter is in the tile 8 value
        else if(TILES_VALUE_10.contains(tileLetter)){return 10;} //returns 10 if the tile letter is in the tile 10 value
        else {
            return 0; //return 0 for the blank tile value
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

    /**
     * Sets the array list of tiles.
     * @param tilesBag the bag of tiles
     */
    public void setTilesBag(ArrayList<Tiles> tilesBag) {
        this.tilesBag = tilesBag;
    }


    /**
     * Converts the TilesBag object into its XML representation.
     *
     * @return A string representing the TilesBag object in XML format.
     * The XML includes all tiles currently in the bag.
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder("<TilesBag>"); //creates the tag of teh tile bag
        for (Tiles tile : tilesBag) { //traverses through the tiles of the tile bag
            xml.append(tile.toXML()); //appends it and calls the xml method in the tile class
        }
        xml.append("</TilesBag>"); //closes the tag of the tiles bag
        return xml.toString(); //returns the xml string
    }

    /**
     * Creates a TilesBag object from its XML representation.
     *
     * @param xml The XML string representing the TilesBag object.
     *            The XML must contain a list of Tile objects.
     * @return A TilesBag object initialized with the data parsed from the XML.
     */
    public static TilesBag fromXML(String xml) {
        TilesBag tilesBag = new TilesBag(); //creates a new tile bag
        tilesBag.tilesBag.clear();

        String tilesXML = xml.substring(xml.indexOf("<TilesBag>") + 10, xml.indexOf("</TilesBag>")); //get the tiles between the tag
        while (tilesXML.contains("<Tile>")) {
            int start = tilesXML.indexOf("<Tile>"); //teh start of the tag
            int end = tilesXML.indexOf("</Tile>") + 7; //the end of the tag
            tilesBag.tilesBag.add(Tiles.fromXML(tilesXML.substring(start, end))); //creates a new tile and adds it to the tiles bag array list of tiles
            tilesXML = tilesXML.substring(end);
        }
        return tilesBag; //returns the created tiles bag
    }


}