/**
 * This class is the board class which will display the board as the game goes along.
 * It will check to make sure that a tile can be put in a specific area of the board.
 * Will update the board when a tile has been added.
 *
 * Author: Rami Ayoub
 * Student Number: 101261583
 * Date: October 15th, 2024
 *
 */

public class Board {

    private int rows;
    private int cols;
    private String[][] board;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new String[rows][cols];
        setUpBoard();
    }

    public void setUpBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                board[i][j] = "  -  ";
            }
        }
    }

    public boolean checkBoardTileEmpty(int row, int col){
        return board[row][col].equals("  -  ");
    }

    /**
     *
     * Returns false if legal and true if illegal
     * @param row
     * @param col
     * @return
     */
    public boolean checkIllegalPlacement(int row, int col){
        if (row == 0 && col == 0 ){
            return board[row + 1][col + 1].equals("  -  ") && board[row + 1][col].equals("  -  ");
        }
        else if (row == 15 && col == 0){
            return board[row][col + 1].equals("  -  ") && board[row - 1][col].equals("  -  ");
        }
        else if (row == 0 && col == 15){
            return board[row + 1][col].equals("  -  ") && board[row][col - 1].equals("  -  ");
        }
        else if (row == 15 && col == 15){
            return board[row - 1][col].equals("  -  ") && board[row][col - 1].equals("  -  ");
        }
        else {
            return board[row + 1][col].equals("  -  ") && board[row - 1][col].equals("  -  ") &&
                    board[row][col + 1].equals("  -  ") && board[row][col - 1].equals("  -  ");
        }
    }

    public void placeBoardTile(int row, int col, String letter){
        board[row][col] = "  " + letter + "  ";
    }

    public boolean checkMiddleBoardEmpty(){
        return board[8][8].equals("  -  ");
    }

    public String[][] getBoard(){
        return board;
    }

    public void displayBoard() {
        // Print column indices
        for (int col = 0; col < 15; col++) {
            System.out.printf("%6d", col); // Single digit (0-9): 3 characters width
        }
        System.out.println();

        // Print each row with row index
        for (int row = 0; row < 15; row++) {
            // Determine spacing based on whether the row index is single or double digit
            if (row < 10) {
                System.out.printf(" %d ", row); // Single digit (0-9): Add space before the index
            } else {
                System.out.printf("%d ", row); // Double digit (10-14): No extra space
            }

            for (int col = 0; col < 15; col++) {
                System.out.print(board[row][col] + " "); // Print the tile or empty space
            }
            System.out.println(); // Move to the next line after each row
        }
    }




    public void copyBoard(Board source) {
        for (int i = 0; i < 15; i++) {
            System.arraycopy(source.board[i], 0, this.board[i], 0, 15);
        }
    }

}



