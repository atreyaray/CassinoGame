package cassino_game

import java.io._
import scala.collection.mutable.Buffer
import java.text.SimpleDateFormat
import java.util.Calendar

class FileHandler {
  
  //reads a file and returns the chunks of data 
  def readFile (textFile : String) : Array[String]= {
    io.Source.fromFile(textFile).mkString.split("###")
  }
  
  def saveGame(textFile : String ) = {
    
    val file = new File("textFile")
    file.delete()

    //file writers
    val fileWriter = new FileWriter(textFile,true)
    val writer = new BufferedWriter(fileWriter)
   
    //getDate
    val today = Calendar.getInstance.getTime.toString()
    
    //Block 1 : General Data
    writer.write("###\nCASSINO GAME\nv1.0\n" + today + "\n\n")
  
    
    //Block 2: Metadata
    //##round number, timer and turn need to be worked on.
    val cardsOnTable = Game.cardsOnTable.map(_.name).foldLeft("")((m,n)=> m+n)
    val lastCapturer = if(Game.lastCapturer.isDefined) Game.lastCapturer.get.name else "NONE"
    val cardsInDeck = Game.cards.map(_.name).foldLeft("")((m,n)=>m+n)
    writer.write("###\nMETADATA\nNUMBEROFPLAYERS "+ Game.players.length + "\nROUNDNUMMBER "+ 1 + "\nCARDSREMAINING " + Game.cardsOnTable.length
                  + "\nCARDSONTABLE " + cardsOnTable + "\nCARDSINDECK " + cardsInDeck  +"\nLASTCAPTURER " + lastCapturer + "\n\n")
     
    //Block 3-N: Player Data
    for (i <- Game.players){
      val playerNo = Game.players.indexOf(i)+1
      val hand = i.cardsInHand.map(_.name).foldLeft("")((m,n)=>m+n)
      val captured  = i.capturedCards.map(_.name).foldLeft("")((m,n)=> m+n)
      writer.write("###\nPLR " + playerNo + "\nNAME " + i.name + "\nSCORE " + i.points + "\nHAND " + hand + "\nCAPTUREDCARDS " + captured + "\n\n")  
    }
    
    //EOF
    writer.write("###\nEOF")
    
    writer.close()
    fileWriter.close()
 
    
  }
}