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

    public void playerPlaceTile(Player player){
        ArrayList<Tiles> tempTiles = new ArrayList<>();
        ArrayList<Integer> tempRowPositions = new ArrayList<>();
        ArrayList<Integer> tempColPositions = new ArrayList<>();

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > player.getTiles().size()) {
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to place: ");
            choiceNumTiles = userInput.nextInt();
        }

        System.out.println("You will now be entering the word to place tile by tile make sure to enter it in the right order!");

        System.out.println("Which direction is the word being placed (Horizontal/Vertical):");
        String direction = userInput.next();

        for (int i = 1; i <= choiceNumTiles; i++){
            System.out.println("Please enter tile " + i + " (index 0-6):");
            int tileIndex = userInput.nextInt();
            tempTiles.add(player.getTiles().get(tileIndex));
            System.out.println("Please enter the row position for tile " + i + " :");
            int rowIndex = userInput.nextInt();
            tempRowPositions.add(rowIndex);
            System.out.println("Please enter the column position for tile " + i + " :");
            int columnIndex = userInput.nextInt();
            tempColPositions.add(columnIndex);
            //checkPlacement(tempTiles, tempRowPositions, tempColPositions, direction);
        }

        // Check if the placement is valid
        if (checkPlacement(tempTiles, tempRowPositions, tempColPositions, direction)) {
            // Validate all words on the board after placing tiles
            if (!validateAllWords(checkBoard)) {
                // If any word is invalid, revert the changes and notify the player
                System.out.println("Invalid word placement. Please try again.");
            } else {
                gameBoard.copyBoard(checkBoard);
                // Remove the placed tiles from the player's hand
                for (Tiles tile : tempTiles) {
                    player.getTiles().remove(tile);
                }
                refillPlayerHand(player); // Refill the player's hand
            }
        }
        refillPlayerHand(player);
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

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}