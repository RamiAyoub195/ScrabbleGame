import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is the game view which shows the game board, the tiles of the current player,
 * all the scores of the players, the words that have been placed on the board, and the
 * remaining tiles in the bag as the game goes on. Each player will have the option through
 * buttons to play, pass or swap. If they which to place a tile they must click on a tile and then a
 * place on the board then press play to check the validity of a word. For swap, they must select each tile then press swap.
 *
 * Author(s): Rami Ayoub
 * Version: 2.0
 * Date: Wednesday, November 6, 2024
 */


public class GameView extends JFrame {
    private ArrayList<String> playerNames; //will store the name of the players
    private int numPlayers; // number of players in the game
    private JTextArea wordArea; //represents the text area where a valid word was placed on the board is displayed
    private JTextField wordCount; //represents a count of the words that have been placed in the game
    private JTextField tileBagCount; //represents a count for the number of tiles remaining in the bag
    private JButton[][] boardFields; //represents the open slots where the tiles can be placed by selecting a tile an open spot
    private JButton[][] playerTiles; //represents the buttons that will have the players tiles on them as a label
    private HashMap<String, JTextField> playerScoreFields; //will store the players name and their score
    private JTextField playerTurn; //the player who has the current turn;
    private JPanel wordPlacedPanel; //this panel will have the words placed in the game and a count of the words
    private JPanel playerPanel; //this panel will have the players names and their scores
    private JPanel tileBagPanel; //this panel will have the number of tiles in the bag during the game
    private JPanel boardPanel; //represents the 15 x 15 board that the tiles will be placed on.
    private JPanel playerChoicePanel; //this panel will have the choices that a player cab choose while it's their turn such as play, pass, or swap
    private JPanel playerTilesPanel; //this panel will display the players tiles.
    private JPanel rightSidePanel; //this will have the words placed, the scores of the player and remaining tiles in the bag.
    private JPanel bottomPanel; //this will have the options for the player to play the game and their tiles.
    private JButton playButton; //this will be the button the user presses to play their turn
    private JButton passButton; //this will be the button the user presses to skip their turn
    private JButton swapButton; //this will be the button the user pressed to swap tiles with the tile bag

    public GameView() {
        super("Scrabble"); //creates a frame called Scrabble
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the system exit when close is clicked
        this.setLayout(new BorderLayout()); //makes a border layout for the frame

        boardPanel = new JPanel(new GridLayout(16, 16)); //makes the board panel which will have the 16 slots (1 slot each for row and column)

        boardFields = new JButton[16][16]; //creates the 16 slots that will be on the board game (1 slot each for row and column)

        playerTiles = new JButton[1][7]; //will be the buttons that represent the 7 tiles of a player

        playerNames = new ArrayList<>(); //initializes the arrayList of names

        playerScoreFields = new HashMap<>(); //initializes the hashmap for the players score

        playerTurn = new JTextField(); //initializes the text field for the players turn which will say which player has their turn

        bottomPanel = new JPanel(); //will display the players tiles and their options of playing such as play, pass and swap
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS)); //makes it a box layout where content is added horizontally

        rightSidePanel = new JPanel(); //will display the words placed, the players score and number of tiles in the tile bag
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS)); //sets the layout of the right side as a box layout

        playerNames = welcomeAndGetPlayerNames(); //get the number of players playing and the player names

        initializeBoard(); //initialized the board panel

        setUpWordsPlacedPanel(); //sets up the word placed panel showing the number of words and the word that was placed

        setUpPlayerPanel(); //sets up the players panel in the game with their scores

        setUpTileBagPanel(); //sets up the tiles bag panel showing the number of tiles at the start

        setUpButtonsPanel(); //sets up the button options for the player

        this.add(boardPanel, BorderLayout.CENTER); //add the board field in the middle
        this.add(rightSidePanel, BorderLayout.EAST); // Adds the right-side panel to the main frame
        this.add(bottomPanel, BorderLayout.SOUTH); //adds the tiles and the buttons of choice for play in the bottom
        this.setSize(500, 400);
        this.setVisible(true);
    }


    /**
     * Prints a welcome message to the players at the beginning of the game and asks for the number of players and,
     * the names of each player creating the players. Returns an array list of the players names
     *
     * @return ArrayList of playerNames
     */
    public ArrayList<String> welcomeAndGetPlayerNames() {
        ArrayList<String> names = new ArrayList<>();
        numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Welcome to the game of Scrabble!\nPlease enter the number of players (2-4)")); //prints a welcome message to the game and asks for the number of players
        for (int i = 1; i <= numPlayers; i++) {
            String name = JOptionPane.showInputDialog("Please Enter Player " + i + " Name"); //gets the name of the player
            names.add(name); //adds the name of the player and creates a new player
        }
        return names;
    }

    /**
     * Sets up each button as a field in the board and makes it clickable.
     * Also sets up the column and row views to guide the reader for clairty and
     * see the board
     */
    public void initializeBoard() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                boardFields[i][j] = new JButton(); //creates a new button
                if (i == 0 && j == 0) { //marks the corner piece as an empty square
                    boardFields[i][j].setText(""); //sets the text of the button
                    boardFields[i][j].setBackground(Color.BLACK); //makes the colour black
                    boardFields[i][j].setEnabled(false); //makes it clickable
                }
                else if (i == 0){ //represents a row index button not for actual game
                    boardFields[i][j].setText(String.valueOf(j)); //adds the column index as the text
                    boardFields[i][j].setFont(new Font("Arial", Font.BOLD, 14)); //makes the font of the text bolded for clarity
                    boardFields[i][j].setBackground(Color.DARK_GRAY); //makes it a dark grey colour button
                    boardFields[i][j].setEnabled(false); //makes it clickable

                }
                else if (j == 0){ //represents a column index button not for actual game
                    boardFields[i][j].setText(String.valueOf(i)); //adds the row index as the text
                    boardFields[i][j].setFont(new Font("Arial", Font.BOLD, 14)); //makes the font of the text bolded for clarity
                    boardFields[i][j].setBackground(Color.DARK_GRAY); //makes it a dark grey colour button
                    boardFields[i][j].setEnabled(false); //makes it not clickable
                }
                else{ //an actual part of the game for the board field button that tiles can be placed on
                    boardFields[i][j].setEnabled(true); //makes it clickable
                    boardFields[i][j].setBackground(Color.WHITE); //makes the colour white
                }
                boardPanel.add(boardFields[i][j]); //adds it to the panel of the board
            }
        }
    }

    /**
     * Sets up the word placed panel which will have a list of the words placed and the
     * number of words
     */
    public void setUpWordsPlacedPanel(){
        wordPlacedPanel = new JPanel(); //creates the panel for the placed word
        wordPlacedPanel.setLayout(new BoxLayout(wordPlacedPanel, BoxLayout.X_AXIS)); //makes it a box layout with components added vertically

        JLabel wordLabel = new JLabel("Words Placed: "); //The header of the words placed
        wordPlacedPanel.add(wordLabel); //adds it to the word placed panel

        wordCount = new JTextField("0"); //sets the number of words added at the beginning as 0
        wordCount.setMaximumSize(new Dimension(30, 30)); //makes the size of the text field a square
        wordCount.setEditable(false); //makes it non editable
        wordCount.setBackground(Color.WHITE);//makes the background colour white
        wordCount.setHorizontalAlignment(SwingConstants.CENTER); // centers the text horizontally
        wordPlacedPanel.add(wordCount); //adds it to the word placed panel

        rightSidePanel.add(wordPlacedPanel); //adds the label and text field to the right side of the game
        wordArea = new JTextArea(1, 5); //Will display the words that are added to the board
        wordArea.setEditable(false); // cannot be entered as input only for output
        wordArea.setBackground(Color.WHITE); //sets the background color for the text area as white
        wordArea.setLineWrap(true); //allows for line wrapping
        wordArea.setWrapStyleWord(true); //allows for neat wrapping so words don't cut out in the middle

        JScrollPane scrollPane = new JScrollPane(wordArea); // Adds scrolling to the text area
        scrollPane.setMinimumSize(new Dimension(50, 20));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Show vertical scrollbar only when needed
        rightSidePanel.add(scrollPane); //adds it to the right side
    }

    /**
     * Sets up the player panel which will have all player names and their scores
     */
    public void setUpPlayerPanel(){
        playerPanel = new JPanel(); //this panel will have all the players and their scores
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS)); //will add their name and score on an x-axis
        for(String playerName: playerNames){
            JLabel playerScoreLabel = new JLabel(playerName + "'s" + " Score:"); //the label for the player i score
            JTextField playerScoreText = new JTextField("0"); //the actual display for the score
            playerScoreText.setEditable(false); //disables the text field so that it cant be editable just see the players score
            playerScoreText.setBackground(Color.WHITE); //makes the background of the text field white
            playerScoreText.setMaximumSize(new Dimension(30, 30)); // Set a max size to keep it small
            playerScoreText.setHorizontalAlignment(SwingConstants.CENTER); // centers the text horizontally
            playerPanel.add(playerScoreLabel); //adds the label
            playerPanel.add(playerScoreText); //adds the score

            playerScoreFields.put(playerName, playerScoreText); //adds it to the hashmap with the players name and their score

        }
        rightSidePanel.add(playerPanel); //adds the player panel to the right side panel
    }

    /**
     * Sets up the tiles in bag panel that can be found on the right side panel of the main frame
     */
    public void setUpTileBagPanel(){
        tileBagPanel = new JPanel(); //creates a new panel for the tiles bag
        tileBagPanel.setLayout(new BoxLayout(tileBagPanel, BoxLayout.X_AXIS)); //makes it a box layout added horizontally
        JLabel tileLeftInBag = new JLabel("Tiles In Bag: "); //the label for the bag of tiles
        tileBagPanel.add(tileLeftInBag); //adds it to the panel of the tiles bag
        tileBagCount = new JTextField(String.valueOf(100 - (7 * playerNames.size()))); //creates a new text field that will display the number of tiles in bag which depends on the number of players in game
        tileBagCount.setEditable(false); //makes it non editable
        tileBagCount.setBackground(Color.WHITE); //makes the color white
        tileBagCount.setMaximumSize(new Dimension(30, 30)); //makes the dimension a small square
        tileBagCount.setHorizontalAlignment(SwingConstants.CENTER); //makes the alignment in th center
        tileBagPanel.add(tileBagCount); //adds it to the panel of the tile bag
        rightSidePanel.add(tileBagPanel); //adds it to the panel of the right side of game
    }

    /**
     * Sets up the display for the players tiles
     */
    public void setUpPlayerTilesPanel(Player player){
        playerTilesPanel = new JPanel(); //creates the panel that will display the players tiles
        playerTilesPanel.setLayout(new BoxLayout(playerTilesPanel, BoxLayout.X_AXIS)); //makes it a box layout that will have the tiles added horizontally
        playerTurn = new JTextField(player.getName() + "'s Tiles: "); //shows a text field for the current players turn
        playerTurn.setEditable(false); //makes it non editable
        playerTurn.setBackground(Color.WHITE); //makes the background colour while
        playerTurn.setMaximumSize(new Dimension(150, 20)); //makes the dimensions of the text label
        playerTurn.setHorizontalAlignment(SwingConstants.CENTER); // centers the text horizontally
        playerTilesPanel.add(playerTurn); //adds the text box saying which players turn it is

        for(int i = 0; i < 7; i++){
            playerTiles[0][i] = new JButton(); //creates the new buttons
            playerTiles[0][i].setEnabled(true); //makes them clickable
            playerTiles[0][i].setText(player.getTiles().get(i).toString()); //will display the tiles of the player
            playerTiles[0][i].setBackground(Color.LIGHT_GRAY); //makes the color of the tile buttons light grey
            playerTilesPanel.add(playerTiles[0][i]); //adds the button to the tiles pane
        }
        bottomPanel.add(playerTilesPanel); //adds the tiles to the bottom panel
    }

    /**
     * Sets up the buttons for the player options
     */
    public void setUpButtonsPanel(){
        playerChoicePanel = new JPanel(); //creates the panel for the buttons
        playerChoicePanel.setLayout(new BoxLayout(playerChoicePanel, BoxLayout.X_AXIS)); //makes it a box layout that adds horizontally
        playButton = new JButton("Play"); //creates a play button
        playButton.setBackground(Color.GREEN); //makes the button color green
        playerChoicePanel.add(playButton); //adds the button to the panel

        swapButton = new JButton("Swap"); //creates the swap button
        swapButton.setBackground(Color.YELLOW); //makes the button color yellow
        playerChoicePanel.add(swapButton); //adds the button to the panel

        passButton = new JButton("Pass"); //creates the pass button
        passButton.setBackground(Color.RED); //makes the button colour red
        playerChoicePanel.add(passButton); //adds the button to the panel

        bottomPanel.add(playerChoicePanel); //adds the panel of buttons to the bottom of the main frame panel
    }

    /**
     * Gets the play button.
     * @return play button
     */
    public JButton getPlayButton(){
        return playButton;
    }

    /**
     * Gets the swap button.
     * @return swap button
     */
    public JButton getSwapButton(){
        return swapButton;
    }

    /**
     * Gets the pass button.
     * @return pass button
     */
    public JButton getPassButton(){
        return passButton;
    }

    /**
     * Gets the row of the button in the button fields of the game
     * @param button the button that was pressed
     * @ return row of the button
     */
    public int getFieldsButtonRow(JButton button){
        for (int i = 0; i < 15; i++) { //traversed through row
            for (int j = 0; j < 15; j++) { //traverses through column
                if(boardFields[i][j] == button){ //if the button was found
                    return i; //return the row
                }
            }
        }
        return -1; //if it was not found
    }


    /**
     * Gets the column of the button in the button fields of the game
     * @param button the button that was pressed
     * @return column of the button
     */
    public int getFieldsButtonCol(JButton button){
        for (int i = 0; i < 15; i++) { //traversed through row
            for (int j = 0; j < 15; j++) { //traverses through column
                if(boardFields[i][j] == button){ //if the button was found
                    return j; //return the column
                }
            }
        }
        return -1; //if the button was not found
    }

    /**
     * Gets the index of the tile represented as a button that was selected by the user to be placed down or swapped
     * @return the index of tile represented as a button selected by player
     */
    public int getTileIndex(JButton button){
        for (int i = 0; i < playerTiles[0].length; i++) { //traverses through the players tiles
                if(playerTiles[0][i] == button){ //if it was the button pressed
                    return i; //return the index of the tile
                }
        }
        return -1; //if it could not be found
    }

    /**
     * Increments the word count after a word has been successfully placed.
     */
    public void incrementWordCount(){
        wordCount.setText(String.valueOf(Integer.parseInt(wordCount.getText()) + 1)); //increment the word count
    }

    /**
     * Updates the word text area with the word that was placed by the player
     * @param word the word placed by the player
     */
    public void addToWordArea(String word){
        wordArea.append(word + "\n"); //adds the word to the text area
    }

    /**
     * Updates the tiles bag text field with the remaining number of tiles left in the bag
     * @param remaining the remaining number of tiles in bag
     */
    public void updateBagTilesCount(int remaining){
        tileBagCount.setText(String.valueOf(remaining)); //updates the number of tiles in bag
    }

    /**
     * Updates the field of the board with the tiles that were just placed and makes the clickable
     * and changes the colour off them to green.
     * @param tile the tile that is being added
     * @param row the row position of tile
     * @param col the col position of tile
     */
    public void updateBoardFields(Tiles tile, int row, int col){
        boardFields[row][col].setText(tile.toString()); //adds the string of the tile
        boardFields[row][col].setBackground(Color.GREEN); //makes the button color green
        boardFields[row][col].setFont(new Font("Arial", Font.BOLD, 14)); //makes it bolded to be seen after the button becomes green
        boardFields[row][col].setEnabled(false); //makes the button not clickable
    }

    /**
     * Updates the tiles of the player who is currently at turn to play the game.
     * @param player who is at turn to play
     */
    public void updatePlayerTiles(Player player){
        playerTurn.setText(player.getName() + "'s Tiles: ");
        for (int i = 0; i < playerTiles[0].length; i++) { //traverses through the player tiles panel of buttons
            if(i < player.getTiles().size()){ //if the player has enough tiles to be displayed the update the button with the tile
                playerTiles[0][i].setText(player.getTiles().get(i).toString()); //updates the tiles with the current players tiles
            }
            else { //The player does not have enough tiles to be displayed on the button panel
                playerTiles[0][i].setText(""); //makes it have no text
                playerTiles[0][i].setEnabled(false); //makes it not clickable
            }
        }
    }

    /**
     * Temporarily suspends the player tiles when a player has clicked on one of the
     * tiles that they will place on the board or swap with tiles in the bag.
     * @param tile the tile selected by the player
     */
    public void tempDisablePlayerTiles(Tiles tile){
        for(int i = 0; i < playerTiles[0].length; i++){ //traverses through the tiles of the player
            if((playerTiles[0][i].getText().equals(tile.toString()))){ //if the tile is one that was selected
                playerTiles[0][i].setEnabled(false); //temporarily disable it
            }
        }
    }

    /**
     * Enables a tile after it was marked temporarily disabled or not clickable
     * @param tile the tile that was previously selected marking the others disabled
     */
    public void enablePlayerTiles(Tiles tile){
        for(int i = 0; i < playerTiles[0].length; i++){ //traverses through the players tiles
            if((playerTiles[0][i].getText().equals(tile.toString()))){ //if it was not the previously selected tile
                playerTiles[0][i].setEnabled(true); //enables it again
            }
        }
    }

    /**
     * Temporarily adds a tile to the board after a player selects one of its tiles and adds it to the board.
     * The player may choose to add another tiles as well, but it will show them what they have placed so far
     * @param tile the tile that is being added
     * @param row the row position of tile
     * @param col the col position of tile
     */
    public void tempAddTileToBoardField(Tiles tile, int row, int col){
        boardFields[row][col].setText(tile.toString()); //adds the string of the tile
        boardFields[row][col].setBackground(Color.GRAY); //makes the button color gray
        boardFields[row][col].setFont(new Font("Arial", Font.BOLD, 14)); //makes it bolded to be seen after the button becomes gray

    }

    /**
     * Clear the temporary placed tiles after a player attempt to add a word to the board.
     * @param row the row position of tile.
     * @param col the column position of tile.
     */
    public void removeTempTilesOnBoardField(int row, int col) {
        boardFields[row][col].setText(""); //sets the text as empty
        boardFields[row][col].setBackground(Color.WHITE); //makes the colour of the board white
    }


    /**
     * Sets up the action listeners for board field buttons when a player clicks on
     * one of the buttons to place a tile on the board, for the player choice buttons to
     * play, pass or swap tile(s) and for the player tile buttons.
     * @param listener the action listener for the all buttons
     */
    public void setAllButtonsActionListener(ActionListener listener){
        for(int i = 1; i < 16; i++){ //traverses through the rows
            for(int j = 1; j < 16; j++){ //traverses through the columns
                boardFields[i][j].addActionListener(listener); //adds an action listener to the board field button
            }
        }
        playButton.addActionListener(listener); //adds an action listener to the play button
        swapButton.addActionListener(listener); //adds an action listener to the play button
        passButton.addActionListener(listener); //adds an action listener to the play button

        for(int i = 0; i < playerTiles[0].length; i++){ //traverses through the buttons of the player tiles
            playerTiles[0][i].addActionListener(listener); //adds an action listener to all the tiles of the player
        }
    }

    /**
     * Returns the array list of player names.
     * @return Arraylist of player names
     */
    public ArrayList<String> getPlayerNames(){
        return playerNames;
    }

    /**
     * Returns the board field.
     * @return board fields
     */
    public JButton[][] getBoardFields() {
        return boardFields;
    }

    /**
     * Returns the player tiles
     * @return player tiles
     */
    public JButton[][] getPlayerTiles() {
        return playerTiles;
    }

    /**
     * Gets a specific button from the board field when given a row and column.
     * @param row the row of the button
     * @param col the column of the button
     * @return the specific button
     */
    public JButton getSpecificBoardField(int row, int col) {
        return boardFields[row][col];
    }

    /**
     * Gets the current colour of a board field.
     * @param row row number of the board field
     * @param col column number of the board field
     * @return current background colour
     */
    public Color getSpecificBoardFieldColour(int row, int col) {
        return boardFields[row][col].getBackground();
    }

    /**
     * Get the number of players in the game
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Gets the current colour of a player tile
     * @param col column number of player tile
     * @return current background colour
     */
    public Color getSpecificPlayerTileColour(int col) {
        return playerTiles[0][col].getBackground();
    }

    /**
     * Gets a specific button from the player tiles when given a column.
     * @param col the column of the button
     * @return the specific button
     */
    public JButton getSpecificPlayerTileButton(int col) {
        return playerTiles[0][col];
    }

    /**
     * Displays a message to the player teeling them if they placed an invalid word, or violated one of the rules
     * etc.
     * @param message the message to the player
     */
    public void displayMessageToPlayer(String message){
        JOptionPane.showMessageDialog(null, message); //displays the message to the player
    }

    /**
     * Updates the players score after they have placed a successful word on the board fields.
     * @param playerName the name of the player
     * @param newScore the new score that will be updated
     */
    public void updatePlayerScore(String playerName, int newScore){
        JTextField scoreField = playerScoreFields.get(playerName); //gets the reference of the player score text field
        scoreField.setText(String.valueOf(newScore)); //sets the new score
    }

    /**
     * Sets the background colour of a player's tile after it has been selected
     * @param col column of selected tile
     * @param colour colour to change the background to
     */
    public void setPlayerTilesColour(int col, Color colour) {
        playerTiles[0][col].setBackground(colour);
    }

    /**
     * Sets the letter of a selected board field.
     * @param row row number of selected board field
     * @param col column number of selected board field
     * @param letter the letter we want to set the button text to
     */
    public void setSpecificBoardFieldLetter(int row, int col, String letter) {
        boardFields[row][col].setText(letter);
    }

    /**
     * Sets the colour of a selected board field background.
     * @param row row number of selected board field
     * @param col column number of selected board field
     * @param colour the colour we want to set the background to
     */
    public void setSpecificBoardFieldColour(int row, int col, Color colour) {
        boardFields[row][col].setBackground(colour);
    }




}


