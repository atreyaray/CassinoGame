

package cassino_game

class Card (givenName:String, givenSuit : String, givenValue : Int) {
  
  val name  = givenName
  val suit  = givenSuit
  val value = givenValue

///////////////////////////////////////////
//Only certain cards get special values  //
//  Aces:        14 in hand, 1 on table  //
//  Diamonds-10: 16 in hand, 10 on table //
//  Spades-2:    15 in hand, 2 on table  //
///////////////////////////////////////////
  val specialValue = (givenSuit,givenValue) match {
                                                    case (givenSuit, 1)  => Some(14)
                                                    case ("s"      , 2)  => Some(15)
                                                    case ("d"      , 10) => Some(16)
                                                    case _ => None
                                                   }    
  
}