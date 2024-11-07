/**
 * This class is the board class which will display the board as the game goes along.
 * It will check to make sure that a tile can be put in a specific area of the board.
 * Will update the board when a tile has been added.
 *
 * Author(s): Rami Ayoub
 * Version: 1.0
 * Date: Wednesday, October 16th, 2024
 *
 */

public class Board {

    private int rows; //rows of the board
    private int cols; //columns of the board
    private String[][] board; //the board

    /**
     * Initialized the board for the game, the board is a 15 by 15 board and sets up each
     * empty spot in the board as "  -  ".
     *
     * @param rows the rows of the board
     * @param cols the columns of the board
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new String[rows][cols];
        setUpBoard();
    }

    /**
     * Copies from one board to another which is useful to retrieve back to a board in case
     * a word couldn't be placed.
     *
     * @return a copy of a board
     */
    public Board copyBoard() {
        Board newBoard = new Board(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }
        return newBoard;
    }

    /**
     * initializes a board where each row and column entry are empty to begin with.
     */
    public void setUpBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                board[i][j] = "  -  ";
            }
        }
    }

    /**
     * Checks to make sure that the tile to be placed is not already existing on the board with
     * another tile.
     *
     * @param row row of the tile to be placed
     * @param col col of the tile to be placed
     * @return true of the board placement is empty, false otherwise
     */
    public boolean checkBoardTileEmpty(int row, int col){
        return board[row][col].equals("  -  ");
    }

    /**
     * Places a tile on the board.
     * @param row row to place tile on the board
     * @param col column to place tile on the board
     * @param letter the letter of the tile to be placed
     */
    public void placeBoardTile(int row, int col, String letter){
        board[row][col] = "  " + letter + "  ";
    }

    /**
     * Checks to see if the middle of the board is empty
     * @return true if middle empty, false otherwise
     */
    public boolean checkMiddleBoardEmpty(){
        return board[7][7].equals("  -  ");
    }

    /**
     * Returns the board in the game to access the board
     * @return board
     */
    public String[][] getBoard(){
        return board;
    }

    /**
     * Checks to see if the tile being inserted is adjacent to another tile. Checks at the corners
     * all edges and anywhere else on the board.
     * @param row row of the tile being places
     * @param col column of the tile being placed
     * @return true if the tile is adjacently connected, false otherwise
     */
    public boolean checkAdjacentBoardConnected(int row, int col){
        if (row == 0 && col == 0){ //Top Left Corner
            return !board[row + 1][col].equals("  -  ") || !board[row][col + 1].equals("  -  ");
        }

        else if (row == 14 && col == 0){ //Bottom left Corner
            return !board[row - 1][col].equals("  -  ") || !board[row][col + 1].equals("  -  ");
        }

        else if (row == 0 && col == 14){ //Top Right Corner
            return !board[row][col - 1].equals("  -  ") || !board[row + 1][col].equals("  -  ");
        }

        else if (row == 14 && col == 14){ //Bottom Right Corner
            return !board[row - 1][col].equals("  -  ") || !board[row][col - 1].equals("  -  ");

        }

        else if (col == 0 && (row > 0 && row < 14)) { //Left Edge
            return !board[row - 1][col].equals("  -  ") || !board[row + 1][col].equals("  -  ") || !board[row][col + 1].equals("  -  ");
        }

        else if (col == 14 && (row > 0 && row < 14)) { //Right Edge
            return !board[row - 1][col].equals("  -  ") || !board[row + 1][col].equals("  -  ") || !board[row][col - 1].equals("  -  ");
        }

        else if (row == 0 && (col > 0 && col < 14)) { //Top Edge
            return !board[row][col - 1].equals("  -  ") || !board[row][col + 1].equals("  -  ") || !board[row + 1][col].equals("  -  ");
        }

        else if (row == 14 && (col > 0 && col < 14)) { //Bottom Edge
            return !board[row][col - 1].equals("  -  ") || !board[row][col + 1].equals("  -  ") || !board[row - 1][col].equals("  -  ");
        }

        //Anywhere else
        return !board[row + 1][col].equals("  -  ") || !board[row][col + 1].equals("  -  ") ||
                !board[row - 1][col].equals("  -  ") || !board[row][col - 1].equals("  -  ");
    }

    /**
     * Displays the board of the game showing each row and column
     */
    public void displayBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
}



