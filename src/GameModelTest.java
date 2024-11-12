import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
                assertEquals(board.getCell(i, j).toString(), game.getGameBoard().getCell(i, j).toString()); //Check each cell is equal
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
            game.getPlayers().get(1).getTiles().remove(0);
        }
        assertTrue(game.isGameFinished()); //Test isGameFinished when there are no more tiles
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
        assertFalse(game.placeWord(tiles, rowPositions, colPositions, game.getPlayers().get(0))); //Test trying to place word away from center

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
        assertFalse(game.placeWord(tiles2, rowPositions2, colPositions2, game.getPlayers().get(1))); //Test placing non-adjacent word

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
        assertEquals(game.getPlayers().get(0).getScore(), 17); //Test that 4 points are added by extending stan to standard

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
        assertEquals(game.getPlayers().get(0).getScore(), 20); //Test that 4 points are added by extending the t in standard to ant vertically
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
}