import java.util.*;

/**
 * This is the model class that brings all the logic together for a game, here also the user will input their commands
 * and get an update of the board, their score etc.
 *
 */

public class GameModel {

    private Player player;
    private Board board;
    private TilesBag tilesBag;
    private WordList wordList;


    public GameModel(){
        player = new Player("Rami");
        board = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        playGame();
    }

    public void playGame(){

        getRandomTile(7, player);
        board.displayBoard();
        player.displayPlayer();

        while (!checkGameFinished()){
            playerTurn(player);
        }
    }

    public void playerTurn(Player player){
        System.out.println("Here are your options: Place Tile(s) (0), Swap Tiles (1), Skip Turn (2)");
        Scanner userInput = new Scanner(System.in);

        int choice = userInput.nextInt();

        if (choice == 0){
            System.out.println("Which tile would you like to place? (Enter the index (0-6)): ");
            int choiceTile = userInput.nextInt();
            Tiles t = player.getTiles().get(choiceTile);
            player.getTiles().remove(choiceTile);
            getRandomTile( 1, player);


            System.out.println("Which row would you like to place the tile: ");
            int row = userInput.nextInt();
            System.out.println("Which column would you like to place the tile: ");
            int column = userInput.nextInt();
            board.placeBoardTile(row, column, t.getLetter());
            board.displayBoard();
        }
        else if (choice == 1){
            System.out.println("Which tile would you like to swap? (Enter the index (0-6)): ");
            int choiceTile = userInput.nextInt();
            Tiles t = player.getTiles().get(choiceTile);
            player.getTiles().remove(choiceTile);
            getRandomTile( 1, player);
            tilesBag.bagArraylist().add(t);
        }
        else if (choice == 2){
            //do nothing
        }
    }

    public boolean checkGameFinished(){
        return tilesBag.bagArraylist().isEmpty();
    }

    /**
     * Gets a random tile from the bag of tiles to a player
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTile(int numOfTiles, Player player){
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
