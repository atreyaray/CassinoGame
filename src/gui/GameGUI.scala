package gui

import scala.swing._
import scala.swing.event._
import java.io._
import java.awt.{Color,Font,Graphics2D,BasicStroke}
import javax.imageio.ImageIO
import javax.swing.SwingUtilities._
import cassino_game._


object GameGUI extends SimpleSwingApplication{
  
  
  var currentPlayer = new Player("")
  var playerNameVec = Vector[String]()
  val instructionMenuItem = new MenuItem("Instructions")
  val newGameMenuItem = new MenuItem("New Game")

 var finalFrame = new BoxPanel(Orientation.Vertical){
   contents += CurrentScreenGUI
   contents += InstructionScreenGUI
   contents += GameScreenGUI
   background = new Color(0,100,0)
    this.listenTo(instructionMenuItem)
    this.listenTo(newGameMenuItem)
    this.listenTo(CurrentScreenGUI.enterNameButton)
    this.listenTo(InstructionScreenGUI.instructionButton)
   // this.listenTo(gameScreen)
    this.reactions+= {
     case any : ButtonClicked => //when "instructions" is clicked under the heading  
                                if(any.source == instructionMenuItem){
                                   CurrentScreenGUI.visible = false
                                   GameScreenGUI.visible = false
                                   InstructionScreenGUI.visible = true
                                 }
                                //"New Game" is pressed inside the menu bar
                                 else if(any.source == newGameMenuItem){
                                   Game.newGame(false, Vector())
                                   playerNameVec = Vector[String]()
                                   GameScreenGUI.visible = false
                                   InstructionScreenGUI.visible = false
                                   CurrentScreenGUI.visible = true
                                   print(Game.toString())
                                   println("Game ended")
                                   
                                 }
                                //All the names are entered
                                 else if(any.source == CurrentScreenGUI.enterNameButton){
                                    //reset the vector containing names
                                    playerNameVec = Vector[String]()
                                    playerNameVec = playerNameVec ++ CurrentScreenGUI.playerName.map(_.text)
                                    playerNameVec = playerNameVec.filterNot(_.isEmpty())
                                    //new Game with given input data
                                    Game.newGame(CurrentScreenGUI.compOpponent, playerNameVec)
                                    //set currentPlayer
                                    currentPlayer = Game.players(0)
                                    //switching screens
                                    CurrentScreenGUI.visible = false
                                    GameScreenGUI.visible = true
                                    //repaint
                                    GameScreenGUI.revalidate()
                                    GameScreenGUI.repaint()
                                  }
                                   else{
                                   if(Game.players.isEmpty) CurrentScreenGUI.visible = true
                                   else GameScreenGUI.visible = true
                                   InstructionScreenGUI.visible = false}
     case e : MouseClicked =>  if (Game.isWon && !GameScreenGUI.winner.isEmpty){
                                   CurrentScreenGUI.compToggleButton.selected = false
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
      Game.fileHandler.loadGame(chooser.selectedFile)
      //updating current player
      currentPlayer = Game.players.filter(_.name == Game.currentPlayer.name)(0)
      //assign Icons 
      var tempPlayer = currentPlayer
      var index = 1
      do{
        if (tempPlayer.compPlayer) {
          tempPlayer.icon = Some(ImageIO.read(new File("./ImageResources/robot.png")))
        }
        else {
          tempPlayer.icon = Some(ImageIO.read(new File("./ImageResources/p"+ index + ".png")))
          index += 1
        }
        tempPlayer = Game.nextPlayer(tempPlayer)
      }while(tempPlayer.name != currentPlayer.name)
      println(currentPlayer)
      //front page is made invisible
      CurrentScreenGUI.visible = false
      //instruction screen is made invisible
      InstructionScreenGUI.visible = false
      //game screen is made visible
      GameScreenGUI.visible = true
      GameScreenGUI.repaint()
    }
  }
  
  //saves file into a text File
  def saveFile = {
     val chooser  = new FileChooser
    if(chooser.showSaveDialog(null)==FileChooser.Result.Approve){
      Game.currentPlayer = currentPlayer
      Game.fileHandler.saveGame(chooser.selectedFile)
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
    resizable = false
  }
  top.visible = true
   
}