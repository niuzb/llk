/* Copyright © 2006 - Fabien GIGANTE */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import de.enough.polish.io.RmsStorage;

/**
 * Game MIDLet composed of a game canvas.
 */
public class GameMIDlet extends MIDlet
{
  public static GameMIDlet MIDLET;

  /** Game canvas */
  GameCanvas canvas = null;

  /** Create the game canvas, to be overloaded */
  public GameCanvas createCanvas()
  {
     return new LodeRunnerCanvas(this);
  }
  DynaMenu menu;
  Rsm rsm;
  MusicManager musicManager;

  RsmManager rsmManager;
  
  public GameMIDlet() {
     MIDLET = this;
     rsmManager = new RsmManager();
     this.rsm = rsmManager.rsm;
     musicManager = new MusicManager();
     menu = new DynaMenu();
        if(rsm.level > 0) 
            menu.showResumeItem(true);
     menu.goMainMenu();
     menu.show_menu();
  }

  

  /** start the game, called from menu*/
  public void startGame() {
      rsm.level = 0;
      rsm.life = 5;
      canvas = createCanvas();
      canvas.start();
      Display.getDisplay(this).setCurrent(canvas);
      //musicManager.start_bg_music();
  }

  /** set d as current displsay  */
  public void  setCurrentDisplay(Displayable d) {   
    Display.getDisplay(this).setCurrent(d);
  }

  
  /** resume the game, called from menu*/
  public void resumeGame() {
      if(canvas == null) {
        canvas = createCanvas();
        canvas.start();
      } else {
        canvas.resume();
      }
      Display.getDisplay(this).setCurrent(canvas);
  }
  
  /** Start the game canvas */
  public void startApp()
  {
  
    if (canvas != null) {
       canvas.resume();
       //musicManager.start_bg_music(); 
    }
  }

  
  /** Pause the game canvas */
  public void pauseApp()
  {
    if (canvas != null) 
        canvas.pause();
    rsmManager.saveRsm();
    musicManager.stopAudioPlayer();
  }

  /** Stop the game canvas */
  public void destroyApp(boolean unconditional)
  {
    if (canvas != null) canvas.stop();
    musicManager.destroyAudioPlayer();
  }


}
