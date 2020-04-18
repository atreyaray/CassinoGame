package cassino_game


import scala.util.Random._
import scala.io._
object Game {//extends App {

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
 
 //initializes a new game
 def newGame (comp : Boolean, playerNames : Vector[String]) = {
    //state variables refreshed
    players       = Vector[Player]()
    cards         = Vector[Card]()
    cardsOnTable  = Vector[Card]()
    lastCapturer  = None
    isWon = false
    //add players
    if (comp) this.addPlayers(("Comp" +: playerNames))
    else this.addPlayers(playerNames)
    shuffle 
    deal
    println(this)
  }
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
  
//  def nextPlayer(p1 : Player) = {
//    val idx = this.players.indexOf(p1)
//    players((idx+1)%players.length)
//  }
  
  //deals cards initially
  def deal = {
    if (cards.length == 52) {
      val initialHands = cards.take(players.length*4).grouped(4).toVector
      cards = cards.drop(players.length*4)
       for (i <- 0 until initialHands.length) players(i).deal(initialHands(i))
       dealTable
    }
  }
  
  //deals one card to a given player
  def dealOne (p1: Player) = {
    if(this.cards.length != 0){  
      val hand = cards.take(1)
      cards = cards.tail
      p1.deal(hand)
   }
  }
 
  //deals cards on table 
  def dealTable = {
     val initialTableHand  = cards.take(4)
     cards = cards.drop(4)
     this.cardsOnTable = this.cardsOnTable ++ initialTableHand
  }
  
  //checks if move is possible
  def checkCapture (p1 : Player, playerCard : Card, combo : Vector[Card]) : Boolean = {
//    val comboVal = combo.map(_.value).reduceLeft((n,sum) => n+sum)
//    val playerVal = playerCard.specialValue.getOrElse(playerCard.value)
//    (comboVal % playerVal == 0) && (combo.forall(_.value <= playerCard.specialValue.getOrElse(playerCard.value) ))
    
    //return value
    var ans = false
    //gives special value if card is special, value otherwise
    val pcVal = playerCard.specialValue.getOrElse(playerCard.value)
    //filters single cards with same value as playerCard
    val filteredCombo = combo.filter(_.value != pcVal)
    //checks if a 4 card combo is left and if it's sum = pcVal
    if(filteredCombo.length ==4 && filteredCombo.map(_.value).sum == pcVal) ans = true
    //checks if a 3 card combo is left and if it's sum = pcVal
    else if(filteredCombo.length == 3 && filteredCombo.map(_.value).sum == pcVal) ans = true
    //checks if a 2 card combo is left and if it's sum = pcVal
    else if(filteredCombo.length == 2 && filteredCombo.map(_.value).sum == pcVal) ans = true
    //checks if a 2 card combo is left and if it's sum = pcVal
    else if(filteredCombo.length == 0 ) ans = true
    //checks if there is pair of groups with length 2 with sum = pcVal
    else if ( filteredCombo.length == 4){
      //all possible groups with length 2 which also have sum = pcVal
      import scala.collection.mutable.Buffer
      var choicesBuf = Buffer[Set[Card]]()
      filteredCombo.combinations(2).filter(n => n.map(_.value).sum == pcVal).map(_.toSet).copyToBuffer(choicesBuf)
      
      for (i <- 0 until choicesBuf.length - 1){
        for (j <- i+1 until choicesBuf.length){
          //checking every combination of 2 member groups if they have distinct elements
          if ( (choicesBuf(i)&choicesBuf(j)).size == 0 ){
            println("Choice 1 " + choicesBuf(i))
            println("Choice 2 " + choicesBuf(j))
            ans = true
          }
        }
      }
      
    }
    ans
  }
 
  //If the move is possible, this method executes the move
  def executeCapture (p1: Player, playerCard : Card, combo : Vector[Card]) ={
    //Specifically in the case of a sweep
    if (combo.length == this.cardsOnTable.length && this.cardsOnTable.length != 1 ) p1.addPoints(1)
    val combinedCombo = combo :+ playerCard
    p1.capture(combinedCombo)
    p1.playCard(playerCard)
    for (i <- 0 until combo.length ) this.cardsOnTable = this.cardsOnTable.filter(_.name != combo(i).name)
    lastCapturer = Some(p1)
  }
  
  //trailing a card
  def trail (p1: Player, playerCard: Card) = {
    p1.playCard(playerCard)
    this.cardsOnTable = this.cardsOnTable :+ playerCard
  }
  
  //computer generated moves
  def optimalMove(p1: Player)={
    
    //combinations of possible combos to capture
    var combinations = Iterator[Vector[Card]]()
    for (i <- 1 to cardsOnTable.length)  combinations = combinations ++ this.cardsOnTable.combinations(i)
    
     var bestChoice : (Int, Option[Vector[Card]])= (0, None)
    
    //check all the combinations 
    while(combinations.hasNext){
      //pick one of the possible choices
      val choice = combinations.next()
      //initalize score 
      var score = 0
      //iterate through all player cards
      for (i <- 0 until p1.cardsInHand.length){
        
        //check if move is possible with this combination and this player cards
        if(checkCapture(p1, p1.cardsInHand(i), choice)){
          //check d10
          if ( (choice :+ p1.cardsInHand(i)).exists(_.name == "d10") ) score += 2
          //check sweep
          if (choice.length == this.cardsOnTable.length) score += 1
          //check ace
          if ( (choice :+ p1.cardsInHand(i)).exists(_.value == 1) ) score +=  (choice :+ p1.cardsInHand(i)).filter(_.value == 1).length
          //check s2
          if ( (choice :+ p1.cardsInHand(i)).exists(_.name == "s2") ) score += 1
        
          //check score against bestChoice
          if(bestChoice._2.isDefined && score > bestChoice._1)
            bestChoice = (score, Some(choice :+ p1.cardsInHand(i)))
          //else if there is no scoring choice but there is no other choice yet
          else if (bestChoice._2.isEmpty) bestChoice = (0,Some(choice :+ p1.cardsInHand(i)))
          //else if there is no scoring choice but is better than existing non scoring choice
          //but IGNORE if existing score is better than current score
          else {
            if (bestChoice._1 == 0 && choice.length +1 > bestChoice._2.size ) bestChoice = (0,Some(choice :+ p1.cardsInHand(i)))
          }
        }
        //if the move is not possible then move on
      } 
   }
      println("Best Choice is : " + bestChoice)
      //if there is a bestChoice then execute it else trail
      if (bestChoice._2.isDefined) this.executeCapture(p1, bestChoice._2.get.last , bestChoice._2.get.dropRight(1))
      else{
        //iterate through all the cards of the player
        var minVal = 0
        var min : Option[Card] = None
        for (i <- 0 until p1.cardsInHand.length){
          //choose the lowest valued card (ie. choose special values, if it is a normal card then choose normal value instead)
          val value = p1.cardsInHand(i).specialValue.getOrElse(p1.cardsInHand(i).value)
          if (minVal > value) {
            min = Some(p1.cardsInHand(i))
            minVal = value
          }
        }
        try{this.trail(p1, min.get)}
        catch{case e : NoSuchElementException => " error thrown"
        case other => println(other)}
    }
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
    
    //capturedCards have to be refreshed for next round
    players.foreach(_.capturedCards = Vector[Card]())
    
  }
  
  def winner = players.zip(this.players.map(_.points)).maxBy(_._2)._1.name
  
  override def toString() = {
    var ans = ""
    for(i <- 0 until players.length) ans = ans + players(i).toString() + "\n"
    ans + "Cards on table " + this.cardsOnTable.map(_.toString()) + "\n" + "Cards remaining on table " + this.cards.length + "\n" 
  }
  
  
  
  
  
  
  
  
  
  
//  //SETUP
//  var playerNames = Vector[String]()
//  var comp = readLine("Do you want computer opponents? ").trim().toLowerCase()
//  if (comp == "yes"){ playerNames = playerNames :+ "Comp"}
//  var n = readLine("How many players ?").toInt
//  for (i <- 0 until n){
//    playerNames = playerNames :+ readLine("Player " + (i+1) + ": ")
//  }
//  //Players added
//  //Vector("Atreya","Long","Aayush","Sergey")
//  this.addPlayers(playerNames)
//  //deck shuffled
//  shuffle
//  //cards dealt
//  deal
//  print(this)
//    
//  var i = -1
//  while (!isWon){
//////////////////////////////////////////////////////////////////////////////////////////////
//  //print cards
//    def showCards : Unit = {
//       print("Cards on deck : " ) 
//      this.cardsOnTable.foreach(n => print( "" + n.toString() + " "))
//    }
//    def showHand (p1: Player): Unit = {
//      print("\nPlayer's hand : " )
//      p1.cardsInHand.foreach(n=> print("" + n.toString() + " "))
//    }
//////////////////////////////////////////////////////////////////////////////////////////////  
//    i += 1
//    i %= 4
//    val player = players(i)
//     
//        var moveFailed  = false   
//        if (player.compPlayer){ this.optimalMove(player)}
//    else{  
//          do{
//            println("\n\nPlayer " + (i+1) + ": " + player.name + "'s chance ") 
//            showCards
//            showHand(player)
//        
//            var input = readLine("\nChoose your move: <capture/trail>: ")
//            input.trim().toLowerCase() match {
//              case "capture" => var playerCard = readLine("Card to capture with : ").trim().toLowerCase()
//                                var playerCombo = readLine("Combo to capture : ").split(":").map(_.trim().toLowerCase())
//                                //checks if card to capture with is in the hand
//                                //checks if cards chosen for combo are on the table
//                                if (player.cardsInHand.exists(_.name == playerCard) 
//                                    && playerCombo.forall(m => !this.cardsOnTable.filter(n =>n.name == m).isEmpty)){
//                                    //selecting card object of choice                      
//                                   val pCard  = player.cardsInHand.filter(_.name == playerCard)(0)
//                                    var combo = Vector[Card]()
//                                    for (i <- playerCombo) combo = combo ++ this.cardsOnTable.filter(_.name == i)
//                                        if(this.checkCapture(player, pCard, combo)){    
//                                            println("Move initiated")
//                                            this.executeCapture(player, pCard, combo)
//                                            moveFailed = false
//                                            dealOne(player)
//                                        }
//                                        else {
//                                          println("Move failed after")
//                                          moveFailed = true
//                                        }
//                                }
//                                else {
//                                 println("Move failed before")
//                                 moveFailed = true
//                                }
//                             
//          
//              case "trail" =>  var trail = readLine("Card to trail : ").trim().toLowerCase()
//                               if(!player.cardsInHand.exists(_.name == trail)){
//                                 println("Move failed")
//                                 moveFailed = true
//                               }
//                               else {
//                                 this.trail(player, player.cardsInHand.filter(_.name == trail)(0))
//                                 moveFailed = false 
//                                 dealOne(player)
//                               }
//                           
//                           
//              case  other => println("No such command possible, move failed")
//                             moveFailed = true
//            }
//         }while(moveFailed)
//      }   
//      println("Player " + (i+1) + "'s deck " + player)
//      println("Cards Left : " + cards.length)
//      
//      val fh = new FileHandler
//      fh.saveGame("testIO_1.txt")
//    //checks if round is over
//    if (players.map(_.cardsInHand.length).sum == 0){
//      //all existing cards on deck go to lastCapturer
//      lastCapturer.getOrElse(null).capture(this.cardsOnTable)
//      //update cardsOnTable
//      this.cardsOnTable = Vector[Card]()
//      //calculate point from the round
//      calculatePoints
//      //show points
//      players.foreach(n => print("" + n.name + " " +  n.points + " "))
//    }
//    //check if game is over
//    if (players.map(_.cardsInHand.length).sum == 0 && cards.isEmpty){
//      isWon = true
//      println("Winner of the game is : " + this.winner)
//    }
//  }
}
  
  
