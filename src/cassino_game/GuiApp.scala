package cassino_game

import scala.swing._
import java.awt.{Color,Font}
import java.io.File
import javax.imageio.ImageIO

object GuiApp extends SimpleSwingApplication {
  
  val finalFrame = new GamePanel
  
  class GamePanel extends Panel{
    
    Game.addPlayers(Vector("Computer","Atreya","Long","Aayush","Sergey","Dean","Last"))
    Game.players.foreach(_.points = 3)
    
      override def paintComponent(g: Graphics2D) = {
      g.setColor(new Color(0,255,0))
      g.fillRect(0, 0, 1000, 750 )
      g.setColor(Color.BLACK)
      g.setFont(new Font("Monospaced",Font.BOLD,48))
      g.drawString("Points", 425, 50)
      g.setFont(new Font("Monospaced",Font.BOLD,32))
        var offSet = 0
          if (Game.players(0).name == "Computer"){
            offSet = 1
            g.drawImage(ImageIO.read(new File("./ImageResources/robot.png")), 50, 80, 50, 50,null)
            g.drawString(Game.players(0).points.toString(),300, 120)
          }
          for(i <- offSet until Game.players.length){
              g.drawImage(ImageIO.read(new File("./ImageResources/p"+(i+1-offSet) +".png")), 50, 80 + 100*i, 50,50,null)
              g.drawString(Game.players(i).points.toString(),300, 120+100*i)
          }
      g.setColor(new Color(30,144,255))
      g.fillRect(575 , 75, 400, 650)
      g.setColor(Color.WHITE)
      g.drawLine(585, 85, 585, 715)
      g.drawLine(965, 85, 965, 715)
      g.drawLine(585, 85, 965, 85)
      g.drawLine(585, 715, 965, 715)
      
      g.setFont(new Font("Monospaced",Font.BOLD,15))
      g.drawString("Maximum Cards Captured : 2 points" , 595, 125)
      g.drawString("Maximum Spades Captured : 1 points" , 595, 205)
      g.drawString("Every Ace Captured : 1 points" , 595, 285)
      g.drawString("Every Sweep : 1 points" , 595, 365)
      g.drawString("10 of Diamonds : 2 points" , 595, 445)
      g.drawString("2 of Spades : 1 points" , 595, 525)
      
      g.setColor(Color.WHITE)
      g.fillRect(750, 665, 75 , 25)
      g.setColor(Color.BLACK)
      g.setFont(new Font("Monospaced",Font.BOLD,22))
      g.drawString("OK", 775, 685)
      val cards = Vector("h5","s3","ck","dq").map(new Card(_)).map(_.image)
      cards.foreach(g.drawImage(_, 100, 100, 90, 120, null))
      }
  }
  
  val top = new MainFrame{  
    title = "Cassino"
    contents =  finalFrame
    size = new Dimension(1000,750)
    centerOnScreen
  }
}