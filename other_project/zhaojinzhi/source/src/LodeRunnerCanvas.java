/* Copyright © 2006 - Fabien GIGANTE */

import java.util.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import de.enough.polish.midp.ui.*;
import de.enough.polish.util.Locale;
/**
 * Specialized GameCanvas used to implement the Lode Runner game.
 */
class LodeRunnerCanvas extends GameCanvas
{
  /** Game name use for store persistency */
  private static final String GAME_NAME = "xianleLodeRunner";
  /** Game splash screen */
  private GameSprite splashScreen = null;
  /** Message to display when game is paused */
  private String pauseMessage = null;

  /** Maximum number of lifes given to the player when starting the game (constant)*/
  private static final int MAX_LIFES = 5;
  /** Lifes left to the player */
  private int lifes = MAX_LIFES;
  /** Level number of the current stage */
  public int level = 0;
  /** Current stage, when game is in progress */
  private LodeRunnerStage stage = null;

  /**
   * Construct the Lode Runner canvas
   * - load resources
   * - read level from store
   * - load stage with current level
   */
  LodeRunnerCanvas(GameMIDlet midlet)
  {
    super(midlet);
	setFullScreenMode(true);
    // Load resources 
    try
    {
      splashScreen = new GameSprite("/LodeRunner.png",112,16,0,0);
    }
    catch (Exception e) {}
    // Read saved members from store
    try
    {
      loadFromStore(GAME_NAME);
    }
    catch (Exception e) {}
    // Load stage with current level
    try
    {
      stage = new LodeRunnerStage(this);
      stage.loadFromResource();
    }
    catch (Exception e) {}
    needsRepaint = REPAINT_ALL;
  }

  /** Implement game serialization (level, lifes) */
  public void serializeState()
  {
  //#if 0
    output.writeInt(level); output.writeInt(lifes);
  //#endif
    GameMIDlet.MIDLET.rsm.level = level;
    GameMIDlet.MIDLET.rsm.life = lifes;
    GameMIDlet.MIDLET.rsmManager.saveRsm();
  }

  /** Implement game deserialization (level, lifes) */
  public void deserializeState() 
  {
  //#if 0
    level = input.readInt(); lifes = input.readInt();
  //#endif
    level = GameMIDlet.MIDLET.rsm.level;
    lifes = GameMIDlet.MIDLET.rsm.life;
  }

  /** Render the message or splash screen */
  private void paintMessage(Graphics g, int w, int h)
  {
    Font font = null;
    // There is a message to render
    if (pauseMessage != null)
    {
      font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD | Font.STYLE_ITALIC, Font.SIZE_LARGE);
      g.setFont(font); g.setColor(0x00ffffff);
      g.drawString(pauseMessage, w / 2, (h - font.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
    }
    // No message but a splash screen
    else if (splashScreen != null)
    {
      if ((level / LodeRunnerStage.GAME_LEVELS) % 2 == 0)
      {
        // Lode Runner splash screen
        splashScreen.paint(g, 0, w / 2, h / 2, Graphics.HCENTER | Graphics.VCENTER);
      }
      else
      {
        // Championship splash screen
        splashScreen.paint(g, 0, w / 2, h / 2 - 6, Graphics.HCENTER | Graphics.VCENTER);
        splashScreen.paint(g, 1, w / 2, h / 2 + 6, Graphics.HCENTER | Graphics.VCENTER);
      }
    }
    // No message, no splash screen
    else
    {
      // Simulate a splash screen with text
      if ((level / LodeRunnerStage.GAME_LEVELS) % 2 == 0)
      {
        // Lode Runner splash screen
        font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(font); g.setColor(0x00ffffff);
        g.drawString("Lode Runner", w / 2, (h - font.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
      }
      else
      {
        // Championship splash screen
        font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(font); g.setColor(0x00ffffff);
        g.drawString("Lode Runner", w / 2, (h - font.getHeight()) / 2 - 6, Graphics.HCENTER | Graphics.TOP);
        font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_ITALIC, Font.SIZE_LARGE);
        g.setFont(font); g.setColor(0x00ff0000);
        g.drawString("Championship", w / 2, (h - font.getHeight()) / 2 + 6, Graphics.HCENTER | Graphics.TOP);
      }
    }
  }

  /** Render the game canvas */
  public void paint(Graphics g)
  {
    // Render the stage
    if (stage != null)
    {
      stage.spriteSize = LodeRunnerStage.SPRITE_NORMAL;
      stage.paint(g);
      if (isPaused || !stage.isLoaded)
      {
        // Low-light the stage
        int w0 = g.getClipWidth(), h0 = g.getClipHeight();
        //DirectGraphics dg = DirectUtils.getDirectGraphics(g);
        //dg.setARGBColor(0xa0000000); 
        g.setColor(0xa0000000);
        g.fillRect(0, 0, w0, h0);
        // Render the stage mini-map
        stage.spriteSize = LodeRunnerStage.SPRITE_SMALL;
        stage.paint(g);
        int cx = stage.STAGE_WIDTH * stage.SPRITE_WIDTH[stage.spriteSize];
        int cy = stage.STAGE_HEIGHT * stage.SPRITE_HEIGHT[stage.spriteSize];
        int x = (w0 - cx) / 2, y = (h0 - cy) / 2;
        // Render the message or splash screen
        paintMessage(g, w0, y);
        // Display game information
        Font font = Font.getDefaultFont(); g.setFont(font); g.setColor(0x00ffffff);
        stage.sprites[LodeRunnerStage.SPRITE_NORMAL].paint(g, stage.spriteMap[LodeRunnerStage.TILE_LADDER], x, y + cy + 3);
        g.drawString(Locale.get("Play.level") + Integer.toString(level % LodeRunnerStage.GAME_LEVELS + 1), x + LodeRunnerStage.SPRITE_WIDTH[LodeRunnerStage.SPRITE_NORMAL] + 1, y + cy + 3, Graphics.TOP | Graphics.LEFT);
        stage.sprites[LodeRunnerStage.SPRITE_NORMAL].paint(g, stage.spriteMap[LodeRunnerStage.TILE_HERO], x + 3 * cx / 4, y + cy + 3);
        g.drawString("x" + Integer.toString(lifes+1), x + 3 * cx / 4 + LodeRunnerStage.SPRITE_WIDTH[LodeRunnerStage.SPRITE_NORMAL] + 1, y + cy + 3, Graphics.TOP | Graphics.LEFT);
        // Display stage information
        if (stage.isLoaded)
        {
          stage.sprites[LodeRunnerStage.SPRITE_NORMAL].paint(g, stage.spriteMap[LodeRunnerStage.TILE_CHEST], x, y + cy + 3 + LodeRunnerStage.SPRITE_HEIGHT[LodeRunnerStage.SPRITE_NORMAL]);
          g.drawString(Integer.toString(stage.hero == null ? 0 : stage.hero.nChests) + "/" + Integer.toString(stage.nChests), x + LodeRunnerStage.SPRITE_WIDTH[LodeRunnerStage.SPRITE_NORMAL] + 1, y + cy + 3 + LodeRunnerStage.SPRITE_HEIGHT[LodeRunnerStage.SPRITE_NORMAL], Graphics.TOP | Graphics.LEFT);
          stage.sprites[LodeRunnerStage.SPRITE_NORMAL].paint(g, stage.spriteMap[LodeRunnerStage.TILE_MONK], x + 3 * cx / 4, y + cy + 3 + LodeRunnerStage.SPRITE_HEIGHT[LodeRunnerStage.SPRITE_NORMAL]);
          g.drawString("x" + Integer.toString(stage.vilains.size()), x + 3 * cx / 4 + LodeRunnerStage.SPRITE_WIDTH[LodeRunnerStage.SPRITE_NORMAL] + 1, y + cy + 3 + LodeRunnerStage.SPRITE_HEIGHT[LodeRunnerStage.SPRITE_NORMAL], Graphics.TOP | Graphics.LEFT);
        }
        else
        {
          g.setColor(0x00ffff00);
          g.drawString("‘ÿ»Î...", x + cx / 2, y + (cy - font.getHeight()) / 2, Graphics.TOP | Graphics.HCENTER);
          g.setColor(0x001463af);
          //g.drawString("© 2006 - Fabien GIGANTE", w0 / 2, h0 - 2, Graphics.HCENTER | Graphics.BOTTOM);
        }
      }
    }
  }
  int alien_xvel=0, alien_yvel=0;

  /** Handle game actions */
  protected void gameAction(boolean key_down, int actionCode)
  {
    if (!stage.isLoaded || stage.hero == null) return;
    if(key_down) {
        switch (actionCode)
        {
          case UP:
            alien_yvel = -1;
            stage.hero.requestMove(LodeRunnerCharacter.MOVE_CLIMB_UP);
            break;
          case DOWN:
            alien_yvel =  1;
            stage.hero.requestMove(LodeRunnerCharacter.MOVE_CLIMB_DOWN);
            break;
          case LEFT:
            alien_xvel = -1;
            stage.hero.requestMove(LodeRunnerCharacter.MOVE_RUN_LEFT);
            break;
          case RIGHT:
            alien_xvel =  1;
            stage.hero.requestMove(LodeRunnerCharacter.MOVE_RUN_RIGHT);
            break;
          case GAME_A:
          case GAME_C:
            stage.hero.requestMove(LodeRunnerHero.MOVE_DIG_LEFT);
            break;
          case GAME_B:
          case GAME_D:
            stage.hero.requestMove(LodeRunnerHero.MOVE_DIG_RIGHT);
            break;
          case FIRE:
            stage.hero.requestMove(stage.hero.lookLeft ? LodeRunnerHero.MOVE_DIG_LEFT : LodeRunnerHero.MOVE_DIG_RIGHT);
            break;
        }
    } else { //key up
        switch (actionCode)
        {
          case UP:
                if(alien_yvel < 0) {
                    alien_yvel = 0;
                }
            break;
          case DOWN:
             if(alien_yvel > 0) {
                    alien_yvel = 0;
             }
            break;
          case LEFT:
            if( alien_xvel < 0 )
                alien_xvel = 0;
            break;
          case RIGHT:
            if( alien_xvel > 0 )
                 alien_xvel = 0;
            break;
        }

    }
  }

  /**
   * Get the game action associated with the given key code of the device.
   * Overloaded to cancel button actions when resuming a paused game.
   */
  public int getGameAction(int keyCode)
  {
    int actionCode = super.getGameAction(keyCode);
    if (isPaused)
      switch (actionCode)
      {
        case GAME_A:
        case GAME_C:
        case GAME_B:
        case GAME_D:
        case FIRE:
          actionCode = 0;
          break;
      }
    return actionCode;
  }

  
  private void processBackCmd() {
     GameMIDlet.MIDLET.menu.showResumeItem(true);
     GameMIDlet.MIDLET.menu.goMainMenu();
     GameMIDlet.MIDLET.menu.show_menu();
  }
  
  /** Handle special keys (that are not game actions) */
  public void keyPressed(int keyCode)
  {
    if(keyCode == -7) {
        pause();
        processBackCmd();
        //#if 0
       GameMIDlet.MIDLET.destroyApp(false);
       GameMIDlet.MIDLET.notifyDestroyed();
       //#endif
       return;
    }
    if (!isPaused && keyCode == KEY_NUM0) pause();
    //#if 0 
    //added by niuzb
    else if (isPaused && keyCode == KEY_SOFTKEY1) { stageOver(true); pauseMessage = null; }
    else if (isPaused && keyCode == KEY_SOFTKEY2) stageOver(false);
    //#endif
    else super.keyPressed(keyCode);
  }

  /** Called when the game is stopped. Save its current state in store. */
  public synchronized void stop()
  {
    super.stop();
    try
    {
      saveToStore(GAME_NAME); 
    }
    catch (Exception e) {}
  }

  /**
   * Define the hero's heartBeat as a game event task.
   */
  protected class HeroHeartbeatTask extends RepaintTask
  {
    /** Scheduled every frame */
    public final static int PERIOD = FRAMERATE_MILLISEC;
    /** Heartbeat */
    public void run()
    {
      if (stage != null && stage.isLoaded && stage.hero != null)
      {
        stage.hero.heartBeat();
        if (stage.endCompleted) stageOver(true);
        else if (stage.endHeroDied) stageOver(false);
      }
      super.run();
    }
  }

  /**
   * Define the vilains' heartBeat as a game event task.
   */
  protected class VilainsHeartbeatTask extends RepaintTask
  {
    /** Scheduled every 2 frames */
    public final static int PERIOD = 2 * FRAMERATE_MILLISEC;
    /** Heartbeat */
    public void run()
    {
      // Loop on every vilain
      if (stage != null && stage.isLoaded)
        for (Enumeration e = stage.vilains.elements(); e.hasMoreElements(); )
          ((LodeRunnerVilain)e.nextElement()).heartBeat();
      super.run();
    }
  }

  /**
   * Define the stage's heartBeat as a game event task.
   */
  protected class StageHeartbeatTask extends RepaintTask
  {
    /** Scheduled every 2 frames */
    public final static int PERIOD = 2 * FRAMERATE_MILLISEC;
    /** Heartbeat */
    public void run()
    {
      // Loop on every hole
      if (stage != null && stage.isLoaded)
        for (Enumeration e = stage.holes.elements(); e.hasMoreElements(); )
          ((LodeRunnerHole)e.nextElement()).heartBeat();
      super.run();
    }
  }

  /** Start the Lode Runner game, in pause */
  public synchronized void start()
  {
    super.start();
    pause();
  }

  /**
   * Resume or start the game.
   * Lode Runner heartBeats game tasks are scheduled.
   */
  public synchronized void resume()
  {
    if(stage != null &&stage.isLoaded)
        GameMIDlet.MIDLET.musicManager.start_bg_music();

    super.resume();
    pauseMessage = null;
    // Schedule the hero's heartBeat
    timer.schedule(new HeroHeartbeatTask(), 0, HeroHeartbeatTask.PERIOD);
    // Schedule the vilains' heartBeat
    timer.schedule(new VilainsHeartbeatTask(), 0, VilainsHeartbeatTask.PERIOD);
    // Schedule the stage's heartBeat
    timer.schedule(new StageHeartbeatTask(), 0, StageHeartbeatTask.PERIOD);
  }

  /**
   * Stage is complete, or hero has died
   */
  public void stageOver(boolean hasCompleted)
  {
    pause();
    GameMIDlet.MIDLET.musicManager.stopAudioPlayer();
    // Adjust lifes and level 
    if (hasCompleted)
    {
      level++;
      lifes++;
      if (level == LodeRunnerStage.MAX_LEVELS) level = 0;
      pauseMessage = (level % LodeRunnerStage.GAME_LEVELS == 0) ? null : 
        Locale.get("Play.guguan");
      serializeState();  
    }
    else
    {
      
      lifes--;
      if (lifes < 0)
      {
        lifes = MAX_LIFES; level = 0; pauseMessage = Locale.get("play.game_over");
      }
      
      else pauseMessage = Locale.get("play.try_again");
    }
    // Load appropriate stage
    try
    {
      stage.loadFromResource();
    }
    catch (Exception e) { }
    needsRepaint = REPAINT_ALL;
  }
}
