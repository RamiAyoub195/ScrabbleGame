import java.io.*;
import java.util.*;


/**
 * This class will contain a list of all valid words. when a player tries to place a word(s) on the board.
 * It will be checked by the list of valid words in this class. It opens a text file that contains a list
 * of words from the example in the milestone project document on brightspace "https://www.mit.edu/~ecprice/wordlist.10000"
 *
 */

public class WordList {

    private ArrayList<String> words; //ArrayList of the words from the file

    /**
     * Initializes the ArrayList of words and opens and reads from the text file
     * adding each word to the list of words.
     */
    public WordList() {

        words = new ArrayList<>(); //Initializes the ArrayList

        File filename = new File("wordlist.txt"); //Creates a file that the scanner will scan

        try(Scanner scanner = new Scanner(filename)){ //Scanner will scan the file line by line
            while(scanner.hasNextLine()){//checks to see if there is a next line
                String word = scanner.nextLine().trim().toUpperCase(); //gets rid of extra spaces/characters
                if (word.length() > 1){ //only add 2-letter minimum words as the document has 1-letter words
                    words.add(word);
                }
            }
        }
        catch(FileNotFoundException e){ //If the file cannot be found in the directory raise exception
            System.out.println(e);
        }

    }

    /**
     * Returns true or false if the given word is found within the list of words.
     * @param word the word being checked
     * @return true if the word is found and false if the word is not found
     */
    public boolean isValidWord(String word){
        return words.contains(word);
    }

}
