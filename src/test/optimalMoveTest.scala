package test

import org.scalatest._
import cassino_game._

class optimalMoveTest extends FlatSpec{
 
  "A simple capture " should "be allowed" in {

    val player = new Player("Computer")
    println("Hello again")
    player.cardsInHand = Vector("sj","c9", "h5", "d7").map(new Card(_))

    Game.players = Game.players :+ player 
    Game.cardsOnTable = Vector("s4","h8","s7","c3").map(new Card(_)) 
    
    val (ansCard, ansCombo) = Game.optimalMove(player)
    assert(ansCard.name == "sj", ansCard.name)
    assert(ansCombo.get.map(_.name) == Vector("s4","h8","s7","c3"),true )
  }
  
  
    "A simple trail " should "be allowed" in {

    val player = new Player("Computer")
    player.cardsInHand = Vector("c10", "h5").map(new Card(_))

    Game.players = Game.players :+ player 
    Game.cardsOnTable = Vector("s7").map(new Card(_)) 
    
    val (ansCard, ansCombo) = Game.optimalMove(player)
    assert(ansCard.name == "h5", ansCard.name)
    assert(ansCombo.isEmpty ,true )
  }
    
}