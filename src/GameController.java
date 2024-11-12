import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This is the game controller which will communicate the view with the model of the game.
 * The controller will take care of any buttons pressed in the view and send the data to the model. It will
 * also return the logic from the model to the view updating the scores, words placed, etc.
 *
 * Author(s): Liam Bennet, Rami Ayoub
 * Version: 2.0
 * Date: Wednesday, November 6, 2024
 */

public class GameController implements ActionListener {
    GameModel model;
    GameView view;
    private ArrayList<Integer> listOfRows;
    private ArrayList<Integer> listOfCols;
    private ArrayList<Tiles> listOfTiles;
    private ArrayList<Integer> tilesToSwapIndices;
    private int numTimesSwapClicked = 0;
    boolean aTileIsSelected = false;
    boolean swapTileSelected = false;
    private Player currentPlayer;
    private int selectedTileCol = 0;
    private int currentTurn = 0;
    private int numPlayers;
    private int numTilesInBag;
    private int numTilesPlacedThisTurn = 0;
    private int tileIndex;
    private String newestWord;
    private int newestScore;
    private ArrayList<String> currentPlayerNames;
    private ArrayList<String> wordsInGame;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        listOfRows = new ArrayList<Integer>();
        listOfCols = new ArrayList<Integer>();
        listOfTiles = new ArrayList<Tiles>();
        tilesToSwapIndices = new ArrayList<Integer>();

        wordsInGame = new ArrayList<>();

        for(String playerName : view.getPlayerNames()){
            model.addPlayer(playerName); //creates players in the game model after getting the names from the view
        }

        numTilesInBag = 100 - (7 * view.getPlayerNames().size()); // Get size of tiles bag initially
        numPlayers = view.getNumPlayers(); // Get number of players
        model.setNumPlayers(numPlayers);   // Set number of players in model
        currentPlayer = model.getCurrentPlayer(currentTurn); // Update current player in model to start game

        view.setUpPlayerTilesPanel(model.getPlayers().get(0)); // Set up the first player's tiles
        currentPlayerNames = view.getPlayerNames();
        this.view.setAllButtonsActionListener(this); //sets the controller as the action listener for all buttons in the view

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getPlayButton()) {
            playButtonAction();
        }
        else if (e.getSource() == view.getSwapButton()) {
            swapButtonAction();
        }
        else if (e.getSource() == view.getPassButton()) {
            passButtonAction();
        }
        // Gets what tile was selected
        else if (e.getSource() == view.getSpecificPlayerTileButton(0)) {
            handleSpecificTileButtonAction(0);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(1)) {
            handleSpecificTileButtonAction(1);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(2)) {
            handleSpecificTileButtonAction(2);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(3)) {
            handleSpecificTileButtonAction(3);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(4)) {
            handleSpecificTileButtonAction(4);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(5)) {
            handleSpecificTileButtonAction(5);
        }
        else if (e.getSource() == view.getSpecificPlayerTileButton(6)) {
            handleSpecificTileButtonAction(6);
        }

        else {
            for (int row = 0; row < 16; row++) {
                for (int col = 0; col < 16; col++) {
                    if (e.getSource() == view.getSpecificBoardField(row, col)) {
                        // add letter to this row/col in model and view
                        handleSpecificBoardFieldAction(row, col);
                    }
                }
            }
        }
    }



    private void playButtonAction() {
        // confirms we are done current turn
        // must call model to check the new word validity
        // if valid set the new word green, next player turn
        // if not valid return the tiles to player hand and remove from board, next player turn
        // handle removing tiles from hand and replacing with new random ones from model
        // then update view with new tiles

        // if word is not valid
        if (model.checkValidWord()) {
            // check that the entire board's tiles are connected
            if (model.getCheckBoard().checkMiddleBoardEmpty()) {
                handleError("Middle square must be covered!");
                model.updateCheckBoard();
            }
            else if (!model.getCheckBoard().checkAdjacency()) {
                handleError("Word must be connected to another word!");
                model.updateCheckBoard();
            }
            else {
                for (int row = 0; row < 16; row++) {
                    for (int col = 0; col < 16; col++) {
                        if (view.getSpecificBoardFieldColour(row, col) == Color.GRAY) {
                            view.setSpecificBoardFieldColour(row, col, Color.GREEN);
                        }
                    }
                }

                model.updateGameBoard();
                // remove tiles from player hand in model
                model.removeTilesFromPlayerHand();
                model.getRandomTiles(numTilesPlacedThisTurn, model.getCurrentPlayer(currentTurn));
                // add the letter to model
                model.printCheckBoard();
                model.printGameBoard();
                // update words placed, player score, tiles in bag
                numTilesInBag = numTilesInBag - numTilesPlacedThisTurn;
                view.updateBagTilesCount(numTilesInBag);
                // update player tiles

                view.resetPlayerTile();
                newestWord = model.getNewestWord();
                System.out.println(newestWord);
                System.out.println(newestWord);
                newestScore = model.getScore(newestWord);

                view.addToWordArea(newestWord);
                newestWord = "";
                String currentPlayerName = currentPlayerNames.get(currentTurn % numPlayers);
                view.updatePlayerScore(currentPlayerName, newestScore);

                nextTurn();

            }
        }
        else {
            model.updateCheckBoard();
            handleError("Invalid word!");
        }
        model.printCheckBoard();
        model.printGameBoard();
        listOfTiles.clear();
        listOfRows.clear();
        listOfCols.clear();
    }

    private void swapButtonAction() {
        // if swap button has been clicked
        if (numTimesSwapClicked % 2 == 0) {
            view.displayMessageToPlayer("Select all tiles to swap then click swap button again");
            swapTileSelected = true;
            view.resetPlayerTile();
        }
        else {
            model.playerSwapTile(model.getCurrentPlayer(currentTurn), tilesToSwapIndices);
            view.resetPlayerTile();
            view.updatePlayerTiles(model.getCurrentPlayer(currentTurn));
            nextTurn();
            view.enableAllBoardFields();
            swapTileSelected = false;
        }
        numTimesSwapClicked++;
    }

    private void passButtonAction() {
        // proceed to next player turn
        nextTurn();
        view.resetPlayerTile();
    }

    private void handleSpecificTileButtonAction(int col) {
        // if swap tile has been selected
        if (swapTileSelected) {
            view.disableAllBoardFields();
            view.setPlayerTilesColour(col, Color.ORANGE);
            tilesToSwapIndices.add(col);
        }

        // if swap tile has not been selected (normal play)
        else {
            // update tile index
            tileIndex = col;
            // Check if selected tile is gray
            if (view.getSpecificPlayerTileColour(col) == Color.GRAY) {
                // do nothing
            }
            // Make sure the other tiles are light gray when not selected
            else {
                for (int index = 0; index < 7; index++) {
                    if (view.getSpecificPlayerTileColour(index) != Color.GRAY) {
                        view.setPlayerTilesColour(index, Color.LIGHT_GRAY);
                    }
                }
                // Set orange to indicate that tile is currently selected
                view.setPlayerTilesColour(col, Color.ORANGE);

                // now we need to set a flag that a current tile is selected
                aTileIsSelected = true;

                // set selected tile column number
                selectedTileCol = col;
            }
        }
    }

    private void handleSpecificBoardFieldAction(int row, int col) {
        // add temp tile to board
        if (aTileIsSelected) {
            // get selected tile letter
            String tileLetter = view.getSpecificPlayerTileButton(selectedTileCol).getText();
            // add tile to view board and model board
            // add only if current spot is not GREEN (solidified letter) or GRAY (temp letter)
            if (view.getSpecificBoardFieldColour(row, col) != Color.GREEN && view.getSpecificBoardFieldColour(row, col) != Color.GRAY) {
                view.setSpecificBoardFieldLetter(row, col, tileLetter); // Set button field letter
                view.setSpecificBoardFieldColour(row, col, Color.GRAY); // Set button field colour (temp letter)
                view.setPlayerTilesColour(selectedTileCol, Color.GRAY); // Mark tile as used on board (gray)
                // Set tile colours
                for (int index = 0; index < 7; index++) {
                    if (view.getSpecificPlayerTileColour(index) != Color.GRAY) {
                        view.setPlayerTilesColour(index, Color.LIGHT_GRAY);
                    }
                }
                aTileIsSelected = false;
                // add letter to model and add letter score to model
                // we need to split the string into letter and score
                Tiles tile = model.getPlayerTile(selectedTileCol); // get tile from players hand at specified spot
                model.addTile(row, col, tile); // Add tile to the model board
                model.addTileToRemove(tile); // add to list of tiles to remove from hand
                numTilesPlacedThisTurn++;
                model.printCheckBoard();
            }
            // otherwise do not add tile to that spot (do nothing)
            else {
                // do nothing
            }
        }
        else {
            // do nothing
        }
    }



    private void nextTurn() {
        currentTurn++;
        view.updatePlayerTiles(model.getCurrentPlayer(currentTurn));
        numTilesPlacedThisTurn = 0;
    }

    private void handleError(String errorMessage) {
        // error message
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if (view.getSpecificBoardFieldColour(row, col) == Color.GRAY) {
                    view.removeTempTilesOnBoardField(row, col);
                }
            }
        }
        view.displayMessageToPlayer(errorMessage);

        // Next turn
        // nextTurn();
        view.resetPlayerTile();
    }

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
    }
}
