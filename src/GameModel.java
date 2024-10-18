import java.util.*;

/**
 * This is the model class that brings all of the logic together for a game, here also the user will input their commands
 * and get an update of the board, their score etc.
 *
 */

public class GameModel {

    private ArrayList<Player> players;
    private Board board;
    private TilesBag tilesBag;


    public GameModel(){
        players = new ArrayList<>();
        board = new Board(15, 15);
        tilesBag = new TilesBag();
        setUpGame();
        playGame();
    }

    public void setUpGame(){
        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome to the game of Scrabble!\n");
        System.out.println("Please enter the number of players (2 - 4):");

        int numOfPlayers = userInput.nextInt();

        for(int i = 1; i <= numOfPlayers; i++){
            System.out.println("Enter player " + i + " name:");
            String playerName = userInput.next();
            players.add(new Player(playerName));
        }

        Random rand = new Random();

        for (Player p : players){
            for (int i = 0; i < 7; i++){
                int rnd = rand.nextInt(tilesBag.bagArraylist().size());
                p.getTiles().add(tilesBag.bagArraylist().get(rnd));
                tilesBag.bagArraylist().remove(rnd);
            }
        }
        board.displayBoard();
    }

    public void playGame(){
        if(!checkGamefinished()){

        }

    }

    public boolean checkGamefinished(){
        return tilesBag.bagArraylist().size() == 0;
    }

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
