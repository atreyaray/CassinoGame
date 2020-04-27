package cassino_game

import java.io._
import scala.collection.mutable.Buffer
import java.text.SimpleDateFormat
import java.util.Calendar

class FileHandler {
  
  //reads a file and returns the chunks of data 
  def readFile (textFile : File) : Array[Array[String]]= {
    io.Source.fromFile(textFile).mkString.split("###").map(_.trim()).map(_.split('\n'))
  }
  
  def saveGame(textFile : File ) = {
    
   // val file = new File("textFile")
    val file = textFile
    file.delete()

    //file writers
    val fileWriter = new FileWriter(textFile)
    val writer = new BufferedWriter(fileWriter)
    
    //getDate
    val today = Calendar.getInstance.getTime.toString()
    
    //Block 1 : General Data
    writer.write("###\nCASSINO GAME\nv1.0\n" + today + "\n\n")
  
    
    //Block 2: Metadata
    //##round number, timer and turn need to be worked on.
    val cardsOnTable = Game.cardsOnTable.map(_.name).foldLeft("")((m,n)=> m+n+":")
    val lastCapturer = if(Game.lastCapturer.isDefined) Game.lastCapturer.get.name else "NONE"
    val cardsInDeck = Game.cards.map(_.name).foldLeft("")((m,n)=>m+n+":")
    writer.write("###\nMETADATA\nNUMBEROFPLAYERS "+ Game.players.length + "\nROUNDNUMMBER "+ 1 + "\nNOOFCARDSREMAINING " + Game.cardsOnTable.length
                  + "\nCARDSONTABLE " + cardsOnTable + "\nCARDSINDECK " + cardsInDeck  +"\nLASTCAPTURER " + lastCapturer + "\nCURRENTPLAYER " + Game.currentPlayer.name +"\n\n")
     
    //Block 3-N: Player Data
    for (i <- Game.players){
      val hand = i.cardsInHand.map(_.name).foldLeft("")((m,n)=>m+n+":")
      val captured  = i.capturedCards.map(_.name).foldLeft("")((m,n)=> m+n+":")
      writer.write("###\nPLR" + "\nNAME " + i.name + "\nSCORE " + i.points + "\nHAND " + hand + "\nCAPTUREDCARDS " + captured + "\n\n")  
    }
    
    //EOF
    writer.write("###\nEOF")
    
    //closing streams
    writer.close()
    fileWriter.close()
  }
  
  def loadGame (textFile : File) = {
    //all the read data
    val blocks = this.readFile(textFile)
    
    //blocks of data containing metadata and player data
    var metadata = Array[String]()
    var playerdata = Array[Array[String]]()
    
    //parsing file to determine metadata and player data
    for (currentBlock <- blocks){
      val x = currentBlock
      currentBlock(0) match{
        case "" => 
        case "METADATA" => metadata = currentBlock
        case "PLR" => playerdata = playerdata :+ currentBlock 
        case other => 
      }
    }
    
    //initalizing metadata
    for (i <- metadata){
        val metaBlocks = i.split(" ").toArray
      metaBlocks(0) match{
        case "CARDSONTABLE" =>  Game.cardsOnTable = Vector[Card]()
                               if(metaBlocks.length > 1 ) metaBlocks(1).split(":").toArray.foreach(n => Game.cardsOnTable = Game.cardsOnTable :+ new Card(n) )
        case "CARDSINDECK" => Game.cards = Vector[Card]()
                               if(metaBlocks.length > 1 )  metaBlocks(1).split(":").toArray.foreach(n => Game.cards= Game.cards :+ new Card(n) )
        case "LASTCAPTURER" => Game.lastCapturer =  if(metaBlocks(1) =="NONE") None else Some(new Player(metaBlocks(1)))
        case "CURRENTPLAYER" => Game.currentPlayer = new Player(metaBlocks(1))
        case other =>                                
      }
    }
     Game.players = Vector[Player]()
    //initializing game data
    for (i <- playerdata){
      //
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
      //adds newPlayer to list
      val newPlayer = new Player(name)
      newPlayer.points = score
      newPlayer.cardsInHand = hand
      newPlayer.capturedCards = capturedCards
      Game.players = Game.players :+ newPlayer
    }
  }
}