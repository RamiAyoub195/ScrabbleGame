import java.util.*;

/**
 * This is the model class that brings all the logic together for a game, here also the user will input their commands
 * and get an update of the board, their score etc.
 *
 */

public class GameModel {
    private Scanner userInput;
    private ArrayList<Player> players;
    private Board gameBoard;
    private Board checkBoard;
    private TilesBag tilesBag;
    private WordList wordList;
    private int numOfPlayers;



    public GameModel(){
        userInput = new Scanner(System.in);
        players = new ArrayList<>();
        gameBoard = new Board(15, 15);
        checkBoard = new Board(15, 15);
        tilesBag = new TilesBag();
        wordList = new WordList();
        numOfPlayers = 0;
        playGame();
    }

    public void playGame(){

        System.out.println("Welcome to the game of Scrabble! Please enter the number of players (2-4):");
        int numOfPlayers = userInput.nextInt();

        while(numOfPlayers < 2 || numOfPlayers > 4){
            System.out.println("Please enter a number between 2 and 4:");
            numOfPlayers = userInput.nextInt();
        }

        for(int i = 1; i <= numOfPlayers; i++){
            System.out.println("Please enter player " + i + " name:");
            String playerName = userInput.next();
            players.add(new Player(playerName));
            getRandomTiles(7, players.get(i - 1));
        }

        while (!checkGameFinished()){
            for(Player player : players){
                gameBoard.displayBoard();
                player.displayPlayer();
                playerTurn(player);
            }
        }
    }

    public void playerTurn(Player player){
        System.out.println("Here are your options: Place Tile(s) (0), Swap Tiles (1), Skip Turn (2)");
        Scanner userInput = new Scanner(System.in);

        int choice = userInput.nextInt();

        while (choice < 0 || choice > 2){
            System.out.println("Please enter a number between 0 and 2:");
            choice = userInput.nextInt();
        }

        if (choice == 0){
            playerPlaceTile(player);
        }

        else if (choice == 1){
            playerSwapTile(player);
        }
    }

    public void playerPlaceTile(Player player){
        ArrayList<Tiles> tempTiles = new ArrayList<>();
        ArrayList<Integer> tempRowPositions = new ArrayList<>();
        ArrayList<Integer> tempColPositions = new ArrayList<>();

        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to place: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > player.getTiles().size()) {
            System.out.println("Invalid, user does not have enough tiles! Please enter the number of tile(s) you would like to place: ");
            choiceNumTiles = userInput.nextInt();
        }

        System.out.println("You will now be entering the word to place tile by tile make sure to enter it in the right order!");

        for (int i = 1; i <= choiceNumTiles; i++){
            System.out.println("Please enter tile " + i + " (index 0-6):");
            int tileIndex = userInput.nextInt();
            tempTiles.add(player.getTiles().get(tileIndex));
            System.out.println("Please enter the row position for tile " + i + " :");
            int rowIndex = userInput.nextInt();
            tempRowPositions.add(rowIndex);
            System.out.println("Please enter the column position for tile " + i + " :");
            int columnIndex = userInput.nextInt();
            tempColPositions.add(columnIndex);
            System.out.println("Which direction is the word being placed (Horizontal/Vertical):");
            String direction = userInput.next();
            checkPlacement(tempTiles, tempRowPositions, tempColPositions, direction);
        }
    }

    public void playerSwapTile(Player player){
        Scanner userInput = new Scanner(System.in);
        System.out.println("How many tile(s) would you like to swap: ");
        int choiceNumTiles = userInput.nextInt();

        while(choiceNumTiles > 0){
            System.out.println("Select a tile to swap (Enter the index (0-6)): ");
            int tileIndex = userInput.nextInt();

            Tiles t = player.getTiles().get(tileIndex);
            player.getTiles().remove(tileIndex);

            getRandomTiles( 1, player);
            tilesBag.bagArraylist().add(t);
            gameBoard.displayBoard();
            player.displayPlayer();
            choiceNumTiles--;
        }
    }

    /**
     * Checks to see when the game is finished if the tiles in the bag have finished.
     * @return a boolean true of bag is empty and false if not empty
     */
    public boolean checkGameFinished(){
        return tilesBag.bagArraylist().isEmpty();
    }

    /**
     * Gets a random tile from the bag of tiles to a player
     *
     * @param numOfTiles number of tiles needed from bag
     * @param player player who needs the tiles
     */
    public void getRandomTiles(int numOfTiles, Player player){
        Random rand = new Random();
        for (int i = 0; i < numOfTiles; i++){
            int rnd = rand.nextInt(tilesBag.bagArraylist().size());
            player.getTiles().add(tilesBag.bagArraylist().get(rnd));
            tilesBag.bagArraylist().remove(rnd);
        }
    }

    public boolean checkPlacement(ArrayList<Tiles> tiles, ArrayList<Integer> rows, ArrayList<Integer> cols, String direction){
        if(gameBoard.checkMiddleBoardEmpty()){
            checkBoard.placeBoardTile(rows.get(0), cols.get(0), tiles.get(0).getLetter());
            for(int i = 1; i < tiles.size() - 1; i++){
                if(checkBoard.checkIllegalPlacement(rows.get(i),cols.get(i))){
                    System.out.println("Words must be connected horizontally or vertically! and the first word must cover over the center of the board");
                    return false;
                }
                checkBoard.placeBoardTile(rows.get(i), cols.get(i), tiles.get(i).getLetter());
                checkWord(checkBoard, rows.get(i), cols.get(i), direction, false);
            }
            checkWord(checkBoard,rows.get(rows.size() - 1), cols.get(cols.size() - 1), direction, true);
        }
        else if (board.checkBoardTileEmpty(row, column) && !board.checkIllegalPlacement(row, column)){
            board.placeBoardTile(row, column, t.getLetter());
            board.displayBoard();
            player.displayPlayer();
            player.getTiles().remove(tileIndex);
            getRandomTiles( 1, player);
            choiceNumTiles--;
        }
    }

    public boolean checkWord(Board board, int row, int col, String direction, boolean lastLetter){
        String wordLeftToRight = "";
        String wordRightToLeft = "";
        StringBuilder wordTopToBottom = new StringBuilder();
        StringBuilder wordBottomToTop = new StringBuilder();

        if (direction.equals("Horizontal") && !lastLetter){
            int i = 1;
            while(!board.getBoard()[row + i][col].equals("  -  ") && row + i < 15){
                wordTopToBottom.append(board.getBoard()[row + i][col]);
                i++;
            }
            int j = 1;
            while(!board.getBoard()[row - j][col].equals("  -  ") && row - j >= 0){
                wordBottomToTop.append(board.getBoard()[row - j][col]);
                j++;
            }
            wordTopToBottom.reverse().append(wordBottomToTop);

        }
        else if (direction.equals("Vertical") && !lastLetter){

        }
    }

    public static void main(String[] args){
        GameModel gameModel = new GameModel();
    }
}
