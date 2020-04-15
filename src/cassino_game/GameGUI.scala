package cassino_game

import scala.swing._
import scala.swing.event._
import java.io._
import java.awt.Color

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
  
  def openInstructions = ???
  
  def nameInput(count : Int) = {
    val panel = new BoxPanel(Orientation.Vertical){
      for (i <- 0 until count){
        contents += new BoxPanel(Orientation.Horizontal){
          contents += new Label("Enter your name")
          contents += new TextField()
          this.font = new Font("Arial",java.awt.Font.CENTER_BASELINE,12)
           this.maximumSize_=(new Dimension(300,30))
        }
        this.maximumSize_=(new Dimension(300,200))
      }
    }
    panel
  }
  
  //Initital screen
  var currentScreen = new BoxPanel(Orientation.Vertical){
    
    var players = 0
    val playerCount  = new ComboBox(Vector(1,2,3,4,5,6))
    var playerFields  = new BoxPanel(Orientation.Vertical){
      contents += new TextField("Alone? Here with your gang?"){
         horizontalAlignment = Alignment.Center
         this.editable = false
         this.maximumSize_=(new Dimension(1000,300))
      }
    }
    val okButton = new Button("OK")
    val enterNameButton = new Button("Next")
    
    //Header : "Cassino: New Game"
    contents += new FlowPanel{
      contents += new Label("Cassino: New Game"){
        this.foreground = (Color.BLUE)
        font = new java.awt.Font("Serif",java.awt.Font.BOLD,48)
      }
       this.maximumSize = (new Dimension(1000,300))
    }
    //Flow Panel for input
    contents += new FlowPanel{
       val button = okButton
       contents += new Label("How many players?")
       contents += playerCount
       contents += button
       contents += nameInput(players)       
       this.maximumSize = (new Dimension(300,100))
    }
   
   contents += playerFields
    this.listenTo(okButton)
    this.listenTo(enterNameButton)
    this.reactions += {
         case e : ButtonClicked =>{ players = playerCount.selection.item.toString().toInt
                                   println(players)
                                   playerFields.contents.clear()
                                   playerFields.contents += nameInput(players)
                                   this.contents += enterNameButton
                                   this.revalidate()
                                   this.repaint()
                                   }
     }
//      override def paintComponent(g: Graphics2D) ={
//       val image = javax.imageio.ImageIO.read(new File("1C.png"))
//       g.drawImage(image, 250,250, 70,100,null )
//      }
    }
  
  
  
  
  
  
  
  
  
  
  
    def menu = {
    new MenuBar{
      contents += new Menu("New Game"){
        
      }
      contents += new Menu("Settings"){
         contents += new MenuItem(Action("Open"){
           openFile
         })
         contents += new MenuItem(Action("Save"){
           saveFile
         })
         contents += new MenuItem(Action("Exit"){
           sys.exit(0)
         })
      }
      contents += new Menu("Rules"){
        contents += new MenuItem(Action("Instructions"){
          openInstructions
        })
        
      }
      //this.listenTo(MenuBar)
    }
  }
  
  
  //Main body 
  val top = new MainFrame {  
    title = "Cassino"
    menuBar = menu
    contents =  currentScreen
    size = new Dimension(1000,750)
    centerOnScreen
  }
  top.visible = true
  
  
}