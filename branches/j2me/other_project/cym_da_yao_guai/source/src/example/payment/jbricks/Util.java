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

import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.*;


public class Util {
    private static Random rnd;

    static {
        rnd = new Random();
    }

    public static int getRandomInt(int low, int high) {
        if (low == high) {
            return low;
        }

        return low + (Math.abs(rnd.nextInt()) % (high - low));
    }

    
    public static Image createImage(String filename) {
      Image image = null;
      try {
        image = Image.createImage(filename);
      } catch (Exception e) {
        e.printStackTrace();
      }  
      return image;
    }

	/**
	 * show a alert , with the specifed title and content
	 * @param string
	 */
	public static void showAlert(String title,String message,Displayable nextDisplayable) {
		//#style messageAlert
		Alert alert = new Alert( title, message, null, AlertType.INFO );
        
		alert.setTimeout( Alert.FOREVER );
        Alert.setCurrent(Main.DISPLAY, alert, nextDisplayable) ;
		//Main.DISPLAY.setCurrent( alert );
	}
}
