
import java.util.*;

/**
 * This is the game model class that brings all the logic together for a game, here
 * also the user will input their commands and get an update of the board, their score etc.
 * Each word that a player places will be checked for adjacency, valid word etc.
 *
 * Author(s): Rami Ayoub
 * Version: 1.0
 * Date: Wednesday, October 16, 2024
 */

public class GameModel {
    private ArrayList<Player> players; //list of players in the game
    private Player winner; //player who win the game
    private int highestScore; //player with the highest score
    private Board gameBoard; //the actual game board displayed
    private Board checkBoard; //a board used to check for adjacency, valid word, etc.
    private TilesBag tilesBag; //a bag of tiles in the game
    private WordList wordList; //valid word list of words from example txt file
    private Random rand; //to get random tiles from the bag when swapping or replacing placed tiles
    private String statusMessage;
    private ArrayList<String> placedWords;

    /**
     * Initializes the list of players, the boards, the user input scanner, the bag of tiles,
     * the list of words and starts the game.
     */
    public GameModel(){
        players = new ArrayList<>();
        winner = null;
        highestScore = 0;
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        rand = new Random();
        placedWords = new ArrayList<>();
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public void addPlayer(String playerName)
    {
        Player player = new Player(playerName);
        players.add(player);
        getRandomTiles(7, player);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public boolean isGameFinished()
    {
        if(tilesBag.bagOfTileIsEmpty())
        {
            for(Player player: players)
            {
                if(player.getTiles().isEmpty())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Takes care of the players decision to place a tile. The tile being placed
     * must be entered horizontally/vertically and the word being inserted or words after
     * tile is inserted must be a valid word.
     *
     * @param player the player's current turn
     */
    public void playerPlaceTile(Player player, ArrayList<Tiles> tiles, ArrayList<Integer> rowPositions,ArrayList<Integer> colPositions){
        if (placeWord(tiles, rowPositions, colPositions, player)){ //if the word was sucessfully placed
            gameBoard = checkBoard.copyBoard(); //real board gets the checked board
            for (Tiles tile : tiles)
            {
                player.getTiles().remove(tile);
            }
            if (!tilesBag.bagOfTileIsEmpty())
            {
                getRandomTiles(tiles.size(), player);
            }
        }
        // Print or log the status message
        System.out.println(statusMessage);
    }

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
            checkBoard.removeBoardTile(row, col, tempTiles.get(i));
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
    public String checkPlaceableWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions, Player player) {
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
            checkBoard.removeBoardTile(row, col, tempTiles.get(i));
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
        ArrayList<String> temp = new ArrayList<>(placedWords);
        placedWords.clear();
        for (int row = 0; row < 15; row++) {
            if (!isValidWordInRow(row)) {
                for (String s : temp){
                    placedWords.add(s);
                }
                return false;
            }
        }
        for (int col = 0; col < 15; col++) {
            if (!isValidWordInColumn(col)) {
                for (String s : temp){
                    placedWords.add(s);
                }
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if all the words formed in the specified row are valid according to
     * the word list. Words are validated both forwards and backwards.
     *
     * @param row the row index to validate
     * @return true if all words in the row are valid, false otherwise
     */
    private boolean isValidWordInRow(int row) {
        StringBuilder word = new StringBuilder();
        for (int col = 0; col < 15; col++) {
            if (checkBoard.getBoard()[row][col].isOccupied()) {
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim());
            } else if (word.length() > 1) {
                if (!isWordValidBothDirections(word)) {
                    return false;
                }
                if (word.length() > 1){
                    addPlacedWord(word);
                }
                word.setLength(0);
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) {
            return false;
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
        StringBuilder word = new StringBuilder();
        for (int row = 0; row < 15; row++) {
            if (checkBoard.getBoard()[row][col].isOccupied()) {
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim());
            } else if (word.length() > 1) {
                if (!isWordValidBothDirections(word)) {
                    return false;
                }
                if (word.length() > 1){
                    addPlacedWord(word);
                }
                word.setLength(0);
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) {
            return false;
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
        for (int index : tileIndices) {
            Tiles t = player.getTiles().get(index);
            player.getTiles().remove(index);
            replaceSwappedTile(player, index);
            tilesBag.bagArraylist().add(t);
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
     * Returns the check board, used for validating word placements and
     * ensuring adjacency requirements.
     *
     * @return the current check board
     */
    public Board getCheckBoard() {
        return checkBoard;
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
     * Returns the word list containing all valid words for the game.
     *
     * @return the word list for validating words
     */
    public WordList getWordList() {
        return wordList;
    }

    /**
     * Retrieves a player from the game by their name.
     *
     * @param name the name of the player to retrieve
     * @return the Player object if a match is found; null otherwise
     */
    public Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null; // Return null if no player with the given name is found
    }

    public ArrayList<String> getPlacedWords(){
        return placedWords;
    }

    public void addPlacedWord(StringBuilder word){
        if (wordList.isValidWord(word.toString())){
            placedWords.add(word.toString());
        }
        else{
            placedWords.add(word.reverse().toString());
        }
    }

    public void updateCheckBoard() {
        checkBoard = gameBoard.copyBoard();
    }
}
