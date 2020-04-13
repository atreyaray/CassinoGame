package cassino_game

import java.io._
import scala.collection.mutable.Buffer

class FileHandler {
  
  //reads a file and returns the chunks of data 
  def readFile (textFile : String) : Array[String]= {
//    
//    //defined FileReader and BufferedReader class objects
//    val fileReader = new FileReader(textFile)
//    val bufferedReader = new BufferedReader(fileReader)
//    //return object
//    var blocks = Buffer[Buffer[String]]()
//    //flag variable
//    var exit = false
//    
//    try{
//        var readLine = bufferedReader.readLine()
//        while (!exit){
//          
//          var block = Buffer[String]()
//          
//          if (readLine.charAt(0) == '#'){
//            while(!readLine.isEmpty()){
//              block += readLine
//              readLine = bufferedReader.readLine()
//            }
//          }
//         blocks += block  
//         readLine = bufferedReader.readLine()
//         
//        }
//    }catch{
//      case e : IOException => println("Error found in readFile method : " + e)
//    }finally{
//      fileReader.close()
//      bufferedReader.close()
//    }
//    blocks
    io.Source.fromFile(textFile).mkString.split("###")
  }
}