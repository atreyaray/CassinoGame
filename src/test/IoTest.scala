package test


import cassino_game._
import java.io._
import org.scalatest._

class IoTest extends FlatSpec{
  
  "A file" should "be not empty" in {
    val fh = new FileHandler
    assert(fh.readFile("testIO_1.txt").map(_.trim()).map(_.split('\n')).length != 0)
  }
  
  
  
  //1.create a new file handler
  //2.create a new FileWriter
//  val newFile = new File ("progress_log.txt")
//  val file = new FileHandler
//  
//  try {
//    newFile.createNewFile()
//     println("Reaches here")
//     val name = newFile.getName
//     println("Reaches here too. Name of file is: " + name )
//    val expectedLength = file.readFile(newFile.getName).length
//    println("Doesn't reach here")
////    this.assert(expectedLength==5)
//  }catch{
//    case e : IOException => println("Test failed: " + e)
//    case nullPointer : NullPointerException => ("Test failed miserably: " + nullPointer)
//    case other: Throwable => println("Test failed: " + other)
//  }
}

