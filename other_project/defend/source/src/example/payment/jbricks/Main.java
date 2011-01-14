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

import java.io.*; 
import java.util.*;


import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;


/** 
 * Main game wrapper 
 */
public class Main extends MIDlet{
    

    private Screen screen;
    private Engine engine;
    private int feature = 0;
    private String title = "";
    private String description = "";
    private int lastTransState;
    public static Main MIDLET;
    public static Display DISPLAY;

    /**
     * Creates game wrappper
     */
    public Main() {
      MIDLET = this;
      init();
    }

    /**
     * Enables/disables "Exit" command in application
     *
     * @param show false for hiding "Exit" command, true otherwise 
     */
    public void showCommandExit(boolean show) {
        if (show) {
            //screen.addCommand(exit);
        } else {
            //screen.removeCommand(exit);
        }
    }

    /**
     * Signals the MIDlet that it has entered the Active state. In the Active 
     * state the MIDlet may hold resources. The method will only be called 
     * when the MIDlet is in the Paused state.
     */

    public void startApp() {/*
        if (engine != null) {
            engine.setPaused(engine.PLAY);
        }*/
    }

    /**
     * Signals the MIDlet to enter the Paused state. In the Paused state 
     * the MIDlet must release shared resources and become quiescent. 
     * This method will only be called called when the MIDlet is in the Active state.
     */
    public void pauseApp() {
        if (engine != null) {
            engine.setPaused(true);
        }
    }

    /**
     * Signals the MIDlet to terminate and enter the Destroyed state. 
     * In the destroyed state the MIDlet must release all resources and save 
     * any persistent state. This method may be called from the Paused or Active states.
     *
     * @param unconditional If true when this method is called, the MIDlet must 
     * cleanup and release all resources. If false the MIDlet may throw 
     * MIDletStateChangeException to indicate it does not want to be destroyed at this time.
     */
    public void destroyApp(boolean unconditional) {
        if (engine != null) {
            engine.stop();
        }
        Display.getDisplay(this).setCurrent(null);
        notifyDestroyed();
    }



	public void setCurrentDisplay(Displayable nextDisplayable) {
		Display.getDisplay(this).setCurrent(nextDisplayable); 
	}


    /**
     * Initiates graphics UI, business logic and transaction module
     */
    private void init() {
        screen = new Screen(this);
        engine = new Engine(screen);

        //screen.setCommandListener(this);
        DISPLAY = Display.getDisplay(this);

        DISPLAY.setCurrent(screen);
    }
}
