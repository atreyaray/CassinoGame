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
 
  def checkCapture (p1 : Player, playerCard : Card, combo : Vector[Card]) : Boolean = {
    val comboVal = combo.map(_.value).reduceLeft((n,sum) => n+sum)
    val playerVal = playerCard.specialValue.getOrElse(playerCard.value)
    (comboVal % playerVal == 0)
  }
 
  def executeCapture (p1: Player, playerCard : Card, combo : Vector[Card]) ={
    //Specifically in the case of a sweep
    if (combo.length == this.cardsOnTable.length ) p1.addPoints(1)
    val combinedCombo = combo :+ playerCard
    p1.capture(combinedCombo)
         println("checkPoint1")
    p1.playCard(playerCard)
            println("checkPoint2")
    for (i <- 0 until combo.length ) this.cardsOnTable = this.cardsOnTable.filter(_.name != combo(i).name)
    lastCapturer = Some(p1)
  }
  
  def trail (p1: Player, playerCard: Card) = {
    p1.playCard(playerCard)
    this.cardsOnTable = this.cardsOnTable :+ playerCard
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
  
  
  
  
  
  
  
import scala.io._  
  //SETUP
  //Players added
  this.addPlayers(Vector("Atreya","Long","Aayush","Sergey"))
  //deck shuffled
  shuffle
  //cards dealt
  deal
  print(this)

  //Show cards on the table
  def showCards : Unit = {
     print("Cards on deck : " ) 
    this.cardsOnTable.foreach(n => print( "" + n.toString() + " "))
  }
  //Show individual hands
  def showHand (p1: Player): Unit = {
    print("\nPlayer's hand : " )
    p1.cardsInHand.foreach(n=> print("" + n.toString() + " "))
  }
  
  //turns
  for (i <- 0 until players.length){
    
    val player = players(i)
     
      println("\n\nPlayer " + (i+1) + ": " + player.name + "'s chance ") 
      showCards
      showHand(player)
      var input = readLine("\nChoose your move: <capture/trail>: ")
      input match {
        case "capture" => var playerCard = readLine("Card to capture with : ")
                          var playerCombo = readLine("Combo to capture : ").split(":")
                          val pCard  = player.cardsInHand.filter(_.name == playerCard)(0)
                          var combo = Vector[Card]()
                          for (i <- playerCombo) combo = combo ++ this.cardsOnTable.filter(_.name == i)
                          if(this.checkCapture(player, pCard, combo)){
                            println("Move initiated")
                            this.executeCapture(player, pCard, combo)
                          }
                          else println("Move failed")
          
        case "trail" =>  var trail = readLine("Card to trail :")
                         this.trail(player, player.cardsInHand.filter(_.name == trail)(0))

        case  other => println("No such command possible")
      }
//      if (inputL(i) == "trail") this.trail(players(i), players(i).cardsInHand.filter(_.name == inputL(1))(0))
//      else {
//        val playerCard =  players(i).cardsInHand.filter(inputL(1) == _.name)(0)
//        inputL = inputL(2).split(",")
//        val combo = inputL.map(n => this.cardsOnTable.filter(n == _.name)(0)).toVector
//        if(this.checkCapture(players(i), playerCard, combo)) this.executeCapture(players(i), playerCard, combo) 
//      }
      println("Player " + (i+1) + "'s deck " + player)
      
    }
 

  
  
  
  
  
  
  
  
  
  
  
  
  
//  while (!isWon){
//    
//    //print cards
//  def showCards : Unit = {
//     print("Cards on deck : " ) 
//    this.cardsOnTable.foreach(n => print( "" + n.toString() + " "))
//  }
//  def showHand (p1: Player): Unit = {
//    print("\nPlayer's hand : " )
//    p1.cardsInHand.foreach(n=> print("" + n.toString() + " "))
//  }
//    //executes moves
//  for (i <- 0 until players.length){
//      println("\n\nPlayer " + (i+1) + ": " + players(i).name + "'s chance ") 
//      showCards
//      showHand(players(i))
//    }
//      
//    //checks if round is over
//    
//    //check if game is over
//    if (this.cardsOnTable.length == 0){
//      isWon = true
//      println("Winner of the game is : " + this.winner)
//    }
//  }
  
  
}