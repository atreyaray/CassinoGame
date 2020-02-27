import java.io._
import scala.collection.mutable.Buffer

class FileHandler {
  
  def readFile (textFile : String) = {
    
    val fileReader = new FileReader(textFile)
    val bufferedReader = new BufferedReader(fileReader)
    
    try{
        var blocks = Buffer[Buffer[String]]()
        var readLine = bufferedReader.readLine()
        while (readLine != null){
          
          var block = Buffer[String]()
          
          if (readLine.charAt(0) == '#'){
            while(!readLine.isEmpty()){
              block += readLine
              readLine = bufferedReader.readLine()
            }
          }
          
          readLine = bufferedReader.readLine()
        }
    }catch{
      case e : IOException => println("Error found in readFile method : " + e)
    }finally{
      fileReader.close()
      bufferedReader.close()
    }
    
  }
}