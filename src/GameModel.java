import java.util.*;

/**
 * This is the model class that brings all the logic together for a game, here also the user will input their commands
 * and get an update of the board, their score etc.
 *
 */

public class GameModel {
    private Scanner userInput;
    private ArrayList<Player> players;
    private Board gameBoard;
    private Board checkBoard;
    private TilesBag tilesBag;
    private WordList wordList;
    private int numOfPlayers;
    private boolean firstTurn;



    public GameModel(){
        userInput = new Scanner(System.in);
        players = new ArrayList<>();
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        numOfPlayers = 0;
        firstTurn = true;
        playGame();
    }

    public void playGame(){

        System.out.println("Welcome to the game of Scrabble! Please enter the number of players (2-4):");
        int numOfPlayers = userInput.nextInt();

        while(numOfPlayers < 2 || numOfPlayers > 4){
            System.out.println("Please enter a number between 2 and 4:");
            numOfPlayers = userInput.nextInt();
        }

        for(int i = 1; i <= numOfPlayers; i++){
            System.out.println("Please enter player " + i + " name:");
            String playerName = userInput.next();
            players.add(new Player(playerName));
            getRandomTiles(7, players.get(i - 1));
        }

        while (!checkGameFinished()){
            for(Player player : players){
                gameBoard.displayBoard();
                player.displayPlayer();
                playerTurn(player);
            }
        }
    }

    public void playerTurn(Player player){
        System.out.println("Here are your options: Place Tile(s) (0), Swap Tiles (1), Skip Turn (2)");
        Scanner userInput = new Scanner(System.in);

        int choice = userInput.nextInt();

        while (choice < 0 || choice > 2){
            System.out.println("Please enter a number between 0 and 2:");
            choice = userInput.nextInt();
        }

        if (choice == 0){
            playerPlaceTile(player);
        }

        else if (choice == 1){
            playerSwapTile(player);
        }
    }

    public void playerPlaceTile(Player player) {
        ArrayList<Tiles> tempTiles = new ArrayList<>();
        ArrayList<Integer> tempRowPositions = new ArrayList<>();
        ArrayList<Integer> tempColPositions = new ArrayList<>();
        boolean actionCancelled = false;

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt();

        while (choiceNumTiles > player.getTiles().size()) {
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to place: ");
            choiceNumTiles = userInput.nextInt();
        }

        System.out.println("You will now be entering the word to place tile by tile, make sure to enter it in the right order!");
        String direction = "does_nothing_right_now_just_need_placeholder"; // Placeholder for direction input

        for (int i = 1; i <= choiceNumTiles; i++) {
            System.out.println("Available tiles: ");
            for (int j = 0; j < player.getTiles().size(); j++) {
                System.out.println((j + 1) + ": " + player.getTiles().get(j).getLetter()); // Display tile with index
            }

            System.out.println("Please enter the letter you want to place (or enter 0 to cancel):");
            String letterInput = userInput.next();

            if (letterInput.equals("0")) {
                System.out.println("Action cancelled.");
                actionCancelled = true;
                return; // Exit if player chooses to cancel
            }

            // Find the tile with the specified letter
            boolean tileFound = false;
            for (int j = 0; j < player.getTiles().size(); j++) {
                if (player.getTiles().get(j).getLetter().equalsIgnoreCase(letterInput)) {
                    tempTiles.add(player.getTiles().get(j)); // Add the tile to temporary list

                    // Get row and column positions
                    System.out.println("Please enter the row position for tile " + letterInput + ":");
                    int rowIndex = userInput.nextInt();
                    tempRowPositions.add(rowIndex);

                    System.out.println("Please enter the column position for tile " + letterInput + ":");
                    int columnIndex = userInput.nextInt();
                    tempColPositions.add(columnIndex);

                    player.getTiles().remove(j); // Remove the tile from player's hand

                    tileFound = true;

                    break; // Break once the tile is found and added
                }
            }

            if (!tileFound) {
                System.out.println("Tile not found. Please try again.");
                i--; // Repeat the iteration for this tile
            }
        }
        if (actionCancelled) {
            // Action was cancelled, so we need to return all tiles from tempTiles back to the player's hand
            player.getTiles().addAll(tempTiles);
        } else {
            // Check if the placement is valid
            if (checkPlacement(tempTiles, tempRowPositions, tempColPositions, direction)) {

                // Validate all words on the board after placing tiles
                if (!validateAllWords(checkBoard)) {
                    // If any word is invalid, revert the changes and notify the player
                    System.out.println("Invalid word placement. Please try again.");
                    player.getTiles().addAll(tempTiles); // Add back tiles if invalid
                } else {
                    // Placement and word validation are valid
                    gameBoard.copyBoard(checkBoard); // Commit the valid board changes

                    // Remove the placed tiles from the player's hand
                    for (Tiles tile : tempTiles) {
                        player.getTiles().remove(tile);
                    }

                    // Refill the player's hand after valid placement
                    refillPlayerHand(player);

                    // Copy current gameBoard to checkBoard for next placement checks
                    checkBoard.copyBoard(gameBoard);

                    // Calculate the score
                    int score = calculateScore(gameBoard, tempRowPositions, tempColPositions);

                    // Add the score to the player's total score
                    player.addScore(score);

                    // Output score
                    System.out.println("Score for this turn: " + score);
                    System.out.println("Total score for " + player.getName() + ": " + player.getScore());
                }
            } else {
                System.out.println("Invalid tile placement. Please try again.");
            }
        }
    }


    public void refillPlayerHand(Player player) {
        int numTilesToDraw = 7 - player.getTiles().size();

        if (numTilesToDraw > 0 && !tilesBag.bagArraylist().isEmpty()) {
            getRandomTiles(numTilesToDraw, player);
            System.out.println(player.getName() + " has drawn " + numTilesToDraw + " new tile(s).");
        } else if (tilesBag.bagArraylist().isEmpty()) {
            System.out.println("No more tiles available to draw from the bag.");
        }
    }


    public void playerSwapTile(Player player){
        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to swap: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > 0){
            System.out.println("Select a tile to swap (Enter the index (0-6)): ");
            int tileIndex = userInput.nextInt();

            Tiles t = player.getTiles().get(tileIndex);
            player.getTiles().remove(tileIndex);

            getRandomTiles( 1, player);
            tilesBag.bagArraylist().add(t);
            gameBoard.displayBoard();
            player.displayPlayer();
            choiceNumTiles--;
        }
    }

    /**
     * Checks to see when the game is finished if the tiles in the bag have finished.
     * @return a boolean true of bag is empty and false if not empty
     */
    public boolean checkGameFinished(){
        return tilesBag.bagArraylist().isEmpty();
    }

    /**
     * Gets a random tile from the bag of tiles to a player
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTiles(int numOfTiles, Player player){
        Random rand = new Random();
        for (int i = 0; i < numOfTiles; i++){
            if (tilesBag.bagArraylist().isEmpty()) {
                break;  // Stop if the bag is empty
            }
            int rnd = rand.nextInt(tilesBag.bagArraylist().size());
            player.getTiles().add(tilesBag.bagArraylist().get(rnd));
            tilesBag.bagArraylist().remove(rnd);
        }
    }

    public boolean checkPlacement(ArrayList<Tiles> tiles, ArrayList<Integer> rows, ArrayList<Integer> cols, String direction){
        // If it's the first turn, ensure one of the tiles is placed on the center square (7, 7)
        if (firstTurn) {
            boolean coversCenter = false;
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i) == 7 && cols.get(i) == 7) {
                    coversCenter = true;
                    break;
                }
            }
            if (!coversCenter) {
                System.out.println("Error: On the first turn, the word must cover the center square (7, 7).");
                return false;
            }
            firstTurn = false; // Mark that the first turn has been completed
        }

        // Existing logic to check placement
        if (gameBoard.checkMiddleBoardEmpty()) {
            checkBoard.placeBoardTile(rows.get(0), cols.get(0), tiles.get(0).getLetter());
            for (int i = 1; i < tiles.size(); i++) {
                if (checkBoard.checkIllegalPlacement(rows.get(i), cols.get(i))) {
                    System.out.println("Words must be connected horizontally or vertically! and the first word must cover over the center of the board");
                    return false;
                }
                checkBoard.placeBoardTile(rows.get(i), cols.get(i), tiles.get(i).getLetter());
                checkWord(checkBoard, rows.get(i), cols.get(i), direction, false);
            }
            checkWord(checkBoard, rows.get(rows.size() - 1), cols.get(cols.size() - 1), direction, true);
        }
        return true;
    }

    public boolean checkWord(Board board, int row, int col, String direction, boolean lastLetter){
        String wordLeftToRight = "";
        String wordRightToLeft = "";
        StringBuilder wordTopToBottom = new StringBuilder();
        StringBuilder wordBottomToTop = new StringBuilder();

        if (direction.equals("Horizontal") && !lastLetter){
            int i = 1;
            while(!board.getBoard()[row + i][col].equals("  -  ") && row + i < 15){
                wordTopToBottom.append(board.getBoard()[row + i][col]);
                i++;
            }
            int j = 1;
            while(!board.getBoard()[row - j][col].equals("  -  ") && row - j >= 0){
                wordBottomToTop.append(board.getBoard()[row - j][col]);
                j++;
            }
            wordTopToBottom.reverse().append(wordBottomToTop);

        }
        else if (direction.equals("Vertical") && !lastLetter){

        }
        return true;
    }


    // Method to validate all collected words
    public boolean validateAllWords(Board board) {
        List<String> words = new ArrayList<>();

        // Check horizontally
        for (int row = 0; row < 15; row++) {
            StringBuilder word = new StringBuilder();
            for (int col = 0; col < 15; col++) {
                String letter = board.getBoard()[row][col];
                if (!letter.equals("  -  ")) {
                    word.append(letter.trim());
                } else {
                    if (word.length() > 1) {
                        words.add(word.toString());
                    }
                    word.setLength(0); // Reset for the next word
                }
            }
            if (word.length() > 1) {
                words.add(word.toString());
            }
        }

        // Check vertically
        for (int col = 0; col < 15; col++) {
            StringBuilder word = new StringBuilder();
            for (int row = 0; row < 15; row++) {
                String letter = board.getBoard()[row][col];
                if (!letter.equals("  -  ")) {
                    word.append(letter.trim());
                } else {
                    if (word.length() > 1) {
                        words.add(word.toString());
                    }
                    word.setLength(0);
                }
            }
            if (word.length() > 1) {
                words.add(word.toString());
            }
        }

        for (String word : words) {
            if (!wordList.isValidWord(word.trim())) { // Convert to lowercase
                System.out.println("Invalid word found: " + word); // Print the invalid word for debugging
                return false; // Invalid word found
            }
        }

        return true;
    }


    // Method to score the new words formed during the turn
    public int calculateScore(Board board, ArrayList<Integer> rowPositions, ArrayList<Integer> colPositions) {
        int totalScore = 0;

        for (int i = 0; i < rowPositions.size(); i++) {
            // Check for horizontal word (left to right)
            int horizontalScore = calculateWordScore(board, rowPositions.get(i), colPositions.get(i), "Horizontal");
            if (horizontalScore > 0) {
                totalScore += horizontalScore; // Only add if score is valid (greater than 0)
            }

            // Check for vertical word (top to bottom)
            int verticalScore = calculateWordScore(board, rowPositions.get(i), colPositions.get(i), "Vertical");
            if (verticalScore > 0) {
                totalScore += verticalScore; // Only add if score is valid (greater than 0)
            }
        }

        return totalScore;
    }

    // Helper function to calculate score of a word in a specific direction
    public int calculateWordScore(Board board, int row, int col, String direction) {
        StringBuilder word = new StringBuilder();
        Set<String> countedLetters = new HashSet<>(); // Set to track counted letters
        int score = 0;

        if (direction.equals("Horizontal")) {
            // Find the start of the word (move left until we find a blank space or edge)
            int startCol = col;
            while (startCol > 0 && !board.getBoard()[row][startCol - 1].equals("  -  ")) {
                startCol--;
            }

            // Build the word from left to right
            for (int c = startCol; c < 15 && !board.getBoard()[row][c].equals("  -  "); c++) {
                String letter = board.getBoard()[row][c].trim();
                if (!countedLetters.contains(letter)) {
                    score += getTileValue(letter); // Add score for this letter
                    countedLetters.add(letter); // Mark this letter as counted
                }
                word.append(letter); // Append letter to the word
            }

        } else if (direction.equals("Vertical")) {
            // Find the start of the word (move up until we find a blank space or edge)
            int startRow = row;
            while (startRow > 0 && !board.getBoard()[startRow - 1][col].equals("  -  ")) {
                startRow--;
            }

            // Build the word from top to bottom
            for (int r = startRow; r < 15 && !board.getBoard()[r][col].equals("  -  "); r++) {
                String letter = board.getBoard()[r][col].trim();
                if (!countedLetters.contains(letter)) {
                    score += getTileValue(letter); // Add score for this letter
                    countedLetters.add(letter); // Mark this letter as counted
                }
                word.append(letter); // Append letter to the word
            }
        }

        // Only return score if the word has more than one letter
        if (word.length() > 1) {
            return score; // Return the total score calculated
        }

        return 0; // Return 0 if the word length is 1 or less
    }




    // Helper function to retrieve the value of a tile based on the letter
    public int getTileValue(String letter) {
        for (Tiles tile : tilesBag.bagArraylist()) {
            if (tile.getLetter().equalsIgnoreCase(letter)) {
                return tile.getNumber(); // Return the value of the tile
            }
        }
        return 0; // Default return if no tile found (shouldn't happen in normal gameplay)
    }



    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}