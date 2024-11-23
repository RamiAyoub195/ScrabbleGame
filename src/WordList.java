import java.io.*;
import java.util.*;


/**
 * This class will contain a list of all valid words. when a player tries to place a word on the board.
 * It will be checked by the list of valid words in this class. It opens a text file that contains a list
 * of words from the official scrabble dictionary of valid list words.
 *
 * Author(s): Rami Ayoub, Andrew Tawfik, Louis Pantazopoulos, Liam Bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 */

public class WordList {

    private HashSet<String> words; //The Hashset that will contain the words from the file

    /**
     * Initializes the HashSet of words by opening and reading from the text file
     * adding each word to the set of words.
     */
    public WordList() {

        words = new HashSet<>(); //Initializes the hashset

        File filename = new File("wordlist.txt"); //The file that the scanner will parse

        try(Scanner scanner = new Scanner(filename)){ //Scanner will scan the file line by line
            while(scanner.hasNextLine()){//checks to see if there is a next line
                String word = scanner.nextLine().trim().toUpperCase(); //gets rid of extra spaces/characters and makes it all uppercase as out tiles are also all uppercase
                if (word.length() > 1){ //only add 2-letter minimum words
                    words.add(word); //appends the word to the hashset
                }
            }
        }
        catch(FileNotFoundException e){ //If the file cannot be found in the directory catches exception
            System.out.println(e); //prints the exception error
        }
    }

    /**
     * Returns true or false if the given word is found within the set of words.
     * @param word the word being checked
     * @return true if the word is found and false if the word is not found
     */
    public boolean isValidWord(String word){
        return words.contains(word);
    }
}
