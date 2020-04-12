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
    val rand = new util.Random(12)
    deck = rand.shuffle(deck)
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
    if (cards.length != 0) {
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
    p1.playCard(playerCard)
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
    println("" + maxCards._1.name + " is given 1 point for max cards")
    maxCards._1.addPoints(1)
    
    val maxSpades = players.zip(players.map(_.capturedCards.filter(_.suit =="s").length)).maxBy(_._2)
    println("" + maxSpades._1.name + " is given 1 point for max cards")
    maxSpades._1.addPoints(1)
  
    val aces = players.zip(players.map(_.capturedCards.filter(_.value == 1).length))
    println(aces)
    aces.foreach(n => n._1.addPoints(n._2))
    
    val d10 = players.zip(players.map(_.capturedCards.filter(_.name == "d10").isEmpty)).filter(_._2 == false)
    println(d10)
    if(!d10.isEmpty) d10(0)._1.addPoints(2)
    
    val s2 = players.zip(players.map(_.capturedCards.filter(_.name == "s2").isEmpty)).filter(_._2 == false)
    println(s2)
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
//  for (i <- 0 until players.length){
//    
//    val player = players(i)
//     
//      println("\n\nPlayer " + (i+1) + ": " + player.name + "'s chance ") 
//      showCards
//      showHand(player)
//      var input = readLine("\nChoose your move: <capture/trail>: ")
//      input match {
//        case "capture" => var playerCard = readLine("Card to capture with : ")
//                          var playerCombo = readLine("Combo to capture : ").split(":")
//                          val pCard  = player.cardsInHand.filter(_.name == playerCard)(0)
//                          var combo = Vector[Card]()
//                          for (i <- playerCombo) combo = combo ++ this.cardsOnTable.filter(_.name == i)
//                          if(this.checkCapture(player, pCard, combo)){
//                            println("Move initiated")
//                            this.executeCapture(player, pCard, combo)
//                          }
//                          else println("Move failed")
//          
//        case "trail" =>  var trail = readLine("Card to trail : ")
//                         this.trail(player, player.cardsInHand.filter(_.name == trail)(0))
//
//        case  other => println("No such command possible")
//      }
//      println("Player " + (i+1) + "'s deck " + player)
//      
//    }
 

  
  
  
  
  
  
  
  
  
  
  
  
  var i = -1
  while (!isWon){
////////////////////////////////////////////////////////////////////////////////////////////
  //print cards
  def showCards : Unit = {
     print("Cards on deck : " ) 
    this.cardsOnTable.foreach(n => print( "" + n.toString() + " "))
  }
  def showHand (p1: Player): Unit = {
    print("\nPlayer's hand : " )
    p1.cardsInHand.foreach(n=> print("" + n.toString() + " "))
  }
////////////////////////////////////////////////////////////////////////////////////////////  
  i += 1
  i %= 4
  val player = players(i)
     
      var moveFailed  = false    
      do{
        println("\n\nPlayer " + (i+1) + ": " + player.name + "'s chance ") 
        showCards
        showHand(player)
        
        var input = readLine("\nChoose your move: <capture/trail>: ")
        input.trim().toLowerCase() match {
          case "capture" => var playerCard = readLine("Card to capture with : ").trim().toLowerCase()
                            var playerCombo = readLine("Combo to capture : ").split(":").map(_.trim().toLowerCase())
                            //checks if card to capture with is in the hand
                            //checks if cards chosen for combo are on the table
                            if (player.cardsInHand.exists(_.name == playerCard) 
                                && playerCombo.forall(m => !this.cardsOnTable.filter(n =>n.name == m).isEmpty)){
                                
                               val pCard  = player.cardsInHand.filter(_.name == playerCard)(0)
                                var combo = Vector[Card]()
                                for (i <- playerCombo) combo = combo ++ this.cardsOnTable.filter(_.name == i)
                                    if(this.checkCapture(player, pCard, combo)){    
                                        println("Move initiated")
                                        this.executeCapture(player, pCard, combo)
                                        moveFailed = false
                                    }
                                    else {
                                      println("Move failed")
                                      moveFailed = true
                                    }
                            }
                           else {
                             println("Move failed")
                             moveFailed = true
                           }
                             
          
          case "trail" =>  var trail = readLine("Card to trail : ").trim().toLowerCase()
                           if(!player.cardsInHand.exists(_.name == trail)){
                             println("Move failed")
                             moveFailed = true
                           }
                           else {
                             this.trail(player, player.cardsInHand.filter(_.name == trail)(0))
                             moveFailed = false 
                           }
                           
                           
          case  other => println("No such command possible, move failed")
                         moveFailed = true
        }
     }while(moveFailed) 
      println("Player " + (i+1) + "'s deck " + player)
      
    //checks if round is over
    if (players.map(_.cardsInHand.length).sum == 0){
      //all existing cards on deck go to lastCapturer
      lastCapturer.getOrElse(null).capture(this.cardsOnTable)
      //update cardsOnTable
      this.cardsOnTable = Vector[Card]()
      //calculate point from the round
      calculatePoints
      //shpw points
      players.foreach(n => print("" + n.name + " " +  n.points + " "))
      deal
    }
    //check if game is over
    if (players.map(_.cardsInHand.length).sum == 0 && cards.isEmpty){
      isWon = true
      println("Winner of the game is : " + this.winner)
    }
  }
}
  
  
