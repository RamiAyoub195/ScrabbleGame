import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * These are the test cases for the game ensuring that a word was placed properly and
 * the player scores are adjusted accordingly.
 *
 * Author(s): Rami Ayoub, Louis Pantazopoulos, Andrew Tawfik, Liam Bennet
 * Version: 3.0
 * Date: Tuesday, December 3, 2024
 */

public class GameModelTest {

    /**
     * Create a new game and test that the board can be retrieved, and begins empty
     */
    @Test
    public void testGetGameBoard(){ //Test get board by comparing the board in a new game to a new board
        Board board = new Board(15, 15);
        GameModel game = new GameModel();
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                assertEquals(board.getCell(i, j).getTile(), game.getGameBoard().getCell(i, j).getTile()); //Check each cell is equal
            }
        }
    }

    /**
     * Test that players can be properly added to and retrieved from the game
     */
    @Test
    public void testAddPlayer(){ //Test adding and getting players
        GameModel game = new GameModel();
        game.addPlayer("Louis"); //Add 4 players to the game
        game.addPlayer("Liam");
        game.addPlayer("Andrew");
        game.addPlayer("Rami");

        assertEquals("Louis", game.getPlayers().get(0).getName()); //Test if players are properly returned
        assertEquals("Liam", game.getPlayers().get(1).getName());
        assertEquals("Andrew", game.getPlayers().get(2).getName());
        assertEquals("Rami", game.getPlayers().get(3).getName());
    }

    /**
     * Test that the game is finished when the tileBag is empty and a player runs ou of tiles
     */
    @Test
    public void testIsGameFinished(){ //Test game finished
        GameModel game = new GameModel();
        game.addPlayer("Louis");
        game.addPlayer("Liam");
        assertFalse(game.isGameFinished()); //Test game finished is initially false
        for (int i = 0; i < 84; i++) {
            game.getTilesBag().bagArraylist().remove(0); //Empty the tiles bag
        }
        for (int i = 0; i < 7; i++) { //remove players tiles
            game.getPlayers().get(0).getTiles().remove(0);
        }

        assertEquals("Louis", game.getPlayers().get(0).getName()); //louis won the game as he has no more tiles

    }

    /**
     * Test that tiles place where the player wants them to
     */
    @Test
    public void testPlayerPlaceTile(){ //Test the function for placing tiles, and the various functions associated with placing tiles
        GameModel game = new GameModel();
        game.addPlayer("Rami");
        assertEquals(game.getPlayers().get(0).getScore(), 0); //Test that new player has 0 score
        ArrayList<Tiles> tiles = new ArrayList<>();
        tiles.add(new Tiles("T", 1));
        tiles.add(new Tiles("A", 1));
        tiles.add(new Tiles("N", 1));
        ArrayList<Integer> rowPositions = new ArrayList<>();
        rowPositions.add(7);
        rowPositions.add(7);
        rowPositions.add(7);
        ArrayList<Integer> colPositions = new ArrayList<>();
        colPositions.add(7);
        colPositions.add(8);
        colPositions.add(9);
        game.playerPlaceTile(game.getPlayers().get(0), tiles, rowPositions, colPositions);
        assertEquals(game.getPlayers().get(0).getScore(), 3); //Test that player got correct score for the word, as the score for tan is 3
        assertEquals(game.getGameBoard().getCell(7, 7).getTile().toString(), tiles.get(0).toString()); //Test if correct tiles are placed in correct board cells
        assertEquals(game.getGameBoard().getCell(7, 8).getTile().toString(), tiles.get(1).toString());
        assertEquals(game.getGameBoard().getCell(7, 9).getTile().toString(), tiles.get(2).toString());
    }

    /**
     * Test that the first word cannot be placed anywhere that is not the center
     */
    @Test
    public void testPlayerPlaceTileNotAtCenter(){ //Test if the first word can be placed elsewhere from the center
        GameModel game = new GameModel();
        game.addPlayer("Andrew");
        assertEquals(game.getPlayers().get(0).getScore(), 0); //Test that new player has 0 score
        ArrayList<Tiles> tiles = new ArrayList<>();
        tiles.add(new Tiles("T", 1));
        tiles.add(new Tiles("A", 1));
        tiles.add(new Tiles("N", 1));
        ArrayList<Integer> rowPositions = new ArrayList<>();
        rowPositions.add(4);
        rowPositions.add(4);
        rowPositions.add(4);
        ArrayList<Integer> colPositions = new ArrayList<>();
        colPositions.add(7);
        colPositions.add(8);
        colPositions.add(9);
        assertFalse(game.placeWord(tiles, rowPositions, colPositions)); //Test trying to place word away from center

        game.playerPlaceTile(game.getPlayers().get(0), tiles, rowPositions, colPositions); //Try again using the playerPlaceTile method
        assertEquals(game.getPlayers().get(0).getScore(), 0); //Test player's score remains 0, since the move is not allowed
        assertEquals(game.getGameBoard().getCell(4, 7).getTile(), null); //Test that no tiles are on the board where the illegal move was attempted to be played
        assertEquals(game.getGameBoard().getCell(4, 8).getTile(), null);
        assertEquals(game.getGameBoard().getCell(4, 9).getTile(), null);
    }

    /**
     * Test that tiles cannot be place if not adjacent to other words
     */
    @Test
    public void testPlaceNonAdjacentTiles(){
        GameModel game = new GameModel();
        game.addPlayer("Louis");
        game.addPlayer("Rami");

        ArrayList<Tiles> tiles1 = new ArrayList<>();
        tiles1.add(new Tiles("T", 1));
        tiles1.add(new Tiles("A", 1));
        tiles1.add(new Tiles("N", 1));
        ArrayList<Integer> rowPositions1 = new ArrayList<>();
        rowPositions1.add(7);
        rowPositions1.add(7);
        rowPositions1.add(7);
        ArrayList<Integer> colPositions1 = new ArrayList<>();
        colPositions1.add(7);
        colPositions1.add(8);
        colPositions1.add(9);
        game.playerPlaceTile(game.getPlayers().get(0), tiles1, rowPositions1, colPositions1); //Player 1 plays a legal move

        ArrayList<Tiles> tiles2 = new ArrayList<>();
        tiles2.add(new Tiles("S", 1));
        tiles2.add(new Tiles("A", 1));
        tiles2.add(new Tiles("D", 2));
        ArrayList<Integer> rowPositions2 = new ArrayList<>();
        rowPositions2.add(0);
        rowPositions2.add(0);
        rowPositions2.add(0);
        ArrayList<Integer> colPositions2 = new ArrayList<>();
        colPositions2.add(7);
        colPositions2.add(8);
        colPositions2.add(9);
        assertFalse(game.placeWord(tiles2, rowPositions2, colPositions2)); //Test placing non-adjacent word

        game.playerPlaceTile(game.getPlayers().get(1), tiles2, rowPositions2, colPositions2); //Try placing illegal move again
        assertEquals(game.getPlayers().get(1).getScore(), 0); //Test if players score remains 0 after attempting illegal move
        assertEquals(game.getGameBoard().getCell(0, 7).getTile(), null); //Test that no tiles are on the board where the illegal move was attempted to be played
        assertEquals(game.getGameBoard().getCell(0, 8).getTile(), null);
        assertEquals(game.getGameBoard().getCell(0, 9).getTile(), null);
    }

    /**
     * Test that score is properly calculated for all cases
     */
    @Test
    public void testGetTurnScore(){
        GameModel game = new GameModel();
        game.addPlayer("Louis");
        assertEquals(game.getPlayers().get(0).getScore(), 0); //Test that new player has 0 score
        ArrayList<Tiles> tiles = new ArrayList<>();
        tiles.add(new Tiles("T", 1));
        tiles.add(new Tiles("A", 1));
        tiles.add(new Tiles("N", 1));
        ArrayList<Integer> rowPositions = new ArrayList<>();
        rowPositions.add(7);
        rowPositions.add(7);
        rowPositions.add(7);
        ArrayList<Integer> colPositions = new ArrayList<>();
        colPositions.add(7);
        colPositions.add(8);
        colPositions.add(9);
        game.playerPlaceTile(game.getPlayers().get(0), tiles, rowPositions, colPositions);
        assertEquals(game.getPlayers().get(0).getScore(), 3);

        ArrayList<Tiles> tiles2 = new ArrayList<>(); //Extend tan to stan
        tiles2.add(new Tiles("S", 1));
        ArrayList<Integer> rowPositions2 = new ArrayList<>();
        rowPositions2.add(7);
        ArrayList<Integer> colPositions2 = new ArrayList<>();
        colPositions2.add(6);
        game.playerPlaceTile(game.getPlayers().get(0), tiles2, rowPositions2, colPositions2);
        assertEquals(game.getPlayers().get(0).getScore(), 7); //Test that 4 points are added by extending tan to stan

        ArrayList<Tiles> tiles3 = new ArrayList<>(); //Extend stan to standard
        tiles3.add(new Tiles("D", 2));
        tiles3.add(new Tiles("A", 1));
        tiles3.add(new Tiles("R", 1));
        tiles3.add(new Tiles("D", 2));
        ArrayList<Integer> rowPositions3 = new ArrayList<>();
        rowPositions3.add(7);
        rowPositions3.add(7);
        rowPositions3.add(7);
        rowPositions3.add(7);
        ArrayList<Integer> colPositions3 = new ArrayList<>();
        colPositions3.add(10);
        colPositions3.add(11);
        colPositions3.add(12);
        colPositions3.add(13);
        game.playerPlaceTile(game.getPlayers().get(0), tiles3, rowPositions3, colPositions3);
        assertEquals(game.getPlayers().get(0).getScore(), 18); //Test that 4 points are added by extending stan to standard

        ArrayList<Tiles> tiles4 = new ArrayList<>(); //Extend word in the vertical direction
        tiles4.add(new Tiles("A", 1));
        tiles4.add(new Tiles("N", 1));
        ArrayList<Integer> rowPositions4 = new ArrayList<>();
        rowPositions4.add(9);
        rowPositions4.add(8);
        ArrayList<Integer> colPositions4 = new ArrayList<>();
        colPositions4.add(7);
        colPositions4.add(7);
        game.playerPlaceTile(game.getPlayers().get(0), tiles4, rowPositions4, colPositions4);
        assertEquals(game.getPlayers().get(0).getScore(), 21); //Test that 4 points are added by extending the t in standard to ant vertically
    }

    /**
     * Test that tiles will be swapped with tilebag if tilebag is not empty
     */
    @Test
    public void testPlayerSwapTile(){
        GameModel game = new GameModel();
        game.addPlayer("Liam");
        assertEquals(game.getPlayers().get(0).getTiles().size(), 7); //Test new player has 7 tiles
        ArrayList<Integer> tileIndecies = new ArrayList<>();
        tileIndecies.add(0);
        tileIndecies.add(1);
        tileIndecies.add(2);
        game.playerSwapTile(game.getPlayers().get(0), tileIndecies);
        assertEquals(game.getPlayers().get(0).getTiles().size(), 7); //Test player is back at 7 tiles after swap
    }

    /**
     * Tests the save and load functionality for saving a game and reloading.
     */
    @Test
    public void testSaveAndLoadGame()
    {
        GameModel game = new GameModel(); //creates a game
        game.addPlayer("Louis"); //sets the players name and adds them
        game.addPlayer("Rami"); //sets the players name and adds them

        ArrayList<Tiles> tiles = new ArrayList<>();
        tiles.add(new Tiles("T", 1)); //tiles to be places as a test
        tiles.add(new Tiles("E", 1)); //tiles to be places as a test
        tiles.add(new Tiles("S", 1)); //tiles to be places as a test
        tiles.add(new Tiles("T", 1)); //tiles to be places as a test
        ArrayList<Integer> rowPositions = new ArrayList<>();
        rowPositions.add(7); //the row position
        rowPositions.add(7); //the row position
        rowPositions.add(7); //the row position
        rowPositions.add(7); //the row position
        ArrayList<Integer> colPositions = new ArrayList<>();
        colPositions.add(7); //the col position
        colPositions.add(8); //the col position
        colPositions.add(9); //the col position
        colPositions.add(10); //the col position

        game.playerPlaceTile(game.getPlayers().get(0), tiles, rowPositions, colPositions); //places the tiles

        String savedXML = game.toXML(); //saves the XML

        GameModel loadedGame = new GameModel(); //creates a new game
        loadedGame.fromXML(savedXML); //applies the XML taht was just created


        assertEquals(game.getPlayers().size(), loadedGame.getPlayers().size()); //checks to make sure the sizes of playesr are the same

        assertEquals(game.getPlayers().get(0).getName(), loadedGame.getPlayers().get(0).getName()); //checks the same name
        assertEquals(game.getPlayers().get(0).getScore(), loadedGame.getPlayers().get(0).getScore()); //checks the score name

        assertEquals(game.getPlayers().get(1).getName(), loadedGame.getPlayers().get(1).getName()); //checks the same name
        assertEquals(game.getPlayers().get(1).getScore(), loadedGame.getPlayers().get(1).getScore()); //checks the score name

        assertEquals(game.getPlayers().get(0).getTiles().size(), loadedGame.getPlayers().get(0).getTiles().size()); //checks the same tiles size
        assertEquals(game.getPlayers().get(1).getTiles().size(), loadedGame.getPlayers().get(1).getTiles().size()); //checks the same tiles size

        assertEquals(game.getGameBoard().getCell(7, 7).getTile().getLetter(), loadedGame.getGameBoard().getCell(7, 7).getTile().getLetter()); //checks t placement
        assertEquals(game.getGameBoard().getCell(7, 8).getTile().getLetter(), loadedGame.getGameBoard().getCell(7, 8).getTile().getLetter()); //checks e placement
        assertEquals(game.getGameBoard().getCell(7, 9).getTile().getLetter(), loadedGame.getGameBoard().getCell(7, 9).getTile().getLetter()); //checks s placement
        assertEquals(game.getGameBoard().getCell(7, 10).getTile().getLetter(), loadedGame.getGameBoard().getCell(7, 10).getTile().getLetter()); //checks t placement

        assertEquals(game.getTilesBag().bagArraylist().size(), loadedGame.getTilesBag().bagArraylist().size()); //checks the same size as the array list of tiles
    }

    /**
     * Tests the undo functionality in the game.
     */
    @Test
    public void testUndoGame(){

        GameModel game = new GameModel();
        game.addPlayer("Louis");
        assertEquals(game.getPlayers().get(0).getScore(), 0); //Test that new player has 0 score

        ArrayList<Tiles> tiles = new ArrayList<>();
        tiles.add(new Tiles("T", 1));
        tiles.add(new Tiles("A", 1));
        tiles.add(new Tiles("N", 1));

        ArrayList<Integer> rowPositions = new ArrayList<>();
        rowPositions.add(7);
        rowPositions.add(7);
        rowPositions.add(7);
        ArrayList<Integer> colPositions = new ArrayList<>();
        colPositions.add(7);
        colPositions.add(8);
        colPositions.add(9);

        game.playerPlaceTile(game.getPlayers().get(0), tiles, rowPositions, colPositions);
        assertEquals(game.getPlayers().get(0).getScore(), 3);

        assertFalse(game.stackOfBoardEmpty()); //makes sure it was added to the stack
        assertFalse(game.stackOfPlayerEmpty()); //makes sure it was added to the stack
        assertFalse(game.stackOfPlacedWordsEmpty()); //makes sure it was added to the stack
        assertFalse(game.stackOfPlayerScoresEmpty()); //makes sure it was added to the stack
        assertFalse(game.stackOfTilesBagEmpty()); //makes sure it was added to the stack
        assertFalse(game.stackOfPlayerTilesEmpty()); //makes sure it was added to the stack

        ArrayList<Tiles> tiles2 = new ArrayList<>(); //Extend tan to stan
        tiles2.add(new Tiles("S", 1));
        ArrayList<Integer> rowPositions2 = new ArrayList<>();
        rowPositions2.add(7);
        ArrayList<Integer> colPositions2 = new ArrayList<>();
        colPositions2.add(6);
        game.playerPlaceTile(game.getPlayers().get(0), tiles2, rowPositions2, colPositions2);
        assertEquals(game.getPlayers().get(0).getScore(), 7); //Test that 4 points are added by extending tan to stan

        assertEquals("Louis", game.getPlayerFromStack().getName());

        game.setBoardFromStack(); //sets the board from the stack back to tan only

        assertFalse(game.getGameBoard().getCell(7, 6).isOccupied()); //makes sure that the s is not there

        game.setPlayerScoresFromStack(); //sets the previous score

        assertEquals(game.getPlayers().get(0).getScore(), 3);

        game.setBoardFromStack(); //sets the board from the stack back to tan only

        assertFalse(game.getGameBoard().getCell(7, 7).isOccupied()); //makes sure that the t is not there
        assertFalse(game.getGameBoard().getCell(7, 8).isOccupied()); //makes sure that the a is not there
        assertFalse(game.getGameBoard().getCell(7, 9).isOccupied()); //makes sure that the n is not there

        game.setPlayerScoresFromStack(); //sets the previous score
        assertEquals(game.getPlayers().get(0).getScore(), 0);

    }

}