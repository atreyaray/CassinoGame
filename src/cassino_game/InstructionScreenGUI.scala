package cassino_game

import scala.swing._
import java.awt.{Font,Color}

object InstructionScreenGUI extends BoxPanel(Orientation.Vertical){
 
   val instructionButton = new Button("Got it"){
    this.horizontalAlignment = Alignment.Center
     background = Color.BLUE
     this.foreground = Color.BLACK
  }
    contents += new FlowPanel{   
                      contents+= new Label("Instructions Page"){
                                      font = new Font("Roman",Font.BOLD,48)
                                      opaque = true
                                      foreground = new Color(240,230,140)
                                      background = new Color(0,100,0)
                                  }
                      background = new Color(0,100,0)
                }
    contents += new ScrollPane(){
                   contents = new TextArea("\n\nThe deck is shuffled in the beginning of every round and the dealer deals 4 cards to every player" 
         +" (they are not visible to other players) and 4 cards on the table (visible for everyone). The rest of the cards are left on the table upside down."  
         + "The player next to the dealer starts the game. On the next round he/she is the dealer.\n\n\n"
         + "What every player does on their turn: \n\n"
         + "1. A player can play out one of his/her cards: it can be used either for taking cards from the table or to just putting it on the table. If the player cannot take anything from the table, he/she must put one of his/her cards on the table.\n\n"
         + "2. If the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended.\n\n"
         + "3. The number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.\n\n"
         + "4. Player must draw a new card from the deck after using a card so that he/she has always 4 cards in his/her hand. (When the deck runs out, everyone plays until there are no cards left in any player’s hand).\n\n"
         + "5. Player can use a card to take one or more cards of the same value and cards such that their summed value is equal to the used card.\n\n\n"
         + "Sweep : If some player gets all the cards (more than one) from the table at the same time, he/she gets a so called sweep which is written down.\n\n\n"
         + "Special Cards : There are a couple of cards that are more valuable in the hand than in the table,\n"
         + "\tAces: 14 in hand, 1 on table \n"  
         + "\tDiamonds-10: 16 in hand, 10 on table\n"
         + "\tSpades-2: 15 in hand, 2 on table\n\n\n"
         + "Scoring: When every player runs out of cards, the last player to take cards from the table gets the rest of the cards from the \n"
         + "              table. After this the points are calculated and added to the existing scores.\n"
         + "              The following things grant points:\n"
         + "\tEvery sweep grants 1 point.\n"
         + "\tEvery Ace grants 1 point.\n"
         + "\tThe player with most cards gets 1 point.\n"
         + "\tThe player with most spades gets 1 points.\n"
         + "\tThe player with Diamonds-10 gets 2 points.\n"
         + "\tThe player with Spades-2 gets 1 point.\n"
         ){
                                    minimumSize = new Dimension(900,500) 
                                    charWrap = true
                                    wordWrap = true
                                    lineWrap = true
                                    editable = false
                                    font = new Font("Serif",Font.BOLD,18)
                                    background = new Color(0,100,0)
                                    foreground  = new Color(230,230,250)
                                }
                   background = new Color(0,100,0)
                }
    contents += new FlowPanel{
                      contents += instructionButton
                      background = new Color(0,100,0)
                }
    background = new Color(0,100,0)
    visible = false 
}