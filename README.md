# Scrabble Game

## Contributors
- **Louis Pantazopoulos**
- **Andrew Tawfik**
- **Rami Ayoub**
- **Liam Bennett**


## Overview
This is a graphical implementation of Scrabble, where 2-4 players compete in a game of word-building. The game enforces standard Scrabble rules, managing player turns, tile placement, and scoring while maintaining a user-friendly interface.

---

## How to Play
1. **Game Setup:**
   - Follow the prompts to set up players (human or AI) and choose the number of participants.
   - The game supports 2-4 players, with AI players taking turns just like human players.

2. **During a Turn:**
   - The current state of the board is displayed.
   - Players can:
      - **Place Tiles:** Form a word by placing tiles on the board according to Scrabble rules.
      - **Swap Tiles:** Exchange unwanted tiles for new ones from the tile bag.
      - **Pass Turn:** Skip their turn if they cannot or choose not to make a move.

3. **End of Game:**
   - The game concludes when:
      - The tile bag is empty, and no more tiles can be placed.
      - All players pass their turns consecutively.
   - Scores are tallied, and the player with the highest score is declared the winner.
   - The winners name is displayed with a flashing animation, along with a gif and an applause sound in the background.

For more detailed instructions, refer to the [User Manual](user-manual.md).

---

## Roadmap (Future Enhancements)
- **AIPlayer Difficulty Levels:**
   - **Easy:** Chooses the lowest-value word from its list of valid plays.
   - **Medium:** Selects a random word from its list of valid plays.
   - **Hard:** Selects the highest-value word from its list of valid plays.

---

## Bonus Milestone Chosen
- **Winner Celebration:**
   - A celebrator message to the player is shown in a pane where the players name is animated as a flashing animation.
   - A trophy gif can also be seen in the pane along with the flashing animation for the players name.
   - Additionally, a applause sound is added in the background to congratulate the winner.  

---

## Additional Information
- For details on team contributions, see the [Contributions File](contributions.md).
- For technical design decisions, see the [Documentation File](documentation.md).
