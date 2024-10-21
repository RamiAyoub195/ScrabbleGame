
# Scrabble Game User Manual

## Welcome to Text-Based Scrabble!

This user manual will guide you through how to play this text-based Scrabble game according to the current implementation.

## 1. Starting the Game

1. Launch the game by running the program.
2. You will be prompted to enter the number of players. You can choose between 2 to 4 players.
   - Input a number between 2 and 4 to continue.

3. Each player will then be prompted to enter their name.
   - Input the player name when prompted.

4. After all players have been initialized, each player will be given 7 random tiles from the tile bag to start their hand.

## 2. Taking a Turn

During each player's turn, the board will be displayed, showing the current state of the game. Players will be presented with the following options:

### 1. Place Tile(s) (Option 0)
   - Choose this option to place tiles on the board.
   - Enter the number of tiles you wish to place (you can only place as many as you have in your hand).
   - For each tile:
     - You will be shown your available tiles.
     - Select the index of the letter you want to place.
     - Input the row and column positions where you wish to place the tile.
   - If you place the tiles correctly, the word will be validated against the game rules and the dictionary.
   - If the word is valid, your score will be calculated and added to your total.
   - If invalid, the tiles will be returned to your hand, and you must try again.

### 2. Swap Tiles (Option 1)
   - Choose this option if you want to swap tiles from your hand.
   - You will be prompted to enter the number of tiles to swap.
   - Select the tiles to swap (by entering the index of the tile in your hand).
   - After the swap, you will receive new tiles from the tile bag, and the swapped tiles will be returned to the bag.

### 3. Skip Turn (Option 2)
   - If you choose not to place or swap tiles, you can skip your turn.

## 3. Special Rules

### 1. First Turn Rule
   - On the first turn of the game, the player must place a word that covers the center of the board (position 7, 7).
   - If the player does not place a word covering this tile, the turn will be canceled, and they must try again until they do.

### 2. Scoring
   - After placing tiles, the game checks for new words formed horizontally and vertically.
   - The value of each tile is added to the score for each valid word formed.

## 4. Ending the Game

- The game continues in rounds until no more tiles are available in the tile bag, and one player uses all their tiles.
- At the end of the game, the player with the highest score wins.

## 5. Additional Notes

- Players can cancel their move at any time by entering "0" when prompted to place a tile.
- Invalid moves will be automatically reverted, and players will have another chance to place valid tiles.

Enjoy your game of Scrabble!
