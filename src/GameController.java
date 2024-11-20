import java.awt.event.*;
import java.util.*;
import java.awt.*;

/**
 * This is the game controller which will communicate the view with the model of the game.
 * The controller will take care of any buttons pressed in the view and send the data to the model. It will
 * also return the logic from the model to the view updating the scores, words placed, etc.
 *
 * Author(s): Rami Ayoub, Louis Pantazopoulos, Andrew Tawfik, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */

public class GameController implements ActionListener {
    private GameModel model; //represents an instance of the model
    private GameView view; //represents an instance of the view
    private int numTimesSwapClicked; //the number of times swap has been clicked
    private int currentTurn; //who's current turn to play the game
    boolean swapTileSelected; //if the button was clicked to swap
    private ArrayList<Integer> tilesToSwapIndices; //the tiles index to be swapped
    private int numTilesPlacedThisTurn;  //the number of tiles paced by a player per turn
    private int tileIndex; //the tile index
    private ArrayList<Integer> listOfRows; //the row of the tile in board
    private ArrayList<Integer> listOfCols; //the col of the tile in board
    private ArrayList<Tiles> listOfTiles; //the list of the tiles selected
    private boolean aTileIsSelected; //if a tile is selected
    private int selectedTileCol; //selected tiles col
    private int tilesInBag; //the amount of tiles left in the bag

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.numTimesSwapClicked = 0;
        this.currentTurn = 0;
        this.swapTileSelected = false;
        this.tilesToSwapIndices = new ArrayList<>();
        this.numTilesPlacedThisTurn = 0;
        this.tileIndex = 0;
        this.listOfRows = new ArrayList<>();
        this.listOfCols = new ArrayList<>();
        this.listOfTiles = new ArrayList<>();
        this.aTileIsSelected = false;
        this.selectedTileCol = 0;

        for(String playerName : view.getPlayerNames()){
            model.addPlayer(playerName); //creates players in the game model after getting the names from the view
        }

        view.setUpPlayerTilesPanel(model.getPlayers().get(0)); // Set up the first player's tiles

        this.tilesInBag = model.getTilesBag().bagArraylist().size();

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
            for (int row = 0; row < 15; row++) {
                for (int col = 0; col < 15; col++) {
                    if (e.getSource() == view.getSpecificBoardCell(row, col)) {
                        // add letter to this row/col in model and view
                        handleSpecificBoardCellAction(row, col);
                    }
                }
            }
        }
    }

    public void playButtonAction() {
        model.checkPlaceableWord(listOfTiles, listOfRows, listOfCols);
        if (model.getStatusMessage().equals("Word placed successfully.")){
            model.playerPlaceTile(model.getPlayers().get(currentTurn % model.getPlayers().size()), listOfTiles, listOfRows, listOfCols);
            for(int i = 0; i < listOfTiles.size(); i++){
                view.updateBoardCell(listOfTiles.get(i), listOfRows.get(i), listOfCols.get(i));
            }

            tilesInBag -= listOfTiles.size();
            view.updateBagTilesCount(tilesInBag);
            view.resetPlayerTile();
            view.updatePlayerScore((model.getPlayers().get(currentTurn % model.getPlayers().size())).getName(), (model.getPlayers().get(currentTurn % model.getPlayers().size()).getScore()));
            view.addToWordArea(model.getPlacedWords());
            view.updateWordCount(model.getPlacedWords());
            nextTurn();
        }
        else {
            model.updateCheckBoard();
            handleError(model.getStatusMessage());
        }
        listOfTiles.clear();
        listOfRows.clear();
        listOfCols.clear();
    }

    /**
     * Handles swap button
     */
    public void swapButtonAction() {
        // if swap button has been clicked
        if (numTimesSwapClicked % 2 == 0) {
            view.displayMessageToPlayer("Select all tiles to swap then click swap button again");
            swapTileSelected = true;
            view.resetPlayerTile();
        }
        else {
            model.playerSwapTile(model.getPlayers().get(currentTurn % model.getPlayers().size()), tilesToSwapIndices);
            view.resetPlayerTile();
            view.updatePlayerTiles(model.getPlayers().get(currentTurn % model.getPlayers().size()));
            nextTurn();
            view.enableAllBoardCells();
            swapTileSelected = false;
        }
        numTimesSwapClicked++;
    }

    /**
     * Handles tile button pressed
     * @param col column number of tile pressed
     */
    private void handleSpecificTileButtonAction(int col) {
        // if swap tile has been selected
        if (swapTileSelected) {
            view.disableAllBoardCells();
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

    /**
     * Handles pass button
     */
    public void passButtonAction() {
        // proceed to next player turn
        nextTurn();
        view.resetPlayerTile();
    }

    public void handleSpecificBoardCellAction(int row, int col) {
        // add temp tile to board
        if (aTileIsSelected) {
            // get selected tile letter
            String tileLetter = view.getSpecificPlayerTileButton(selectedTileCol).getText();
            // add tile to view board and model board
            // add only if current spot is not GREEN (solidified letter) or GRAY (temp letter)
            if (view.getSpecificBoardCellColour(row, col) != Color.GREEN && view.getSpecificBoardCellColour(row, col) != Color.GRAY) {
                view.setSpecificBoardCellLetter(row, col, tileLetter); // Set button field letter
                view.setSpecificBoardCellColour(row, col, Color.GRAY); // Set button field colour (temp letter)
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
                listOfRows.add(row);
                listOfCols.add(col);
                listOfTiles.add(model.getPlayers().get(currentTurn % model.getPlayers().size()).getTiles().get(tileIndex));
                //Tiles tile = model.getPlayerTile(selectedTileCol); // get tile from players hand at specified spot
                //model.addTile(row, col, tile); // Add tile to the model board
                //model.addTileToRemove(tile); // add to list of tiles to remove from hand
                numTilesPlacedThisTurn++;
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

    /**
     * Goes to next turn
     */
    private void nextTurn() {
        currentTurn++;
        view.updatePlayerTiles(model.getPlayers().get((currentTurn % model.getPlayers().size())));
        numTilesPlacedThisTurn = 0;
    }

    private void handleError(String errorMessage) {
        // error message
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (view.getSpecificBoardCellColour(row, col) == Color.GRAY) {
                    view.removeTempTilesOnBoardCell(row, col);
                }
            }
        }
        view.displayMessageToPlayer(errorMessage);

        view.resetPlayerTile();
    }

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
    }
}
