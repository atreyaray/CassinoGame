package cassino_game

import scala.swing._
import scala.swing.event._
import java.io._
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import javax.imageio.ImageIO
import javax.swing.SwingUtilities._

object GameGUI extends SimpleSwingApplication{
  
  
  
  
  val textArea = new TextArea
  //val image = 
  def openFile = {
    val chooser  = new FileChooser
    if(chooser.showOpenDialog(null)==FileChooser.Result.Approve){
      val source = scala.io.Source.fromFile(chooser.selectedFile)
      textArea.text = source.mkString
      source.close()
    }
  }
  
  def saveFile = {
     val chooser  = new FileChooser
    if(chooser.showSaveDialog(null)==FileChooser.Result.Approve){
      val pw = new java.io.PrintWriter(chooser.selectedFile)
      pw.print(textArea.text) 
      pw.close()
    }
  }
  
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
  
  var topPanel = new FlowPanel{
                    contents += new Label("Cassino: New Game"){
                      this.foreground = (Color.BLUE)
                      font = new java.awt.Font("Serif",java.awt.Font.BOLD,48)
                     }
                     this.maximumSize = (new Dimension(1000,100))
                    // this.background = new Color(181,192,245)
                     override def paintComponent(g : Graphics2D) ={
                       val image = ImageIO.read(new File("icon.png"))
                       g.drawImage(image,730,5,75,75,null)
                     }
                    }
  var bottomPanel = new FlowPanel{
                     contents += new Label("How many players?")
                     contents += playerCount
                     contents += okButton
                     contents += nameInput(players)       
                     //size of flowPanel
                     this.maximumSize = (new Dimension(300,100))
                    // this.background = new Color(173,230,216)
                }
  var middlePanel = new FlowPanel{
                       contents += new Label("Do you want a computer opponent ?"){
                         opaque = true
                        // this.background = new Color(173,230,216)
                       }
                       contents += compToggleButton   
                       //size of flowPanel
                       this.maximumSize = (new Dimension(300,100))
                       //this.background = new Color(173,230,216)
                     }
  

  
  
  //Creates a panel which displays textFields
  def nameInput(count : Int) = {
    val panel = new BoxPanel(Orientation.Vertical){
      for (i <- 0 until count){
        contents += new BoxPanel(Orientation.Horizontal){
          //add to playerName
          playerName = playerName :+ (new TextField())
          contents += new Label("Enter your name")
          contents += playerName(i)
          this.font = new Font("Arial",java.awt.Font.CENTER_BASELINE,12)
          // background = new Color(173,230,216)
           this.maximumSize_=(new Dimension(300,30))
        }
        this.maximumSize_=(new Dimension(300,200))
      //   background = new Color(173,230,216)
      } }
    panel
  }
  
  

    
  var gameScreen =  new GamePanel 
  class GamePanel extends Panel{

     //state variable
      visible = false
    // var playerIcon = ???
     //current selection of cards 
     var selectedCards = Vector[Card]() 
     var alreadySelected = Vector[Boolean]()
     //only the first card is selected initially
     //card selected from hand of player
     var playerSelection = 0
     var flag = 0
      
    //showing the current board  
     def paintComp(g: Graphics2D, n : Int) = {
         if (alreadySelected.isEmpty) for (i <- 0 until Game.cardsOnTable.length) alreadySelected = alreadySelected :+ (i==0)
           println("AlreadySelected Vector : " + alreadySelected)
          
         //background color and window size
          g.setColor(new Color(73,159,104))
          g.fillRect(0, 0, 1000, 750)
          g.setBackground( new Color(173,230,216))
          
          //cards on the table and cards with certain player
          val image = Game.cardsOnTable.map(_.image)
          val playerCards = currentPlayer.cardsInHand.map(_.image)
          
          //player Icon
          g.drawImage(ImageIO.read(new File("p2.png")), 190, 510, 50, 50,null)
          //player Name
          g.setFont(new Font("Serif",Font.BOLD,15))
          g.setColor(Color.BLACK) 
          g.drawString("Player 1", 190, 575)
          //capture icon
          g.drawImage(ImageIO.read(new File("captureIcon3.png")), 700,510,50,50, null)
          g.setColor(Color.CYAN)
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
          
          g.setColor(new Color(12,116,137))
          g.drawRect( 730 , 10, 250, 400)
          g.setFont(new Font("Monospaced",Font.BOLD,32))
          g.drawString("Score ", 800,50)
          g.drawImage(ImageIO.read(new File("robot.png")), 750, 80, 30, 30,null)
          g.drawImage(ImageIO.read(new File("p2.png")), 745, 130, 40,40,null)
          g.drawImage(ImageIO.read(new File("p1.png")), 750, 200, 30,30, null)
          
        
         //draw cards on the table 
          for(i <- 0 until image.length) {
            g.drawImage(image(i), 290 + 100*i , 220,90 ,120,null)
            if (alreadySelected(i)){
              g.setColor(new Color(139,95,191))
              g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
              g.drawLine(290 + 100*i -5, 215 ,290 + 100*i + 95, 215 )
              g.drawLine(290 + 100*i -5, 345 ,290 + 100*i + 95, 345 )
              g.drawLine(290 + 100*i -5, 215 ,290 + 100*i -5, 345 )
              g.drawLine(290 + 100*i + 95, 215 ,290 + 100*i + 95, 345 )
            }
          }
          //draw player's cards
          for(i <- 0 until currentPlayer.cardsInHand.size){
            g.drawImage(playerCards(i),290 + 100*i, 500, 90, 120 , null)
            //draw a border if it is the selection
            if(i==playerSelection){
              g.setColor(Color.RED)
              g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
              g.drawLine(290 + 100*i -5, 495 ,290 + 100*i + 95, 495 )
              g.drawLine(290 + 100*i -5, 625 ,290 + 100*i + 95, 625 )
              g.drawLine(290 + 100*i -5, 495 ,290 + 100*i -5, 625 )
              g.drawLine(290 + 100*i + 95, 495 ,290 + 100*i + 95, 625 )
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
          //CASE 1 : Player selects
          if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 500 && e.point.y < 620) {
            if (currentPlayer.cardsInHand.isDefinedAt(0) ){
                //first card is selected from currentPlayer
                //added toselection
                playerSelection = 0
                //draw selection1
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
                    //draw selection1
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
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
          
          
            else if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 220 && e.point.y < 340){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(0) ){
                    if(alreadySelected(0)) alreadySelected = alreadySelected.updated(0, false) 
                    else alreadySelected  = alreadySelected.updated(0, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            } 
           else if(e.point.x >= 390 && e.point.x < 480 && e.point.y > 220 && e.point.y < 340){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(1) ){
                    if(alreadySelected(1)) alreadySelected = alreadySelected.updated(1, false) 
                    else alreadySelected  = alreadySelected.updated(1, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            }
           else if(e.point.x >= 490 && e.point.x < 580 && e.point.y > 220 && e.point.y < 340){
              //if there is a card  
              if (Game.cardsOnTable.isDefinedAt(2) ){
                    if(alreadySelected(2)) alreadySelected = alreadySelected.updated(2, false) 
                    else alreadySelected  = alreadySelected.updated(2, true)
                    //fourth card is selected from currentPlayer
                    //draw selection1
                    this.revalidate()
                    this.repaint()
                    println("Hello it's working again")
                }
            } 
           else if(e.point.x >= 590 && e.point.x < 680 && e.point.y > 220 && e.point.y < 340){
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
            else println(e.point)
     }
        

  
  }
  
  var instructionsScreen = new BoxPanel(Orientation.Vertical){
    contents += new FlowPanel{   
                      contents+= new Label("Instructions Page"){
                                      font = new Font("Roman",Font.BOLD,48)
                                      opaque = true
                                      foreground = new Color(98,207,125)
                                      background = new Color(173,216,230)
                                  }
                      background = new Color(173,216,230)
                }
    contents += new FlowPanel{
                   contents += new TextArea(){
                                    editable = false
                                    font = new Font("Serif",Font.BOLD,24)
                                    background = new Color(173,216,230)
                                    foreground  = Color.WHITE
                                }
                   background = new Color(173,216,230)
                }
    contents += new FlowPanel{
                      contents += instructionButton
                      background = new Color(173,216,230)
                }
    visible = false 
  }
  
  //Initital screen
  var currentScreen = new BoxPanel(Orientation.Vertical){
   
      playerFieldPanel  = new BoxPanel(Orientation.Vertical){
      contents += new TextField("Alone? Here with your gang?"){
         horizontalAlignment = Alignment.Center
         this.editable = false
         this.maximumSize_=(new Dimension(1000,300))
      //   this.background = new Color(173,230,216)
      }
    }
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
  
  
//      override def paintComponent(g: Graphics2D) ={
//       val image = javax.imageio.ImageIO.read(new File("1C.png"))
//       g.drawImage(image, 250,250, 70,100,null )
//      }
  
  
  
 var finalFrame = new BoxPanel(Orientation.Vertical){
   contents += currentScreen
   contents += instructionsScreen
   contents += gameScreen
   
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
                                    playerNameVec = playerNameVec ++ playerName.map(_.text)
                                    println("Name entered " + playerNameVec)
                                   Game.newGame(compOpponent, playerNameVec)
                                   currentScreen.visible = false
                                   currentPlayer = Game.players(0)
                                   gameScreen.visible = true
                                   gameScreen.revalidate()
                                   gameScreen.repaint()
                                  }
                                   else{
                                   currentScreen.visible = true
                                   instructionsScreen.visible = false}
     case e : MouseClicked => println(e)
   }
 }
  
  
  
  
  
  
  
    def menu = {
    new MenuBar{
      contents += new Menu("New Game"){
        contents += newGameMenuItem
      }
      contents += new Menu("Settings"){
         contents += new MenuItem(Action("Open"){
           openFile
         })
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
    centerOnScreen
  }
  top.visible = true
  
  
  
}