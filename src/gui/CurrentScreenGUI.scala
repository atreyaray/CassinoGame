package gui

import scala.swing._
import scala.swing.event._
import java.io._
import javax.imageio._
import java.awt.{Font, Color}
import scala.Vector

object CurrentScreenGUI extends BoxPanel(Orientation.Vertical){
  
  val okButton = new Button("OK")
  val playerCount  = new ComboBox(Vector(1,2,3,4,5,6))
  val compToggleButton = new ToggleButton("No")
  var players = 0 
  var playerFieldPanel = new BoxPanel(Orientation.Vertical)
  var playerName = Vector[TextField]()
  val enterNameButton = new Button("Next")
  var playerFields = new BoxPanel(Orientation.Vertical)
  var compOpponent = false

 var topPanel = new FlowPanel{
                    contents += new Label("Cassino: New Game"){
                      this.foreground = (new Color(240,230,140))
                      font = new Font("Serif",java.awt.Font.BOLD,48)
                     }
                     this.maximumSize = (new Dimension(1000,100))
                    // this.background = new Color(181,192,245)
                     override def paintComponent(g : Graphics2D) ={
                       val image = ImageIO.read(new File("./ImageResources/icon.png"))
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
                                    if (!compOpponent && players == 1){}
                                    else{
                                       playerFieldPanel.contents.clear()
                                       playerFields = nameInput(players)
                                       playerFieldPanel.contents += playerFields
                                       this.contents += enterNameButton
                                       this.revalidate()
                                       this.repaint()
                                    }
                                  }

                                  else {
                                    println(e)
                                    compOpponent = compToggleButton.selected
                                    //update toggle button
                                    if (compToggleButton.selected) compToggleButton.text = "Yes"
                                    else compToggleButton.text = "No"
                                  }
         case e: MouseMoved => println(e)        
     }

}