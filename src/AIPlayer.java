import java.util.*;
import java.lang.*;
import java.util.Comparator;

/**
 * Represents an AI player in the game, an AI player is a virtual player that simulates a real player
 * in the game of scrabble. An AI player can place tiles to form a word on the board, pass and swap.
 * The AI's object just like the players objective is to win the game by gathering the most points.
 *
 * Author(s):
 * Version:
 * Date:
 */

public class AIPlayer extends Player  {
    Random rand = new Random();

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
        ArrayList<Tiles> tileList = new ArrayList<>(); // array list of tiles in players hand
        String tilesAsString = getTilesToString(); //gets the AIPlayers tiles as a string

//        System.out.println(tilesAsString);

        // List of tiles in hand
        tileList = getTiles();

        // StringBuilder to change tilesAsString easier
        StringBuilder sb = new StringBuilder(tilesAsString);

        // For each blank tile, replace it with a random letter
        for (int index = 0; index < sb.length(); index++) {
            // If blank is found
            if (sb.charAt(index) == ' ') {
                String randomLetter = String.valueOf((char) ('a' + rand.nextInt(26))).toUpperCase(); // get random letter
                sb.setCharAt(index, randomLetter.charAt(0)); // set the random letter at the current blank tile index
                tileList.get(index).setLetter(randomLetter); // set the random letter in the actual tile object
            }
        }

        // convert back to string
        tilesAsString = sb.toString();

//        System.out.println(tilesAsString);
        for(int i = 2; i <= tilesAsString.length(); i++){ //traverses to get all possible words of length 2 to 7
            HashSet<String> allPermutations = permute(tilesAsString, i);
            for(String word : allPermutations){
                if(wordList.isValidWord(word)){
                    validWords.add(word);
//                    System.out.println(word);
                }
            }
        }
//        System.out.println("\n");
        return validWords;
    }

    private HashSet<String> generateAllBlankPermutations(String tilesAsString, int length) {
        HashSet<String> permutations = new HashSet<>();
        for (char c = 'A'; c <= 'Z'; c++) { // Replace the blank tile with every letter in the alphabet
            String replacedTiles = tilesAsString.replaceFirst(" ", String.valueOf(c));
            permutations.addAll(permute(replacedTiles, length));
        }
        return permutations;
    }

    private HashSet<String> permute(String tilesAsString, int length) {
        HashSet<String> permutations = new HashSet<>();
        generatePermutations(tilesAsString.toCharArray(), 0, length, permutations);
        return permutations;
    }

    private void generatePermutations(char[] stringAsCharArray, int index, int length, HashSet<String> permutations) {
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
        HashMap<String, Integer> validPermutations = new HashMap<>();

        // Iterate over valid words and compute their score
        for (String word : validWords) {
            int score = 0; // Reset score for each word

            // Calculate score for each word
            for (char letter : word.toCharArray()) {
                int number = getValueOfChar(letter);
                score += number;
            }

            // Store word and score in HashMap
            validPermutations.put(word, score);
        }

        // Create a list to store the scores (values from the map)
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : validPermutations.entrySet()) {
            list.add(entry.getValue()); // Add values (scores) to the list
        }

        // Sort the list in ascending order
        Collections.sort(list, Collections.reverseOrder());

        // Create a LinkedHashMap to maintain the order after sorting
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        // Iterate over the sorted list of scores and add the corresponding word-value pairs back into the sorted map
        for (int num : list) {
            for (Map.Entry<String, Integer> entry : validPermutations.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num); // Put the word and its score in sorted order
                }
            }
        }

        // Return the sorted map
        return sortedMap;
    }

}