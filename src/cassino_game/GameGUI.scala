package cassino_game

import scala.swing._
import scala.swing.event._
import java.io._
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
    //this.background = new Color(173,216,230) 
    //this.contents += new Label("Game started!")
      visible = false
      var flag = false
     def paintComp(g: Graphics2D) = {
      g.setColor(new Color(73,159,104))
      g.fillRect(0, 0, 1000, 750)
      g.setBackground( new Color(173,230,216))
      val image = Game.cardsOnTable.map(_.image)
      val playerCards = Game.players(0).cardsInHand.map(_.image)
      g.drawImage(ImageIO.read(new File("p2.png")), 190, 510, 50, 50,null)
      g.setFont(new Font("SanSerif",Font.BOLD,15))
      g.setColor(Color.BLACK) 
     //  g.drawString("Hello",100,100)
      g.drawString("Player 1", 190, 575)
      for(i <- 0 until image.length){
       g.drawImage(image(i), 290 + 100*i , 220,90 ,120,null)
       g.drawImage(playerCards(i),290 + 100*i, 500, 90, 120 , null)}
      }
      override def paintComponent(g : Graphics2D) ={
        if (!flag) paintComp(g)
        else g.drawString("Hello",500,500)
      }
//       println(this)
       this.listenTo(this.mouse.clicks,this.mouse.moves)
       this.reactions += {
      case e : MouseClicked => if(e.point.x >= 290 && e.point.x < 380 && e.point.y > 500 && e.point.y < 620) {
        flag = true
        this.revalidate()
        this.repaint()
        println("Hello it's working")
      }else println(e.point)
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