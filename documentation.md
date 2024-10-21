# Documentation
## 1. Two-Board System: gameBoard and checkBoard
### Design Decision: 
A major design decision in this project was to implement two separate board instances: gameBoard and checkBoard. The gameBoard represents the actual state of the game that the players interact with, while the checkBoard serves as a temporary board used for validating tile placements before committing them to the gameBoard.

### Benefits:

- Prevents the corruption of the main board with invalid moves.
- Provides a clean and efficient way to test moves before committing them.
- Allows for smoother gameplay, as invalid actions can be easily rolled back without consequence.

### Trade-off: 
The trade-off is increased memory usage since two board states need to be maintained at all times. However, the design ensures the game’s integrity and reduces bugs, making the extra memory usage worth it.

## 2. Handling of the First Turn Special Rule
 
### Design Decision: 
The second notable design decision is the way the game handles the special rule for the first turn, where the player is required to place the first word so that it covers the center of the board (position 7, 7). The checkMiddleBoardEmpty() method helps with this check.

### Benefits:

- Ensures adherence to Scrabble’s rules from the very first turn.
- Provides user-friendly feedback if the rule is not followed, allowing players to adjust their move without the game advancing incorrectly.
- The method is simple yet effective in differentiating between the first and next turns, after which standard rules apply.

### Trade-off: 
The logic for checking the first move adds some complexity and edge cases to handle.

## 3. Tile Validation and Word Scoring System

### Design Decision: 
Another key design decision revolves around the validation of tile placement and the subsequent scoring system. When a player places tiles, the game ensures that the newly placed tiles form valid words both horizontally and vertically. The scoring system works by calculating points for each newly formed word and only credits points once the move has been validated as legal.

### Benefits:

- Maintains game integrity by only awarding points for valid moves.
- Prevents situations where players could exploit the system by placing invalid words and still gaining points.
- Keeps the game challenging and in line with official Scrabble rules.

### Trade-off: 
The complexity of managing multiple words formed by a single placement adds overhead to the game's logic. Ensuring that words are connected, valid, and correctly scored in both directions requires careful checking and iteration over the board. However, this trade-off is necessary for a true-to-life Scrabble experience.