package test

import org.scalatest._
import cassino_game._

class checkCaptureTest extends FlatSpec{
  "A possible 2 member move with 1 card on the table " should "be approved" in {
    val boardCards = Vector("s5").map(new Card(_))
    val pc = new Card("d5")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  "A possible 2 member move with 3 card on the table " should "be approved" in {
    val boardCards = Vector("s5","d7","hj","sa").map(new Card(_))
    val pc = new Card("d5")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "A possible 2 member move with 4 cards on the table " should "be approved" in {
    val boardCards = Vector("s8","d9","c4","s3").map(new Card(_))
    val pc = new Card("h4")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "A possible 2 member move" should "be approved " in {
    val boardCards = Vector("s4","d6").map(new Card(_))
    val pc = new Card("h10")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "A possible 3 member move" should "be approved " in {
    val boardCards = Vector("s4","d4","c2").map(new Card(_))
    val pc = new Card("h10")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "A possible 2 member move twice " should "be approved" in {
    val boardCards = Vector("s3","d2","c4","sa").map(new Card(_))
    val pc = new Card("h5")
    
    //assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "An impossible 2 member move twice " should "not be approved" in {
    val boardCards = Vector("s3","h3","c4","sa").map(new Card(_))
    val pc = new Card("h5")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == false)
  }
  
  "A special card move twice " should "be approved" in {
    val boardCards = Vector("sj","h5").map(new Card(_))
    val pc = new Card("d10")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
  
  "A 2-1-1 comb move " should "be approved" in {
    val boardCards = Vector("s10","h3","d3").map(new Card(_))
    val pc = new Card("d10")
    assert(Game.checkCapture(new Player("Atreya"), pc, boardCards) == true)
  }
}