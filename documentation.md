# Documentation
## 1. Cell Class for Board Representation

### Design Decision:
The introduction of the `Cell` class replaces `String` to represent individual cells on the board. This choice not only simplifies board traversal but also future-proofs the design, allowing for the easy addition of special cell types (e.g., double or triple word score cells) without requiring major structural changes.

### Benefits:

- Provides a flexible and modular board structure.
- Enhances code readability and maintainability, as each cell is a distinct object.
- Supports future upgrades, especially with the inclusion of special cells to diversify gameplay.

### Trade-off:
Using a `Cell` class increases the complexity of board management slightly, but the benefits of scalability and modularity justify this choice, especially as the game evolves.

## 2. turnScore Function for Scoring Adjustments

### Design Decision:
The `turnScore` function calculates the score for each turn by not only accounting for new words formed but also incorporating extensions to existing words. This ensures accurate scoring for each play, especially when placed tiles augment pre-existing words.

### Benefits:

- Enables accurate scoring by accounting for both new and extended words.
- Maintains fairness by preventing point discrepancies, especially in complex turns involving multiple words.
- Keeps gameplay true to Scrabble’s scoring standards, rewarding strategic placements.

### Trade-off:
The logic required to detect and score both new and extended words adds computational overhead. However, this complexity is essential for providing an accurate and fair scoring system.

## 3. Blocked Tiles for Enhanced Game Flow

### Design Decision:
When a tile is placed on the board, it becomes blocked within the `view` class, preventing the user from altering it and disrupting the game's progress.

### Benefits:

- Ensures game flow is maintained by disallowing changes to previously played tiles.
- Prevents players from accidentally or intentionally altering completed moves.
- Simplifies board state management by maintaining a clear distinction between placed and available tiles.

### Trade-off:
Blocking tiles introduces additional checks to confirm placement status. However, this feature is crucial for protecting the integrity of the game and ensuring consistent gameplay.
