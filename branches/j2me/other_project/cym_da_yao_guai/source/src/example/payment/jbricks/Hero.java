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
import de.enough.polish.util.RgbImage;
import de.enough.polish.util.ImageUtil;


/**
 * This class represents an hero ,ther usr cntron a hero to shoot enemy
 * with its
 * position, hero  know
 * how to draw themselves, and how to handle collisions.
 */
public class Hero extends Sprite {

    private int pos;
    private int type;
    private Engine owner;
    private int dx;
    private int dy;

    static {
      
    }
    public Hero(Engine owner, int x, int y) {
        this.owner = owner;
        pic = Util.createImage("/hero.png");
        hit_pic = Util.createImage("/hero2.png");
        width = pic.getWidth();
        height = pic.getHeight();
        //rgbpic = new RgbImage(pic, true);
        //hit_rgbpic = ImageUtil.changeColorBalance(rgbpic, 20, 0, 0, 50);
        
        moveTo(x, y);
        dx = dy = 0;
        //TODO blood should change the next level 
        blood = INIT_BLOOD;
    }
    
    public void setSteps(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    static int count = 10;
    public void paint(Graphics g) {
        
        if (ishit) {
            //hit_rgbpic.paint(x, y, g);
            g.drawImage(hit_pic, x, y, Graphics.TOP | Graphics.LEFT);
            if(count-- < 0){
                ishit = false;
                count = 10;
            };
        } else {
            //rgbpic.paint(x, y, g);
            g.drawImage(pic, x, y, Graphics.TOP | Graphics.LEFT);
        }
       //
    }


    public void erase(Graphics g) {
        g.setColor(Screen.BACKGROUND);
        g.fillRect(x, y, width , height);
    }
    
    public boolean shouldFireBullet() {
        if (fire_delay-- < 0) {
            fire_delay = FIRE_DELAY;
            return true;
        }
        return true;
    }

    
    /**
     * Resets the hero parameter
     */
    public void reset() {
        blood = INIT_BLOOD;
        speed = 1;
        bullet_type = Ball.H_BULLET0;
        fire_delay = FIRE_DELAY;
    }

    
    public int getBulletType() {
        return bullet_type;
    }
    
    public void setBulletType(int s) {
        bullet_type = s;
    }
    
    public int getBlood() {
        return blood;
    }

    public void setBloodDeta(int deta) {
        blood += deta;
        ishit = true;
        if (blood < 0)
            blood = 0;
        if(deta < 0) {
            owner.getMusicManager().start_bang_music();
        }
    }
    public void setIsHit(boolean hit) {
        ishit = hit;
    }
    public void setBlood(int b) {
        blood = b;
    }

    /**
     * @return false:if has get the max bullet type
     *         true: otherwise
     */
    public boolean setBulleTypeDeta(int t) {
        bullet_type += t;
        if(bullet_type >= Ball.E_BULLET0) {
            bullet_type = Ball.E_BULLET0 - 1;
            --fire_delay;
           fire_delay = (fire_delay > 1) ? fire_delay :1;
            return false;
        }
        return true;
    }

    
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int s) {
        speed = s;
    }
    public void setSpeedDeta(int t) {
        speed += t;
        if (speed > 10) 
            speed = 10;
    }

    /****************************************************
    private method and variable
    *****************************************************/
    private static final int FIRE_DELAY = 4;//to do 4
    private static final int MAX_BULLET_SPEED = 10;
    private static final int INIT_BLOOD = 6; // to do 5
    private int fire_delay = FIRE_DELAY; //delay of fire the next ball
    private int bullet_type = Ball.H_BULLET0;
    private int speed = 1; //to do 2
    private int blood;
    
    private boolean ishit = false;
}

