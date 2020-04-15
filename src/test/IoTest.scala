package test


import cassino_game._
import java.io._
import org.scalatest._

class IoTest extends FlatSpec{
  
  "A file" should "be not empty" in {
    val fh = new FileHandler
    assert(fh.readFile("testIO_1.txt").length != 0)
  }
}

