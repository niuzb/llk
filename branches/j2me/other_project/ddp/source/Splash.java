import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.media.*;
import java.io.*;
import de.enough.polish.util.Locale;
import javax.microedition.media.control.*;

public class Splash extends Canvas implements Runnable
{
   public final  int SCREEN_WIDTH = getWidth();
   public final  int SCREEN_HEIGHT = getHeight();

   private boolean running = true;
   //private Graphics osg;
   private llk theMidlet;

   
   //private Font font;
   //private int spriteSeq = 0;
   //private int maxSeq;
   //private Image osb;
   //private Sprite welcome;
   //private int fontHeight;
   Image bg;
   Font defaultFont;
   VolumeControl vc;
   public Splash(llk midlet)
   {
      theMidlet = midlet;
	  setFullScreenMode(true);
      initResources();
	  defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

      // create the timer thread
      Thread t = new Thread(this);
      t.start();
   }
   Player musicPlayer;

   private void initResources()
   {
      // setup the screen
	   try {
            bg = Image.createImage("/spla.png"); 
			//sun = Image.createImage("/sun.png"); 
			
			InputStream is = getClass().getResourceAsStream("/bg.mid");
			musicPlayer = Manager.createPlayer(is, "audio/midi");


			musicPlayer.prefetch();
			
			vc = (VolumeControl)musicPlayer.getControl("VolumeControl");
			if (vc != null)
				vc.setLevel(20);
			musicPlayer.setLoopCount(1);
			musicPlayer.start();
            //welcome = new Sprite(image, 128, 32); 
       } catch(Exception e) { e.printStackTrace(); }
	  //maxSeq = welcome.getFrameSequenceLength();
      //font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
      //fontHeight = font.getHeight() + 2;
	  //osb = Image.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
	  //osg = osb.getGraphics();
   }

   private  final int MAX_CPS = 10;
   private  final int MS_PER_FRAME = 1000 / MAX_CPS;
   private  final long  lastcycletime = System.currentTimeMillis();
   public void run()
   {
      try
      {
         while (running)
         {
            // remember the starting time
            long cycleStartTime = System.currentTimeMillis();

            // do our work
            repaint();

            // sleep if we've finished our work early
            long timeSinceStart = (System.currentTimeMillis()-cycleStartTime);
            if (timeSinceStart < MS_PER_FRAME)
            {
               try
               {
                  Thread.sleep(MS_PER_FRAME - timeSinceStart);
               }
               catch (java.lang.InterruptedException e)
               {
               }
            }
            if (System.currentTimeMillis()-lastcycletime > 15000){
            	running = false;
            }
          
         }
		
		try {
			musicPlayer.stop();
			musicPlayer.close();
		 } catch (Exception e) {
		   System.out.println("App exception: " + e);
		   e.printStackTrace();
		}
         // fall back to the splash form at the end of our loop
         theMidlet.createNewMenu();
      }
      catch (Exception e)
      {
         System.out.println("App exception: " + e);
         e.printStackTrace();
      }

   }
/*
   private void renderSplash()
   {
	  
      osg.setColor(0xffffff);
      osg.fillRect(0, 0, getWidth(), getHeight());
	  
	  welcome.setPosition((SCREEN_WIDTH-128)/2, (SCREEN_WIDTH-32)/2);
      welcome.paint(osg);
	  
	 
	  welcome.nextFrame();
      osg.setColor(0x00ffffff);
      osg.drawString("Press  any key", getWidth() / 2, getHeight() - fontHeight * 1,
                     Graphics.HCENTER | Graphics.TOP);
	
   }*/
   int x2=getWidth();
   int y2=getHeight();
   //boolean stop =false;
   //int x,y;
   //int vv = 35;
   protected void paint(Graphics graphics)
   {
     // renderSplash();
      //graphics.drawImage(osb, 0, 0, Graphics.LEFT | Graphics.TOP);
    int y = y2/2;
    graphics.drawImage(bg, 0, 0, Graphics.LEFT | Graphics.TOP);
    graphics.setFont(defaultFont);
    graphics.setColor(255,0,0);
    graphics.drawString(Locale.get("messages.welcome"), x2/2, y, Graphics.TOP|Graphics.HCENTER);

    graphics.drawString(Locale.get("messages.keyany"), x2/2, y+defaultFont.getHeight(), Graphics.TOP|Graphics.HCENTER);

  }
   protected void pointerPressed(int x, int y) {
		 running = false;
   }

   protected void keyPressed(int keyCode)
   {
      running = false;
   }
}


