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
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */


public class GameView extends JFrame {
    private ArrayList<String> playerNames; //will store the name of the players
    private JTextArea wordArea; //represents the text area where a valid word was placed on the board is displayed
    private JTextField wordCount; //represents a count of the words that have been placed in the game
    private JTextField tileBagCount; //represents a count for the number of tiles remaining in the bag
    private JButton[][] boardField; //represents the whole board that the player sees including the views of the rows and column
    private JButton[][] boardCells; //represents the 15 cells that a player can place a tile on.
    private JButton[] playerTiles; //represents the buttons that will have the players tiles on them as a label
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

        boardField = new JButton[16][16]; //creates the whole board which will also includes the view of the row and the column

        boardCells = new JButton[15][15]; //initializes the actual game board which a player can place a tile on

        playerTiles = new JButton[7]; //will be the buttons that represent the 7 tiles of a player

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
        int numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Welcome to the game of Scrabble!\nPlease enter the number of players (2-4)")); //prints a welcome message to the game and asks for the number of players
        for (int i = 1; i <= numPlayers; i++) {
            String name = JOptionPane.showInputDialog("Please Enter Player " + i + " Name"); //gets the name of the player
            names.add(name); //adds the name of the player and creates a new player
        }
        return names;
    }

    /**
     * Sets up each button as a field in the board and makes it clickable. It also Sets up the
     * rows and column of the board to guide the user to see where they are placing the
     * tiles. These are for a visual and are not clickable.
     *
     */
    public void initializeBoard() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                boardField[i][j] = new JButton(); //creates a new button
                if (i == 0 && j == 0) { //marks the corner piece as an empty square
                    boardField[i][j].setText(""); //sets the text of the button
                    boardField[i][j].setBackground(Color.BLACK); //makes the colour black
                    boardField[i][j].setEnabled(false); //makes it clickable
                }
                else if (i == 0){ //represents a row index button not for actual game
                    boardField[i][j].setText(String.valueOf(j)); //adds the column index as the text
                    boardField[i][j].setFont(new Font("Arial", Font.BOLD, 14)); //makes the font of the text bolded for clarity
                    boardField[i][j].setBackground(Color.DARK_GRAY); //makes it a dark grey colour button
                    boardField[i][j].setEnabled(false); //makes it clickable

                }
                else if (j == 0){ //represents a column index button not for actual game
                    boardField[i][j].setText(String.valueOf(i)); //adds the row index as the text
                    boardField[i][j].setFont(new Font("Arial", Font.BOLD, 14)); //makes the font of the text bolded for clarity
                    boardField[i][j].setBackground(Color.DARK_GRAY); //makes it a dark grey colour button
                    boardField[i][j].setEnabled(false); //makes it not clickable
                }
                else{ //an actual part of the game for the board field button that tiles can be placed on
                    boardField[i][j].setEnabled(true); //makes it clickable
                    boardField[i][j].setFont(new Font("Arial", Font.BOLD, 14)); //makes the font of the board field so the tiles are seen better
                    if (isTripleWordSquare(i - 1, j - 1)){ //checks to see if it's a triple word square
                        boardField[i][j].setBackground(Color.RED); // Makes the color of the tile red
                        boardField[i][j].setText("TWS"); //sets it as a TWS
                    } else if (isDoubleWordSquare(i - 1, j - 1)) { //checks to see if it's a double word square
                        boardField[i][j].setBackground(Color.PINK); //makes the color pink
                        boardField[i][j].setText("DWS"); //sets it as DWS
                    }
                    else if (isTripleLetterSquare(i - 1, j - 1)){ //checks to see if it's triple letter square
                        boardField[i][j].setBackground(Color.BLUE); //makes the square blue
                        boardField[i][j].setText("TLS"); //makes it TWS
                    }
                    else if (isDoubleLetterSquare(i - 1, j - 1)) { //checks to see its it's a double letter square
                        boardField[i][j].setBackground(Color.CYAN); //makes the color cyan
                        boardField[i][j].setText("DLS"); //makes it DWS
                    } else if (i - 1 == 7 && j - 1 == 7) { //checks for the middle square
                        boardField[i][j].setBackground(Color.ORANGE); //makes the color orange
                        boardField[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
                        boardField[i][j].setText("★"); //sets it as a star
                    }
                    else{
                        boardField[i][j].setBackground(Color.WHITE); //the rest of the board is set as white
                    }
                    boardCells[i - 1][j - 1] = boardField[i][j]; //has reference of the board field to the actual board cell
                }
                boardPanel.add(boardField[i][j]); //adds it to the panel of the board
            }
        }
    }

    /**
     * Check if a square is a triple word square. This logic is based of online to see which row and column are the
     * TWS in a board.
     */
    private boolean isTripleWordSquare(int row, int col) {
        int [][] tripleWordCoords = {
                {0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}
        };
        return containsAnyPremiumSquare(tripleWordCoords, row, col); //checks to see if it's a TWS
    }

    /**
     * Check if a square is a double word score.This logic is based of online to see which row and column are the
     * DWS in a board.
     */
    private boolean isDoubleWordSquare(int row, int col) {
        int[][] doubleWordCoords = {
                {1, 1}, {2, 2}, {3, 3}, {4, 4}, {13, 1}, {12, 2}, {11, 3}, {10, 4},
                {1, 13}, {2, 12}, {3, 11}, {4, 10}, {13, 13}, {12, 12}, {11, 11}, {10, 10},
        };
        return containsAnyPremiumSquare(doubleWordCoords, row, col); //checks to see if it's a DWS
    }

    /**
     * Check if a square is a triple letter score. This logic is based of online to see which row and column are the
     * TLS in a board.
     */
    private boolean isTripleLetterSquare(int row, int col) {
        int[][] tripleLetterCoords = {
                {1, 5}, {1, 9}, {5, 1}, {5, 13}, {9, 1}, {9, 13}, {13, 5}, {13, 9}
        }; //will store the coordinates of a TLS
       return containsAnyPremiumSquare(tripleLetterCoords, row, col); //checks to see if it's a TLS
    }

    /**
     * Check if a square is a double letter score.This logic is based of online to see which row and column are the
     * DLS in a board.
     */
    private boolean isDoubleLetterSquare(int row, int col) {
        int[][] doubleLetterCoords = {
                {0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2},
                {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8},
                {8, 12}, {11, 0}, {11, 7}, {11, 14}, {12, 6}, {12, 8}, {14, 3}, {14, 11}
        }; //will store the coordinates of a DLS
        return containsAnyPremiumSquare(doubleLetterCoords, row, col); //checks to see if it's a DLS
    }

    /**
     * Checks to see if a given coordinate is any of the premium squares such as TLS, DLS,
     * etc.
     */
    private boolean containsAnyPremiumSquare(int[][] coords, int row, int col) {
        for (int[] coord : coords) { //traverses through the coordinated of the specific special square coordinates
            if (coord[0] == row && coord[1] == col) { //if it is one of the coordinates return true
                return true;
            }
        }
        return false; //else return false
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
     * Sets up the display for the players tiles. Showing the tiles of the player.
     * @param player the player who is at turn.
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
            playerTiles[i] = new JButton(); //creates the new buttons
            playerTiles[i].setEnabled(true); //makes them clickable
            playerTiles[i].setText(player.getTiles().get(i).toString()); //will display the tiles of the player
            playerTiles[i].setBackground(Color.LIGHT_GRAY); //makes the color of the tile buttons light grey
            playerTilesPanel.add(playerTiles[i]); //adds the button to the tiles pane
        }
        bottomPanel.add(playerTilesPanel); //adds the tiles to the bottom panel
    }

    /**
     * Sets up the buttons for the player options such as play, swap or pass.
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
     * Increments the word count after a word has been successfully placed.
     */
    public void updateWordCount(ArrayList<String> words){

        wordCount.setText(String.valueOf(words.size())); //increment the word count
    }

    /**
     * Updates the word text area with the word that was placed by the player
     * @param words the list of words placed by the players
     */
    public void addToWordArea(ArrayList<String> words){
        wordArea.setText("");
        for(String word: words){
            wordArea.append(word + "\n");//adds the word to the text area
        }
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
    public void updateBoardCell(Tiles tile, int row, int col){
        boardCells[row][col].setText(tile.toString()); //adds the string of the tile
        boardCells[row][col].setBackground(Color.GREEN); //makes the button color green
        boardCells[row][col].setFont(new Font("Arial", Font.BOLD, 14)); //makes it bolded to be seen after the button becomes green
        boardCells[row][col].setEnabled(false); //makes the button not clickable
    }

    /**
     * Updates the tiles of the player who is currently at turn to play the game.
     * @param player who is at turn to play
     */
    public void updatePlayerTiles(Player player){
        playerTurn.setText(player.getName() + "'s Tiles: ");
        for (int i = 0; i < playerTiles.length; i++) { //traverses through the player tiles panel of buttons
            if(i < player.getTiles().size()){ //if the player has enough tiles to be displayed the update the button with the tile
                playerTiles[i].setText(player.getTiles().get(i).toString()); //updates the tiles with the current players tiles
            }
            else { //The player does not have enough tiles to be displayed on the button panel
                playerTiles[i].setText(""); //makes it have no text
                playerTiles[i].setEnabled(false); //makes it not clickable
            }
        }
    }

    /**
     * Clear the temporary placed tiles after a player attempt to add a word to the board.
     * @param row the row position of tile.
     * @param col the column position of tile.
     */
    public void removeTempTilesOnBoardCell(int row, int col) {
        boardCells[row][col].setText(""); //sets the text as empty
        boardCells[row][col].setBackground(Color.WHITE); //makes the colour of the board white
    }


    /**
     * Sets up the action listeners for board field buttons when a player clicks on
     * one of the buttons to place a tile on the board, for the player choice buttons to
     * play, pass or swap tile(s) and for the player tile buttons.
     * @param listener the action listener for the all buttons
     */
    public void setAllButtonsActionListener(ActionListener listener){
        for(int i = 0; i < 15; i++){ //traverses through the rows
            for(int j = 0; j < 15; j++){ //traverses through the columns
                boardCells[i][j].addActionListener(listener); //adds an action listener to the board field button
            }
        }
        playButton.addActionListener(listener); //adds an action listener to the play button
        swapButton.addActionListener(listener); //adds an action listener to the play button
        passButton.addActionListener(listener); //adds an action listener to the play button

        for(int i = 0; i < playerTiles.length; i++){ //traverses through the buttons of the player tiles
            playerTiles[i].addActionListener(listener); //adds an action listener to all the tiles of the player
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
     * Gets a specific button from the board field when given a row and column.
     * @param row the row of the button
     * @param col the column of the button
     * @return the specific button
     */
    public JButton getSpecificBoardCell(int row, int col) {
        return boardCells[row][col];
    }

    /**
     * Gets a specific button from the player tiles when given a column.
     * @param col the column of the button
     * @return the specific button
     */
    public JButton getSpecificPlayerTileButton(int col) {
        return playerTiles[col];
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
     * Resets player tiles to light gray
     */
    public void resetPlayerTile() {
        for (int col = 0; col < 7; col++) {
            setPlayerTilesColour(col, Color.LIGHT_GRAY);
        }
    }

    /**
     * Sets the background colour of a player's tile after it has been selected
     * @param col column of selected tile
     * @param colour colour to change the background to
     */
    public void setPlayerTilesColour(int col, Color colour) {
        playerTiles[col].setBackground(colour);
    }

    /**
     * Disables all board fields.
     */
    public void disableAllBoardCells() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                boardCells[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Enables all board fields.
     */
    public void enableAllBoardCells() {
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                boardCells[i][j].setEnabled(true);
            }
        }
    }

    /**
     * Gets the current colour of a player tile
     * @param col column number of player tile
     * @return current background colour
     */
    public Color getSpecificPlayerTileColour(int col) {
        return playerTiles[col].getBackground();
    }

    /**
     * Gets the current colour of a board field.
     * @param row row number of the board field
     * @param col column number of the board field
     * @return current background colour
     */
    public Color getSpecificBoardCellColour(int row, int col) {
        return boardCells[row][col].getBackground();
    }

    /**
     * Sets the letter of a selected board field.
     * @param row row number of selected board field
     * @param col column number of selected board field
     * @param letter the letter we want to set the button text to
     */
    public void setSpecificBoardCellLetter(int row, int col, String letter) {
        boardCells[row][col].setText(letter);
    }

    /**
     * Sets the colour of a selected board field background.
     * @param row row number of selected board field
     * @param col column number of selected board field
     * @param colour the colour we want to set the background to
     */
    public void setSpecificBoardCellColour(int row, int col, Color colour) {
        boardCells[row][col].setBackground(colour);
    }
}