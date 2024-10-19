import java.util.*;

/**
 * This is the model class that brings all the logic together for a game, here also the user will input their commands
 * and get an update of the board, their score etc.
 *
 */

public class GameModel {

    private ArrayList<Player> player;
    private Board board;
    private TilesBag tilesBag;
    private WordList wordList;
    private int numOfPlayers;


    public GameModel(){
        player = new ArrayList<>();
        board = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        numOfPlayers = 0;
        playGame();
    }

    public void playGame(){

        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome to the game of Scrabble! Please enter the number of players (2-4):");
        int numOfPlayers = userInput.nextInt();

        while(numOfPlayers < 2 || numOfPlayers > 4){
            System.out.println("Please enter a number between 2 and 4:");
            numOfPlayers = userInput.nextInt();
        }

        for(int i = 1; i <= numOfPlayers; i++){
            System.out.println("Please enter player " + i + " name:");
            String playerName = userInput.next();
            player.add(new Player(playerName));
            getRandomTiles(7, player.get(i - 1));
        }

        while (!checkGameFinished()){
            for(Player player : player){
                board.displayBoard();
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
        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > 0){
            System.out.println("Select a tile to place (Enter the index (0-6)): ");
            int tileIndex = userInput.nextInt();

            Tiles t = player.getTiles().get(tileIndex);
            player.getTiles().remove(tileIndex);
            getRandomTiles( 1, player);

            System.out.println("Which row would you like to place the tile: ");
            int row = userInput.nextInt();
            System.out.println("Which column would you like to place the tile: ");
            int column = userInput.nextInt();

            if(board.checkBoardTileEmpty(row, column)){
                if(board.checkLegalPlacement(row, column)){
                    board.placeBoardTile(row, column, t.getLetter());
                    board.displayBoard();
                    player.displayPlayer();
                    choiceNumTiles--;
                }
                else {
                    System.out.println("Tile must be connected horizontally or vertically");
                }
            }
            else{
                System.out.println("That tile is already placed");
            }
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
            board.displayBoard();
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
            int rnd = rand.nextInt(tilesBag.bagArraylist().size());
            player.getTiles().add(tilesBag.bagArraylist().get(rnd));
            tilesBag.bagArraylist().remove(rnd);
        }
    }

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
