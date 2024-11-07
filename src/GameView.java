import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.image.TileObserver;
import java.util.ArrayList;

public class GameView extends JFrame {

    private ArrayList<String> playerNames; //will get the name of the players at the beginning of the game
    private JTextArea wordArea; //represents the text area where a valid word was placed on the board is displayed
    private JButton[][] boardFields; //represents the open slots where the tiles can be placed by selecting a tile an open spot
    private JPanel playerPanel; //this panel will have the players names and their scores
    private JPanel boardPanel; //represents the 15 x 15 board that the tiles will be placed on.
    private JPanel rightSidePanel; //this will have the words placed, the scores of the player and remaining tiles in the bag.
    private JPanel bottomPanel; //this will have the options for the player to play the game and their tiles.
    private JButton playButton; //this will be the button the user presses to play their turn
    private JButton quitButton; //this will be the button the user presses to skip their turn
    private JButton helpButton; //this will be the button the user pressed to swap tiles with the tile bag

    public GameView() {
        super("Scrabble"); //creates a frame called Scrabble
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the system exit when close is clicked
        this.setLayout(new BorderLayout()); //makes a border layout for the frame

        boardPanel = new JPanel(new GridLayout(15, 15)); //makes the board panel which will have the 15 slots

        boardFields = new JButton[15][15]; //creates the 15 slots that will be on the board game

        playerNames = new ArrayList<>(); //initializes the arrayList of names

        initializeBoard(); //initialized the board panel

        welcomeAndGetPlayerNames(); //gets the names of the players who are playing the game and a welcome

        rightSidePanel = new JPanel(); //will display the words placed, the players score and number of tiles in the tile bag
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS)); //sets the layout of the right side as a box layout

        JLabel wordLabel = new JLabel("Words Placed: "); //The header of the words placed
        rightSidePanel.add(wordLabel); //adds it to the right side

        wordArea = new JTextArea(5, 5); //Will display the words that are added to the board
        wordArea.setEditable(false); // cannot be entered as input only for output
        JScrollPane scrollPane = new JScrollPane(wordArea); // Adds scrolling to the text area
        rightSidePanel.add(scrollPane); //adds it to the right side

        setUpPlayerPanel();

        JLabel tileLeftInBag = new JLabel("Tiles Left In Bag: "); //the remaining number of tiles in the bag
        rightSidePanel.add(tileLeftInBag); //adds the tiles label to the right side panel

        bottomPanel = new JPanel(); //will display the players tiles and their options of playing
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        playButton = new JButton("Play"); //will create a play button
        bottomPanel.add(playButton);

        quitButton = new JButton("Pass"); //creates the pass button
        bottomPanel.add(quitButton);

        helpButton = new JButton("Swap"); //creates the swap button
        bottomPanel.add(helpButton);

        this.add(boardPanel, BorderLayout.CENTER);
        this.add(rightSidePanel, BorderLayout.EAST); // Adds the right-side panel to the main frame
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.setSize(500, 400);
        this.setVisible(true);

    }

    /**
     * Set up the player panel which will have all player names and their scores
     */
    public void setUpPlayerPanel(){
        for(int i = 0; i < playerNames.size(); i++){
            playerPanel = new JPanel(); //this panel will have all the players and their scores
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS)); //will add their name and score on an x-axis
            JLabel playerScoreLabel = new JLabel(playerNames.get(i) + "'s" + " Score:"); //the label for the player i score
            JTextField playerScoreText = new JTextField("0"); //the actual display for the score
            playerScoreText.setEditable(false); //disables the text field so that it cant be editable just see the players score
            playerScoreText.setBackground(Color.WHITE); //makes the background of the text field white
            playerScoreText.setMaximumSize(new Dimension(30, 30)); // Set a max size to keep it small
            playerPanel.add(playerScoreLabel); //adds the label
            playerPanel.add(playerScoreText); //adds the score
            rightSidePanel.add(playerPanel); //adds the player panel to the right side panel
        }
    }

    /**
     * Sets up each button as a field in the board and makes it clickable
     */
    public void initializeBoard() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                boardFields[i][j] = new JButton(); //creates a new button
                boardFields[i][j].setEnabled(true); //makes it clickable
                boardPanel.add(boardFields[i][j]); //adds it to the panel of the board
            }
        }
    }

    /**
     * Prints a welcome message to the users when beginning of the game and asks for the number of players and,
     * the names of each player.
     */
    public void welcomeAndGetPlayerNames() {
        int numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Welcome to the game of Scrabble!\nPlease enter the number of players (2-4)")); //prints a welcome message to the game and asks for the number of players
        for (int i = 1; i <= numPlayers; i++) {
            String name = JOptionPane.showInputDialog("Please Enter Player " + i + " Name"); //gets the name of the player
            playerNames.add(name); //adds the name to the list of player names
        }
    }


    public static void main(String[] args) {
        GameView gameView = new GameView();
    }

}
