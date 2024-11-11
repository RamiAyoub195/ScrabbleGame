import java.awt.event.*;
import java.util.*;

/**
 * This is the game controller which will communicate the view with the model of the game.
 * The controller will take care of any buttons pressed in the view and send the data to the model. It will
 * also return the logic from the model to the view updating the scores, words placed, etc.
 *
 * Author(s): Liam bennet, Rami Ayoub
 * Version: 2.0
 * Date: Wednesday, November 6, 2024
 */

public class GameController implements ActionListener {
    GameModel model;
    GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        for(String playerName : view.getPlayerNames()){
            model.addPlayer(playerName); //creates players in the game model after getting the names from the view
        }

        view.setUpPlayerTilesPanel(model.getPlayers().get(0)); // Set up the first player's tiles

        this.view.setAllButtonsActionListener(this); //sets the controller as the action listener for all buttons in the view

    }

    public void actionPerformed(ActionEvent e) {

    }



    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
    }
}
