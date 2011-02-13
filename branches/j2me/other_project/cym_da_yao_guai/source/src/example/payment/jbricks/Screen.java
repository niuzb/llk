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
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.Locale;


/**
 * This class handles and coordinates most of the drawing for this game. 
 * It knows how to draw the splash, menu and game over screen, as well as the
 * actual games screen. It also forwards 'cooked' key events to the Engine class.
 */
public class Screen extends Canvas {
    public static int width;
    public static int height;
    private static final int TL_ANCHOR = Graphics.TOP | Graphics.LEFT;
    private static final int TH_ANCHOR = Graphics.TOP | Graphics.HCENTER;
    public static Graphics GRAPHICS;
    public static final int BACKGROUND = ThreeDColor.gray.getRGB();
    
    private static Image BGIMAGE = Util.createImage("/bg.png");
    private static Image splash = Util.createImage("/bg1.png");

    private Font fontSmall;
    private Font fontMedium;
    private Font fontLarge;
    private Font font;
    private int lastState;
    private int lastScore;
    private int lastlevel;
    private Image buf;
    private Engine engine;
    private EngineState state;
    private int fontHeight;
    private MIDlet midlet;
	public static final BitMapFont font1;
	private BitMapFontViewer viewer;
    static {
        font1 = BitMapFont.getInstance( "/example.bmf" );
    }
    /**
     * Creates Screen instance
     * 
     * @param midlet A MIDlet using this Screen
     */
    public Screen(MIDlet midlet) {
        setFullScreenMode(true);
        width = 240;
        height = 320;

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
        
        state = new EngineState();

        lastState = -1;
        lastScore = -1;
        lastlevel = -1;

        this.midlet = midlet;
        
    }

    /**
     * Sets game business logic
     *
     * @param game engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
        engine.getState(state);
        //state.menu.setMIDlet(midlet);
    }

    /**
     * Called when a key is pressed
     *
     * @param keycode the key code of the key that was pressed
     */
    public void keyPressed(int keycode) {
        if (state.state == Engine.MENU) {
            engine.setLastKeyPressed(System.currentTimeMillis());
            //state.menu.keyPressed(getGameAction(keycode));
        } else {
            engine.keyPressed(keycode, getGameAction(keycode));
        }
    }

    /**
     * Called when a key is released
     *
     * @param keycode the key code of the key that was released
     */
    public void keyReleased(int keycode) {
        if (state.state != Engine.MENU) {
            engine.keyReleased(keycode, getGameAction(keycode));
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
    
        if (state.state != Engine.PLAY)
            return;
        if (y > (height-fontMedium.getHeight()-2)) {
           if (x > (width - 50)) {
             engine.keyPressed(-7, 3); //exit
           } 
           return;
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
        g.drawImage(BGIMAGE, 0, 0, TL_ANCHOR);
        
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
        //#if 0
        engine.getState(state);

        if (lastState != Engine.TITLE) {
            paintBackground(GRAPHICS);
            state.bricks.paintShadow(GRAPHICS);
            state.bricks.paint(GRAPHICS);
        }

        g.drawImage(buf, 0, 0, TL_ANCHOR);
        g.setColor(BACKGROUND);
        g.fillRect(0, height - (fontHeight + 5), width, fontHeight + 5);

        g.setFont(font);

        g.setColor(ThreeDColor.black.getRGB());
        g.drawString("Bricks", (width / 2) + 1, (height / 2) + 1, TH_ANCHOR);

        g.setColor(ThreeDColor.red.getRGB());
        g.drawString("Bricks", width / 2, height / 2, TH_ANCHOR);
        //#endif
        g.drawImage(splash, 0, 0, TL_ANCHOR);
        
        g.setFont(font);
        g.setColor(ThreeDColor.darkOrange.getRGB());
        g.drawString(Locale.get("messages.welcome"), width/2, height/2, Graphics.
        TOP|Graphics.HCENTER);
        
        g.drawString(Locale.get("messages.keyany"), width/2, height/2+font.
        getHeight(), Graphics.TOP|Graphics.HCENTER);
    }

    /**
     * Paints game over info
     *
     * @param g the Graphics object to be used for rendering
     */
    private void paintOver(Graphics g) {
        paintBackground(g);

        g.setFont(font);

        g.setColor(ThreeDColor.black.getRGB());
        g.drawString("Game", (width / 2) + 1, (height / 2) - 10 + 1, TH_ANCHOR);
        g.drawString("Over", (width / 2) + 1, (height / 2) + 10 + 1, TH_ANCHOR);

        g.setColor(ThreeDColor.red.getRGB());
        g.drawString("Game", width / 2, (height / 2) - 10, TH_ANCHOR);
        g.drawString("Over", width / 2, (height / 2) + 10, TH_ANCHOR);
    }


    /**
     * Renders the Canvas
     *
     * @param g the Graphics object to be used for rendering the Canvas
     */
    public void paint(Graphics g) {

        engine.getState(state);
        g.setFont(font);

        if (state.state == Engine.TITLE) {
            paintTitle(g);
        } else if (state.state == Engine.MENU) {
            //paintMenu(g);
        } else if (state.state == Engine.OVER) {
            paintOver(g);
            //state.menu.resetSelected();
            //state.menu.showResumeItem(false);
        } else if (state.state == Engine.PLAY){

            g.drawImage(buf, 0, 0, TL_ANCHOR);
            state.bricks.paint(g);

            if (true) {
                int str_width = fontSmall.stringWidth(Locale.get("display.level"));
                
                g.setFont(fontSmall);
                g.setColor(ThreeDColor.lightPurple.getRGB());
                g.drawString(Locale.get("display.level"), 2, 2, TL_ANCHOR);
                g.drawString(Locale.get("display.blood"), 70, 2, TL_ANCHOR);
                g.drawString(Locale.get("SuccessDisplay.score"), 150, 2, TL_ANCHOR);

                int str_height = fontSmall.getHeight();
                g.drawString(Locale.get("display.speed"), 2, 
                height-str_height-2, TL_ANCHOR);
                g.drawString(Locale.get("display.bullet"), 70, 
                    height-str_height-2, TL_ANCHOR);
                g.setColor( 0xFF0000 );
                
                this.viewer = this.font1.getViewer(""+(state.level+1));
                this.viewer.paint(4+str_width, 5, g );
                this.viewer = this.font1.getViewer(""+state.blood);
                this.viewer.paint(72+str_width, 5, g );
                
                this.viewer = this.font1.getViewer(""+state.score);
                this.viewer.paint(152+str_width, 5, g );
                
                this.viewer = this.font1.getViewer(""+state.paddle.getSpeed());
                this.viewer.paint(4+str_width, height-str_height-2, g );
                
                this.viewer = 
                this.font1.getViewer(""+(state.paddle.getBulletType()+1));
                this.viewer.paint(72+str_width, height-str_height-2, g );
            }

            //state.ball.paintShadow(g);
            //state.paddle.paintShadow(g);

            //state.ball.paint(g);
            state.balls.paint(g);
            state.paddle.paint(g);
        }

        lastState = state.blood;
        lastScore = state.score;
        lastlevel = state.level;
    }
}
