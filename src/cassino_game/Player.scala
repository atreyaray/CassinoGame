package cassino_game

class Player (givenName : String){
  
  val name = givenName
  var points = 0
  var capturedCards = Vector[Card]()
  var cardsInHand = Vector[Card]()
  
  def addPoints (addition : Int) = points += addition
  def capture (cards : Vector[Card]) = capturedCards = capturedCards ++ cards
  def playCard (card : Card) = cardsInHand = cardsInHand.filter(_.name != card.name)
  def deal(cards : Vector[Card]) = cardsInHand = cardsInHand ++ cards
  
  
  override def toString() = "Player " + name + " with cards " + cardsInHand + "and captured cards " + capturedCards
}