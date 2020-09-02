# CassinoGame
(excerpts from the documentation file)

Cassino, a fishing card game, played by drawing cards and matching them with those on the deck with similar value or symbols. Made with Scala, using graphics from ScalaFX and tested with ScalaTest.

### General Description :
Cassino
The goal of the project was to make the Cassino (deck-cassino) card game with a fully functioning graphical user interface. The rules are as follows:
- The deck is shuffled at the beginning of every round and the dealer deals 4 cards to every player (they are not visible to other players) and 4 cards on the table (visible for everyone). The rest of the cards are left on the table upside down. The player next to the dealer starts the game. In the next round, he/she is the dealer.
- On their turn, A player can play out one of his/her cards: it can be used either for taking cards from the table or to just put it on the table. If the player cannot take anything from the table, he/she must put one of his/her cards on the table. If the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended. A player must draw a new card from the deck after using a card so that he/she always has 4 cards in his/her hand. (When the deck runs out, everyone plays until there are no cards left in any player’s hand).
- A player can use a card to take one or more cards of the same value and cards such that their summed value is equal to the used card.
- The number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.
- Sweep: If some player gets all the cards from the table at the same time, he/she gets a ‘sweep’ which is written down.
- Special Cards: There are a couple of cards that are more valuable in the hand than in the table. (Aces: 14 in hand, 1 on the table, Diamonds-10: 16 in hand, 10 on the table, Spades-2: 15 in hand, 2 on the table)
Scores are calculated when every player runs out of cards and the last player to take cards from the table takes the remaining cards from the table.
- Every sweep grants 1 point.
- Every Ace grants 1 point.
- The player with most cards gets 2 points.
- The player with the most spades gets 1 point.
- The player with Diamonds-10 gets 2 points.
- The player with Spades-2 gets 1 point.
At any point in time, the game state can be saved onto a file and also retrieved from a file using a custom file format.


### User Interface ​:
The program can be run through a graphical user interface (GUI) which can be run through the GameGUI.scala file. Upon running, the GUI presents the first page where the user may choose a computer opponent and the number of human opponents.

<img src="https://github.com/atreyaray/CassinoGame/blob/master/Documentation/images/FrontPage_2.png"  width="300"/>

After selecting whether or not to have a computer opponent with the help of the toggle button and choosing the number of human opponents with a drop-down list, when the “OK” button is pressed, multiple text fields appear to allow inputs for the name of players.
 
<img src="https://github.com/atreyaray/CassinoGame/blob/master/Documentation/images/FrontPage_3.png"  width="300"/> 
 
The names of all the opponents can then be entered and then the “Next” button may be pressed. After this, a new page appears which contains the game window.

 <img src="https://github.com/atreyaray/CassinoGame/blob/master/Documentation/images/GamePlay_1.png"  width="300"/> 
 
 The game window is where the entirety of the game runs. There are “Capture” and “Trail” buttons on the bottom right which may be clicked to execute those respective moves. The player’s icon, name and hand are displayed on the bottom. In the centre, the cards on the table, visible to all the players, are shown. The right side contains the Score panel where the score of each player is given besides their corresponding icon. On the right of the panel, there is a text box which gives some helpful tips about the gameplay.
During the computer’s turn, the GUI displays the move that the computer opponent makes. This involves having one card from the hand being visible, along with the selection to capture from the table (if any). The user may click anywhere on the window to proceed to the next turn.
For a regular player’s turn, the player may click on any card from their own hand along with one or more cards from those available on the table to execute a capture. After selecting the required collection of cards, the “Capture” button must be clicked. An invalid capture move is not allowed and prompts a suggestion on the text box above
 
  On the other hand, a “Trail” move is always valid and can be executed by the player by first selecting the card in their hand that they want to trail and subsequently pressing the Trail button. Upon the successful execution of a “Capture” or “Trail” move, a new window pops up, informing the players of the change of turn, thereby allowing privacy and hiding the hand of individual players.
 
  The instructions page can be accessed through a drop-down in the menu bar. Clicking on it takes the user to a separate page.
The instructions are given in text format and the user may scroll down to read the entirety of the text. After reading, the instructions, clicking on the “Got It” button will take the user back.
 
 The data of a particular game may be saved in a .txt file and the same file may be loaded at a later time to allow continuity of the game. The “Open” and “Save” buttons inside the “Settings” dropdown open FileChoosers which allow users to do the same.
Apart from that, the “New Game” option in the menu bar creates a new game whenever clicked.
