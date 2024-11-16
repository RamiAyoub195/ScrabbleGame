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
    private Cell[][] board; //the board
    private static final int CENTER_ROW = 7; // Center of the board (row)
    private static final int CENTER_COL = 7;
    private boolean[][] visited; // tracks visited cells during DFS; // Center of the board (col)

    // Directions for moving up, down, left, and right in DFS
    private static final int[] DIR_X = {-1, 1, 0, 0};
    private static final int[] DIR_Y = {0, 0, -1, 1};

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
        board = new Cell[rows][cols];
        this.visited = new boolean[rows][cols];
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
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                if (this.board[i][j].isOccupied())
                {
                    newBoard.board[i][j].placeTile(new Tiles(this.board[i][j].getTile().getLetter(), this.board[i][j].getTile().getNumber()));
                }
            }
        }
        return newBoard;
    }

    /**
     * initializes a board where each row and column entry are empty to begin with.
     */
    public void setUpBoard()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                board[i][j] = new Cell();
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
    public boolean checkBoardTileEmpty(int row, int col)
    {
        return !board[row][col].isOccupied();
    }

    /**
     * Places a tile on the board.
     * @param row row to place tile on the board
     * @param col column to place tile on the board
     */
    public void placeBoardTile(int row, int col, Tiles tile)
    {
        board[row][col].placeTile(tile);
    }

    public void removeBoardTile(int row, int col, Tiles tile)
    {
        board[row][col].removeTile();
    }

    /**
     * Checks to see if the middle of the board is empty
     * @return true if middle empty, false otherwise
     */
    public boolean checkMiddleBoardEmpty()
    {
        return !board[7][7].isOccupied();
    }

    /**
     * Returns the board in the game to access the board
     * @return board
     */
    public Cell[][] getBoard(){
        return board;
    }

    /**
     * Checks to see if the tile being inserted is adjacent to another tile. Checks at the corners
     * all edges and anywhere else on the board.
     * @param row row of the tile being places
     * @param col column of the tile being placed
     * @return true if the tile is adjacently connected, false otherwise
     */
    public boolean checkAdjacentBoardConnected(int row, int col) {
        if (row == 0 && col == 0) { // Top left corner
            return board[row + 1][col].isOccupied() || board[row][col + 1].isOccupied();
        } else if (row == rows - 1 && col == 0) { // Bottom left corner
            return board[row - 1][col].isOccupied() || board[row][col + 1].isOccupied();
        } else if (row == 0 && col == cols - 1) { // Top right corner
            return board[row][col - 1].isOccupied() || board[row + 1][col].isOccupied();
        } else if (row == rows - 1 && col == cols - 1) { // Bottom right corner
            return board[row - 1][col].isOccupied() || board[row][col - 1].isOccupied();
        } else if (col == 0 && (row > 0 && row < rows - 1)) { // Left edge
            return board[row - 1][col].isOccupied() || board[row + 1][col].isOccupied() || board[row][col + 1].isOccupied();
        } else if (col == cols - 1 && (row > 0 && row < rows - 1)) { // Right edge
            return board[row - 1][col].isOccupied() || board[row + 1][col].isOccupied() || board[row][col - 1].isOccupied();
        } else if (row == 0 && (col > 0 && col < cols - 1)) { // Top edge
            return board[row][col - 1].isOccupied() || board[row][col + 1].isOccupied() || board[row + 1][col].isOccupied();
        } else if (row == rows - 1 && (col > 0 && col < cols - 1)) { // Bottom edge
            return board[row][col - 1].isOccupied() || board[row][col + 1].isOccupied() || board[row - 1][col].isOccupied();
        }
        // Anywhere else
        return board[row + 1][col].isOccupied() || board[row][col + 1].isOccupied() ||
                board[row - 1][col].isOccupied() || board[row][col - 1].isOccupied();
    }

    /**
     * Displays the board of the game showing each row and column
     */
    public void displayBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j].toString());
            }
            System.out.println();
        }
    }

    /**
     * Performs DFS to check connectivity from the center.
     * @param row current row in DFS search
     * @param col current column in DFS search
     */
    private void dfs(int row, int col) {
        // If the position is out of bounds or already visited or not occupied, return
        if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col] || !board[row][col].isOccupied()) {
            return;
        }

        // Mark the current cell as visited
        visited[row][col] = true;

        // Explore the four neighboring directions (up, down, left, right)
        for (int i = 0; i < 4; i++) {
            int newRow = row + DIR_X[i];
            int newCol = col + DIR_Y[i];
            dfs(newRow, newCol); // Recursively visit the neighboring cells
        }
    }

    /**
     * Checks if all occupied spots are connected to the center.
     * @return true if all occupied spots are connected to the center, false otherwise
     */
    public boolean checkAdjacency() {
        // Reset the visited array for a new DFS traversal
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                visited[i][j] = false;
            }
        }

        // Start DFS from the center if it's occupied
        if (board[CENTER_ROW][CENTER_COL].isOccupied()) {
            dfs(CENTER_ROW, CENTER_COL);
        } else {
            return false; // If the center is not occupied, return false
        }

        // After DFS, check if there is any unvisited occupied cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col].isOccupied() && !visited[row][col]) {
                    return false; // Found an occupied spot that is not connected to the center
                }
            }
        }

        // If we visited all occupied spots, return true
        return true;
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }
}



