import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.awt.*;

/**
 * This is the game controller which will communicate the view with the model of the game.
 * The controller will take care of any buttons pressed in the view and send the data to the model. It will
 * also return the logic from the model to the view updating the scores, words placed, etc.
 *
 * Author(s): Rami Ayoub, Louis Pantazopoulos, Andrew Tawfik, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 24, 2024
 */

public class GameController implements ActionListener {
    private GameModel model; //represents an instance of the model
    private GameView view; //represents an instance of the view
    private int numTimesSwapClicked; //the number of times swap has been clicked
    private int currentTurn; //who's current turn to play the game
    boolean swapTileSelected; //if the button was clicked to swap
    private ArrayList<Integer> tilesToSwapIndices; //the tiles index to be swapped
    private ArrayList<Integer> aiTilesToSwapIndices;
    private int tileIndex; //the tile index
    private ArrayList<Integer> listOfRows; //the row of the tile in board
    private ArrayList<Integer> listOfCols; //the col of the tile in board
    private ArrayList<Tiles> listOfTiles; //the list of the tiles selected
    private boolean aTileIsSelected; //if a tile is selected
    private int selectedTileCol; //selected tiles col

    public GameController(GameModel model, GameView view) {
        // initialize
        this.model = model;
        this.view = view;
        this.numTimesSwapClicked = 0;
        this.currentTurn = 0;
        this.swapTileSelected = false;
        this.tilesToSwapIndices = new ArrayList<>();
        this.tileIndex = 0;
        this.listOfRows = new ArrayList<>();
        this.listOfCols = new ArrayList<>();
        this.listOfTiles = new ArrayList<>();
        this.aiTilesToSwapIndices = new ArrayList<>();
        this.aTileIsSelected = false;
        this.selectedTileCol = 0;

        // set up players
        for(String playerName : view.getPlayerNames()){
            int index = playerName.indexOf("AI"); //used to differentiate between a player and an AI player
            if(index != -1) { //means that this name has AI init and is an AI player name
                model.addAIPlayer(playerName);
            }
            else{
                model.addPlayer(playerName); //creates players in the game model after getting the names from the view
            }
        }

        view.setUpPlayerTilesPanel(model.getPlayers().get(0)); // Set up the first player's tiles

        if(view.getLoadCB()){ //if the user wants to load a customizable board
            handleLoadCustomizableBoard(); //calls a function to load a customizable board
        }

        this.view.setAllButtonsActionListener(this); //sets the controller as the action listener for all buttons in the view

    }

    /**
     * Processes any events that happen.
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        // Play button
        if (e.getSource() == view.getPlayButton()) {
            playButtonAction();
        }
        // Swap button
        else if (e.getSource() == view.getSwapButton()) {
            swapButtonAction();
        }
        // Pass button
        else if (e.getSource() == view.getPassButton()) {
            passButtonAction();
        }
        // Undo button
        else if(e.getSource() == view.getUndoButton()){
            undoButtonAction();
        }
        // Save button
        else if (e.getSource() == view.getSaveButton()){
            saveButtonAction();
        }
        // Load button
        else if (e.getSource() == view.getLoadButton()){
            loadButtonAction();
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
        // Board button
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

    /**
     * Handles the logic for when play button is pressed
     */
    public void playButtonAction() {
        boolean AiPlacedSuccesful = false; // flag for is AI placed a word successfully

        // Logic if current player is an AI player
        if(isAITurn()){
            AIPlayer aiPlayer = (AIPlayer)getCurrentPlayer(); // player
            HashSet<String> allPossibleWord = aiPlayer.getAllWordComputations(model.getWordList()); // all possible words

            // iterate through all possible words until one is placed on the board
            for(String word : allPossibleWord){
                ArrayList<Tiles> tempTiles = model.AIWordToTiles(word, aiPlayer);
                // iterate through the board
                for (int i = 0; i < 15; i++) {
                    ArrayList<Integer> temprowsPositions = new ArrayList<>();
                    // get list of rows for each letter in word
                    for (int a = 0; a < word.length(); a++) {
                        temprowsPositions.add(i);
                    }
                    // iterate through the board
                    for (int j = 0; j < 15 - word.length() + 1; j++) {
                        ArrayList<Integer> tempcolPositions = new ArrayList<>();
                        // get list of columns for each letter in word
                        for (int b = 0; b < word.length(); b++) {
                            tempcolPositions.add(j + b);
                        }
                        // if list of rows and columns is complete
                        if (tempTiles.size() == temprowsPositions.size() && tempTiles.size() == tempcolPositions.size()) {
                            model.checkPlaceableWord(tempTiles, temprowsPositions, tempcolPositions); // check word validity
                            // if word valid
                            if (model.getStatusMessage().equals("Word placed successfully.")){
                                // place word
                                model.playerPlaceTile(aiPlayer, tempTiles, temprowsPositions, tempcolPositions);
                                // update view
                                for(int r = 0; r < tempTiles.size(); r++){
                                    view.updateBoardCell(tempTiles.get(r), temprowsPositions.get(r), tempcolPositions.get(r));
                                }
                                view.updateBagTilesCount(model.getTilesBag().bagArraylist().size()); // Update view count
                                view.updatePlayerScore((aiPlayer.getName()), (aiPlayer.getScore())); // Update AI player score
                                view.addToWordArea(model.getPlacedWords()); // Update word list view
                                view.updateWordCount(model.getPlacedWords()); // Update word count in view
                                AiPlacedSuccesful = true; // Set flag
                                break;
                            }
                        }
                    }
                    // exit loop if a word is placed
                    if (AiPlacedSuccesful){
                        break;
                    }
                }
                // exit loop if a word is placed
                if (AiPlacedSuccesful){
                    break;
                }
            }
            // if AI player can't play then swap
            if(!AiPlacedSuccesful){
                if(!model.isGameFinished() && !model.getTilesBag().bagOfTileIsEmpty()){ //only swaps if the game is not finished
                    view.getSwapButton().doClick();
                }
            }
            // next turn
            nextTurn();
        }
        // if current player is not an AI player
        else {
            // Check word validity
            model.checkPlaceableWord(listOfTiles, listOfRows, listOfCols);
            // if word valid
            if (model.getStatusMessage().equals("Word placed successfully.")) {
                // place word
                model.playerPlaceTile(getCurrentPlayer(), listOfTiles, listOfRows, listOfCols);
                // update view
                for(int i = 0; i < listOfTiles.size(); i++){
                    view.updateBoardCell(listOfTiles.get(i), listOfRows.get(i), listOfCols.get(i));
                }
                view.updateBagTilesCount(model.getTilesBag().bagArraylist().size());
                view.resetPlayerTile();
                view.updatePlayerScore(getCurrentPlayer().getName(), getCurrentPlayer().getScore());
                view.addToWordArea(model.getPlacedWords());
                view.updateWordCount(model.getPlacedWords());
                nextTurn();
            }
            // if word invalid
            else {
                // update model check board
                model.updateCheckBoard();
                // Reset blank tile
                handleError(model.getStatusMessage());
                for(Tiles tile: listOfTiles){
                    if(tile.getNumber() == 0){
                        tile.setLetter(" ");
                    }
                }
            }
            // reset lists and buttons
            view.enableSwapPassUndoSaveLoad();
            listOfTiles.clear();
            listOfRows.clear();
            listOfCols.clear();
        }

        // check if the game finishes at the end of this turn
        if (model.isGameFinished()) {
            // Disable game if over
            view.disableAllButtons();
            view.disableAllBoardCells();

            //Trigger confetti animation
            view.displayWinnerCelebration(model.getWinner().getName());

        }
    }

    /**
     * Returns true if the current turn is an AIPlayers turn.
     * @return true if AIPlayer turn false otherwise.
     */
    public boolean isAITurn(){
        return getCurrentPlayer() instanceof AIPlayer; //checks if the current player is an AIPlayer
    }

    /**
     * Returns the current player at turn.
     * @return player at turn
     */
    public Player getCurrentPlayer(){
        return model.getPlayers().get(currentTurn % model.getPlayers().size());
    }

    /**
     * Handles logic for when swap button is pressed
     */
    public void swapButtonAction() {
        // if the current player is an AI player
        if(isAITurn()){
            AIPlayer aiPlayer = (AIPlayer) getCurrentPlayer();
            // get all tiles
            for(int i = 0; i < aiPlayer.getTiles().size(); i++){
                if (aiPlayer.getTiles().get(i).getNumber() == 0) {
                    aiPlayer.getTiles().get(i).setLetter(" ");
                }
                aiTilesToSwapIndices.add(i);
            }
            // swap all tiles
            model.playerSwapTile(aiPlayer, aiTilesToSwapIndices);
            view.updatePlayerTiles(aiPlayer);
        }
        // if the current player is not an AI player
        else {
            // first swap button click tells users to select which tiles to swap
            if (numTimesSwapClicked % 2 == 0) {
                view.displayMessageToPlayer("Select all tiles to swap then click swap button again");
                view.disableSwapPassUndoSaveLoad(); // disable all other buttons
                swapTileSelected = true; // set flag
                view.resetPlayerTile();
            // on second swap button click, swap selected tiles
            } else {
                model.playerSwapTile(getCurrentPlayer(), tilesToSwapIndices); // swap tile
                // update views
                view.resetPlayerTile();
                view.updatePlayerTiles(getCurrentPlayer());
                view.enableSwapPassUndoSaveLoad();
                nextTurn();
                view.enableAllBoardCells();
                swapTileSelected = false;
            }
            numTimesSwapClicked++; // increment number of times swapped is pressed
        }
    }

    /**
     * Handles tile button pressed
     * @param col column number of tile pressed
     */
    private void handleSpecificTileButtonAction(int col) {
        // if swap tile has been selected
        if (swapTileSelected) {
            // update view with selected tiles
            view.disableAllBoardCells();
            view.setPlayerTilesColour(col, Color.ORANGE);
            tilesToSwapIndices.add(col);
        }
        // if swap tile has not been selected (normal play)
        else {
            tileIndex = col; // update tile column index
            // Check if selected tile is gray
            if (view.getSpecificPlayerTileColour(col) == Color.GRAY) {
                // do nothing
            }
            // Make sure the other tiles are light gray when not selected
            else {
                // iterate through tiles in hand
                for (int index = 0; index < 7; index++) {
                    // if the tiles is gray, set light gray
                    if (view.getSpecificPlayerTileColour(index) != Color.GRAY) {
                        view.setPlayerTilesColour(index, Color.LIGHT_GRAY);
                    }
                }
                view.setPlayerTilesColour(col, Color.ORANGE); // set orange to indicate a tile is selected
                aTileIsSelected = true; // set flag
                selectedTileCol = col; // set selected tile column number
            }
        }
    }

    /**
     * Handles logic for when pass button is pressed
     */
    public void passButtonAction() {
        // proceed to next player turn
        nextTurn();
        view.resetPlayerTile();
    }

    /**
     * Handles the logic for when an undo button is pressed
     */
    public void undoButtonAction(){
        if(model.stackOfBoardEmpty() || model.stackOfPlayerTilesEmpty() || model.stackOfPlayerScoresEmpty() || model.stackOfTilesBagEmpty() || model.stackOfPlacedWordsEmpty() || model.stackOfPlayerEmpty()){ //if any of the stacks are empty then we are at the beginning of the game and cannot perform undo
            view.displayMessageToPlayer("Cannot implement undo! Please place tiles first"); //lets the player now that we are at the beginning of teh game again
        }else{ //there has been at least one played turn
            currentTurn--; //goes back a turn

            model.setBoardFromStack(); //calls a function to set the board from the stack
            model.setPlayerTilesFromStack(); //calls a function to set the player tiles from the stack
            model.setPlayerScoresFromStack(); //calls a function to set the player scores from the stack
            model.setPlacedWordsFromStack(); //calls a function to set the player scores from the stack
            model.setTilesBagFromStack(); //calls a function to set the tiles bag from the stack

            if(model.getPlayerFromStack() instanceof AIPlayer){ //if the previous player was an AI Player then update the board and teh AI Player replays

                view.addToWordArea(model.getPlacedWords()); //updates the view for the placeable words
                view.updateWordCount(model.getPlacedWords()); //updates the word count for the placeable words
                view.updateGameBoard(model.getGameBoard()); //updates the board to the previous board before it was played
                view.updateBagTilesCount(model.getTilesBag().bagArraylist().size()); //updates the bag tiles count

                for(Player player: model.getPlayers()){ //traverses through the players in the game
                    view.updatePlayerScore(player.getName(), player.getScore()); //updates the score for the specific player
                }

                view.displayMessageToPlayer("AI player is replaying its turn."); //tells the message that the AI Player is replaying its turn
                view.getPlayButton().doClick(); //simulate a click on the play button to make the AI play again
            }
            else{ //the previous player was a real player

                view.addToWordArea(model.getPlacedWords()); //updates the view for the placeable words
                view.updateWordCount(model.getPlacedWords()); //updates the word count for the placeable words
                view.updateGameBoard(model.getGameBoard()); //updates the board to the previous board before it was played
                view.updateBagTilesCount(model.getTilesBag().bagArraylist().size()); //updates the bag tiles count

                for(Player player: model.getPlayers()){ //traverses through the players in the game
                    view.updatePlayerScore(player.getName(), player.getScore()); //updates the score for the specific player
                }

                view.updatePlayerTiles(model.getCurrentPlayer()); // Update the tiles of the current player

            }
        }
    }

    /**
     * Handles the logic for when a save button is pressed.
     */
    public void saveButtonAction(){
        // Generate the XML representation of the current game state
        String xml = model.toXML(); // Assuming `model` is your game model

        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));

        // Show the save dialog
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure the file has the .xml extension
            if (!selectedFile.getName().endsWith(".xml")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xml");
            }

            try {
                // Write the XML content to the file
                Files.write(Paths.get(selectedFile.getAbsolutePath()), xml.getBytes());
                JOptionPane.showMessageDialog(null, "Game saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                // Show an error message if saving fails
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the logic for when a load button is pressed.
     */
    public void loadButtonAction(){
        // Create a file chooser for selecting the XML file to load
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));

        // Show the file chooser dialog and check if the user approves the selection
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // Read the XML content from the selected file
                String xml = Files.readString(Paths.get(selectedFile.getAbsolutePath()));

                // Update the model using the loaded XML
                model.fromXML(xml);

                // Notify the user of the successful load
                JOptionPane.showMessageDialog(null, "Game loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                view.addToWordArea(model.getPlacedWords()); //updates the view for the placeable words
                view.updateWordCount(model.getPlacedWords()); //updates the word count for the placeable words
                view.updateGameBoard(model.getGameBoard()); //updates the board to the previous board before it was played
                view.updateBagTilesCount(model.getTilesBag().bagArraylist().size()); //updates the bag tiles count

                for(Player player: model.getPlayers()){ //traverses through the players in the game
                    view.updatePlayerScore(player.getName(), player.getScore()); //updates the score for the specific player
                }

                view.updatePlayerTiles(getCurrentPlayer()); //update the tiles of the current player

            } catch (IOException e) {
                // Handle file access errors and notify the user
                JOptionPane.showMessageDialog(null, "Error loading the file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                // Handle XML parsing or other issues and notify the user
                JOptionPane.showMessageDialog(null, "Invalid file format. Please select a valid game file.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles logic when a button on the board is pressed
     * @param row row to add tile to
     * @param col column to add tile to
     */
    public void handleSpecificBoardCellAction(int row, int col) {
        // if any tile is selected
        if (aTileIsSelected) {
            // get selected tile letter
            String tileLetter = view.getSpecificPlayerTileButton(selectedTileCol).getText();
            // add a tile to the view is the selected board button is not GREEN or GRAY
            if (view.getSpecificBoardCellColour(row, col) != Color.GREEN && view.getSpecificBoardCellColour(row, col) != Color.GRAY) {
                // if the user places a blank tile, prompt for letter assignment
                if (view.getSpecificPlayerTileButton(selectedTileCol).getText().equals(" :0 ")){
                    view.setBlankTileLetter(getCurrentPlayer().getTiles().get(selectedTileCol)); // set letter
                    tileLetter = getCurrentPlayer().getTiles().get(selectedTileCol).getLetter();
                }
                view.setSpecificBoardCellLetter(row, col, tileLetter); // Set button field letter
                view.setSpecificBoardCellColour(row, col, Color.GRAY); // Set button field colour (temp letter)
                view.setPlayerTilesColour(selectedTileCol, Color.GRAY); // Mark tile as used on board (gray)
                view.disableSwapPassUndoSaveLoad();
                // Set tile colours
                for (int index = 0; index < 7; index++) {
                    if (view.getSpecificPlayerTileColour(index) != Color.GRAY) {
                        view.setPlayerTilesColour(index, Color.LIGHT_GRAY);
                    }
                }
                aTileIsSelected = false; // reset flag
                // add to list of rows, columns and tiles
                listOfRows.add(row);
                listOfCols.add(col);
                listOfTiles.add(getCurrentPlayer().getTiles().get(tileIndex));
            }
            // if selected board button is GREEN or GRAY
            else {
                // do nothing
            }
        }
        // if no tiles selected
        else {
            // do nothing
        }
    }

    /**
     * Goes to next turn
     */
    private void nextTurn() {
        currentTurn++; // update current game turn
        view.updatePlayerTiles(getCurrentPlayer());  // update player tiles
        // if it is an AI player's turn, initiate play
        if (isAITurn()) {
            if(!model.isGameFinished()){ //only clicks play when the game is not finished
                view.getPlayButton().doClick();
            }
        }
    }

    /**
     * Handles error messages
     * @param errorMessage the message to be displayed
     */
    private void handleError(String errorMessage) {
        // remove tiles at coordinate
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (view.getSpecificBoardCellColour(row, col) == Color.GRAY) {
                    view.removeTempTilesOnBoardCell(row, col);
                }
            }
        }
        view.displayMessageToPlayer(errorMessage); // display error message
        view.resetPlayerTile(); // update view
    }

    /**
     * Handles the logic for when players want to load a customizable board.
     */
    public void handleLoadCustomizableBoard(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Custom Board XML File");

        // Filter to allow only XML files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml"));

        int userChoice = fileChooser.showOpenDialog(null);  // Show the file chooser

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();

            // Read the content of the file into a string
            try {
                String xmlContent = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);

                // Now pass this XML string to the fromXML method to update the board
                model.getGameBoard().fromCustomBoardXML(xmlContent);

                JOptionPane.showMessageDialog(null, "Custom Board Loaded Successfully!");

                view.updateGameBoard(model.getGameBoard()); //updates the board to the previous board before it was played

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading the XML file.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No file selected.");
        }
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
    }
}
