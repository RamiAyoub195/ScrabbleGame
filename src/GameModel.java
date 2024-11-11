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
        else
        {
            playerPlaceTile(player, tiles, rowPositions, colPositions); //if the word was not placed, player plays again
        }
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
    public boolean placeWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions, Player player)
    {
        Board savedCheckBoard = checkBoard.copyBoard(); // saves the board while checking the conditions
        for (int i = 0; i < tempTiles.size(); i++)
        { // checks to make sure that the board space is not already occupied
            if (checkBoard.checkBoardTileEmpty(tempRowPositions.get(i), tempColPositions.get(i))) {
                checkBoard.placeBoardTile(tempRowPositions.get(i), tempColPositions.get(i), tempTiles.get(i));
            } else {
                checkBoard = savedCheckBoard.copyBoard(); // restores the original board
                return false;
            }
        }
        if (!checkBoard.checkMiddleBoardEmpty()) { // the middle board was covered
            for (int i = 0; i < tempTiles.size(); i++) {
                if (!checkBoard.checkAdjacentBoardConnected(tempRowPositions.get(i), tempColPositions.get(i))) { // checks for tile adjacency
                    checkBoard = savedCheckBoard.copyBoard(); // restores the original board
                    return false;
                }
            }
        } else { // the middle of the board was not covered
            checkBoard = savedCheckBoard.copyBoard(); // restores the original board
            return false;
        }

        if (!checkValidWord()) { // checks to make sure that a word is valid
            checkBoard = savedCheckBoard.copyBoard(); // restores the original board
            return false;
        }

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
        for (int row = 0; row < 15; row++) { // goes through each row
            StringBuilder word = new StringBuilder(); // creates a string for each row
            for (int col = 0; col < 15; col++) {
                if (checkBoard.getBoard()[row][col].isOccupied()) {
                    word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); // appends and trims a letter in a row
                } else if (word.length() > 1) { // if the word is at least two letters
                    if (!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())) { // checks for both directions of the words
                        return false;
                    }
                    word.setLength(0); // resets the string size to 0
                }
            }

            if (word.length() > 1) { // in case a word ended at the end of a row checks for the word
                if (!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())) { // checks for both directions of the word
                    return false;
                }
            }
        }
        for (int col = 0; col < 15; col++) { // goes through each column
            StringBuilder word = new StringBuilder(); // creates a string for each column
            for (int row = 0; row < 15; row++) {
                if (checkBoard.getBoard()[row][col].isOccupied()) {
                    word.append(checkBoard.getBoard()[row][col].getTile().getLetter().trim()); // adds and trims each letter in the column
                } else if (word.length() > 1) { // must be at least a two-letter word
                    if (!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())) { // checks both sides of the word
                        return false;
                    }
                    word.setLength(0); // resets the string size back
                }
            }
            if (word.length() > 1) { // in case a word ended at the end of a column checks for the word
                if (!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())) {
                    return false;
                }
            }
        }
        return true;
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

    public Board getCheckBoard() {
        return checkBoard;
    }

    public TilesBag getTilesBag() {
        return tilesBag;
    }

    public WordList getWordList() {
        return wordList;
    }

    public Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null; // Return null if no player with the given name is found
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameModel gameModel = new GameModel();

        // Add players through user input
        for (int i = 1; i <= 2; i++) { // Assuming a 2-player game for simplicity
            System.out.print("Enter name for Player " + i + ": ");
            String playerName = scanner.nextLine();
            gameModel.addPlayer(playerName);
        }

        Board board = gameModel.getGameBoard();
        boolean gameOver = false;
        int currentPlayerIndex = 0;

        while (!gameOver) {
            Player currentPlayer = gameModel.getPlayers().get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn:");
            board.displayBoard();
            currentPlayer.displayPlayer();

            System.out.println("Choose an action: 1) Place Tiles 2) Swap Tiles 3) Pass");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Simulate placing tiles (ask for user input for simplicity)
                System.out.print("Enter number of tiles to place: ");
                int numTiles = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                ArrayList<Tiles> tiles = new ArrayList<>(); // Mock tiles, you may need to add logic to select actual tiles
                ArrayList<Integer> rowPositions = new ArrayList<>();
                ArrayList<Integer> colPositions = new ArrayList<>();

                for (int i = 0; i < numTiles; i++) {
                    System.out.print("Enter tile index (0-6): ");
                    int tileIndex = scanner.nextInt();
                    tiles.add(currentPlayer.getTiles().get(tileIndex));

                    System.out.print("Enter row position (0-14): ");
                    int row = scanner.nextInt();
                    rowPositions.add(row);

                    System.out.print("Enter column position (0-14): ");
                    int col = scanner.nextInt();
                    colPositions.add(col);
                }

                gameModel.playerPlaceTile(currentPlayer, tiles, rowPositions, colPositions);
            } else if (choice == 2) {
                // Swap tiles logic
                System.out.print("Enter number of tiles to swap: ");
                int numTiles = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                List<Integer> tileIndices = new ArrayList<>();
                for (int i = 0; i < numTiles; i++) {
                    System.out.print("Enter tile index (0-6) to swap: ");
                    tileIndices.add(scanner.nextInt());
                }
                gameModel.playerSwapTile(currentPlayer, tileIndices);
            }
            else if (choice == 3) {
                System.out.println(currentPlayer.getName() + " passes their turn.");
            }

            // Check if the game is finished
            gameOver = gameModel.isGameFinished();

            // Move to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % gameModel.getPlayers().size();

            // Get updated gameboard
            board = gameModel.getGameBoard();
        }

        // Display game results
        System.out.println("The game has ended. Final results:");
        for (Player player : gameModel.getPlayers()) {
            System.out.println(player.getName() + " scored " + player.getScore());
        }

        Player winner = gameModel.getPlayers().stream().max(Comparator.comparingInt(Player::getScore)).orElse(null);
        if (winner != null) {
            System.out.println("The winner is " + winner.getName() + " with a score of " + winner.getScore());
        }
    }
}

