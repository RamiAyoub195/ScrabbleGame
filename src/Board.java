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

    public void displayBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public void copyBoard(Board source) {
        for (int i = 0; i < 15; i++) {
            System.arraycopy(source.board[i], 0, this.board[i], 0, 15);
        }
    }

}



