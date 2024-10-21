# Text-Based Scrabble Game

## Overview
This is a text-based version of Scrabble that allows 2 to 4 players to compete in a game of word-building. The game enforces standard Scrabble rules while managing player turns, tile placement, and scoring.

## How to Play
- Start the game by following the instructions for player setup.
- During each player's turn, the board will be displayed, and the player can choose to place tiles, swap tiles, or skip their turn.
- The game continues until the tile bag is empty and no more tiles can be placed.

For detailed instructions on how to play, see the [User Manual](user-manual.md).

## Known Issues

1. **Single and Double Letter Words:**
    - The word validation logic allows single-letter and double-letter words that are not valid in standard Scrabble (e.g., certain tiles in the text file appear as words).

2. **Middle Tile Enforcement:**
    - Once the first word is placed in the middle of the board (center tile), any word can be placed on the board without proper validation, allowing incorrect placements.

3. **Inefficient Game Flow:**
    - Players are required to manually compute the row and column for each letter in the word they place. There is no board indexing system in place, forcing the player to count out the position of each letter, which can be time-consuming and error-prone.

## Roadmap (Solutions to Known Issues)

1. **Improved Word Validation:**
    - Update the word validation logic to disallow single-letter and double-letter words unless they are valid according to Scrabble’s rules (e.g., "a", "I"). Implement checks to filter out these words from the dictionary or validation list.

2. **Strict Placement Rules After First Turn:**
    - Enforce proper tile connections and word formations for every turn after the first move. Words must connect to existing tiles on the board, and only valid placements should be allowed.

3. **Enhanced User Experience:**
    - Implement an indexing system for the board that clearly labels rows and columns, making it easier for players to choose the correct positions for their tiles. This will improve the efficiency of tile placement and make the game more user-friendly.
    - This will fix itself once we complete the next milestones, creating the GUI. Once, the controller, and view classes as part of the MVC (Model-View-Controller) architecture are created.
---

## Contributors
- **Louis** 
- **Andrew** 
- **Rami**
- **Liam** 

## Additional Information

For more information about the contributions of the project, refer to the [Contributions File](contributions.md).

For a more detailed explanation for specific design decisions, open the [Documentation File](documentation.md). 