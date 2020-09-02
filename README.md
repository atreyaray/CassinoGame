# CassinoGame
(excerpts from the documentation file)

Cassino, a fishing card game, played by drawing cards and matching them with those on the deck with similar value or symbols. Made with Scala, using graphics from ScalaFX and tested with ScalaTest.

General Description :
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
