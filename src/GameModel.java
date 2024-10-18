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


    public GameModel(){
        player = new Player("Rami");
        board = new Board(15, 15);
        tilesBag = new TilesBag();
        playGame();
    }

    public void playGame(){

        Random rand = new Random();
        for (int i = 0; i < 7; i++){
            int rnd = rand.nextInt(tilesBag.bagArraylist().size());
            player.getTiles().add(tilesBag.bagArraylist().get(rnd));
            tilesBag.bagArraylist().remove(rnd);
        }

        for (int i = 0; i < tilesBag.bagArraylist().size(); i++){
            System.out.println(tilesBag.bagArraylist().get(i).toString());
        }

        board.displayBoard();
        player.displayPlayer();

    }

    public boolean checkGameFinished(){
        return tilesBag.bagArraylist().isEmpty();
    }


    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
