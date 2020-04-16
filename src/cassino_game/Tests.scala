package cassino_game

object Tests extends App{
  val fh = new FileHandler
  val blocks = fh.readFile("testIO_2.txt")
   for(i <- blocks){
    for(j<-i) println(j)
    println()
  } 
    //blocks of data containing metadata and player data
    var metadata = Array[String]()
    var playerdata = Array[Array[String]]()
    
    //parsing file to determine metadata and player data
    for (currentBlock <- blocks){
      val x = currentBlock
      currentBlock(0) match{
        case "" => 
        case "METADATA" => metadata = currentBlock
        case "PLR " => playerdata = playerdata :+ currentBlock 
        case other => 
      }
    }
    

    //
//    for(i <- metadata) {
//      val x= i.split(" ").toArray
//      if ( x(0) == "CARDSINDECK") {
//        val temp = x(1).grouped(2).toArray
//        temp.foreach(println)
//      }
//     
//    }
    
    //initalizing metadata
    for (i <- metadata){
        val metaBlocks = i.split(" ").toArray
      metaBlocks(0) match{
        case "CARDSONTABLE" =>  Game.cardsOnTable = Vector[Card]()
                               if(metaBlocks.length > 1 ) metaBlocks(1).split(":").toArray.foreach(n => Game.cardsOnTable = Game.cardsOnTable :+ new Card(n) )
        case "CARDSINDECK" => Game.cards = Vector[Card]()
                               if(metaBlocks.length > 1 )  metaBlocks(1).split(":").toArray.foreach(n => Game.cards= Game.cards :+ new Card(n) )
        case "LASTCAPTURER" => Game.lastCapturer =  if(metaBlocks(1) =="NONE") None else Some(new Player(metaBlocks(1)))
        case other =>                                
      }
    }
    
    //initializing game data
    for (i <- playerdata){
      //
      Game.players = Vector[Player]()
      var name = ""
      var score = 0 
      var hand = Vector[Card]()
      var capturedCards = Vector[Card]()
      //iterates through different players
      for(j <- i){
        val data = j.split(" ")
        //iterates through data of each player 
        data(0) match{
          case "NAME" =>  name = data(1)
          case "SCORE" => score = data(1).toInt
          case "HAND" => if(data.length > 1) hand = data(1).split(":").toVector.map(new Card(_))
          case "CAPTUREDCARDS" => if(data.length > 1)  capturedCards = data(1).split(":").toVector.map(new Card(_))
          case other => 
        }
      }
      val newPlayer = new Player(name)
      newPlayer.points = score
      newPlayer.cardsInHand = hand
      newPlayer.capturedCards = capturedCards
      println("New Player " + newPlayer)
      Game.players = Game.players :+ newPlayer
    }
    println(Game.players)
    println(Game.lastCapturer)
    println(Game.cardsOnTable)
    println(Game.cards)
    

}