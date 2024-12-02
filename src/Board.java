import java.util.*;


/**
 * This class is the board class which will display the board as the game goes along.
 * It will check to make sure that a tile can be put in a specific area of the board.
 * Will update the board when a tile has been added. It will also take care of the premium squares
 * which can be found for the logic in the website page provided.: https://en.wikipedia.org/wiki/Scrabble (section of premium squares gives the coordinates)
 *
 * Author(s): Rami Ayoub, Louis Pantazopoulos, Andrew Tawfik, Liam bennet
 * Version: 3.0
 * Date: Sunday, November 17, 2024
 *
 */

public class Board {

    private int rows; //rows of the board
    private int cols; //columns of the board
    private Cell[][] board; //the board od cells
    private int[][] tripleWordCoords = { //the coordinates for a TWS
            {0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}
    };
    private int[][] doubleWordCoords = { //the coordinates for a DWS
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {13, 1}, {12, 2}, {11, 3}, {10, 4},
            {1, 13}, {2, 12}, {3, 11}, {4, 10}, {13, 13}, {12, 12}, {11, 11}, {10, 10},
    };
    private int[][] tripleLetterCoords = { //the coordinates for a TLS
            {1, 5}, {1, 9}, {5, 1}, {5, 13}, {9, 1}, {9, 13}, {13, 5}, {13, 9}
    };
    private int[][] doubleLetterCoords = { //the coordinates for a DLS
            {0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2},
    {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8},
    {8, 12}, {11, 0}, {11, 7}, {11, 14}, {12, 6}, {12, 8}, {14, 3}, {14, 11}
    };

    /**
     * Initialized the board for the game, the board is a row by col board and sets up each
     * cell in the board.
     *
     * @param rows the rows of the board
     * @param cols the columns of the board
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new Cell[rows][cols];
        setUpBoard();
    }

    /**
     * Clears the board by creating a new board.
     */
    public void clearBoard() {
        for (int i = 0; i < rows; i++) { //travreses through the rows
            for (int j = 0; j < cols; j++) { //travreses through the cols
                board[i][j] = new Cell(); //creates a new cell
            }
        }
    }

    /**
     * initializes a board where each row and column entry are empty to begin with.
     */
    public void setUpBoard()
    {
        for(int i = 0; i < rows; i++) //traverses through the rows
        {
            for(int j = 0; j < cols; j++) //traverses through the cols
            {
                if (isTripleWordSquare(i, j)) { //checks if the coord is a TWS
                    board[i][j] = new Cell(); //creates a new cell
                    board[i][j].setSpecialType("TWS");// sets it as a triple word square
                } else if (isDoubleWordSquare(i, j)) { //checks if the coord is a DWS
                    board[i][j] = new Cell(); //creates a new cell
                    board[i][j].setSpecialType("DWS"); // sets it as a double word square
                } else if (isTripleLetterSquare(i, j)) { //checks if the coord is a TLS
                    board[i][j] = new Cell(); //creates new cell
                    board[i][j].setSpecialType("TLS"); // sets it as a triple letter square
                } else if (isDoubleLetterSquare(i, j)) { //checks if the coord is a DLS
                    board[i][j] = new Cell(); //creates a new cell
                    board[i][j].setSpecialType("DLS"); // sets it as a double letter square
                } else { //if it's not any of the premium square coord then a regular cell
                    board[i][j] = new Cell(); //creates a new cell with a default special type null
                }
            }
        }
    }

    /**
     * Checks if a specific row and col is a TWS square.
     *
     * @param row the row
     * @param col the column
     * @return true if it's a TWS, false otherwise
     */
    private boolean isTripleWordSquare(int row, int col) {
        return containsAnyPremiumSquare(tripleWordCoords, row, col);
    }

    /**
     * Checks if a specific row and col is a DWS square.
     *
     * @param row the row being checked
     * @param col the col being checked
     * @return if a DWS, false otherwise
     */
    private boolean isDoubleWordSquare(int row, int col) {
        return containsAnyPremiumSquare(doubleWordCoords, row, col);
    }

    /**
     * Checks if a specific row and col is a TLS square.
     *
     * @param row the row being checked
     * @param col the col being checked
     * @return if a TLS, false otherwise
     */
    private boolean isTripleLetterSquare(int row, int col) {
        return containsAnyPremiumSquare(tripleLetterCoords, row, col);
    }

    /**
     * Checks if a specific row and col is a DLS square.
     * @param row the row being checked
     * @param col the col being checked
     * @return if a DLS, false otherwise
     */
    private boolean isDoubleLetterSquare(int row, int col) {
        return containsAnyPremiumSquare(doubleLetterCoords, row, col);
    }

    /**
     * Checks to see if a given coordinate is any of the premium squares such as TLS, DLS,
     * etc.
     */
    private boolean containsAnyPremiumSquare(int[][] coords, int row, int col) {
        for (int[] coord : coords) { //traverses through the coordinated of the specific special square coordinates
            if (coord[0] == row && coord[1] == col) { //if it is one of the coordinates return true
                return true;
            }
        }
        return false; //else return false
    }

    /**
     * Copies from one board to another which is useful to retrieve back to a board in case
     * a word couldn't be placed.
     *
     * @return a copy of a board
     */
    public Board copyBoard() {
        Board newBoard = new Board(this.rows, this.cols); //creates a new board
        for (int i = 0; i < this.rows; i++) //traverses through the rows
        {
            for (int j = 0; j < this.cols; j++) //traverses through the cols
            {
                if (this.board[i][j].isOccupied()) //if there is a tile on the original board
                {
                    newBoard.board[i][j].placeTile(new Tiles(this.board[i][j].getTile().getLetter(), this.board[i][j].getTile().getNumber())); //creates the new board cells to have the same cells as the original board
                }
            }
        }
        return newBoard; //returns the new board
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

    public void removeBoardTile(int row, int col)
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
     * Gets a specific cell in the board.
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the cell
     */
    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    /**
     * Converts the Board object into its XML representation.
     *
     * @return A string representing the Board object in XML format.
     * The XML includes all rows and cells on the board.
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder("<Board>");
        for (int i = 0; i < rows; i++) {
            xml.append("<Row>");
            for (int j = 0; j < cols; j++) {
                xml.append(board[i][j].toXML());
            }
            xml.append("</Row>");
        }
        xml.append("</Board>");
        return xml.toString();
    }

    /**
     * Creates a Board object from its XML representation.
     *
     * @param xml The XML string representing the Board object.
     *            The XML must include all rows and cells with their respective states.
     * @return A Board object initialized with the data parsed from the XML.
     */
    public static Board fromXML(String xml) {
        int rows = 15; // Assuming fixed dimensions for Scrabble
        int cols = 15;
        Board board = new Board(rows, cols);

        String rowsXML = xml.substring(xml.indexOf("<Board>") + 8, xml.indexOf("</Board>"));
        String[] rowEntries = rowsXML.split("</Row><Row>");
        for (int i = 0; i < rowEntries.length; i++) {
            rowEntries[i] = rowEntries[i].replace("<Row>", "").replace("</Row>", "");
            String[] cellXMLs = rowEntries[i].split("</Cell><Cell>");
            for (int j = 0; j < cellXMLs.length; j++) {
                board.getBoard()[i][j] = Cell.fromXML("<Cell>" + cellXMLs[j] + "</Cell>");
            }
        }

        return board;
    }



    public static void main(String[] args) {
        Board board = new Board(15, 15);
        board.tripleWordCoords = new int[][]{{0, 0}, {0, 14}, {14, 0}, {14, 14}};
        board.tripleLetterCoords = new int[][]{
                {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {8, 8}, {9, 9}, {10, 10}, {11, 11}, {12, 12}, {13, 13},
                {1, 13}, {2, 12}, {3, 11}, {4, 10}, {5, 9}, {6, 8}, {8, 6}, {9, 5}, {10, 4}, {11, 3}, {12, 2}, {1, 13}
        };
        board.doubleWordCoords = new int[][]{{0, 7}, {7, 0}, {14, 7}, {7, 14}};
        board.doubleLetterCoords = new int[][]{
                {1, 7}, {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7}, {8, 7}, {9, 7}, {10, 7}, {11, 7}, {12, 7}, {13, 7},
                {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13},
        };
        System.out.println(board.toXML());


    }
}



