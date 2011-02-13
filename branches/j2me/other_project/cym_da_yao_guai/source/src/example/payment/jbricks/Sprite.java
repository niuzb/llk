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

import javax.microedition.lcdui.Graphics;
import java.util.*;
import javax.microedition.lcdui.Image;
/**
 * Provide some basic sprite functionality:
 * getting and setting of coordinates and
 * dimensions, as well as simple collision
 * detection.
 */
public class Sprite {
    public int x;
    public int y;
    public int shadow;
    
    protected  Image pic;
    
    protected  Image hit_pic;
    public  int width;
    public  int height;

    public Sprite() {
        shadow = Math.max(2, Screen.width / 60);
    }

    public int getCenterX() {
        return x + (width / 2);
    }

    public int getCenterY() {
        return y + (height / 2);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveBy(int x, int y) {
        this.x += x;
        if(this.x < -width)  {
            this.x = Screen.width;
        } else if (this.x > Screen.width) {
            this.x = -width;
        }
        this.y += y;
    }

    public void paint(Graphics g) {
    }

    public void paintShadow(Graphics g) {
    }

    public boolean intersects(Sprite other) {
        int x1= x+3;
        int y1 = y+3;
        int x2= other.x+3;
        int y2 = other.y+3;
        int xw = x1 + width-2;
        int yh = y1 + height-2;
        int oxw = x2 + other.width-2;
        int oyh = y2 + other.height-2;

        return ((x1 >= x2) && (x1 < oxw) && (y1 >= y2) && (y1 < oyh)) ||
        ((xw >= x2) && (xw < oxw) && (y1 >= y2) && (y1 < oyh)) ||
        ((x1 >= x2) && (x1 < oxw) && (yh >= y2) && (yh < oyh)) ||
        ((xw >= x2) && (xw < oxw) && (yh >= y2) && (yh < oyh));
    }
}
