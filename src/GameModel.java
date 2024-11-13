
import java.util.*;

/**
 * This is the game model class that brings all the logic together for a game, here
 * also the user will input their commands and get an update of the board, their score etc.
 * Each word that a player places will be checked for adjacency, valid word etc.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 2.0
 * Date: Wednesday, November 6th, 2024
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
    private Player currentPlayer;  // current player
    private int numPlayers;
    private ArrayList<Tiles> tilesToRemove; // list of tiles to remove from a players hand
    private Set<Pair<Integer, Integer>> newestLettersCoordinates; // keeps track of the newest word placed letter coordinates
    private ArrayList<String> placedWords; // places words on the board
    private String newestWord;
    private TilesBag valueOfLetter;
    private int wordScore;

    /**
     * Initializes the list of players, the boards, the user input scanner, the bag of tiles,
     * the list of words and starts the game.
     */
    public GameModel(){
        players = new ArrayList<>();
        tilesToRemove = new ArrayList<>();
        winner = null;
        highestScore = 0;
        wordScore = 0;
        gameBoard = new Board(16, 16);
        checkBoard = new Board(16, 16);
        tilesBag = new TilesBag();
        wordList = new WordList();
        rand = new Random();
        newestLettersCoordinates = new HashSet<>();
        placedWords = new ArrayList<>();
        valueOfLetter = new TilesBag();
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
                player.addScore(tile.getNumber());
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
            if (isFirstWord && row == 8 && col == 8) {
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

        // Validate the formed word
        if (!checkValidWord())
        {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Invalid word.";
            return false;
        }

        for (int i = 0; i < tempTiles.size(); i++)
        {
            int row = tempRowPositions.get(i);
            int col = tempColPositions.get(i);

            checkBoard.placeBoardTile(row, col, tempTiles.get(i));

        }

        statusMessage = "Word placed successfully.";
        return true;
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
        newestWord = "";
        for (int row = 0; row < 16; row++) {
            if (!isValidWordInRow(row)) {
                return false;
            }
        }
        for (int col = 0; col < 16; col++) {
            if (!isValidWordInColumn(col)) {
                return false;
            }
        }
        return true;
    }


    public void updateCheckBoard() {
        checkBoard = gameBoard.copyBoard();
    }

    public void updateGameBoard() {
        gameBoard = checkBoard.copyBoard();
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
        for (int col = 0; col < 16; col++) {
            if (checkBoard.getBoard()[row][col].isOccupied()) {
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim());
            } else if (word.length() > 1) {
                if (!isWordValidBothDirections(word)) {
                    return false;
                }
                if (word.length() > 1 && !placedWords.contains(word.toString())) {
                    newestWord = word.toString();
                    placedWords.add(newestWord);
                    System.out.println(word.toString());
//                    addPlacedWord(word);
                }
                else {
                    newestWord = "";
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
        for (int row = 0; row < 16; row++) {
            if (checkBoard.getBoard()[row][col].isOccupied()) {
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim());
            } else if (word.length() > 1) {
                if (!isWordValidBothDirections(word)) {
                    return false;
                }
                if (word.length() > 1 && !placedWords.contains(word.toString())) {
                    // word
                    newestWord = word.toString();
                    placedWords.add(newestWord);
                    System.out.println(word);
//                    addPlacedWord(word);
                }
                else {
                    newestWord = "";
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
     * Gets the newest word added to the board
     * @return string of newest word
     */
    public String getNewestWord() {
        return newestWord;
    }


    /**
     * Gets the score of the word
     * @param word Word to calculate score
     */
    public int getScore(String word) {
        int value = 0;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            String strLetter = String.valueOf(letter);
            System.out.println(strLetter);
            HashMap<String, Integer> check = valueOfLetter.getValueOfTiles();
            value += check.get(strLetter);
        }
        return value;
    }

    public void addPlacedWord(StringBuilder word){
        if (wordList.isValidWord(word.toString())){
            placedWords.add(word.toString());
        }
        else{
            placedWords.add(word.reverse().toString());
        }
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
     * Sets the number of players
     * @param numberOfPlayers number of players
     */
    public void setNumPlayers(int numberOfPlayers) {
        numPlayers = numberOfPlayers;
    }

    public void removeTilesFromPlayerHand() {
        for (Tiles tile: tilesToRemove) {
            currentPlayer.getTiles().remove(tile);
        }
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

    /**
     * Gets the tile in the players hand at specified spot
     * @param col column number of tile to get
     * @return the tile at specified spot
     */
    public Tiles getPlayerTile(int col) {
        // get current player
        Tiles tile = currentPlayer.getTiles().get(col);
        return tile;
    }

    /**
     * Adds a tile to a specified spot on the checkBoard
     * @param row row number of grid spot to add tile
     * @param col column number of grid spot to add tile
     * @param tile tile to add
     */
    public void addTile(int row, int col, Tiles tile) {
        // adds a tile to specified spot on checkboard
        checkBoard.placeBoardTile(row, col, tile);
    }

    /**
     * Add a tile to a list containing tiles to remove from a players hand if the word
     * is found to be valid.
     * @param tile the tile to add to the remove list
     */
    public void addTileToRemove(Tiles tile) {
        tilesToRemove.add(tile);
    }


    /**
     * Creates a set of the coordinates of the letters placed on a given turn.
     * @param row row number of letter
     * @param col column number of letter
     */
    public void addLetterToSet(int row, int col) {
        newestLettersCoordinates.add(new Pair<>(row, col));
    }

    /**
     * Resets the set of letter coordinates
     */
    public void resetLetterSet() {
        newestLettersCoordinates.clear();
    }

    public void getWordPlaced() {

    }


    /**
     * Gets the current player based on the current game turn
     * @param currentTurn the current game turn
     * @return the current player
     */
    public Player getCurrentPlayer(int currentTurn) {
        currentPlayer = players.get(currentTurn % numPlayers);
        return currentPlayer;
    }

    public void printCheckBoard() {
        System.out.println("Check board");
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                System.out.print(checkBoard.getBoard()[row][col].toString());
            }
            System.out.println();
        }
    }

    public void printGameBoard() {
        System.out.println("Game board");
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                System.out.print(gameBoard.getBoard()[row][col].toString());
            }
            System.out.println();
        }
    }










    public boolean searchWordDirection() {
        // First, we'll check if the set of coordinates form a horizontal or vertical line
        boolean isHorizontal = isHorizontal();   // Check if the letters form a horizontal line
        boolean isVertical = isVertical();      // Check if the letters form a vertical line

        if (isHorizontal) {
            // Check for horizontal line: find any adjacent occupied grid spots in the left or right direction
            return checkAdjacentInDirection(true);  // 'true' for horizontal direction (left/right)
        } else if (isVertical) {
            // Check for vertical line: find any adjacent occupied grid spots in the up or down direction
            return checkAdjacentInDirection(false); // 'false' for vertical direction (up/down)
        }

        // If not horizontal or vertical, return false (invalid word placement)
        return false;
    }


    private boolean isHorizontal() {
        // Assume the letters are ordered by row/col in your set, check if all rows are the same (horizontal)
        int firstRow = newestLettersCoordinates.iterator().next().getKey();  // Get row of the first coordinate
        for (Pair<Integer, Integer> coordinate : newestLettersCoordinates) {
            if (!coordinate.getKey().equals(firstRow)) {
                return false;  // If rows are not the same, it's not horizontal
            }
        }
        return true;  // All rows are the same, so it's horizontal
    }

    private boolean isVertical() {
        // Assume the letters are ordered by row/col in your set, check if all columns are the same (vertical)
        int firstCol = newestLettersCoordinates.iterator().next().getValue();  // Get column of the first coordinate
        for (Pair<Integer, Integer> coordinate : newestLettersCoordinates) {
            if (!coordinate.getValue().equals(firstCol)) {
                return false;  // If columns are not the same, it's not vertical
            }
        }
        return true;  // All columns are the same, so it's vertical
    }


    private boolean checkAdjacentInDirection(boolean isHorizontal) {
        // Check each coordinate in the set and see if there are adjacent occupied cells in the same direction
        for (Pair<Integer, Integer> coordinate : newestLettersCoordinates) {
            int row = coordinate.getKey();
            int col = coordinate.getValue();

            // If checking horizontally, check adjacent cells to the left and right
            if (isHorizontal) {
                // Check left
                if (col > 0 && gameBoard.getBoard()[row][col - 1].isOccupied()) {
                    return true;  // Found an adjacent occupied cell
                }
                // Check right
                if (col < 16 - 1 && gameBoard.getBoard()[row][col + 1].isOccupied()) {
                    return true;  // Found an adjacent occupied cell
                }
            } else {
                // If checking vertically, check adjacent cells above and below
                // Check up
                if (row > 0 && gameBoard.getBoard()[row - 1][col].isOccupied()) {
                    return true;  // Found an adjacent occupied cell
                }
                // Check down
                if (row < 16 - 1 && gameBoard.getBoard()[row + 1][col].isOccupied()) {
                    return true;  // Found an adjacent occupied cell
                }
            }
        }
        return false;  // No adjacent occupied cells found in the specified direction
    }

    public String buildWordFromCoordinates() {
        StringBuilder word = new StringBuilder();  // To build the word

        // First, determine if the new letters form a horizontal or vertical line.
        boolean isHorizontal = isHorizontalLine(newestLettersCoordinates);

        // Determine the direction and find the full set of coordinates forming the word
        Set<Pair<Integer, Integer>> fullCoordinates = new HashSet<>(newestLettersCoordinates);  // Start with the new letters' coordinates

        if (isHorizontal) {
            // Collect adjacent existing letters horizontally (to the left and right)
            for (Pair<Integer, Integer> coordinate : newestLettersCoordinates) {
                int row = coordinate.getKey();
                int col = coordinate.getValue();

                // Check to the left
                int leftCol = col - 1;
                while (leftCol >= 0 && gameBoard.getBoard()[row][leftCol].isOccupied()) {
                    fullCoordinates.add(new Pair<>(row, leftCol));  // Add left letters to the set
                    leftCol--;
                }

                // Check to the right
                int rightCol = col + 1;
                while (rightCol < gameBoard.getCols() && gameBoard.getBoard()[row][rightCol].isOccupied()) {
                    fullCoordinates.add(new Pair<>(row, rightCol));  // Add right letters to the set
                    rightCol++;
                }
            }

            // Now, build the word starting from the leftmost coordinate and moving right
            int minCol = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> coord : fullCoordinates) {
                minCol = Math.min(minCol, coord.getValue());  // Find the leftmost coordinate
            }

            // Start from the leftmost column and iterate right until an empty space is encountered
            for (int col = minCol; col < gameBoard.getCols(); col++) {
                boolean foundLetter = false;
                for (int row = 0; row < gameBoard.getRows(); row++) {
                    if (fullCoordinates.contains(new Pair<>(row, col)) && gameBoard.getBoard()[row][col].isOccupied()) {
                        word.append(gameBoard.getBoard()[row][col].getTile().getLetter());
                        foundLetter = true;
                        break;  // Move to the next column after finding the letter in this row
                    }
                }
                if (!foundLetter) break;  // Stop once we hit an empty square (end of the word)
            }

        } else {
            // Collect adjacent existing letters vertically (above and below)
            for (Pair<Integer, Integer> coordinate : newestLettersCoordinates) {
                int row = coordinate.getKey();
                int col = coordinate.getValue();

                // Check above
                int topRow = row - 1;
                while (topRow >= 0 && gameBoard.getBoard()[topRow][col].isOccupied()) {
                    fullCoordinates.add(new Pair<>(topRow, col));  // Add top letters to the set
                    topRow--;
                }

                // Check below
                int bottomRow = row + 1;
                while (bottomRow < gameBoard.getRows() && gameBoard.getBoard()[bottomRow][col].isOccupied()) {
                    fullCoordinates.add(new Pair<>(bottomRow, col));  // Add bottom letters to the set
                    bottomRow++;
                }
            }

            // Now, build the word starting from the topmost coordinate and moving down
            int minRow = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> coord : fullCoordinates) {
                minRow = Math.min(minRow, coord.getKey());  // Find the topmost coordinate
            }

            // Start from the topmost row and iterate down until an empty space is encountered
            for (int row = minRow; row < gameBoard.getRows(); row++) {
                boolean foundLetter = false;
                for (int col = 0; col < gameBoard.getCols(); col++) {
                    if (fullCoordinates.contains(new Pair<>(row, col)) && gameBoard.getBoard()[row][col].isOccupied()) {
                        word.append(gameBoard.getBoard()[row][col].getTile().getLetter());
                        foundLetter = true;
                        break;  // Move to the next row after finding the letter in this column
                    }
                }
                if (!foundLetter) break;  // Stop once we hit an empty square (end of the word)
            }
        }
        System.out.println("Test");
        System.out.println(word);
        return word.toString();  // Return the built word
    }

    // Utility method to check if the set of coordinates form a horizontal line
    private boolean isHorizontalLine(Set<Pair<Integer, Integer>> coordinates) {
        // Check if all the coordinates share the same row, i.e., they form a horizontal line
        int row = -1;
        for (Pair<Integer, Integer> coordinate : coordinates) {
            if (row == -1) {
                row = coordinate.getKey();
            } else if (row != coordinate.getKey()) {
                return false;  // If any coordinate is in a different row, it's not horizontal
            }
        }
        return true;  // All coordinates are in the same row
    }
}