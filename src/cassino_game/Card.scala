package cassino_game

import javax.imageio.ImageIO._
import java.io._
class Card (givenName:String) {
  
  val name  : String = givenName
  val suit  : String = givenName.charAt(0).toString()
  val value : Int    = givenName.drop(1) match{
    case "a" => 1
    case "j" => 11
    case "q" => 12
    case "k" => 13
    case other => givenName.drop(1).toInt
  }
  
 val image = read(new File(name+".png"))
///////////////////////////////////////////
//Only certain cards get special values  //
//  Aces:        14 in hand, 1 on table  //
//  Diamonds-10: 16 in hand, 10 on table //
//  Spades-2:    15 in hand, 2 on table  //
///////////////////////////////////////////
  val specialValue = (suit,value) match {
                                                    case (givenSuit, 1)  => Some(14)
                                                    case ("s"      , 2)  => Some(15)
                                                    case ("d"      , 10) => Some(16)
                                                    case _ => None
                                                   }    
  
  
  override def toString() = "Card " + name
}