
import javax.swing.*;
import java.util.*;

/**
 * This is the game model class that brings all the logic together for a game, here
 * also the user will input their commands and get an update of the board, their score etc.
 * Each word that a player places will be checked for adjacency, valid word etc.
 *
 * Author(s): Rami Ayoub, Louis Pantazopoulos, Andrew Tawfik, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */

public class GameModel {
    private ArrayList<Player> players; //list of players in the game
    private Player winner; //player who win the game
    private Board gameBoard; //the actual game board displayed
    private Board checkBoard; //a board used to check for adjacency, valid word, etc.
    private TilesBag tilesBag; //a bag of tiles in the game
    private WordList wordList; //valid word list of words from example txt file
    private Random rand; //to get random tiles from the bag when swapping or replacing placed tiles
    private String statusMessage; //a message that inform the player of an illegal game play move has occured
    private ArrayList<String> placedWords; //keeps an array list of the words placed on the board

    /**
     * Initializes the list of players, the boards, the user input scanner, the bag of tiles,
     * the list of words and starts the game.
     */
    public GameModel(){
        players = new ArrayList<>();
        winner = null;
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        rand = new Random();
        placedWords = new ArrayList<>();
    }

    /**
     * Returns the game baord of cells that either have a tile or does not have a tile.
     * @return the game board.
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     * Creates and adds a new player to the game storing it in an array list of players.
     * @param playerName the name of the player
     */
    public void addPlayer(String playerName)
    {
        Player player = new Player(playerName); //creates a new player with the player name
        players.add(player); //adds it to the array list of players
        getRandomTiles(7, player); //assigns 7 random tiles to the player
    }

    /**
     * Adds an AIPlayer to the list of AIPlayers, kept them separate to avoid
     * down casting and for easier functionality.
     *
     * @param AITag the name of the AIPlayer.
     */
    public void addAIPlayer(String AITag){
        AIPlayer aiPlayer = new AIPlayer(AITag); //creates a new AIPlayer
        players.add(aiPlayer); //adds it to the array list of AI players
        getRandomTiles(7, aiPlayer); //assigns 7 random tiles to the AI player
    }

    /**
     * Returns the array list of players in the game.
     * @return list of players
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    /**
     * Checks to see is the game has fished. When a game has finished the array list of tile bag is
     * empty and at least one of the players does not have any more tiles.
     * @return true if the game is finished, false otherwise
     */
    public boolean isGameFinished()
    {
        if(tilesBag.bagOfTileIsEmpty()) //checks if array list bag is empty
        {
            for(Player player: players) //traverses through the players
            {
                if(player.getTiles().isEmpty()) //if one of the players does not have any more tiles
                {
                    winner = player; //that player is the winner
                    return true; //returns true
                }
            }
        }
        return false; //if none of the conditions are met returns false
    }

    /**
     * Takes care of the players decision to place a tile. The tile being placed
     * must be entered horizontally/vertically and the word being inserted or words after
     * tile is inserted must be a valid word.
     *
     * @param player the player's current turn
     */
    public void playerPlaceTile(Player player, ArrayList<Tiles> tiles, ArrayList<Integer> rowPositions,ArrayList<Integer> colPositions){
        if (placeWord(tiles, rowPositions, colPositions, player)){ //if the word was successfully placed
            gameBoard = checkBoard.copyBoard(); //real board gets the checked board
            for (Tiles tile : tiles) //traverses through the tiles
            {
                player.getTiles().remove(tile); //removes the tiles from the player
            }
            if (!tilesBag.bagOfTileIsEmpty()) //if the array list of bags still has tiles left
            {
                getRandomTiles(tiles.size(), player); //gets the player random tiles
            }
        }
    }

    /**
     * Gets the status message if the player has played a turn and commited an illegal move, ie.
     * placed a non-adjacent word, non-existing word, etc.
     * @return the status message
     */
    public String getStatusMessage()
    {
        return statusMessage;
    }


    /**
     * Places a word on the board, the first word needs to go through the middle square.
     * Then all words must also be connected horizontally or vertically and must be a valid word
     * from the word list.
     *
     * @param tempTiles a list of the tiles
     * @param tempRowPositions a list of row positions for the tiles
     * @param tempColPositions a list of column positions for the tiles
     * @param player the player who is currently at turn
     *
     * @return boolean true if the word was placed, false otherwise
     */
    public boolean placeWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions, Player player) {
        Board savedCheckBoard = checkBoard.copyBoard(); // Save the board state for rollback
        boolean isFirstWord = checkBoard.checkMiddleBoardEmpty(); // True if middle cell is empty, indicating the first move
        boolean isAdjacent = false;

        // Attempt to place each tile and check for valid conditions
        for (int i = 0; i < tempTiles.size(); i++) {
            int row = tempRowPositions.get(i);
            int col = tempColPositions.get(i);

            // Ensure the board space is empty
            if (!checkBoard.checkBoardTileEmpty(row, col)) {
                checkBoard = savedCheckBoard.copyBoard(); // Restore board state
                statusMessage = "Error: Board space already occupied.";
                return false;
            }

            // Temporarily place tile to check adjacency and center coverage
            checkBoard.placeBoardTile(row, col, tempTiles.get(i));

            // Check if the middle cell is covered on the first move
            if (isFirstWord && row == 7 && col == 7) {
                isAdjacent = true; // Middle cell is covered, so first word placement is valid
            }

            // For subsequent words, check if the tile is adjacent to any occupied cell
            if (!isFirstWord && checkBoard.checkAdjacentBoardConnected(row, col)) {
                isAdjacent = true; // Tile is adjacent to an existing tile, making placement valid
            }
            checkBoard.removeBoardTile(row, col);
        }

        // Restore the board if conditions are not met
        if (isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Middle cell not covered for the first word.";
            return false;
        } else if (!isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Word placement is not adjacent to any existing words.";
            return false;
        }

        for (int i = 0; i < tempTiles.size(); i++)
        {
            int row = tempRowPositions.get(i);
            int col = tempColPositions.get(i);

            checkBoard.placeBoardTile(row, col, tempTiles.get(i));

        }

        // Validate the formed word
        if (!checkValidWord())
        {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Invalid word.";
            return false;
        }

        statusMessage = "Word placed successfully.";
        player.addScore(turnScore(tempTiles, tempRowPositions, tempColPositions));
        return true;
    }

    /**
     * When a word/words are placed on the board, this method will be called to calculate
     * the score earned by these moves. It will achieve this by adding the score values of all tiles
     * placed, as well as adjacent tiles to give points for the entire words created, not
     * just newly placed tiles
     *
     * @param tempTiles a list of the tiles
     * @param tempRowPositions a list of row positions for the tiles
     * @param tempColPositions a list of column positions for the tiles
     * @return the total score earned in the move
     */
    public int turnScore(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions){
        int score = 0; //Create a variable for the score to be returned
        ArrayList<Cell> visitedCells = new ArrayList<>(); //Keep track of cells used to add to score
        for (Tiles t : tempTiles){
            score += t.getNumber(); //Add the combined score of all the new tiles placed to score
        }
        for (int j = 0; j < tempTiles.size(); j++){ //For each tile, we will see the words that they formed, and add to the score for the whole word
            for (int i = tempRowPositions.get(j) + 1; i < 15; i++){ //Starting from the placed tile, look for tiles to the right
                if (gameBoard.getCell(i, tempColPositions.get(j)).getTile() != null && !(visitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j))))){ //Check that the cell is has a tile placed
                    score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //For all other tiles in the word to the right of the placed tiles, add their score, as they are a part of the newly placed word
                    visitedCells.add(gameBoard.getCell(i, tempColPositions.get(j))); //Mark this tile as visited so we don't add it's score twice in one move
                }
                else{
                    break;
                }
            }
            for (int i = tempRowPositions.get(j) - 1; i >= 0; i--){ //Starting from the placed tile, look for tiles to the left
                if (gameBoard.getCell(i, tempColPositions.get(j)).getTile() != null && !(visitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j))))){
                    score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber();
                    visitedCells.add(gameBoard.getCell(i, tempColPositions.get(j)));//Mark this tile as visited so we don't add it's score twice in one move
                }
                else{
                    break;
                }
            }
            for (int i = tempColPositions.get(j) + 1; i < 15; i++){ //Starting from the placed tile, look for tiles down
                if (gameBoard.getCell(tempRowPositions.get(j), i).getTile() != null && !(visitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i)))){
                    score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber();
                    visitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i)); //Mark this tile as visited so we don't add it's score twice in one move
                }
                else{
                    break;
                }
            }
            for (int i = tempColPositions.get(j) - 1; i >= 0; i--){ //Starting from the placed tile, look for tiles up
                if (gameBoard.getCell(tempRowPositions.get(j), i).getTile() != null && !(visitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i)))){
                    score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber();
                    visitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i)); //Mark this tile as visited so we don't add it's score twice in one move
                }
                else{
                    break;
                }
            }
        }
        return score;
    }


    /**
     * Checks to see if the added word is a valid word from the list of words in the
     * txt file. Checks each row and column of the board to make sure that all added words and
     * subwords match the word list.
     *
     * @return true if the word is a valid word, false otherwise
     *
     */
    public boolean checkValidWord() {
        ArrayList<String> temp = new ArrayList<>(placedWords); //creates a temp array list to store the current words place already
        placedWords.clear(); //clears the array list of words.
        for (int row = 0; row < 15; row++) { //traverses through all rows of the board to checks for words
            if (!isValidWordInRow(row)) { //if there was not a valid word in that row then appends the word back to the array list
                for (String s : temp){
                    placedWords.add(s);
                }
                return false; //returns false
            }
        }
        for (int col = 0; col < 15; col++) { //traverses through all columns in the board
            if (!isValidWordInColumn(col)) { //if there was not a valid word that in that column then apprehends the word list
                for (String s : temp){
                    placedWords.add(s);
                }
                return false; //returns false
            }
        }
        return true; //returns true that all words were valid
    }


    /**
     * Checks if all the words formed in the specified row are valid according to
     * the word list. Words are validated both forwards and backwards.
     *
     * @param row the row index to validate
     * @return true if all words in the row are valid, false otherwise
     */
    private boolean isValidWordInRow(int row) {
        StringBuilder word = new StringBuilder(); //creates a string builder that will add all the words in the row
        for (int col = 0; col < 15; col++) {
            if (checkBoard.getBoard()[row][col].isOccupied()) { //traverses through the row adding all cells with a tile to form a word
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); //adds them to the string builder
            } else if (word.length() > 1) { //if there is at least 2 tiles
                if (!isWordValidBothDirections(word)) { //checks the words, in both directions if not valid returns false
                    return false;
                }
                if (word.length() > 1){ //adds it to the word list
                    addPlacedWord(word);
                }
                word.setLength(0); //resets the length of the string builder
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) { //if there is a word at the end of the row and is not valid
            return false; //returns false
        }
        return true;
    }

    /**
     * Checks if all the words formed in the specified column are valid according to
     * the word list. Words are validated both forwards and backwards.
     *
     * @param col the column index to validate
     * @return true if all words in the column are valid, false otherwise
     */
    private boolean isValidWordInColumn(int col) {
        StringBuilder word = new StringBuilder(); //creates a sring builder to check for the word
        for (int row = 0; row < 15; row++) { //traverses through all columns on the board.
            if (checkBoard.getBoard()[row][col].isOccupied()) { //if there is a tile placed in the cell
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); //adds it to the string builder
            } else if (word.length() > 1) { //if there is at least two tiles on the board
                if (!isWordValidBothDirections(word)) { //if it's not a valid word return false
                    return false;
                }
                if (word.length() > 1){ //adds it to the word list
                    addPlacedWord(word);
                }
                word.setLength(0); //reset the string builder list size
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) { //if there is a word at the end of a column and is not valid
            return false; //return false
        }
        return true;
    }

    /**
     * Checks if the provided word is valid in either forward or reverse direction
     * according to the word list.
     *
     * @param word the word to validate
     * @return true if the word or its reverse is valid, false otherwise
     */
    private boolean isWordValidBothDirections(StringBuilder word) {
        return wordList.isValidWord(word.toString()) || wordList.isValidWord(word.reverse().toString());
    }


    /**
     * Takes care of when a player decides to swap tile(s). The tile(s) are swapped
     * with the tiles in the tile bag. A player may swap tiles as long as they have enough tiles to swap
     * and that the bag of tiles is not empty.
     * @param player who currently has a turn
     */
    public void playerSwapTile(Player player, List<Integer> tileIndices) {
        if (tilesBag.bagOfTileIsEmpty()) {
            return; // No tiles to swap
        }
        for (int index : tileIndices) { //gets the tiles the player wants to swap
            Tiles t = player.getTiles().get(index); //get the tile
            player.getTiles().remove(index); //removes from the player
            replaceSwappedTile(player, index); //gets a new tile from array list bag
            tilesBag.bagArraylist().add(t); //adds it bad to the array list bag
        }
    }

    /**
     * Gets a random number of tiles from the bag of tiles to a player at the beginning
     * of the game and assigns it to the players.
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTiles(int numOfTiles, Player player){
        for (int i = 0; i < numOfTiles; i++){
            int rnd = rand.nextInt(tilesBag.bagArraylist().size()); //random index
            player.getTiles().add(tilesBag.bagArraylist().get(rnd)); //gets random tile
            tilesBag.bagArraylist().remove(rnd); //removes it from bag
        }
    }

    /**
     * Replaces a players tile(s) with tile(s) from the bag placing them
     * at the right index.
     * @param player the player who is swapping
     * @param index the index of the tile being replaced
     */
    public void replaceSwappedTile(Player player, int index){
        int rnd = rand.nextInt(tilesBag.bagArraylist().size()); //random index
        player.getTiles().add(index, tilesBag.bagArraylist().get(rnd)); //places at correct index
        tilesBag.bagArraylist().remove(rnd); //removes from bag
    }

    /**
     * Returns the bag containing all tiles used in the game.
     *
     * @return the tile bag for the game
     */
    public TilesBag getTilesBag() {
        return tilesBag;
    }


    /**
     * Returns the array list of words added on the board.
     * @return list of words on board.
     */
    public ArrayList<String> getPlacedWords(){
        return placedWords;
    }

    /**
     * Adds a word to the array list of words.
     * @param word the word to be added
     */
    public void addPlacedWord(StringBuilder word){
        if (wordList.isValidWord(word.toString())){
            placedWords.add(word.toString());
        }
        else{
            placedWords.add(word.reverse().toString());
        }
    }

    /**
     * Updates the check board to have the current baord from the game.
     */
    public void updateCheckBoard() {
        checkBoard = gameBoard.copyBoard();
    }

    /**
     * Gets the winner of the game
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Returns the word list.
     * @return word list.
     */
    public WordList getWordList() {
        return wordList;
    }

    /**
     * Coverts a word into an array list of tiles.
     * @param word the word.
     * @param player the player at hand.
     * @return list of player tiles.
     */
    public ArrayList<Tiles> AIWordToTiles(String word, Player player){
        ArrayList<Tiles> tempTiles = new ArrayList<>();
        for (char c : word.toCharArray()){
            for(Tiles tiles: player.getTiles()){
                if(tiles.getLetter().equals(String.valueOf(c))){
                    tempTiles.add(tiles);
                }
            }
        }
        return tempTiles;
    }

    /**
     * Gets all possible coordinates to place a tile on the board based on a row or column number and
     * length of word.
     * @param number the starting row or column.
     * @param range the end of row or column.
     * @return list of range row or column.
     */
    public static ArrayList<Integer> getAllPossibleEntries(int number, int range) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i <= range; i++) {
            if (number + i <= 14) {
                result.add(number + i);
            } else {
                break;
            }
        }
        return result;
    }

    public String checkPlaceableWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions) {
        Board savedCheckBoard = checkBoard.copyBoard(); // Save the board state for rollback
        boolean isFirstWord = checkBoard.checkMiddleBoardEmpty(); // True if middle cell is empty, indicating the first move
        boolean isAdjacent = false;

        // Attempt to place each tile and check for valid conditions
        for (int i = 0; i < tempTiles.size(); i++) {
            int row = tempRowPositions.get(i);
            int col = tempColPositions.get(i);

            // Ensure the board space is empty
            if (!checkBoard.checkBoardTileEmpty(row, col)) {
                checkBoard = savedCheckBoard.copyBoard(); // Restore board state
                statusMessage = "Error: Board space already occupied.";
                return statusMessage;
            }

            // Temporarily place tile to check adjacency and center coverage
            checkBoard.placeBoardTile(row, col, tempTiles.get(i));

            // Check if the middle cell is covered on the first move
            if (isFirstWord && row == 7 && col == 7) {
                isAdjacent = true; // Middle cell is covered, so first word placement is valid
            }

            // For subsequent words, check if the tile is adjacent to any occupied cell
            if (!isFirstWord && checkBoard.checkAdjacentBoardConnected(row, col)) {
                isAdjacent = true; // Tile is adjacent to an existing tile, making placement valid
            }
            checkBoard.removeBoardTile(row, col);
        }

        // Restore the board if conditions are not met
        if (isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Middle cell not covered for the first word.";
            return statusMessage;
        } else if (!isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Word placement is not adjacent to any existing words.";
            return statusMessage;
        }

        for (int i = 0; i < tempTiles.size(); i++)
        {
            int row = tempRowPositions.get(i);
            int col = tempColPositions.get(i);

            checkBoard.placeBoardTile(row, col, tempTiles.get(i));

        }

        // Validate the formed word
        if (!checkValidWord())
        {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Invalid word.";
            return statusMessage;
        }

        statusMessage = "Word placed successfully.";
        checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
        return statusMessage;
    }


    public static void main(String[] args) {
        GameModel gameModel = new GameModel();

        gameModel.addAIPlayer("AI1");

        for (Player player : gameModel.getPlayers()) {
            if (player instanceof AIPlayer) {
                AIPlayer aiPlayer = (AIPlayer) player;
                HashSet<String> test1 = aiPlayer.getAllWordComputations(gameModel.getWordList());
                for (String word : test1) {
                    ArrayList<Tiles> tempTiles = gameModel.AIWordToTiles(word, aiPlayer);
                    for (int i = 0; i < 15; i++) {
                        ArrayList<Integer> temprowsPositions = new ArrayList<>();
                        // Fill temprowsPositions with 'i' repeated for the length of the word
                        for (int a = 0; a < word.length(); a++) {
                            temprowsPositions.add(i);
                        }

                        for (int j = 0; j < 15 - word.length() + 1; j++) {
                            ArrayList<Integer> tempcolPositions = new ArrayList<>();
                            // Generate consecutive column positions starting at 'j'
                            for (int b = 0; b < word.length(); b++) {
                                tempcolPositions.add(j + b);
                            }

                            // Ensure temprowsPositions and tempcolPositions have the same size as tempTiles
                            if (tempTiles.size() == temprowsPositions.size() && tempTiles.size() == tempcolPositions.size()) {
                                gameModel.playerPlaceTile(aiPlayer, tempTiles, temprowsPositions, tempcolPositions);
                                if(!gameModel.getGameBoard().checkMiddleBoardEmpty()){
                                    gameModel.getGameBoard().printBoard();
                                    System.out.println();
                                    System.out.println();
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}
