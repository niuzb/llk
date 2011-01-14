
/* Copyright © 2006 - Fabien GIGANTE */

/**
 * Lode Runner game MIDLet.
 * Replicate the original game behavior fidely.
 */
public class LodeRunnerMIDlet extends GameMIDlet
{
  /** Create a Lode Runner game canvas */
  public GameCanvas createCanvas()
  {
    return new LodeRunnerCanvas(this);
  }
  
  public void startApp() {
     super.startApp();
  }
    /** Pause the game canvas */
  public void pauseApp()
  {
    super.pauseApp();
  }

  /** Stop the game canvas */
  public void destroyApp(boolean unconditional)
  {
    super.destroyApp(unconditional);
  }
}
