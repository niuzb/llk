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

import java.util.*;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
//import de.enough.polish.ui.texteffects.BitmapFontTextEffect;


/**
 * This class represents an individual brick, with its
 * position, dimension, type and color. Bricks know
 * how to draw themselves, and how to handle collisions.
 */
public class Brick extends Sprite {
    private static Image ME = Util.createImage("/enemy.png");
    
    private static Image en1 = Util.createImage("/enemy1.png");
    
    private static Image en2 = Util.createImage("/enemy2.png");
    public static final int framesCountX = 4;
    public static final int framesCountY = 4;

    public static final int WIDTH = ME.getWidth()/framesCountX;
    public static final int HEIGHT = ME.getHeight()/framesCountY;

    

    
    public static final int STEP = Screen.width / 25;
    public static final int GAP = Math.max(2, Screen.width / 80);
    public static final int ZOMBIE = 0;
    public static final int STANDARD = 1;
    public static final int FIXED = 2;
    public static final int SLIDE = 3;

    private int pos;
    private int type;
    private int score;
    private int life;
    private int bullettype;
    
    private ThreeDColor color;
    private ThreeDColor brighter;
    private ThreeDColor darker;
    private BrickList owner;
    private static int[][] ATTRIBUTE= {
          //score,life,bullet type
          {0,0,0},            
          {1,1,0},     //enemy type 1     
          {1,1,1},     //enemy type 2
          {2,2,1},     //enemy type 3
          {2,3,2},     //enemy type 4
          {2,3,2},     //enemy type 5
          {3,4,3},     //enemy type 6
          {3,4,3},     //enemy type 7
          {3,5,4},     //enemy type 8
          {4,5,5},     //enemy type 9
          //#if 0
          {4,6,3},     //enemy type 10
          {4,6,4},     //enemy type 11
          {5,7,4},     //enemy type 12
          {5,7,4},     //enemy type 13
          {6,8,5},     //enemy type 14
          {6,9,5},     //enemy type 15
          {6,9,5},     //enemy type 16
          //#endif
    };
	private BitMapFontViewer viewer;
    //private BitmapFontTextEffect bfte;
    public Brick(BrickList owner, int x, int y, int pos, int type) {
        this.pos = pos;
        this.owner = owner;

        moveTo(x, y);

        if (type == ZOMBIE) {
            width = 0;
            height = 0;
        } else {
            width = WIDTH;
            height = HEIGHT;
        }
        //pic = Util.createImage("/p1.png");

        this.type = type;
        this.score = ATTRIBUTE[type][0];        
        this.life = ATTRIBUTE[type][1];
        this.bullettype = ATTRIBUTE[type][2]+Ball.E_BULLET0;
        //bfte = new BitmapFontTextEffect();
        //bfte.setFont(Screen.font1);

    }

    public Brick(Brick brick) {
        pos = brick.pos;
        owner = brick.owner;
        moveTo(brick.x, brick.y);
        type = brick.type;
        width = brick.width;
        height = brick.height;
        score = brick.score;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    public int getBtype() {
        return bullettype;
    }

    public void setColor(ThreeDColor color) {
        this.color = color;
        brighter = color.brighter();
        darker = color.darker();
    }

    public void clear() {
        type = ZOMBIE;
        width = 0;
        height = 0;
    }

    public int hit(Ball b) {
        int s = 0;
//#if 0
        if (type == SLIDE) {
            Brick neighbor = owner.getNeighbor(this, direction);

            if ((neighbor != null) && (neighbor.getType() == 0)) {
                owner.moveBrick(pos, neighbor.pos);
            }
        }
//#endif
        life -= b.getScore();
        if (life <= 0) {
            clear();
            s = score;
        } else {
            synchronized (this) {
                ishit=true;
            }
        }
        return s;
    }

    public int getType() {
        return type;
    }
    
    public boolean shouldFire(int delay) {
       if(fire_count-- < 0) {
           fire_count = delay+Util.getRandomInt(0,6);
           return true;
       }
       return false;
    }

    public boolean isFixed() {
        return (type == ZOMBIE) || (type == FIXED);
    }

    public void paint(Graphics g) {
        switch(type) {
        case ZOMBIE:
            //TO DO, and some eplorerer damn good things and music
            {
                int image_index;
                
                if(current_index >= animation_seq.length) return;
                
                image_index = animation_seq[current_index];
                current_index++;
                
                
                g.drawRegion(explosion, 0, 
                    image_index * 30, 30, 30,
                    0, x, y, Graphics.TOP | Graphics.LEFT);
               
                //g.setColor( );
                
                this.viewer = Screen.font1.getViewer("+"+(score));
                this.viewer.paint(x+3, y+10-current_index/2, g );
                 //#if 0
                bfte.drawString("+"+(score), ThreeDColor.red.getRGB(),
                        x+3, y+10-current_index/2,Graphics.TOP | Graphics.LEFT,
                        g);
                //#endif
            }
            break;
        default:
            if(ishit){
                if(hit_index >= hit_seq.length) {
                  synchronized (this) {
                    ishit = false;
                    hit_index = 0;
                    return;
                  }
                }
                g.drawRegion(explore_pic, 0, hit_seq[hit_index]*16, 
                    16, 16,
                    0, x+width/4, y+height, Graphics.TOP | Graphics.LEFT);
                
                hit_index++;
            }
            //below we draw the enemy
            if(count-- < 0) {
                picture_index = Util.getRandomInt(0,4);
                count = 100+Util.getRandomInt(100,800);
            }
            int r = picture_index;
            if ((type > 0) && (type <= 4)) {
                g.drawRegion(ME, (r) * width, 
                    ((type-1)) * height, width, height,
                    0, x, y, Graphics.TOP | Graphics.LEFT);
            } else if ((type > 4) && (type <= 7)){
                g.drawRegion(en1, (r) * width, 
                    ((type-5)) * height, width, height,
                    0, x, y, Graphics.TOP | Graphics.LEFT);
            } else if((type > 7) && (type <= 9)) {
                g.drawRegion(en2, (r) * width, 
                    ((type-8)) * height, width, height,
                    0, x, y, Graphics.TOP | Graphics.LEFT);
            }
            //#if 0
            g.drawRegion(ME, ((type-1) % framesCountX) * width, 
                ((type-1) / framesCountX) * height, width, height,
                0, x, y, Graphics.TOP | Graphics.LEFT);
            //#endif
            break;
        }
    }

    public void paintShadow(Graphics g) {
        if (type == ZOMBIE) {
            return;
        }

        g.setColor(ThreeDColor.black.getRGB());
        g.fillRect(x + shadow, y + shadow, width, height);
    }

    public void erase(Graphics g) {
        if (isFixed()) {
            return;
        }

        g.setColor(Screen.BACKGROUND);
        g.fillRect(x, y, width + shadow, height + shadow);
    }

    
    
    private static Image explore_pic = Util.createImage("/fire.png");
    private static Image explosion = Util.createImage("/explosion.png");

    private int animation_seq[] = {0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,7};
    private int current_index = 0;

    private boolean ishit = false;
    private int count = 200;
    private int fire_count = Util.getRandomInt(0,10);
    private int picture_index = 0;
    private int hit_seq[] = {0,0,0,1,1,1,2,2,2,3,3,3};
    private int hit_index = 0;
}
