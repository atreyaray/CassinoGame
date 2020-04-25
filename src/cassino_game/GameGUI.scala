package cassino_game

import scala.swing._
import scala.swing.event._
import java.io._
import java.awt.{Color,Font,Graphics2D,BasicStroke}
import javax.imageio.ImageIO
import javax.swing.SwingUtilities._

object GameGUI extends SimpleSwingApplication{
  
  
  var currentPlayer = new Player("")
  var players = 0 
  val okButton = new Button("OK")
  val enterNameButton = new Button("Next")
  val instructionButton = new Button("Got it"){
    this.horizontalAlignment = Alignment.Center
     background = Color.BLUE
     this.foreground = Color.BLACK
  }
  var playerFields = new BoxPanel(Orientation.Vertical)
  var playerFieldPanel = new BoxPanel(Orientation.Vertical)
  val playerCount  = new ComboBox(Vector(1,2,3,4,5,6))
  var playerName = Vector[TextField]()
  var playerNameVec = Vector[String]()
  val compToggleButton = new ToggleButton("Yes")
  var compOpponent = false
  val instructionMenuItem = new MenuItem("Instructions")
  val newGameMenuItem = new MenuItem("New Game")
  var moveOn = false
  var turnChange = false
  var winner = ""
  var panelString = "Click on Capture or Trail to make a move"
  
  

  //Window where game is played  
  var gameScreen =  new GamePanel 
  class GamePanel extends Panel{
     //state variable
      visible = false
     //current selection of cards 
     var alreadySelected = Vector[Boolean]()
     //only the first card is selected initially
     //card selected from hand of player
     var playerSelection = 0
     //var flag = 0
    //helper method
     def drawBorder(g : Graphics2D , x : Int, y : Int, i : Int) = {
      g.drawLine(x + 100*i -5  , y - 5      ,x + 100*i + 95, y - 5 )
      g.drawLine(x + 100*i -5  , y - 5 + 130,x + 100*i + 95, y - 5 + 130 )
      g.drawLine(x + 100*i -5  , y - 5      ,x + 100*i - 5 , y - 5 + 130 )
      g.drawLine(x + 100*i + 95, y - 5      ,x + 100*i + 95, y - 5 + 130 )  
      } 
      
  def checkRound = {  
     //If game has not been won AND some players exist AND there are 0 cards in any player's hand
     if (!Game.isWon && !Game.players.isEmpty && Game.players.map(_.cardsInHand.length).sum == 0){
       //all existing cards on deck go to lastCapturer
       Game.lastCapturer.getOrElse(null).capture(Game.cardsOnTable)
       //calculate point from the round
       Game.calculatePoints
       //update cardsOnTable
       Game.cardsOnTable = Vector[Card]()
       //reset captured cards
       Game.players.foreach(_.capturedCards = Vector[Card]())
       //show points
       Game.players.foreach(n => print("" + n.name + " " +  n.points + " "))
       Game.isWon = true
       this.revalidate()
       this.repaint()
     }  
  }   
     
   //showing the current board  
    def paintComp(g: Graphics2D, n : Int) = {
     if(Game.isWon){
      //set background
      g.setColor(new Color(0,100,0))
      g.fillRect(0, 0, 1000, 750 )
      //Points Header
      g.setColor(new Color(240,230,140))
      g.setFont(new Font("Serif",Font.BOLD,48))
      g.drawString("Points", 425, 50)
      //Display Icons and corresponding points
      g.setColor(new Color(230,230,250))
      g.setFont(new Font("Serif",Font.BOLD,32))
        for(i <- 0 until Game.players.length){
          g.drawImage(Game.players(i).icon.get, 50, 80 + 100*i, 50, 50 ,null)
          g.drawString(Game.players(i).points.toString(), 300, 120 + 100*i)
        }
      //Draw sidebar and border
      g.setColor(new Color(143,188,143))
      g.fillRect(575 , 75, 400, 630)
      g.setColor(new Color(0,100,0))
      g.drawLine(585, 85, 585, 695)
      g.drawLine(965, 85, 965, 695)
      g.drawLine(585, 85, 965, 85)
      g.drawLine(585, 695, 965, 695)
      //Draw points distribution
      g.setColor(Color.WHITE)
      g.setFont(new Font("Monospaced",Font.BOLD,15))
      g.drawString("Maximum Cards Captured : 2 points" , 595, 125)
      g.drawString("Maximum Spades Captured : 1 points" , 595, 205)
      g.drawString("Every Ace Captured : 1 points" , 595, 285)
      g.drawString("Every Sweep : 1 points" , 595, 365)
      g.drawString("10 of Diamonds : 2 points" , 595, 445)
      g.drawString("2 of Spades : 1 points" , 595, 525)
      //Draw ok button
      g.setColor(Color.WHITE)
      g.fillRect(750, 665, 75 , 25)
      g.setColor(Color.BLACK)
      g.setFont(new Font("Monospaced",Font.BOLD,22))
      g.drawString("OK", 775, 685) 
      if(!Game.players.exists(_.points >= 16)){
        Game.newRound()
        currentPlayer = Game.players(0)
      }
      else{
        winner = Game.players.maxBy(_.points).name
        g.drawString("Winner is : " + winner, 595, 645)
      }
     }
     else if(turnChange){
       g.setColor(new Color(0,100,0))
       g.fillRect(0, 0, 1000, 750)
       g.setColor(new Color(143,188,143))
       g.fillRect(150, 0, 700, 300)
       g.setColor(Color.WHITE)
       g.setFont(new Font("Monospaced", Font.BOLD, 32))
       g.drawString(currentPlayer.name + "'s turn", 350, 100 )
       g.setFont(new Font("Monospaced", Font.BOLD, 15))
       g.setColor(Color.BLACK)
       g.drawString("Click anywhere to continue", 350,275)
       
     }
     //If round has not ended 
     else{
         if (alreadySelected.isEmpty) for (i <- 0 until Game.cardsOnTable.length) alreadySelected = alreadySelected :+ (i==0)
         if(Game.cardsOnTable.length != alreadySelected.length){
           alreadySelected = Vector(true)
           for (i <- 0 until Game.cardsOnTable.length -1)  alreadySelected = alreadySelected :+ false
         }
         if (!Game.players.isEmpty && currentPlayer.name == "Computer") alreadySelected = Array.fill[Boolean](Game.cardsOnTable.length)(false).toVector
         println("AlreadySelected Vector : " + alreadySelected)
          
         //background color and window size
          g.setColor(new Color(0,100,0))
          g.fillRect(0, 0, 1000, 750)
          g.setBackground( new Color(173,230,216))
          
          //textBox
          g.setColor(new Color(143,188,143))
          g.fillRect(60, 80, 625, 150)
          g.setColor(new Color(0,100,0))
          g.drawLine(70, 90, 70, 220)
          g.drawLine(675, 90, 675, 220)
          g.drawLine(70, 90, 675, 90)
          g.drawLine(70, 220, 675, 220)
          
          //text on the textbox
          g.setColor(Color.WHITE)
          g.setFont(new Font("Monospaced",Font.BOLD, 18))
          g.drawString(panelString, 130, 170)
          
          //cards on the table and cards with certain player
          val image = Game.cardsOnTable.map(_.image)
          val playerCards = currentPlayer.cardsInHand.map(_.image)
          println("gets here")          
          //player Icon
           g.drawImage(currentPlayer.icon.get, 190, 510, 50, 50,null)
          //Header
          g.setFont(new Font("Serif",java.awt.Font.BOLD,52))
          g.setColor(new Color(240,230,140))
          g.drawString("Cassino", 300, 50)
          //player Name
          g.setFont(new Font("Serif",Font.BOLD,15))
          g.setColor(Color.WHITE) 
          g.drawString("Player " + " :  " + currentPlayer.name, 145, 585)
          
          //capture icon
          g.drawImage(ImageIO.read(new File("captureIcon3.png")), 700,510,50,50, null)
          g.setColor(new Color(173,216,230))
          g.drawRect(755, 520, 95,25 )
          //capture text
          g.setFont(new Font("Monospaced",Font.BOLD,20))
          g.drawString("Capture",760,540)
         //trail icon
          g.drawImage(ImageIO.read(new File("trailIcon.png")), 700, 570, 50,50, null )
         //trail text
          g.setColor(new Color(238,46,49))
          g.setFont(new Font("Monospaced",Font.BOLD,20))
          g.drawString("Trail", 760, 605)
          g.drawRect(755,585,75,25)
          //Scoreboard Panel
          g.setColor(Color.WHITE)
          g.drawRect( 730 , 10, 250, 400)
          g.setFont(new Font("Monospaced",Font.BOLD,32))
          g.drawString("Score ", 800,50)
          g.setFont(new Font("Monospaced",Font.BOLD,15))
          
         //Points
           g.setColor(Color.WHITE)
          for (i <- 0 until Game.players.length){
            g.drawImage(Game.players(i).icon.get, 745, 80 + 50*i, 30, 30, null)
            g.drawString(Game.players(i).points.toString(), 850, 100 + 50*i)
          }
         //draw cards on the table 
         for(i <- 0 until image.length) {
            if (i < 4) g.drawImage(image(i), 290 + 100*i , 300,90 ,120,null)
            else g.drawImage(image(i), 290 + 100*(3-i) , 300,90 ,120,null)
            if (alreadySelected(i)){
              g.setColor(new Color(143,188,143))
              g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
              val x = 290
              val y = 300
              if (i > 3)drawBorder(g,x,y,3-i)
              else drawBorder(g,x,y,i)
            }
         }
         
         if (currentPlayer.name == "Computer"){
           //Painting out all the cardbacks 
           for(i <- 0 until currentPlayer.cardsInHand.size){
               val cardImage = ImageIO.read(new File("cardBack.png"))
               g.drawImage(cardImage,290 + 100*i, 500, 90, 120 , null) 
             }
           //save cards on the table
           val table = Game.cardsOnTable
           //checkMove
           println("Executed now")
           println("Current Player name : " + currentPlayer)
           val (card, combo) = Game.optimalMove(currentPlayer)
           if (combo.isDefined){
             //update selections
             alreadySelected = Array.fill[Boolean](Game.cardsOnTable.length)(false).toVector
             for (i <-  combo.get){
                     //indices to be highlighted
                      val index = table.filter(i.name == _.name).map(table.indexOf(_))
                      println("Index " + index)
                      g.setColor(new Color(139,95,191))
                      g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))                    
                      if (index(0) > 3)drawBorder(g,290,300,3-index(0))
                      else drawBorder(g,290,300,index(0))
                   }
           }
             //card chosen to capture or trail    
             g.drawImage(card.image, 290, 500,90,120, null)
             g.setColor(Color.RED)
             drawBorder(g,290,500,0)
             Game.dealOne(currentPlayer)
             currentPlayer = Game.nextPlayer(currentPlayer)
             moveOn = true
         }
         else{
              //draw player's cards
              for(i <- 0 until currentPlayer.cardsInHand.size){
                g.drawImage(playerCards(i),290 + 100*i, 500, 90, 120 , null)
                //draw a border if it is the selection
                if(i==playerSelection){
                  g.setColor(Color.RED)
                  g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
                  val x = 290 
                  val y = 500
                  drawBorder(g,x,y,i)
                 }
               }
         }
      }
    }
   override def paintComponent(g : Graphics2D) ={
          paintComp(g,playerSelection)
   }
//       println(this)
     this.listenTo(this.mouse.clicks,this.mouse.moves)
     this.reactions += {
       //when mouse is clicked
        case e : MouseClicked =>
          if (moveOn || turnChange){
            turnChange = false
            moveOn = false
            this.repaint()
          }
          //CASE 1 : Player selects
          else if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 500 && e.point.y < 620) {
            if (currentPlayer.cardsInHand.isDefinedAt(0) ){
                //first card is selected from currentPlayer
                //added toselection
                playerSelection = 0
                //draw selection
                this.revalidate()
                this.repaint()
                println("Hello it's working")
            }
          }   
         // second card clicked, draw selection
             else if(e.point.x >= 390 && e.point.x < 480 && e.point.y > 500 && e.point.y < 620){
              if (currentPlayer.cardsInHand.isDefinedAt(1) ){
                    //second card is selected from currentPlayer
                    //added toselection
                    playerSelection = 1
                    //draw selection
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
             }
              else if(e.point.x >= 490 && e.point.x < 580 && e.point.y > 500 && e.point.y < 620){
                if (currentPlayer.cardsInHand.isDefinedAt(2) ){
                    //third card is selected from currentPlayer
                    //added toselection
                    playerSelection = 2
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
             else if(e.point.x >= 590 && e.point.x < 680 && e.point.y > 500 && e.point.y < 620){
                if (currentPlayer.cardsInHand.isDefinedAt(3) ){
                    //fourth card is selected from currentPlayer
                    //added toselection
                    playerSelection = 3
                    //draw selection
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
          
          //CASE where cards on table are selected.
            else if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 300  && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(0) ){
                    if(alreadySelected(0)) alreadySelected = alreadySelected.updated(0, false) 
                    else alreadySelected  = alreadySelected.updated(0, true)
                    //first card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            } 
           else if(e.point.x >= 390 && e.point.x < 480 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(1) ){
                    if(alreadySelected(1)) alreadySelected = alreadySelected.updated(1, false) 
                    else alreadySelected  = alreadySelected.updated(1, true)
                    //second card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
           else if(e.point.x >= 490 && e.point.x < 580 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(2) ){
                    if(alreadySelected(2)) alreadySelected = alreadySelected.updated(2, false) 
                    else alreadySelected  = alreadySelected.updated(2, true)
                    //third card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            } 
           else if(e.point.x >= 590 && e.point.x < 680 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(3) ){
                    if(alreadySelected(3)) alreadySelected = alreadySelected.updated(3, false) 
                    else alreadySelected  = alreadySelected.updated(3, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
           else if(e.point.x >= 190 && e.point.x < 280 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(4) ){
                    if(alreadySelected(4)) alreadySelected = alreadySelected.updated(4, false) 
                    else alreadySelected  = alreadySelected.updated(4, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
           else if(e.point.x >= 90 && e.point.x < 180 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(5) ){
                    if(alreadySelected(5)) alreadySelected = alreadySelected.updated(5, false) 
                    else alreadySelected  = alreadySelected.updated(5, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
          
          
           //TRAIL button is clicked
           else if (e.point.x > 700 && e.point.x <832 && e.point.y > 570 && e.point.y < 622){
             println("Trail clicked!")
             println("Current Player : " + currentPlayer)
             println("Player Selection : "+ playerSelection  )
            // Call the trail method
             Game.trail(currentPlayer, currentPlayer.cardsInHand(playerSelection))
             println(Game.toString())
             panelString = "Click on Capture or Trail to make a move"
             //checkIfWon
             checkRound
             if (!Game.isWon){  
               //deal another card
               Game.dealOne(currentPlayer)
               //update currentPlayer
               currentPlayer = Game.nextPlayer(currentPlayer)
               println("Next Player = " + currentPlayer)
               //change of turn
               turnChange = true
               //repaint()
               this.revalidate()
               this.repaint()
             }
             else{
               moveOn = true
             }
           }
          
          
           //CAPTURE button is clicked
           else if (e.point.x > 700 && e.point.x <832 && e.point.y > 515 && e.point.y < 560){
             println("Capture clicked!")
             //gather data
             val pCard = currentPlayer.cardsInHand(playerSelection)
             val combo = alreadySelected.zip(Game.cardsOnTable).filter(_._1).map(_._2)
             //checkcapture
             if (Game.checkCapture(currentPlayer, pCard, combo)){
               if(combo.length == Game.cardsOnTable.length) panelString = "Nice! Sweep for " + currentPlayer.name
               else panelString = "Click on Capture or Trail to make a move"
               //lastCapturer updated
               Game.lastCapturer = Some(currentPlayer)
               //capture executed
               Game.executeCapture(currentPlayer, pCard, combo)
               checkRound
               //currentPlayer is dealt one card and play moves on only if round and game aren't over
               if (!Game.isWon){  
                 Game.dealOne(currentPlayer)
                 currentPlayer = Game.nextPlayer(currentPlayer)
                 turnChange = true
               }
               else{
                 moveOn = true
               }
               println(Game.toString())
             }
             else {
               panelString = "Move failed, try another ?"
               println("Player's choice : " + pCard)
               println("Card from table : " + combo)
               println("Condition for move was : " + Game.checkCapture(currentPlayer, pCard, combo))
               println("Move failed!")
             }
             //execute capture or failed, 
             //if successful update currentPlayer
             this.revalidate()
             this.repaint()
             //repaint
           }
          
          
            else {
              panelString = "Oops! Click wasn't on the buttons."
              println(e.point)
              this.repaint()
            }
     }
      
  }
  
  var instructionsScreen = new BoxPanel(Orientation.Vertical){
    contents += new FlowPanel{   
                      contents+= new Label("Instructions Page"){
                                      font = new Font("Roman",Font.BOLD,48)
                                      opaque = true
                                      foreground = new Color(240,230,140)
                                      background = new Color(0,100,0)
                                  }
                      background = new Color(0,100,0)
                }
    contents += new ScrollPane(){
                   contents = new TextArea("\n\nThe deck is shuffled in the beginning of every round and the dealer deals 4 cards to every player" 
         +" (they are not visible to other players) and 4 cards on the table (visible for everyone). The rest of the cards are left on the table upside down."  
         + "The player next to the dealer starts the game. On the next round he/she is the dealer.\n\n\n"
         + "What every player does on their turn: \n\n"
         + "1. A player can play out one of his/her cards: it can be used either for taking cards from the table or to just putting it on the table. If the player cannot take anything from the table, he/she must put one of his/her cards on the table.\n\n"
         + "2. If the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended.\n\n"
         + "3. The number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.\n\n"
         + "4. Player must draw a new card from the deck after using a card so that he/she has always 4 cards in his/her hand. (When the deck runs out, everyone plays until there are no cards left in any player’s hand).\n\n"
         + "5. Player can use a card to take one or more cards of the same value and cards such that their summed value is equal to the used card.\n\n\n"
         + "Sweep : If some player gets all the cards (more than one) from the table at the same time, he/she gets a so called sweep which is written down.\n\n\n"
         + "Special Cards : There are a couple of cards that are more valuable in the hand than in the table,\n"
         + "\tAces: 14 in hand, 1 on table \n"  
         + "\tDiamonds-10: 16 in hand, 10 on table\n"
         + "\tSpades-2: 15 in hand, 2 on table\n\n\n"
         + "Scoring: When every player runs out of cards, the last player to take cards from the table gets the rest of the cards from the \n"
         + "              table. After this the points are calculated and added to the existing scores.\n"
         + "              The following things grant points:\n"
         + "\tEvery sweep grants 1 point.\n"
         + "\tEvery Ace grants 1 point.\n"
         + "\tThe player with most cards gets 1 point.\n"
         + "\tThe player with most spades gets 1 points.\n"
         + "\tThe player with Diamonds-10 gets 2 points.\n"
         + "\tThe player with Spades-2 gets 1 point.\n"
         ){
                                    minimumSize = new Dimension(900,500) 
                                    charWrap = true
                                    wordWrap = true
                                    lineWrap = true
                                    editable = false
                                    font = new Font("Serif",Font.BOLD,18)
                                    background = new Color(0,100,0)
                                    foreground  = new Color(230,230,250)
                                }
                   background = new Color(0,100,0)
                }
    contents += new FlowPanel{
                      contents += instructionButton
                      background = new Color(0,100,0)
                }
    background = new Color(0,100,0)
    visible = false 
  }
  
  
  var topPanel = new FlowPanel{
                    contents += new Label("Cassino: New Game"){
                      this.foreground = (new Color(240,230,140))
                      font = new Font("Serif",java.awt.Font.BOLD,48)
                     }
                     this.maximumSize = (new Dimension(1000,100))
                    // this.background = new Color(181,192,245)
                     override def paintComponent(g : Graphics2D) ={
                       val image = ImageIO.read(new File("icon.png"))
                       g.drawImage(image,730,5,75,75,null)
                     }
                    }
  var bottomPanel = new FlowPanel{
                     contents += new Label("How many players?"){
                       foreground = Color.WHITE
                     }
                     contents += playerCount
                     contents += okButton
                     contents += nameInput(players)       
                     //size of flowPanel
                     this.maximumSize = (new Dimension(300,100))
                    background = new Color(0,100,0)
                }
  var middlePanel = new FlowPanel{
                       contents += new Label("Do you want a computer opponent ?"){
                         opaque = true
                         background = new Color(0,100,0)
                         foreground = Color.WHITE
                       }
                       contents += compToggleButton   
                       //size of flowPanel
                       this.maximumSize = (new Dimension(300,100))
                       background = new Color(0,100,0)
                     }
  
  
  //Creates a panel which displays textFields
  def nameInput(count : Int) = {
    val panel = new BoxPanel(Orientation.Vertical){
      for (i <- 0 until count){
        contents += new BoxPanel(Orientation.Horizontal){
          //add to playerName
          playerName = playerName :+ (new TextField())
          contents += new Label("Enter your name"){
            foreground = new Color(230,230,250)
          }
          contents += playerName(i)
          this.font = new Font("Arial",java.awt.Font.CENTER_BASELINE,12)
          background = new Color(0,100,0)
           this.maximumSize_=(new Dimension(300,30))
        }
        this.maximumSize_=(new Dimension(300,200))
        background = new Color(0,100,0)
      } }
    panel
  }
  
  //Initital screen
  var currentScreen = new BoxPanel(Orientation.Vertical){
   
      playerFieldPanel  = new BoxPanel(Orientation.Vertical){
      contents += new TextField("Alone? Here with your gang?"){
         font = new Font("Monospaced", Font.BOLD, 24)
         horizontalAlignment = Alignment.Center
         this.editable = false
         this.maximumSize_=(new Dimension(1000,300))
         background = new Color(0,100,0)
      }
    }
    background = new Color(0,100,0)
    //Header : "Cassino: New Game"
    contents += topPanel
    //Flow Panel for input
    contents += middlePanel
    //Flow Panel for input
    contents += bottomPanel
   //Panel to enter Player Names
   contents += playerFieldPanel
//   contents += imagePanel
  // this.background = new Color(173,230,216)
   //Action Listeners
    this.listenTo(okButton)
    this.listenTo(compToggleButton)
    //Reactions
    this.reactions += {
         case e : ButtonClicked =>if (e.source == okButton){
                                    players = playerCount.selection.item.toString().toInt
                                     println(Game.players)
                                     playerFieldPanel.contents.clear()
                                     playerFields = nameInput(players)
                                     playerFieldPanel.contents += playerFields
                                     this.contents += enterNameButton
                                     this.revalidate()
                                     this.repaint()
                                  }

                                  else {
                                    println(e)
                                    compOpponent = compToggleButton.selected
                                  }
         case e: MouseMoved => println(e)        
     }

   
  }
  

  
  
  
 var finalFrame = new BoxPanel(Orientation.Vertical){
   contents += currentScreen
   contents += instructionsScreen
   contents += gameScreen
   background = new Color(0,100,0)
    this.listenTo(instructionMenuItem)
    this.listenTo(newGameMenuItem)
    this.listenTo(enterNameButton)
    this.listenTo(instructionButton)
   // this.listenTo(gameScreen)
    this.reactions+= {
     case any : ButtonClicked => if(any.source == instructionMenuItem){  
                                   currentScreen.visible = false
                                   gameScreen.visible = false
                                  
                                   instructionsScreen.visible = true}
                                 else if(any.source == newGameMenuItem){
                                   Game.newGame(false, Vector())
                                   playerNameVec = Vector[String]()
                                   gameScreen.visible = false
                                   instructionsScreen.visible = false
                                   currentScreen.visible = true
                                   
                                 }
                                 else if(any.source == enterNameButton){
                                    playerNameVec = Vector[String]()
                                    playerNameVec = playerNameVec ++ playerName.map(_.text)
                                    println("Name entered " + playerNameVec)
                                   Game.newGame(compOpponent, playerNameVec)
                                   currentScreen.visible = false
                                   println("Game players " + Game.players)
                                   currentPlayer = Game.players(0)
                                   gameScreen.visible = true
                                   gameScreen.revalidate()
                                   gameScreen.repaint()
                                  }
                                   else{
                                   currentScreen.visible = true
                                   instructionsScreen.visible = false}
     case e : MouseClicked =>  if (Game.isWon && !winner.isEmpty){
                                   compToggleButton.selected = false
                               }
                               println(e)
   }
  }
  
  
  
  //Loads text file and starts a new game
  def openFile = {
    val chooser  = new FileChooser
    //If option is clicked
    if(chooser.showOpenDialog(null)==FileChooser.Result.Approve){
      //Updating game state variables
      Game.fileHandler.loadGame(chooser.selectedFile.getName)
      //updating current player
      currentPlayer = Game.players.filter(_.name == Game.currentPlayer.name)(0)
      //assign Icons 
      var tempPlayer = currentPlayer
      var index = 1
      do{
        if (tempPlayer.compPlayer) tempPlayer.icon = Some(ImageIO.read(new File("robot.png")))
        else {
          tempPlayer.icon = Some(ImageIO.read(new File("p"+ index + ".png")))
          index += 1
        }
        tempPlayer = Game.nextPlayer(tempPlayer)
      }while(tempPlayer.name != currentPlayer.name)
      println(currentPlayer)
      //front page is made invisible
      currentScreen.visible = false
      //instruction screen is made invisible
      instructionsScreen.visible = false
      //game screen is made visible
      gameScreen.visible = true
      gameScreen.repaint()
    }
  }
  
  //saves file into a text File
  def saveFile = {
     val chooser  = new FileChooser
    if(chooser.showSaveDialog(null)==FileChooser.Result.Approve){
      Game.currentPlayer = currentPlayer
      Game.fileHandler.saveGame(chooser.selectedFile.getName)
    }
  }
  
  //menu bar
  def menu = {
    new MenuBar{
      contents += new Menu("New Game"){
        contents += newGameMenuItem
      }
      contents += new Menu("Settings"){
         //Opening and Loading a text file
         contents += new MenuItem(Action("Open"){
           openFile
         })
         //saving game into a textFile
         contents += new MenuItem(Action("Save"){
           saveFile
         })
      }
      contents += new Menu("Rules"){
        contents += instructionMenuItem
        
      }
      contents += new MenuItem(Action("Exit"){
           sys.exit(0)
         })
    }
  }
  
  
  //Main body 
  val top = new MainFrame {  
    title = "Cassino"
    menuBar = menu
    contents =  finalFrame
    size = new Dimension(1000,750)
    background = new Color(127,255,0)
    centerOnScreen
  }
  top.visible = true
   
}