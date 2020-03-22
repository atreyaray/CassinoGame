package cassino_game

import scala.util.Random._

object Game extends App {

  val fileHandler = new FileHandler
  var players = Vector[Player]()
  var cards = Vector[Card]()
  var cardsOnTable = Vector[Card]()
  var lastCapturer : Option[Player] = None
  var isWon = false
  
 var deck = Vector("ha","h2","h3","h4","h5","h6","h7","h8","h9","h10","hj","hq","hk") ++
            Vector("da","d2","d3","d4","d5","d6","d7","d8","d9","d10","dj","dq","dk") ++
            Vector("sa","s2","s3","s4","s5","s6","s7","s8","s9","s10","sj","sq","sk") ++
            Vector("ca","c2","c3","c4","c5","c6","c7","c8","c9","c10","cj","cq","ck") 
 
 // Creates shuffled deck (Vector) of Cards class          
 def shuffle = {
    deck = util.Random.shuffle(deck)
    for (i <- 0 until deck.length) cards = cards :+ new Card(deck(i))
  }
  
  // Adds new players 
  // ##Need to check if  no. of players is acceptable
  def addPlayers (names : Vector[String]) = {
    for (i <- 0 until names.length) players = players :+ new Player(names(i))
  }
  
  //deals new cards
  // ##Need to check if there are insufficient number of cards.
  def deal = {
    if (cards.length == 52) {
      val initialHands = cards.take(players.length*4).grouped(4).toVector
      cards = cards.drop(players.length*4)
      for (i <- 0 until initialHands.length) players(i).deal(initialHands(i))
    }
  }
 
  def checkMove (p1 : Player, playerCard : Card, combo : Vector[Card]) : Boolean = {
    val comboVal = combo.map(_.value).reduceLeft((n,sum) => n+sum)
    val playerVal = playerCard.specialValue.getOrElse(playerCard.value)
    (comboVal % playerVal == 0)
  }
 
  def executeMove (p1: Player, playerCard : Card, combo : Vector[Card]) ={
    val combinedCombo = combo :+ playerCard
    p1.capture(combinedCombo)
    p1.playCard(playerCard)
    for (i <- 0 until combinedCombo.length ) this.cardsOnTable = this.cardsOnTable.filter(_.name != combo(i).name)
    lastCapturer = Some(p1)
  }
  
  
// var count = 0
// for (i<- 0 until 52) {
//   shuffle
//   println(deck.distinct.length)
//   if ((i+1) % 13 == 0) println(" ")
//   print(cards(i) + " ")
//   count += 1
// }
  
  
//  this.addPlayers(Vector("Atreya","Long","Aayush","Sergey","Dean"))
//  for (i <- 0 until players.length) println(players(i))
  
  
}