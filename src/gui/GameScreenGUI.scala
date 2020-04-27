package gui

import scala.swing._
import scala.swing.event._
import java.io._
import java.awt.{Font,Color,BasicStroke}
import javax.imageio._
import cassino_game._

object GameScreenGUI extends Panel{
     //state variable
      visible = false
     //current selection of cards 
     var alreadySelected = Vector[Boolean]()
     //only the first card is selected initially
     //card selected from hand of player
     var playerSelection = 0
     //var flag = 0
     var winner = ""
     var turnChange = false
      var moveOn = false
     var panelString = "Click on Capture or Trail to make a move"
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
        GameGUI.currentPlayer = Game.players(0)
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
       g.drawString(GameGUI.currentPlayer.name + "'s turn", 350, 100 )
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
         if (!Game.players.isEmpty && GameGUI.currentPlayer.name == "Computer") alreadySelected = Array.fill[Boolean](Game.cardsOnTable.length)(false).toVector
          
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
          val playerCards = GameGUI.currentPlayer.cardsInHand.map(_.image)         
          //player Icon
           g.drawImage(GameGUI.currentPlayer.icon.get, 190, 510, 50, 50,null)
          //Header
          g.setFont(new Font("Serif",java.awt.Font.BOLD,52))
          g.setColor(new Color(240,230,140))
          g.drawString("Cassino", 300, 50)
          //player Name
          g.setFont(new Font("Serif",Font.BOLD,15))
          g.setColor(Color.WHITE) 
          g.drawString("Player " + " :  " + GameGUI.currentPlayer.name, 145, 585)
          
          //capture icon
          g.drawImage(ImageIO.read(new File("./ImageResources/captureIcon3.png")), 700,510,50,50, null)
          g.setColor(new Color(173,216,230))
          g.drawRect(755, 520, 95,25 )
          //capture text
          g.setFont(new Font("Monospaced",Font.BOLD,20))
          g.drawString("Capture",760,540)
         //trail icon
          g.drawImage(ImageIO.read(new File("./ImageResources/trailIcon.png")), 700, 570, 50,50, null )
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
         
         if (GameGUI.currentPlayer.name == "Computer"){
           //Painting out all the cardbacks 
           for(i <- 0 until GameGUI.currentPlayer.cardsInHand.size){
               val cardImage = ImageIO.read(new File("./ImageResources/cardBack.png"))
               g.drawImage(cardImage,290 + 100*i, 500, 90, 120 , null) 
             }
           //save cards on the table
           val table = Game.cardsOnTable
           //checkMove
           println("Current Player name : " + GameGUI.currentPlayer)
           val (card, combo) = Game.optimalMove(GameGUI.currentPlayer)
           if (combo.isDefined){
             //update selections
             alreadySelected = Array.fill[Boolean](Game.cardsOnTable.length)(false).toVector
             for (i <-  combo.get){
                     //indices to be highlighted
                      val index = table.filter(i.name == _.name).map(table.indexOf(_))
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
             Game.dealOne(GameGUI.currentPlayer)
             GameGUI.currentPlayer = Game.nextPlayer(GameGUI.currentPlayer)
             moveOn = true
         }
         else{
              //draw player's cards
              for(i <- 0 until GameGUI.currentPlayer.cardsInHand.size){
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
     this.listenTo(this.mouse.clicks,this.mouse.moves)
     this.reactions += {
       //when mouse is clicked
        case e : MousePressed =>
          if (moveOn || turnChange){
            turnChange = false
            moveOn = false
            this.repaint()
          }
          //CASE 1 : Player selects
          else if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 500 && e.point.y < 620) {
            if (GameGUI.currentPlayer.cardsInHand.isDefinedAt(0) ){
                //first card is selected from GameGUI.currentPlayer
                //added toselection
                playerSelection = 0
                //draw selection
                this.revalidate()
                this.repaint()
            }
          }   
         // second card clicked, draw selection
             else if(e.point.x >= 390 && e.point.x < 480 && e.point.y > 500 && e.point.y < 620){
              if (GameGUI.currentPlayer.cardsInHand.isDefinedAt(1) ){
                    //second card is selected from GameGUI.currentPlayer
                    //added toselection
                    playerSelection = 1
                    //draw selection
                    this.revalidate()
                    this.repaint()
                }
             }
              else if(e.point.x >= 490 && e.point.x < 580 && e.point.y > 500 && e.point.y < 620){
                if (GameGUI.currentPlayer.cardsInHand.isDefinedAt(2) ){
                    //third card is selected from GameGUI.currentPlayer
                    //added toselection
                    playerSelection = 2
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            }
             else if(e.point.x >= 590 && e.point.x < 680 && e.point.y > 500 && e.point.y < 620){
                if (GameGUI.currentPlayer.cardsInHand.isDefinedAt(3) ){
                    //fourth card is selected from GameGUI.currentPlayer
                    //added toselection
                    playerSelection = 3
                    //draw selection
                    this.revalidate()
                    this.repaint()
                }
            }
          
          //CASE where cards on table are selected.
            else if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 300  && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(0) ){
                    if(alreadySelected(0)) alreadySelected = alreadySelected.updated(0, false) 
                    else alreadySelected  = alreadySelected.updated(0, true)
                    //first card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            } 
           else if(e.point.x >= 390 && e.point.x < 480 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(1) ){
                    if(alreadySelected(1)) alreadySelected = alreadySelected.updated(1, false) 
                    else alreadySelected  = alreadySelected.updated(1, true)
                    //second card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            }
           else if(e.point.x >= 490 && e.point.x < 580 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(2) ){
                    if(alreadySelected(2)) alreadySelected = alreadySelected.updated(2, false) 
                    else alreadySelected  = alreadySelected.updated(2, true)
                    //third card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            } 
           else if(e.point.x >= 590 && e.point.x < 680 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(3) ){
                    if(alreadySelected(3)) alreadySelected = alreadySelected.updated(3, false) 
                    else alreadySelected  = alreadySelected.updated(3, true)
                    //fourth card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            }
           else if(e.point.x >= 190 && e.point.x < 280 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(4) ){
                    if(alreadySelected(4)) alreadySelected = alreadySelected.updated(4, false) 
                    else alreadySelected  = alreadySelected.updated(4, true)
                    //fourth card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            }
           else if(e.point.x >= 90 && e.point.x < 180 && e.point.y > 300 && e.point.y < 420){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(5) ){
                    if(alreadySelected(5)) alreadySelected = alreadySelected.updated(5, false) 
                    else alreadySelected  = alreadySelected.updated(5, true)
                    //fourth card is selected from GameGUI.currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                }
            }
          
          
           //TRAIL button is clicked
           else if (e.point.x > 700 && e.point.x <832 && e.point.y > 570 && e.point.y < 622){
             println("Trail clicked!")
             println("Current Player : " + GameGUI.currentPlayer)
             println("Player Selection : "+ playerSelection  )
            // Call the trail method
             Game.trail(GameGUI.currentPlayer, GameGUI.currentPlayer.cardsInHand(playerSelection))
             println(Game.toString())
             panelString = "Click on Capture or Trail to make a move"
             //checkIfWon
             checkRound
             if (!Game.isWon){  
               //deal another card
               Game.dealOne(GameGUI.currentPlayer)
               //update GameGUI.currentPlayer
               GameGUI.currentPlayer = Game.nextPlayer(GameGUI.currentPlayer)
               println("Next Player = " + GameGUI.currentPlayer)
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
             val pCard = GameGUI.currentPlayer.cardsInHand(playerSelection)
             val combo = alreadySelected.zip(Game.cardsOnTable).filter(_._1).map(_._2)
             //checkcapture
             if (Game.checkCapture(GameGUI.currentPlayer, pCard, combo)){
               if(combo.length == Game.cardsOnTable.length) panelString = "Nice! Sweep for " + GameGUI.currentPlayer.name
               else panelString = "Click on Capture or Trail to make a move"
               //lastCapturer updated
               Game.lastCapturer = Some(GameGUI.currentPlayer)
               //capture executed
               Game.executeCapture(GameGUI.currentPlayer, pCard, combo)
               checkRound
               //GameGUI.currentPlayer is dealt one card and play moves on only if round and game aren't over
               if (!Game.isWon){  
                 Game.dealOne(GameGUI.currentPlayer)
                 GameGUI.currentPlayer = Game.nextPlayer(GameGUI.currentPlayer)
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
               println("Condition for move was : " + Game.checkCapture(GameGUI.currentPlayer, pCard, combo))
               println("Move failed!")
             }
             //execute capture or failed, 
             //if successful update GameGUI.currentPlayer
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