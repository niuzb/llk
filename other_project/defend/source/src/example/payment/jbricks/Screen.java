/*
 *
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.Locale;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;


/**
 * This class handles and coordinates most of the drawing for this game. 
 * It knows how to draw the splash, menu and game over screen, as well as the
 * actual games screen. It also forwards 'cooked' key events to the Engine class.
 */
public class Screen extends javax.microedition.lcdui.Canvas {
    public static int width;
    public static int height;
     static final int TL_ANCHOR = Graphics.TOP | Graphics.LEFT;
     static final int TH_ANCHOR = Graphics.TOP | Graphics.HCENTER;
    public static Graphics GRAPHICS;
    public static final int BACKGROUND = ThreeDColor.gray.getRGB();
    

    private Font fontSmall;
    private Font fontMedium;
    private Font fontLarge;
    private Font font;
    
    public static final BitMapFont font1;
    
    private BitMapFontViewer viewer;
    static {
       font1 = BitMapFont.getInstance("/example.bmf" );
    }
    private int lastState;
    private int lastScore;
    private int lastlevel;
    Image buf;
    private Engine engine;
    private int fontHeight;
    private MIDlet midlet;
    /**
     * Creates Screen instance
     * 
     * @param midlet A MIDlet using this Screen
     */
    public Screen(MIDlet midlet) {
        setFullScreenMode(true);
        width = getWidth();
        height = getHeight();

        fontSmall = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        fontMedium = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        fontLarge = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        if (height < 180) {
            font = fontSmall;
        } else if (height >= 180 && height < 280) {
            font = fontMedium;
        } else {
            font = fontLarge;
        }

        fontHeight = font.getHeight();
        buf = Image.createImage(width, height);
        GRAPHICS = buf.getGraphics();
        
        paintBackground(GRAPHICS);
        
       // state = new EngineState();

        lastState = -1;
        lastScore = -1;
        lastlevel = -1;

        this.midlet = midlet;
        engine = Engine.ME;
    }

    /**
     * Sets game business logic
     *
     * @param game engine
     */

    /**
     * Called when a key is pressed
     *
     * @param keycode the key code of the key that was pressed
     */
    public void keyPressed(int keycode) {
        if (Engine.ME.state == Engine.MENU) {
            //Engine.ME.setLastKeyPressed(System.currentTimeMillis());
            //state.menu.keyPressed(getGameAction(keycode));
            Engine.ME.menu.keyPressed(keycode, getGameAction(keycode));
            repaint();
        } 
        {
            Engine.ME.keyPressed(keycode, getGameAction(keycode));
        }
    }

    /**
     * Called when a key is released
     *
     * @param keycode the key code of the key that was released
     */
    public void keyReleased(int keycode) {
         {
            Engine.ME.keyReleased(keycode, getGameAction(keycode));
        }
    }
    /**
     * Called when the pointer is pressed.
     * Record the target for the pusher.
     *
     * @param x location in the Canvas
     * @param y location in the Canvas
     */
    protected void pointerPressed(int x, int y) {
        //System.out.println("x:"+x+"y:"+y);
        if (Engine.ME.state != Engine.PLAY &&
            Engine.ME.state != Engine.MENU)
            return;
        //System.out.println("x:"+x+"y:"+y);
        int h;
        if (Engine.ME.state == Engine.MENU)
            h=70;
        else 
            h=40;
        if (y > (height-h)) {
           if (x < (ICON_WIDTH)) {
         
             keyPressed(-6);
             return;
           } 
           else if (x > (width - ICON_WIDTH)) {
           // System.out.println("switch");
         
             keyPressed(-7);
             return;
           }
           
        }
        if (Engine.ME.state != Engine.PLAY)
            return;
        
        if(Math.abs(y-Engine.ME.racer.pos.getY()) < PLAYER_HEIGHT) {
            if(x < Engine.ME.racer.pos.getX()) {
                left = true;
                Engine.ME.keyPressed(getKeyCode(Canvas.LEFT), Canvas.LEFT);
            } else {
                right = true;
                Engine.ME.keyPressed(getKeyCode(Canvas.RIGHT), Canvas.RIGHT);
            }
        } else {
            
            if (y < Engine.ME.racer.pos.getY()) {
                up = true;
                Engine.ME.keyPressed(getKeyCode(Canvas.UP), Canvas.UP);
            } else {
                down = true;
                Engine.ME.keyPressed(getKeyCode(Canvas.DOWN), Canvas.DOWN);
            }
        }
    }
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    private final static int ICON_WIDTH=80;
    private final static int PLAYER_HEIGHT=30;
    protected void pointerReleased(int x, int y) {
        if (Engine.ME.state != Engine.PLAY &&
            Engine.ME.state != Engine.MENU)
            return;
          if (y > (height-50)) {
           if (x < (ICON_WIDTH)) {
             keyReleased(-6); //exit
             return;
           } 
           else if (x > (width - ICON_WIDTH)){
             keyReleased(-7); //exit
             return;
           }
        }
        if (Engine.ME.state != Engine.PLAY)
            return;
         {
            if(left) {
                Engine.ME.keyReleased(getKeyCode(Canvas.LEFT), Canvas.LEFT);
                left = false;
            } else if(right){
               right = false;
                Engine.ME.keyReleased(getKeyCode(Canvas.RIGHT), Canvas.RIGHT);
            }
        } 
        {
            if (up) {
                (up) = false;
                Engine.ME.keyReleased(getKeyCode(Canvas.UP), Canvas.UP);
            } else if(down){
                (down) = false;
                Engine.ME.keyReleased(getKeyCode(Canvas.DOWN), Canvas.DOWN);
            }
        }
    }

    /**
     * Paints background on the Canvas
     *
     * @param g the Graphics object to be used for rendering
     */
    private void paintBackground(Graphics g) {
        //g.setColor(BACKGROUND);
        //g.fillRect(0, 0, width, height);
        
        int str_width = fontMedium.stringWidth(Locale.get("General.Exit"));
        int str_height = fontMedium.getHeight();
        //g.drawImage(BGIMAGE, 0, 0, TL_ANCHOR);
        
        g.setFont(fontMedium);
        g.setColor(ThreeDColor.lightPurple.getRGB());
        g.drawString(Locale.get("General.Exit"), width-str_width-5,
            height-str_height-2, TL_ANCHOR);
    }

    /**
     * Paints splash screen
     *
     * @param g the Graphics object to be used for rendering
     */
    private void paintTitle(Graphics g) {
        //g.drawImage(splash, 0, 0, TL_ANCHOR);
        //#if 0
        g.setFont(font);
        g.setColor(ThreeDColor.darkOrange.getRGB());
        g.drawString(Locale.get("messages.welcome"), width/2, height/2, Graphics.
        TOP|Graphics.HCENTER);
        
        g.drawString(Locale.get("messages.keyany"), width/2, height/2+font.
        getHeight(), Graphics.TOP|Graphics.HCENTER);
        //#endif
        Image title = Util.createImage("/title.png");
        g.drawImage(title, 0,0,Graphics.TOP|Graphics.LEFT);
    }

    
    public void drawString(Graphics g, String str, int x, int y)
    {
  
      this.viewer = this.font1.getViewer(str);
      this.viewer.paint(x, y, g );
    }


    private void paintMenu(Graphics g) {
        engine = Engine.ME;
        engine.menu.paint(g);
    }

    /**
     * Paints game over info
     *
     * @param g the Graphics object to be used for rendering
     */
     Image loseimage;
    private void paintDead(Graphics g) {
    //#if 0
        paintBackground(g);

        g.setFont(font);

        g.setColor(ThreeDColor.black.getRGB());
        g.drawString("Game", (width / 2) + 1, (height / 2) - 10 + 1, TH_ANCHOR);
        g.drawString("Over", (width / 2) + 1, (height / 2) + 10 + 1, TH_ANCHOR);

        g.setColor(ThreeDColor.red.getRGB());
        g.drawString("Game", width / 2, (height / 2) - 10, TH_ANCHOR);
        g.drawString("Over", width / 2, (height / 2) + 10, TH_ANCHOR);
    //#endif
        if(loseimage == null)
            loseimage = Util.createImage("/lose.png");
        g.drawImage(loseimage,(width-loseimage.getWidth())/2, 
            (height-loseimage.getHeight())/2,Global.TL);
    }

    
    Image overimage;
    Image fontEnd;
    private void paintOver(Graphics g) {
        if(overimage == null)
            overimage = Util.createImage("/end.png");
        g.drawImage(overimage,0, 
            0,Global.TL);
         if(fontEnd == null)
            fontEnd = Util.createImage("/gameend.png");
        g.drawImage(fontEnd,(width-fontEnd.getWidth())/2, 
            (height-fontEnd.getHeight())/2,Global.TL);
    }
    Image fontEnemycount;
    Image fontEnemykilled;
    Image fontPoints;
    private void paintSuccess(Graphics g) {
        
       if(overimage == null)
            overimage = Util.createImage("/end.png");
        if(fontEnemycount == null)
            fontEnemycount = Util.createImage("/enemycount.png");
        if(fontEnemykilled == null)
            fontEnemykilled = Util.createImage("/jiandi.png");
        if(fontPoints == null)
            fontPoints = Util.createImage("/defen.png");
        int offset =10;
        g.drawImage(overimage,0,0,Global.TL);
        g.drawImage(fontEnemycount,0,100,Global.TL);
        drawString(g, ""+engine.enemys.enemysGenerated,145, 100+offset);
        g.drawImage(fontEnemykilled,0,140,Global.TL);
        drawString(g, ""+engine.enemys.enemysKilled,75, 140+offset);
        g.drawImage(fontPoints,0,180,Global.TL);
        drawString(g, ""+(int)engine.racer.points,75, 180+offset);
        //g.fillRect(0, 0, width, height);
       
        engine = Engine.ME;
        
        
        g.setColor(ThreeDColor.yellow.getRGB());
        int str_height = fontSmall.getHeight();
        g.setFont(fontSmall);
        g.drawString(Locale.get("messages.keyany"), width / 2, 
        (height)-str_height-20, TH_ANCHOR);
    }

    private void paintLoadGame(Graphics g) {
        
        g.setColor(ThreeDColor.black.getRGB());
        g.fillRect(0, 0, width, height);
        g.setColor(ThreeDColor.white.getRGB());
        g.setFont(fontSmall);
        
        g.drawString("‘ÿ»Î÷–", (width / 2) + 1, (height / 2)  + 1, TH_ANCHOR);
        g.setColor(ThreeDColor.red.getRGB());
        g.fillRect(0, height-5, Engine.ME.progress*width/100, 5);
    }
    /**
     * Renders the Canvas
     *
     * @param g the Graphics object to be used for rendering the Canvas
     */
     Image sw;
     Image nuclear;
    public void paint(Graphics g) {
        if (Engine.ME.state == Engine.TITLE) {
            paintTitle(g);
        } else if (Engine.ME.state == Engine.MENU) {
            paintMenu(g);
        } else if(Engine.ME.state == Engine.LOADGAME) {
            paintLoadGame(g);
        } else if(Engine.ME.state == Engine.MIDDLE_SUCCESS) {
            paintSuccess(g);
        } else if (Engine.ME.state == Engine.GS_ROUNDFINISHED) {
            paintDead(g);
            //state.menu.resetSelected();
            //state.menu.showResumeItem(false);
        } 
        else if (Engine.ME.state == Engine.OVER) {
            paintOver(g);
            //state.menu.resetSelected();
            //state.menu.showResumeItem(false);
        } else if (Engine.ME.state == Engine.PLAY) {
            //Engine.ME.drawPlayOn(g);
            g.drawImage(this.buf,0,0, Graphics.TOP|Graphics.LEFT);
            
            if(sw == null)
                sw = Util.createImage("/switching.png");
            g.drawImage(sw,0,height-20, Graphics.TOP|Graphics.LEFT);
            if(nuclear == null)
                nuclear = Util.createImage("/nuclear.png");
            g.drawImage(nuclear,width-20,height-20, Graphics.TOP|Graphics.LEFT);
        }

    }
}
