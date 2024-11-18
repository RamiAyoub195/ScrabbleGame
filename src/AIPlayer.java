/**
 * Represents an AI player in the game, an AI player is a virtual player that simulates a real player
 * in the game of scrabble. An AI player can place tiles to form a word on the board, pass and swap.
 * The AI's object just like the players objective is to win the game by gathering the most points.
 *
 * Author(s):
 * Version:
 * Date:
 */

public class AIPlayer extends Player {
    private int bestScore; //represents the best possible score an AIPlayer can get

    /**
     * This is the constructor of the AIPlayer class which extends from the player class.
     * It calls the super method to initiate a player where each AIPlayer will have a special tag
     * to differentiate them form the other AI.
     * @param AITag the name of the AIPlayer ie, AI1, AI2, etc.
     */
    public AIPlayer(String AITag) {
        super(AITag);
        bestScore = 0;
    }

    /**
     * This method is the move an AIPlayer makes, an AIPlayer attempt to place a word on the current
     * board horizontally that gets them the most points.
     *
     * @param gameBoard the game board with the current tiles on it
     * @param wordList the wordlist of valid words
     *
     */
    public void makeTheBestMoveHorizontal(Board gameBoard, WordList wordList) {
        for (int i = 0; i < gameBoard.getRows(); i++) {
            for (int j = 0; j < gameBoard.getCols(); i++){
                checkWord(gameBoard, wordList, i, j, true);
            }
        }
    }

    public void makeTheBestMoveVertical(Board gameBoard, WordList wordList) {
        for (int i = 0; i < gameBoard.getRows(); i++) {
            for (int j = 0; j < gameBoard.getCols(); j++){
                checkWord(gameBoard, wordList, i, j, false);
            }
        }
    }

    public void checkWord(Board gameBoard, WordList wordList, int row, int col, boolean isHorizontal) {

    }



}
