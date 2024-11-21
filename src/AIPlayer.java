import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;


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

    /**
     * This is the constructor of the AIPlayer class which extends from the player class.
     * It calls the super method to initiate a player where each AIPlayer will have a special tag
     * to differentiate them form the other AI.
     * @param AITag the name of the AIPlayer ie, AI1, AI2, etc.
     */
    public AIPlayer(String AITag) {
        super(AITag);
    }

    /**
     * This is a method used to get all possible word computations from the tiles of the AIPlayer. If
     * an AIPlayer has a blank tile then it must implement 26 possible letter implementations.
     * It will return a set of the word computations in ascending order of length.
     *
     * @param wordList the wordlist of valid words
     * @return a set of all word computations in ascending order.
     */
    public HashSet<String> getAllWordComputations(WordList wordList) {
        HashSet<String> validWords = new HashSet<>(); //creates a new set
        String tilesAsString = getTilesToString(); //gets the AIPlayers tiles as a string
        System.out.println(tilesAsString);
        for(int i = 2; i <= getTiles().size(); i++){ //traverses to get all possible words of length 2 to 7
            ArrayList<String> allPermutations = permute(tilesAsString, i);
            for(String word : allPermutations){
                if(wordList.isValidWord(word)){
                    validWords.add(word);
                }
            }
        }
        return validWords;
    }

    private ArrayList<String> permute(String tilesAsString, int length) {
        ArrayList<String> permutations = new ArrayList<>();
        generatePermutations(tilesAsString.toCharArray(), 0, length, permutations);
        return permutations;
    }

    private void generatePermutations(char[] stringAsCharArray, int index, int length, ArrayList<String> permutations) {
        if(index == length){
            permutations.add(new String(stringAsCharArray, 0, length));
            return;
        }

        for(int i = index; i < stringAsCharArray.length; i++){
            bubbleSwap(stringAsCharArray, index, i);
            generatePermutations(stringAsCharArray, index + 1, length, permutations);
            bubbleSwap(stringAsCharArray, index, i);
        }
    }

    private void bubbleSwap(char[] stringAsCharArray, int index, int i) {
        char temp = stringAsCharArray[index];
        stringAsCharArray[index] = stringAsCharArray[i];
        stringAsCharArray[i] = temp;
    }

    /**
     * Creates a string of the AIPlayer's tiles.
     */
    public String getTilesToString() {
        String tilesToString = "";
        for (Tiles t : getTiles()) {
            tilesToString += t.getLetter();
        }
        return tilesToString;
    }

    private int getValueOfChar(char l){
        for (Tiles t : getTiles()) {
            if(t.getLetter().equals(String.valueOf(l))){
                return t.getNumber();
            }
        }
        return 0;
    }

    public HashMap<String, Integer> valueOfValidPermutations(HashSet<String> validWords) {
        int score = 0;
        HashMap<String, Integer> validPermutations = new HashMap<>();
        for(String word : validWords){
            for (char letter : word.toCharArray()) {
                int number = getValueOfChar(letter);
                score += number;
            }
            validPermutations.put(word, score);
        }
        return validPermutations;
    }

    public static void main(String[] args) {
        // Create a GameModel object (assuming GameModel and AIPlayer are already defined)
        GameModel model = new GameModel(); // Assuming this is where the game logic is handled
        HashSet<String> validWords = new HashSet<>(); // To store valid words found by AI

        // Create an AIPlayer with a tag (for example, "AI1")
        AIPlayer aiPlayer = new AIPlayer("AI1");

        // Add the AI player to the game model (assuming addPlayer method handles it correctly)
        model.addPlayer(aiPlayer.getName());

        // Print the tiles (letters) of the AI player
        System.out.println("AI Player Tiles:");


        // Find valid words for all players (including AI)
        for (Player p : model.getPlayers()) {
            // Check if the player is an instance of AIPlayer
            if (p instanceof AIPlayer ai) {
                // Cast Player to AIPlayer
                for (Tiles tile : ai.getTiles()) {
                    System.out.println(tile.getLetter()); // Assuming getLetter returns a char or String
                }

                // Assuming getAllWordComputations uses a word list to calculate valid words for AI
                validWords.addAll(ai.getAllWordComputations(model.getWordList()));
            }
        }

        // Print out the valid words computed by the AIPlayer
        System.out.println("Valid Words:");
        for (String word : validWords) {
            System.out.println(word); // Print each valid word
        }
    }

}


