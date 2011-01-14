/* Copyright © 2006 - Fabien GIGANTE */

import java.util.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
//import com.nokia.mid.ui.*;
import de.enough.polish.midp.ui.*;
import de.enough.polish.util.*;
/**
 * Specialized TimerTask used to keep the LCD lights on during the game.
 */
class KeepLightsOnTask extends TimerTask
{
  /** Typically scheduled every 10 sec. */
  public final static int PERIOD = 10000;
  /** Triggered by the Timer. Set on the LCD lights. */
  public void run()
  {
    DeviceControl.lightOn();
  }
}

/**
 * Specialized FullCanvas used to implement a game, providing:
 * - an animation thread for periodic rendering (frame rate)
 * - a timer thread for game event sequencing (including keeping the lights on)
 * - game persistency management in record stores
 * 
 * This is NOT the MIDP 2.0 javax.microedition.lcdui.game.GameCanvas
 */
abstract class GameCanvas extends Canvas
{
  /** Parent MIDlet */
  protected GameMIDlet midlet;
  /** Rendering frequency used by the animation thread */
  protected final static int FRAMERATE_MILLISEC = 66;

  /** Canvas width in pixels */
  protected final int gameWidth;
  /** Canvas height in pixels */
  protected final int gameHeight;

  /** "In pause" status of the game */
  protected volatile boolean isPaused = false;

  /** All game events are scheduled and sequenced by a single Timer thread */
  protected Timer timer = null;
  /** Rendering is done in a dedicated animation thread */
  private volatile Thread animationThread = null;
  /** Flag set to false if this game canvas was never rendered, to true otherwise */
  private volatile boolean hasBeenShown = false;

  /** No repaint is needed, display is up to date */
  protected static final int REPAINT_NONE = 0;
  /** Repaint requested by a timer event (generally minor changes) */
  protected static final int REPAINT_TIMER = 1;
  /** Repaint requested by a key event (generally more important changes) */
  protected static final int REPAINT_KEY = 2;
  /** Full repaint is needed, display must be rendered again entirely */
  protected static final int REPAINT_ALL = 3;
  /** Tells the animation threads what elements should be rendered again */
  protected volatile int needsRepaint;

  /**
   * Game events are scheduled and sequenced by a Timer thread of GameCanvas.
   * Game events should inherit from this GameTask abstract class.
   */
  protected abstract class GameTask extends TimerTask { }

  /**
   * Game event tasks that require a display rendering refresh should inherit from this class
   */
  protected class RepaintTask extends GameTask
  {
    /** Triggered by the Timer. Ask the animation thread for a repaint (due to timer). */
    public void run()
    {
      needsRepaint|=REPAINT_TIMER;
    }
  }

  /** Constructor is called by the owning game MIDlet */
  GameCanvas(GameMIDlet midlet)
  {
    this.midlet = midlet;
    gameWidth = getWidth();
    gameHeight = getHeight();
    needsRepaint = REPAINT_ALL;
  }

  /** Animation thread for rendering  */
  private class AnimationThread extends Thread
  {
    /** Main loop for animation thread. Manage rendering every frame rate, when necessary. */
    public void run()
    {
      try
      {
        // when the GameCanvas will set its animationThread member to null, this thread will die
        while (this == animationThread)
        {
          long startTime = System.currentTimeMillis();
          if (!isPaused)
          {
            if (isShown()) hasBeenShown = true;
            else if (hasBeenShown) pause();
          }
          if (isShown() && needsRepaint != REPAINT_NONE)
          {
            // a rendering is needed
            needsRepaint = REPAINT_NONE;
            repaint(0, 0, gameWidth, gameHeight);
            serviceRepaints();
          }
          long timeTaken = System.currentTimeMillis() - startTime;
          // see you soon (at next frame rate)...
          if (timeTaken < FRAMERATE_MILLISEC)
            synchronized (this) { wait(FRAMERATE_MILLISEC - timeTaken); }
          else yield();
        }
      }
      catch (InterruptedException e) { }
    }
  }

  /** Start the animation thread and start/resume the game */
  public synchronized void start()
  {
    animationThread = new AnimationThread();
    animationThread.start();
    resume();
  }

  /** Pause/stop the game and kill the animation thread */
  public synchronized void stop()
  {
    pause();
    animationThread = null;
  }

  /** Pause or stop the game. Timer is destroyed. */
  public synchronized void pause()
  {
    GameMIDlet.MIDLET.musicManager.stopAudioPlayer();
    needsRepaint = REPAINT_ALL; isPaused = true;
    if (timer != null) { timer.cancel(); timer = null; }
  }

  /** Resume or start the game. Timer is constructed, its tasks are scheduled. */
  public synchronized void resume()
  {
    needsRepaint = REPAINT_ALL; isPaused = false;
    timer = new Timer();
    timer.schedule(new KeepLightsOnTask(), 0, KeepLightsOnTask.PERIOD);
    
  }

  /** Game must be paused when the screen is hidden */
  public void hideNotify() { pause(); }

  /**
   * Get the game action associated with the given key code of the device.
   * Overloaded to support button keys.
   */
  public int getGameAction(int keyCode)
  {
    int actionCode = 0;
    try
    {
      actionCode = super.getGameAction(keyCode);
    }
    catch (Exception e) { }
    if (actionCode == 0)
      switch (keyCode)
      {
        case KEY_NUM1:
          actionCode = GAME_A;
          break;
        case KEY_NUM3:
          actionCode = GAME_B;
          break;
      }
    return actionCode;
  }

  /** Called when a game action key is pressed */
  abstract protected void gameAction(boolean key_down, int actionCode);

  /** Pressing a key trigers a game action or resumes a paused game */
  public void keyPressed(int keyCode)
  {
    int actionCode = getGameAction(keyCode);
    if (isPaused && isShown()) resume();
    if (actionCode != 0) gameAction(true,actionCode);
    needsRepaint |= REPAINT_KEY;
  }
  protected void keyReleased(int keyCode) {
    int actionCode = getGameAction(keyCode);
    if (actionCode != 0) gameAction(false,actionCode);
    needsRepaint |= REPAINT_KEY;
  } 
  protected void pointerPressed(int x, int y) {
  
      if (y > (gameHeight-30)) {
         if (x > (gameWidth - 40)) {
           keyPressed(-7); //exit
         } 
         return;
      }
  }

  /** Implemented by derivated classes to serialize the persistent state of the game */
  protected abstract void serializeState() ;

  /** Implemented by derivated classes to deserialize the persistent state of the game */
  protected abstract void deserializeState(); 

  /** Saves the game in a persistent record store */
  protected void saveToStore(String storeName) throws IOException, RecordStoreException
  {
//#if 0
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataOutput output = new DataOutputStream(stream);
    serializeState(output);
    RecordStore store = RecordStore.openRecordStore(storeName, true);
    byte[] record = stream.toByteArray();
    if (store.getNumRecords() == 0)
      store.addRecord(record, 0, record.length);
    else
      store.setRecord(1, record, 0, record.length);
    store.closeRecordStore();
//#endif
   serializeState();

  }

  /** Loads the game from a persistent record store */
  protected void loadFromStore(String storeName) throws IOException, RecordStoreException
  {
//#if 0
    RecordStore store = RecordStore.openRecordStore(storeName,false);
    if (store != null)
    {
      ByteArrayInputStream stream = new ByteArrayInputStream(store.getRecord(1));
      store.closeRecordStore();
      DataInput input = new DataInputStream(stream);
      deserializeState(input);
    }
//#endif
    deserializeState();
  }
}
