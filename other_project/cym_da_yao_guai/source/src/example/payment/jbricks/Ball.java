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
import javax.microedition.lcdui.Image;


/**
 * This class implements the ball, with its radius,
 * and x and y step size. The ball knows how to draw
 * itself, and every once in a while it will do a
 * subtle, pseudo-random path modification to prevent
 * closed ball paths.
 */
public class Ball extends Sprite {
    public static final int RADIUS = Math.max(2, Screen.width / 55);

    
    private static Image enemy_ball = Util.createImage("/ball.png");
    public static final int framesCountX = 4;
    public static final int framesCountY = 3;
    public static final int enemy_width = enemy_ball.getWidth()/framesCountX;
    public static final int enymy_height = enemy_ball.getHeight()/framesCountY;

    
    private static Image hero_ball = Util.createImage("/ball1.png");
    public static final int hframesCountX = 8;
    public static final int hframesCountY = 1;
    public static final int hero_width = hero_ball.getWidth()/hframesCountX;
    public static final int hero_height = hero_ball.getHeight()/hframesCountY;

    private static Image bomber_ball = Util.createImage("/ball2.png");

    //start with H means hero can have this bullet
    public static final int H_BULLET0 = 0;
    public static final int H_BULLET1 = 1;
    public static final int H_BULLET2 = 2;
    public static final int H_BULLET3 = 3;
    
    public static final int E_BULLET0 = 4;
    public static final int E_BULLET1 = 5;
    public static final int E_BULLET_MAX = 10;
    public static final int DIED = 21;

    public  int type = H_BULLET0;

    static {
     
    }

    public Ball(int x, int y, int dx, int dy) {
        width = 12;
        height = 12;


        moveTo(x, y);
        setSteps(dx, dy);

        offset = 0;
        xo = yo = 0;
    }
    
    public Ball(int x, int y, int dx, int dy, int type) {
        this(x,y,dx,dy);
        this.type = type;
    }

    public void setSteps(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public void setType(int t) {
        this.type = t;
        if (t<E_BULLET_MAX) {
            damage_score = BULLET_ATTR[t][0];
        }
        if(type < E_BULLET0) {
            dy = -BULLET_ATTR[t][1];
            width = hero_width;
            height = hero_height;
        } else if(type < E_BULLET_MAX) {
            dy = BULLET_ATTR[t][1];
            width = enemy_width;
            height = enymy_height;
        } else {
            dy = 0;
            width = height = 0;
        } 
    }

    public int getXStep() {
        return dx;
    }

    public int getYStep() {
        return dy;
    }

    public void move() {
        x = x + dx + xo;
        y = y + dy + yo;
        xo = yo = 0;
    }

    public void bounceHorizontal() {
        dx = -dx;

        if (Util.getRandomInt(0, 1000) < 70) {
            xo = (dx < 0) ? (-1) : 1;
        }
    }

    public void bounceVertical() {
        dy = -dy;

        if (Util.getRandomInt(0, 1000) < 70) {
            yo = (dy < 0) ? (-1) : 1;
        }
    }

    public void bounce(Sprite other) {
        int cx;
        int cy;

        cx = getCenterX();
        cy = getCenterY();

        if (dx < 0) {
            if ((cy >= other.y) && (cy < (other.y + other.height)) &&
                    (x < (other.x + other.width))) {
                dx = Math.abs(dx);
            }
        } else {
            if ((cy >= other.y) && (cy < (other.y + other.height)) && ((x + width) >= other.x)) {
                dx = -Math.abs(dx);
            }
        }

        if (dy < 0) {
            if ((cx >= other.x) && (cx < (other.x + other.width)) &&
                    (y < (other.y + other.height))) {
                dy = Math.abs(dy);
            }
        } else {
            if ((cx >= other.x) && (cx < (other.x + other.width)) && ((y + height) >= other.y)) {
                dy = -Math.abs(dy);
            }
        }

        if (Util.getRandomInt(0, 1000) < 70) {
            xo = (dx < 0) ? (-1) : 1;
        }

        if (Util.getRandomInt(0, 1000) < 70) {
            yo = (dy < 0) ? (-1) : 1;
        }
    }
    
    public int getScore() {
        return damage_score;
    }



    public void paint(Graphics g) {
        int image_index;
        
        if(type < E_BULLET0) {
            //Draw hero bullet ,type is from 0~3                                                                      
            image_index = 2 * type + animation_seq[current_index];
            current_index++;
            if(current_index >= animation_seq.length) current_index = 0;
            
            g.drawRegion(hero_ball, ((image_index) % hframesCountX) * width, 
                ((image_index) / hframesCountX) * height, width, height,
                0, x, y, Graphics.TOP | Graphics.LEFT);
        } else if(type < E_BULLET_MAX) {
            image_index = 2 * (type-E_BULLET0) + animation_seq[current_index];
            current_index++;
            if(current_index >= animation_seq.length) current_index = 0;
            
            g.drawRegion(enemy_ball, ((image_index) % framesCountX) * width, 
                ((image_index) / framesCountX) * height, width, height,
                0, x, y, Graphics.TOP | Graphics.LEFT);
        } else {
            return;
        } 
        
    }
    
    private int dx;
    private int dy;
    private int xo;
    private int yo;
    private int counter;
    private int offset;
    private int damage_score=0;
    private int animation_seq[] = {0,0,1,1,1,0,0,0,0,1,1};
    private int current_index = 0;
    //descripe every bullet attribute.
    private static int[][] BULLET_ATTR= {
    //{damage_score, step}
          {1,3},                     //H_BULLET0
          {2,3},          //H_BULLET1
          {3,4},           //H_BULLET2 
          {4,4},          //H_BULLET3
          {1,3},          //E_BULLET0
          {1,3},          //E_BULLET1
          {1,3},          //E2
          {1,3},//E3
          {1,3},//E4
          {1,4},//E5
    };
}
