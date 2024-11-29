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
    private Stack<Board> boardsAtEachTurn; //will store the board at each time something is sucessfull placed
    private Stack<ArrayList<Integer>> previousPlayerScores; //stores the score of the player
    private Stack<ArrayList<ArrayList<Tiles>>> previousPlayersTiles; //stores the tiles that was previously in the hand of each  as a nested array list
    private Stack<ArrayList<String>> previousPlacedWords; //stores the words that were previously placed in a previous turn
    private Stack<TilesBag> previousTilesBags; //stores the previous tiles bag as a player proceeds ti place tiles on the board
    private Stack<Player> previousPlayers; //stores the previous players that nave placed a turn
    private Player currentPlayer; //the curent player who is a turn after being poped from the player stack

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
        boardsAtEachTurn = new Stack<>();
        previousPlayerScores = new Stack<>();
        previousPlayersTiles = new Stack<>();
        previousPlacedWords = new Stack<>();
        previousTilesBags = new Stack<>();
        previousPlayers = new Stack<>();
        currentPlayer = null;
    }

    /**
     * Pushes a board to the stack of board.
     */
    public void saveBoardInStack() {
        boardsAtEachTurn.push(gameBoard);
    }

    /**
     * Stets a board from the stack.
     */
    public void setBoardFromStack(){
        if(!boardsAtEachTurn.isEmpty()){ //checks that the stack is not empty
            Board boardInStack = boardsAtEachTurn.pop(); //gets the board from the top of the stack
            gameBoard = boardInStack.copyBoard(); //copys it to the game board
            checkBoard = boardInStack.copyBoard(); //copies it oto the check board
        }
    }

    /**
     * Returns the game baord of cells that either have a tile or does not have a tile.
     * @return the game board.
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     * Saves the tiles of a player before they are played and stores them in a stack. Will store
     * the based on the players order of names entered at the beginning of the game
     */
    public void savePlayerTilesInStack(){
        ArrayList<ArrayList<Tiles>> allPlayerTiles = new ArrayList<>(); //a nested array list that will store the array list of players and then push it to the stack
        for(Player player: players){ //traverses through the players
            ArrayList<Tiles> playerTilesCopy = new ArrayList<>(); //this will hold a copy of players tiles
            for (Tiles tile : player.getTiles()) { //traverses through the tiles of that specific player
                playerTilesCopy.add(new Tiles(tile.getLetter(), tile.getNumber())); //creates new Tiles that match the players tiles as a copy
            }
            allPlayerTiles.add(playerTilesCopy); //adds the copies tiles of the player to the nexted arratlist of player tiles
        }
        previousPlayersTiles.push(allPlayerTiles); //pushes the array list of array list of player tiles to the stack
    }

    /**
     * Gets the tile of the players from the stack and updates.
     */
    public void setPlayerTilesFromStack() {
        if (!previousPlayersTiles.empty()) { //checks to make sure that the stack is not empty
            ArrayList<ArrayList<Tiles>> allPlayerTiles = previousPlayersTiles.pop(); //gets the array list of array list of players tiles form the top of the stack
            for (int i = 0; i < players.size(); i++) { //traverse though the number of players
                ArrayList<Tiles> playerTilesCopy = new ArrayList<>(); //this will hold a copy of players tiles
                for (Tiles tile : allPlayerTiles.get(i)) { //traverses through the nest loop of player tiles
                    playerTilesCopy.add(new Tiles(tile.getLetter(), tile.getNumber())); //creates a new tiles and save them in a copy array list
                }
                players.get(i).setTiles(playerTilesCopy); //sets the copy array list of tiles to the player
            }
        }
    }

    /**
     * Saves the scores of the players in the stack.
     */
    public void savePlayerScoresInStack(){
        ArrayList<Integer> playerScores = new ArrayList<>(); //creates an array list of integers for the players scores
        for(Player player: players){ //traverses through the players
            playerScores.add(player.getScore()); //adds the scores of the players to teh array list
        }
        previousPlayerScores.push(playerScores); //pushes the array list to the stack
    }

    /**
     * Gets the scores of the players from the stack and updates it.
     */
    public void setPlayerScoresFromStack(){
        if(!previousPlayerScores.isEmpty()){ //checks that the stack is not empty
            ArrayList<Integer> playerScores = previousPlayerScores.pop(); //pops the scores of the players from the stack
            for(int i = 0; i < players.size(); i++){ //traverses through the number of players
                players.get(i).setScore(playerScores.get(i)); //sets the scores of the players
            }
        }
    }

    /**
     * Saves the tiles bag in the stack.
     */
    public void saveTilesBagInStack(){
        previousTilesBags.push(new TilesBag(tilesBag.bagArraylist())); //pushes a copy of the tiles bag to the stack
    }

    /**
     * Gets the tiles bag form the stack and updates it.
     */
    public void setTilesBagFromStack(){
        if(!previousTilesBags.isEmpty()){ //checks to make sure that the stack is not empty
           TilesBag poppedTileBag = previousTilesBags.pop(); //pops the tiles bag from teh top of the stack
           tilesBag.setTilesBag(new ArrayList<>(poppedTileBag.bagArraylist())); //updates the array list of tiles for the tiles bag
        }
    }

    /**
     * Saves the list of words that has been placed in the game from the previous round in the stack.
     */
    public void savePlacedWordsInStack(){
        previousPlacedWords.push(new ArrayList<>(placedWords)); //pushes a copy of the words placed in the stack
    }

    /**
     * Gets the placed words list from the top of the stack and updates it.
     */
    public void setPlacedWordsFromStack(){
        if(!previousPlacedWords.isEmpty()){ //makes sure that the stack is not empty
            placedWords = previousPlacedWords.pop(); //gets the list of words from the top of the stack
        }
    }

    /**
     * Saves the player in the stack.
     * @param player the player being saved in the stack
     */
    public void savePlayerInStack(Player player){
        previousPlayers.push(player); //pushes the player in the stack
    }

    /**
     * Gets the player form the top of the stack.
     */
    public Player getPlayerFromStack(){
        if(!previousPlayers.isEmpty()){ //makes suer that the stack is not empty
            currentPlayer = previousPlayers.pop(); //pops the player from the stack
            return currentPlayer; //returns the player
        }

        return null; //returns null if the stack is empty
    }

    /**
     * Returns the current player that was popped from the stack.
     * @return the current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
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
                    // Determine the winner based on the highest score
                    winner = players.stream().max(Comparator.comparingInt(Player::getScore)).orElse(null); // Find the player with the highest score or returns null if there isn't a player in then list
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
        if (placeWord(tiles, rowPositions, colPositions)){ //if the word was successfully place
            saveBoardInStack(); //calls a function to save the board in the stack
            savePlayerTilesInStack(); //calls a function to save the player tiles in the stack
            savePlayerScoresInStack(); //calls a function to save the player scores in the stack
            saveTilesBagInStack(); //calls a function to save the tiles bag in the stack
            savePlayerInStack(player); //calls a function to save the player in the stack
            gameBoard = checkBoard.copyBoard(); //real board gets the checked board
            player.addScore(turnScore(tiles, rowPositions, colPositions)); //updates the players score
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
     *
     * @return boolean true if the word was placed, false otherwise
     */
    public boolean placeWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions) {
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
            return false;
        } else if (!isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
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
            return false;
        }

        if(isFirstWord){
            for (int i = 0; i < tempTiles.size(); i++){
                if(!checkBoard.checkAdjacentBoardConnected(tempRowPositions.get(i), tempColPositions.get(i))){
                    return false;
                }
            }
        }
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
     *
     * @return the status message if the word was successfully place or violated a rule.
     */
    public void checkPlaceableWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions) {
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
                return;
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
            return;

        } else if (!isFirstWord && !isAdjacent) {
            checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
            statusMessage = "Error: Word placement is not adjacent to any existing words.";
            return;
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
            return;
        }


        if(isFirstWord){ //If this is the first placed word, we must further explore to check that all placed tiles are adjacent to each other, with no spaces between them
            ArrayList<Tiles> visitedTiles = new ArrayList<>(); //ArrayList to keep track of all tiles adjacent to the first placed tile
            visitedTiles.add(tempTiles.get(0)); //We will use the first placed tile , and make sure all other tiles are adjacent to this one
            for (int i = tempRowPositions.get(0) + 1; i < 15; i++) { //Check every tile placed to the right of tile 0
                if (checkBoard.getCell(i, tempColPositions.get(0)).getTile() != null){
                    visitedTiles.add(checkBoard.getCell(i, tempColPositions.get(0)).getTile()); //If there is a tile placed there, add it to the visited tiles list
                } else{
                    break;
                }
            }
            for (int i = tempRowPositions.get(0) - 1; i >= 0; i--) { //Check every tile placed to the left of tile 0
                if (checkBoard.getCell(i, tempColPositions.get(0)).getTile() != null){
                    visitedTiles.add(checkBoard.getCell(i, tempColPositions.get(0)).getTile()); //If there is a tile placed there, add it to the visited tiles list
                } else{
                    break;
                }

            }
            for (int i = tempColPositions.get(0) + 1; i < 15; i++) { //Check every tile placed above tile 0
                if (checkBoard.getCell(tempRowPositions.get(0), i).getTile() != null){
                    visitedTiles.add(checkBoard.getCell(tempRowPositions.get(0), i).getTile()); //If there is a tile placed there, add it to the visited tiles list
                } else{
                    break;
                }
            }
            for (int i = tempColPositions.get(0) - 1; i >= 0; i--) { //Check every tile placed below tile 0
                if (checkBoard.getCell(tempRowPositions.get(0), i).getTile() != null){
                    visitedTiles.add(checkBoard.getCell(tempRowPositions.get(0), i).getTile()); //If there is a tile placed there, add it to the visited tiles list
                } else{
                    break;
                }
            }

            for (Tiles tile : tempTiles){ //Make sure that every placed tile is in the list of tiles adjacent to tile 0
                if (!visitedTiles.contains(tile)){
                    statusMessage = "Error: Word placement is not adjacent."; //If there is a tile that is not adjacent to tile 0, the word placement is not adjacent
                    return;
                }
            }
        }
        checkBoard = savedCheckBoard.copyBoard(); // Restore original board state
        statusMessage = "Word placed successfully."; //If all conditions passed, the word has been placed successfully
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
        ArrayList<Cell> visitedCells = new ArrayList<>(); //Keep track of visited cells that have already had they're scores added
        ArrayList<Cell> doubleScoreVisitedCells = new ArrayList<>(); //In the case of a DWS, all adjacent tiles will be visited twice
        ArrayList<Cell> tripleScoreVisitedCells = new ArrayList<>(); //In the case of a TWS, all adjacent tiles will be visited twice
        int wordPointMultiplier; //Used to identify if a tile is adjacent to newly placed tile on a DWS or TWS
        for (int i = 0; i < tempTiles.size(); i++) { //loop through each newly placed tile and get their score
            wordPointMultiplier = getWordPointMultiplier(tempRowPositions.get(i), tempColPositions.get(i));
            score += (tempTiles.get(i).getNumber() * getLetterPointMultiplier(tempRowPositions.get(i), tempColPositions.get(i))) * getWordPointMultiplier(tempRowPositions.get(i), tempColPositions.get(i)); //Add the combined score of all the new tiles placed to score
            visitedCells.add(gameBoard.getCell(tempRowPositions.get(i), tempColPositions.get(i)));
            if (wordPointMultiplier == 2){ //If the tile is placed on a DWS
                doubleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(i), tempColPositions.get(i)));
            }
            if (wordPointMultiplier == 3){ //If the tile is placed on a TWS
                tripleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(i), tempColPositions.get(i)));
            }
        }
        for (int j = 0; j < tempTiles.size(); j++){ //For each tile, we will see the words that they formed, and add to the score for the whole word
            wordPointMultiplier = getWordPointMultiplier(tempRowPositions.get(j), tempColPositions.get(j)); //Get the current cells word score multiplier
            for (int i = tempRowPositions.get(j) + 1; i < 15; i++){ //Starting from the placed tile, look for tiles to the right
                if (gameBoard.getCell(i, tempColPositions.get(j)).getTile() != null){ //Check that the cell is has a tile placed
                    if (!(visitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j))))){ //If the cell has not yet been visited
                        score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //Add the tile's score
                        visitedCells.add(gameBoard.getCell(i, tempColPositions.get(j))); //Mark this tile as visited so we don't add it's score twice in one move
                    }
                    if (!(doubleScoreVisitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j)))) && wordPointMultiplier == 2){ //If a tile in this word is placed on a DWS we add the score from it an extra time
                        score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //Add the tile's score for a second time
                        doubleScoreVisitedCells.add(gameBoard.getCell(i, tempColPositions.get(j)));
                    }
                    if (!(tripleScoreVisitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j)))) && wordPointMultiplier == 3){ //If a tile in this word is placed on a tWS we add the score from it an extra 2 times
                        score += 2 * gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber();
                        tripleScoreVisitedCells.add(gameBoard.getCell(i, tempColPositions.get(j))); //Add the tile's score for a third time
                    }
                }
                else{
                    break; //Break if we reach the end of the word
                }
            }
            for (int i = tempRowPositions.get(j) - 1; i >= 0; i--){ //Starting from the placed tile, look for tiles to the left
                if (gameBoard.getCell(i, tempColPositions.get(j)).getTile() != null){
                    if (!(visitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j))))){ //If the cell has not yet been visited
                        score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //Add the tile's score
                        visitedCells.add(gameBoard.getCell(i, tempColPositions.get(j))); //Mark this tile as visited so we don't add it's score twice in one move
                    }
                    if (!(doubleScoreVisitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j)))) && wordPointMultiplier == 2){ //If a tile in this word is placed on a DWS we add the score from it an extra time
                        score += gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //Add the tile's score
                        doubleScoreVisitedCells.add(gameBoard.getCell(i, tempColPositions.get(j))); //Add the tile's score for a second time
                    }
                    if (!(tripleScoreVisitedCells.contains(gameBoard.getCell(i, tempColPositions.get(j)))) && wordPointMultiplier == 3){ //If a tile in this word is placed on a tWS we add the score from it an extra 2 times
                        score += 2 * gameBoard.getCell(i, tempColPositions.get(j)).getTile().getNumber(); //Add the tile's score for a third time
                        tripleScoreVisitedCells.add(gameBoard.getCell(i, tempColPositions.get(j)));
                    }
                }
                else{
                    break; //Break if we reach the end of the word
                }
            }
            for (int i = tempColPositions.get(j) + 1; i < 15; i++){ //Starting from the placed tile, look for tiles down
                if (gameBoard.getCell(tempRowPositions.get(j), i).getTile() != null){
                    if (!(visitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i)))){ //If the cell has not yet been visited
                        score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score
                        visitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i)); //Mark this tile as visited so we don't add it's score twice in one move
                    }
                    if (!(doubleScoreVisitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i))) && wordPointMultiplier == 2){ //If a tile in this word is placed on a DWS we add the score from it an extra time
                        score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score for a second time
                        doubleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i));
                    }
                    if (!(tripleScoreVisitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i))) && wordPointMultiplier == 3){ //If a tile in this word is placed on a tWS we add the score from it an extra 2 times
                        score += 2 * gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score for a third time
                        tripleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i));
                    }
                }
                else{
                    break; //Break if we reach the end of the word
                }
            }
            for (int i = tempColPositions.get(j) - 1; i >= 0; i--){ //Starting from the placed tile, look for tiles up
                if (gameBoard.getCell(tempRowPositions.get(j), i).getTile() != null){
                    if (!(visitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i)))){ //If the cell has not yet been visited
                        score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score
                        visitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i)); //Mark this tile as visited so we don't add it's score twice in one move
                    }
                    if (!(doubleScoreVisitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i))) && wordPointMultiplier == 2){ //If a tile in this word is placed on a DWS we add the score from it an extra time
                        score += gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score for a second time
                        doubleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i));
                    }
                    if (!(tripleScoreVisitedCells.contains(gameBoard.getCell(tempRowPositions.get(j), i))) && wordPointMultiplier == 3){ //If a tile in this word is placed on a tWS we add the score from it an extra 2 times
                        score += 2 * gameBoard.getCell(tempRowPositions.get(j), i).getTile().getNumber(); //Add the tile's score for a third time
                        tripleScoreVisitedCells.add(gameBoard.getCell(tempRowPositions.get(j), i));
                    }
                }
                else{
                    break; //Break if we reach the end of the word
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
        int addedToList = 0;
        for (int col = 0; col < 15; col++) {
            addedToList = 0;
            if (checkBoard.getBoard()[row][col].isOccupied()) { //traverses through the row adding all cells with a tile to form a word
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); //adds them to the string builder
            } else if (word.length() > 1) { //if there is at least 2 tiles
                if (!isWordValidBothDirections(word)) { //checks the words, in both directions if not valid returns false
                    return false;
                }
                addPlacedWord(word); //adds it to the word list
                addedToList = 1;
                word.setLength(0); //resets the length of the string builder
            } else if (word.length() == 1) {
                word.setLength(0);
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) { //if there is a word at the end of the row and is not valid
            return false; //returns false
        }

        if(word.length() > 1 && addedToList == 0){
            addPlacedWord(word);
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
        int addedToList = 0;
        for (int row = 0; row < 15; row++) { //traverses through all columns on the board.
            addedToList = 0;
            if (checkBoard.getBoard()[row][col].isOccupied()) { //if there is a tile placed in the cell
                word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); //adds it to the string builder
            } else if (word.length() > 1) { //if there is at least two tiles on the board
                if (!isWordValidBothDirections(word)) { //if it's not a valid word return false
                    return false;
                }
                addPlacedWord(word); //adds it to the word list
                addedToList = 1;
                word.setLength(0); //reset the string builder list size
            }
            else if (word.length() == 1) {
                word.setLength(0);
            }
        }
        if (word.length() > 1 && !isWordValidBothDirections(word)) { //if there is a word at the end of a column and is not valid
            return false; //return false
        }

        if(word.length() > 1 && addedToList == 0){
            addPlacedWord(word);
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

        savePlacedWordsInStack(); //calls a function to save the placed words in the stack

        if (wordList.isValidWord(word.toString())){ //If the word is valid the way it was read, add it to the word list
            placedWords.add(word.toString());
        }
        else{
            placedWords.add(word.reverse().toString()); //If the word is not valid the way it was read, add it's reverse orientation to the word list
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

    /**
     * When a turn score is being calculated, we need to add double or triple to words placed on a DWS or TWS.
     * This method will identify those with an int for the turnScore method to use.
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the letter point multiplier of the cell
     */
    public int getWordPointMultiplier(int row, int col){
        if (gameBoard.getCell(row, col).getSpecialType() == null){ //If the cell is not a DWS or TWS
            return 1;
        }
        else if (gameBoard.getCell(row, col).getSpecialType().equals("DWS")){ //If the cell is a DWS
            return 2;
        }
        else if (gameBoard.getCell(row, col).getSpecialType().equals("TWS")){ //If the cell is a TWS
            return 3;
        }
        return 1;
    }

    /**
     * When a turn score is being calculated, we need to add double or triple to letter's placed on a DLS or TLS.
     * This method will identify those with an int for the turnScore method to use.
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the letter point multiplier of the cell
     */
    public int getLetterPointMultiplier(int row, int col){
        if (gameBoard.getCell(row, col).getSpecialType() == null){ //If the cell is not a DLS or TLS
            return 1;
        }
        else if (gameBoard.getCell(row, col).getSpecialType().equals("DLS")){ //If the cell is a DLS
            return 2;
        }
        else if (gameBoard.getCell(row, col).getSpecialType().equals("TLS")){ //If the cell is a TLS
            return 3;
        }
        return 1;
    }

    /**
     * Checks to see if the stack of board is empty.
     * @return true if empty false otherwise
     */
    public boolean stackOfBoardEmpty(){
        return boardsAtEachTurn.isEmpty();
    }

    /**
     * Checks to see if the stack of player tiles is empty.
     * @return true is empty false otherwise
     */
    public boolean stackOfPlayerTilesEmpty(){
        return previousPlayersTiles.isEmpty();
    }

    /**
     * Checks to see if the stack of the players scores is empty.
     * @return true if empty false otherwise
     */
    public boolean stackOfPlayerScoresEmpty(){
        return previousPlayerScores.isEmpty();
    }

    /**
     * Checks to see if the stack of tile bags is empty.
     * @return true if empty false otherwise.
     */
    public boolean stackOfTilesBagEmpty(){
        return previousTilesBags.isEmpty();
    }

    /**
     * Checks to see if the stack of placed words is empty.
     * @return true if empty false otherwise
     */
    public boolean stackOfPlacedWordsEmpty(){
        return previousPlacedWords.isEmpty();
    }

    /**
     * Checks to see if the stack of players is empty.
     * @return true if empty false otherwise
     */
    public boolean stackOfPlayerEmpty(){
        return previousPlayers.isEmpty();
    }
}
