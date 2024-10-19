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
    private boolean firstWordPlaced;
    private Random rand;

    /**
     * Initializes the list of players, the boards, the user input scanner, the bag of tiles,
     * the list of words and starts the game.
     */
    public GameModel(){
        userInput = new Scanner(System.in);
        players = new ArrayList<>();
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        firstWordPlaced = false;
        rand = new Random();
        playGame();
    }

    /**
     * Starts the game of Scrabble by asking for the players names, then iterates through
     * each player until the game has finished.
     */
    public void playGame(){

        System.out.println("Welcome to the game of Scrabble!");

        for(int i = 1; i <= 2; i++){
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

    /**
     * This function displays the user of their choices when it's their turn.
     * The player may choose to play tile(s), swap tile(s) or skip their turn.
     * @param player the player's current turn
     */
    public void playerTurn(Player player){
        System.out.println("Here are your options: Place Tile(s) (0), Swap Tiles (1), Skip Turn (2)");
        Scanner userInput = new Scanner(System.in);

        int choice = userInput.nextInt(); //gets the user choice

        while (choice < 0 || choice > 2){ //choice must be a valid choice from the options
            System.out.println("Please enter a number between 0 and 2:");
            choice = userInput.nextInt();
        }

        if (choice == 0){ //player plays a tile
            playerPlaceTile(player);
        }

        else if (choice == 1){ //player swaps a tile
            if(tilesBag.bagOfTileIsEmpty()){ //makes sure that the bag of tiles is not empty for swapping
                System.out.println("The bag of tiles is empty! cannot swap tile(s)");
            }
            else{
                playerSwapTile(player);
            }
        }

        else{ //player passes their turn
            System.out.println("Player turn passed.");
        }
    }

    /**
     * Takes care of the players decision to place a tile. The tile being placed
     * must be entered horizontally/vertically and the word being inserted or words after
     * tile is inserted must be a valid word.
     *
     * @param player the player's current turn
     */
    public void playerPlaceTile(Player player){
        ArrayList<Tiles> tempTiles = new ArrayList<>(); //temp tiles to place on a temp board
        ArrayList<Integer> tempRowPositions = new ArrayList<>(); //temp row position of temp tile
        ArrayList<Integer> tempColPositions = new ArrayList<>(); //temp column position of temp tile

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt(); //player tile(s) they would like to place

        while(choiceNumTiles > player.getTiles().size()) { //make sure the user selects a valid amount of tiles to play
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to place: ");
            choiceNumTiles = userInput.nextInt();
        }

        System.out.println("You will now be entering the word to place tile by tile make sure to enter it in the right order!");

        for (int i = 1; i <= choiceNumTiles; i++){ //stores each tile and their position to be checked on a temp board for placement and real word
            System.out.println("Please enter tile " + i + " (index 0-6):");
            int tileIndex = userInput.nextInt();
            tempTiles.add(player.getTiles().get(tileIndex));
            System.out.println("Please enter the row position for tile " + i + " :");
            int rowIndex = userInput.nextInt();
            tempRowPositions.add(rowIndex);
            System.out.println("Please enter the column position for tile " + i + " :");
            int columnIndex = userInput.nextInt();
            tempColPositions.add(columnIndex);
        }

        placeWord(tempTiles, tempRowPositions, tempColPositions);

    }

    /**
     * Places a word on the board, the first word needs to go through the middle square.
     * Then all words must also be connected horizontally or vertically and must be a valid word
     * from the word list.
     *
     * @param tempTiles a list of the tiles
     * @param tempRowPositions a list of row positions for the tiles
     * @param tempColPositions a list of column positions for the tiles
     */
    public void placeWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions){
    }


    /**
     * Checks to see if the added word is a valid word from the list of words in the
     * txt file. Checks each row and column of the board to make sure that all added words and
     * subwords match the word list.
     *
     */
    public void checkValidWord(){

    }

    /**
     * Takes care of when a player decides to swap tile(s). The tile(s) are swapped
     * with the tiles in the tile bag. A player may swap tiles a
     * @param player
     */
    public void playerSwapTile(Player player){
        System.out.println("How many tile(s) would you like to swap: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > player.getTiles().size()) { //make sure the user selects a valid amount of tiles to play
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to swap: ");
            choiceNumTiles = userInput.nextInt();
        }

        for (int i = 1; i <= choiceNumTiles; i++){
            System.out.println("Select tile " + i + " to swap (Enter the index (0-6)): ");
            int tileIndex = userInput.nextInt();

            Tiles t = player.getTiles().get(tileIndex);
            player.getTiles().remove(tileIndex);

            replaceSwappedTile(player, tileIndex);
            tilesBag.bagArraylist().add(t);
        }
        System.out.println("Tile(s) successfully swapped!");
        player.displayTiles();
    }

    /**
     * Checks to see when the game is finished if the tiles in the bag have finished.
     * @return a boolean true of bag is empty and false if not empty
     */
    public boolean checkGameFinished(){
        boolean status = true;
        for(Player p : players){
            if(!p.getTiles().isEmpty()){
                status = false;
            }
        }
        return status;
    }

    /**
     * Gets a random number of tiles from the bag of tiles to a player at the beginning
     * of the game and assigns it to the players. It is also used when a player places tile(s)
     * and gets new random tiles to replace the placed tiles.
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTiles(int numOfTiles, Player player){
        for (int i = 0; i < numOfTiles; i++){
            int rnd = rand.nextInt(tilesBag.bagArraylist().size());
            player.getTiles().add(tilesBag.bagArraylist().get(rnd));
            tilesBag.bagArraylist().remove(rnd);
        }
    }

    /**
     * Replaces a players tile(s) with tile(s) from the bag placing them
     * at the right index.
     * @param player the player who is swapping
     * @param index the index of the tile being replaced
     */
    public void replaceSwappedTile(Player player, int index){
        int rnd = rand.nextInt(tilesBag.bagArraylist().size());
        player.getTiles().add(index, tilesBag.bagArraylist().get(rnd));
        tilesBag.bagArraylist().remove(rnd);
    }

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
