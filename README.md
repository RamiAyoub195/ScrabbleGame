# Scrabble Game

## Overview
This is a text-based version of Scrabble that allows 2-4 players to compete in a game of word-building. The game enforces standard Scrabble rules while managing player turns, tile placement, and scoring.

## How to Play
- Start the game by following the instructions for player setup.
- During each player's turn, the board will be displayed, and the player can choose to place tiles, swap tiles, or skip their turn.
- The game continues until the tile bag is empty and no more tiles can be placed by either player.

For detailed instructions on how to play, see the [User Manual](user-manual.md).

## Known Issues

1. **Incorrect Score Calculation**  
   The scoring system does not correctly calculate the total score when adding tiles to an existing word. Score system has a bug in it. 
2. **Recognition of Multiple Words**  
   The game does not correctly recognize or score two new words formed in a single turn if they intersect or are placed adjacent to each other.

## Roadmap (Solutions to Known Issues)

1. **Improved Score Handling**  
   Update score calculation to account for both new and existing tiles in each word, ensuring that all valid word scores are added correctly.

2. **Multiple Word Recognition**  
   Implement logic to detect and score multiple words formed in a single turn, even when they are adjacent or intersecting.

---

## Contributors
- **Louis**
- **Andrew**
- **Rami**
- **Liam**

## Additional Information

For more information about project contributions, refer to the [Contributions File](contributions.md).

For specific design decisions, refer to the [Documentation File](documentation.md).
