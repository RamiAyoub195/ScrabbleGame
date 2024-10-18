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
    }

    public void setUpBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                board[i][j] = "-";
            }
        }
    }

    public boolean checkBoardTileEmpty(int row, int col){
        return board[row][col].equals("-");
    }

    public void placeBoardTile(int row, int col, String tile){
        board[row][col] = tile;
    }

    public void displayBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                System.out.print("  " + board[i][j] + "  ");
            }
            System.out.println();
        }
    }
}



