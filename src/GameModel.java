import java.util.*;

/**
 * This is the game model class that brings all the logic together for a game, here
 * also the user will input their commands and get an update of the board, their score etc.
 * Each word that a player places will be checked for adjacency, valid word etc.
 *
 * Author(s): Rami Ayoub
 * Version: 1.0
 * Date: Wednesday, October 16, 2024
 */

public class GameModel {
    private Scanner userInput; //for userInput
    private ArrayList<Player> players; //list of players in the game
    private Board gameBoard; //the actual game board displayed
    private Board checkBoard; //a board used to check for adjacency, valid word, etc.
    private TilesBag tilesBag; //a bag of tiles in the game
    private WordList wordList; //valid word list of words from example txt file
    private Random rand; //to get random tiles from the bag when swapping or replacing placed tiles

    /**
     * Initializes the list of players, the boards, the user input scanner, the bag of tiles,
     * the list of words and starts the game.
     */
    public GameModel(){
        userInput = new Scanner(System.in);
        players = new ArrayList<>();
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        rand = new Random();
        playGame();
    }

    /**
     * Starts the game of Scrabble by asking for the players names, then iterates through
     * each player until the game has finished. The game finishes when both players no longer
     * have any tiles left.
     */
    public void playGame(){

        System.out.println("Welcome to the game of Scrabble!"); //Into to user

        for(int i = 1; i <= 2; i++){ //Based it of a two player game
            System.out.println("Please enter player " + i + " name:");
            String playerName = userInput.next(); //gets a players name
            players.add(new Player(playerName)); //creates the new player
            getRandomTiles(7, players.get(i - 1)); //assigns 7 random tiles from the bag to each player at the start
        }

        while (!checkGameFinished()){ //keep playing the game until it is finished
            for(Player player : players){ //traverses through each player getting a turn
                gameBoard.displayBoard(); //displays the game board
                player.displayPlayer(); //displays the player
                playerTurn(player); //plays the playes turn
            }
        }

    }

    /**
     * This function displays the user of their choices when it's their turn.
     * The player may choose to play tile(s), swap tile(s) or skip their turn.
     * @param player the player's current turn
     */
    public void playerTurn(Player player){
        System.out.println("Here are your options: Place Tile(s) (0), Swap Tiles (1), Skip Turn (2)"); //user options
        Scanner userInput = new Scanner(System.in);

        int choice = userInput.nextInt(); //gets the user choice

        while (choice < 0 || choice > 2){ //choice must be a valid choice from the options
            System.out.println("Please enter a number between 0 and 2:");
            choice = userInput.nextInt();
        }

        if (choice == 0){ //player plays a tile
            playerPlaceTile(player);
        }

        else if (choice == 1){ //player swaps a tile
            if(tilesBag.bagOfTileIsEmpty()){ //makes sure that the bag of tiles is not empty for swapping
                System.out.println("The bag of tiles is empty! cannot swap tile(s)");
                playerTurn(player);
            }
            else{
                playerSwapTile(player);
            }
        }

        else{ //player passes their turn
            System.out.println("Player turn passed.");
        }
    }

    /**
     * Takes care of the players decision to place a tile. The tile being placed
     * must be entered horizontally/vertically and the word being inserted or words after
     * tile is inserted must be a valid word.
     *
     * @param player the player's current turn
     */
    public void playerPlaceTile(Player player){
        ArrayList<Tiles> tempTiles = new ArrayList<>(); //temp tiles to place on a temp board
        ArrayList<Integer> tempRowPositions = new ArrayList<>(); //temp row position of temp tile
        ArrayList<Integer> tempColPositions = new ArrayList<>(); //temp column position of temp tile

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt(); //player tile(s) they would like to place

        while(choiceNumTiles > player.getTiles().size()) { //make sure the user selects a valid amount of tiles to play
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to place: ");
            choiceNumTiles = userInput.nextInt();
        }

        System.out.println("You will now be entering the word to place tile by tile make sure to enter it in the right order!");

        for (int i = 1; i <= choiceNumTiles; i++){ //gets a tiles index from player tiles
            System.out.println("Please enter tile " + i + " (index 0-6):");
            int tileIndex = userInput.nextInt();

            while(tileIndex < 0 || tileIndex > 6){ //must be a valid index
                System.out.println("Invalid index, please enter a valid index (index 0-6):");
                tileIndex = userInput.nextInt();
            }

            tempTiles.add(player.getTiles().get(tileIndex)); //adds it to temporary list of tiles
            System.out.println("Please enter the row position for tile (index 0-14) " + i + " :");
            int rowIndex = userInput.nextInt(); //row position of tiles

            while(rowIndex < 0 || rowIndex > 14){ //must be valid within the board
                System.out.println("Invalid row, please enter a valid index (index 0-14):");
                rowIndex = userInput.nextInt();
            }

            tempRowPositions.add(rowIndex); //adds row positions
            System.out.println("Please enter the column position for tile (index 0-14) " + i + " :");
            int columnIndex = userInput.nextInt(); //column position of tiles

            while(columnIndex < 0 || columnIndex > 14){ //must be a valid column position
                System.out.println("Invalid column, please enter a valid index (index 0-14):");
                columnIndex = userInput.nextInt();
            }

            tempColPositions.add(columnIndex); //adds column position
        }

        if(placeWord(tempTiles, tempRowPositions, tempColPositions, player)){ //if the word was sucessfully placed
            gameBoard = checkBoard.copyBoard(); //real board gets the checked board
            for (int i = 0; i < tempTiles.size(); i++){
                player.getTiles().remove(tempTiles.get(i)); //remove the tiles from the player
                player.addScore(tempTiles.get(i).getNumber()); //adds the score
            }
            if(!tilesBag.bagOfTileIsEmpty()){
                getRandomTiles(tempTiles.size(), player); //replaces placed tiles
            }
        }
        else{
           playerPlaceTile(player); //if the word was not placed, player plays again
        }
    }

    /**
     * Places a word on the board, the first word needs to go through the middle square.
     * Then all words must also be connected horizontally or vertically and must be a valid word
     * from the word list.
     *
     * @param tempTiles a list of the tiles
     * @param tempRowPositions a list of row positions for the tiles
     * @param tempColPositions a list of column positions for the tiles
     * @param player the player who is currently at turn
     *
     * @return boolean true if the word was placed, false otherwise
     */
    public boolean placeWord(ArrayList<Tiles> tempTiles, ArrayList<Integer> tempRowPositions, ArrayList<Integer> tempColPositions, Player player){
        Board savedCheckBoard = checkBoard.copyBoard(); //saves the board while checking the conditions

        for (int i = 0; i < tempTiles.size(); i++){ //checks to make sure that the board space is not already occupied
            if(checkBoard.checkBoardTileEmpty(tempRowPositions.get(i), tempColPositions.get(i))){
                checkBoard.placeBoardTile(tempRowPositions.get(i), tempColPositions.get(i), tempTiles.get(i).getLetter());
            }
            else{
                System.out.println("A tile is already in place! please place a tile on an empty board space!");
                checkBoard = savedCheckBoard; //restores the original board
                return false;
            }
        }


        if (!checkBoard.checkMiddleBoardEmpty()){ //the middle board was covered
            for (int i = 0; i < tempTiles.size(); i++){
                if(!checkBoard.checkAdjacentBoardConnected(tempRowPositions.get(i), tempColPositions.get(i))){ //checks for tile adjacency
                    System.out.println("Cannot place word as it is not connected horizontally/vertically!");
                    checkBoard = savedCheckBoard; //restores the original board
                    return false;
                }
            }
        }
        else{ //the middle of the board was not covered
            System.out.println("The first word must cover the middle of the board!");
            checkBoard = savedCheckBoard; //restores the original board
            return false;
        }



        if (!checkValidWord()){ //checks to make sure that a word is valid
            System.out.println("Invalid word! Please enter a valid word or the word must !");
            checkBoard = savedCheckBoard; //restores the original board
            return false;
        }

        return true;
    }


    /**
     * Checks to see if the added word is a valid word from the list of words in the
     * txt file. Checks each row and column of the board to make sure that all added words and
     * subwords match the word list.
     *
     * @return true if the word is a valid word, false otherwise
     *
     */
    public boolean checkValidWord(){
        for(int row = 0; row < 15; row++){ //goes throw each row
            StringBuilder word = new StringBuilder(); //creates a string for each row
            for(int col = 0; col < 15; col++){
                if(!checkBoard.getBoard()[row][col].equals("  -  ")){
                    word.append(checkBoard.getBoard()[row][col].trim()); //appends and trims a letter in a row
                }
                else if (word.length() > 1) { //if the word is at least two words
                    if(!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())){ //checks for both directions of the words
                        return false;
                    }
                    word.setLength(0); //resets the string size to 0
                }
            }

            if (word.length() > 1){ //in case a word ended at the end of a row checks for the word
                if(!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())){ //checks for both directions of the word
                    return false;
                }
            }
        }

        for(int col = 0; col < 15; col++){ //goes through each column
            StringBuilder word = new StringBuilder(); //creates a string for each column
            for(int row = 0; row < 15; row++){
                if(!checkBoard.getBoard()[row][col].equals("  -  ")){
                    word.append(checkBoard.getBoard()[row][col].trim()); //adds and trims each letter in the column
                }
                else if (word.length() > 1) { //must be at least a two-letter word
                    if(!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())){ //checks both sides of the word
                        return false;
                    }
                    word.setLength(0); //resets the string size back
                }
            }

            if (word.length() > 1){ //in case a word ended at the end of a column checks for the word
                if(!wordList.isValidWord(word.toString()) && !wordList.isValidWord(word.reverse().toString())){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Takes care of when a player decides to swap tile(s). The tile(s) are swapped
     * with the tiles in the tile bag. A player may swap tiles as long as they have enough tiles to swap
     * and that the bag of tiles is not empty.
     * @param player who currently has a turn
     */
    public void playerSwapTile(Player player){
        System.out.println("How many tile(s) would you like to swap: ");
        int choiceNumTiles = userInput.nextInt(); //the number of tiles player wants to swap

        while(choiceNumTiles > player.getTiles().size()) { //make sure the user selects a valid amount of tiles to play
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to swap: ");
            choiceNumTiles = userInput.nextInt(); //must swap valid number of tiles
        }

        for (int i = 1; i <= choiceNumTiles; i++){
            System.out.println("Select tile " + i + " to swap (Enter the index (0-6)): ");
            int tileIndex = userInput.nextInt(); //gets the tile

            Tiles t = player.getTiles().get(tileIndex);
            player.getTiles().remove(tileIndex); //removes the tile

            replaceSwappedTile(player, tileIndex); //gets a tile from the bag
            tilesBag.bagArraylist().add(t); //adds the tiles
        }
    }

    /**
     * Signals that the game has finished when all players have placed all of their tiles.
     *
     * @return a boolean true if both players have no more tiles, false otherwise
     */
    public boolean checkGameFinished(){
        boolean status = true;
        for(Player p : players){
            if(!p.getTiles().isEmpty()){ //the player still has tiles
                status = false;
            }
        }
        return status;
    }

    /**
     * Gets a random number of tiles from the bag of tiles to a player at the beginning
     * of the game and assigns it to the players.
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTiles(int numOfTiles, Player player){
        for (int i = 0; i < numOfTiles; i++){
            int rnd = rand.nextInt(tilesBag.bagArraylist().size()); //random index
            player.getTiles().add(tilesBag.bagArraylist().get(rnd)); //gets random tile
            tilesBag.bagArraylist().remove(rnd); //removes it from bag
        }
    }

    /**
     * Replaces a players tile(s) with tile(s) from the bag placing them
     * at the right index.
     * @param player the player who is swapping
     * @param index the index of the tile being replaced
     */
    public void replaceSwappedTile(Player player, int index){
        int rnd = rand.nextInt(tilesBag.bagArraylist().size()); //random index
        player.getTiles().add(index, tilesBag.bagArraylist().get(rnd)); //places at correct index
        tilesBag.bagArraylist().remove(rnd); //removes from bag
    }

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
