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
    for (i <-  names) players = players :+ new Player(i)
  }
  
  //deals new cards
  // ##Need to check if there are insufficient number of cards.
  def deal = {
    if (cards.length == 52) {
      val initialHands = cards.take(players.length*4).grouped(4).toVector
      cards = cards.drop(players.length*4)
      val initialTableHand  = cards.take(4)
      cards = cards.drop(4)
      for (i <- 0 until initialHands.length) players(i).deal(initialHands(i))
      this.cardsOnTable = this.cardsOnTable ++ initialTableHand
    }
  }
 
  def checkMove (p1 : Player, playerCard : Card, combo : Vector[Card]) : Boolean = {
    val comboVal = combo.map(_.value).reduceLeft((n,sum) => n+sum)
    val playerVal = playerCard.specialValue.getOrElse(playerCard.value)
    (comboVal % playerVal == 0)
  }
 
  def executeMove (p1: Player, playerCard : Card, combo : Vector[Card]) ={
    //Specifically in the case of a sweep
    if (combo.length == this.cardsOnTable.length ) p1.addPoints(1)
    val combinedCombo = combo :+ playerCard
    p1.capture(combinedCombo)
    p1.playCard(playerCard)
    for (i <- 0 until combinedCombo.length ) this.cardsOnTable = this.cardsOnTable.filter(_.name != combo(i).name)
    lastCapturer = Some(p1)
  }
  
  //NOTE Have to check for behaviour of maxBy in '0' cases
  def calculatePoints = {
    
    val maxCards = players.zip(players.map(_.capturedCards.length)).maxBy(_._2)
    maxCards._1.addPoints(1)
    
    val maxSpades = players.zip(players.map(_.capturedCards.filter(_.suit =="s").length)).maxBy(_._2)
    maxSpades._1.addPoints(1)
    
    val aces = players.zip(players.map(_.capturedCards.filter(_.value == 1).length))
    aces.foreach(n => n._1.addPoints(n._2))
    
    val d10 = players.zip(players.map(_.capturedCards.filter(_.name == "d10").isEmpty)).filter(_._2 == false)
    if(!d10.isEmpty) d10(0)._1.addPoints(2)
    
    val s2 = players.zip(players.map(_.capturedCards.filter(_.name == "s2").isEmpty)).filter(_._2 == false)
    if(!s2.isEmpty) s2(0)._1.addPoints(1)
    
  }
  
  def winner = players.zip(this.players.map(_.points)).maxBy(_._2)._1
  
  override def toString() = {
    var ans = ""
    for(i <- 0 until players.length) ans = ans + players(i).toString() + "\n"
    ans + "Cards on table " + this.cardsOnTable.map(_.toString()) + "\n" + "Cards remaining on table " + this.cards.length + "\n" 
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
//  shuffle
//  deal
//  for (i <- 0 until players.length) println(players(i))
//  print(this)
//  while (!isWon){
//    
//  }
  
  
}