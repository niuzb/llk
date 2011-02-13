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


/**
 * This is a container class that some of the
 * initialization, drawing and collision detection
 * for the bricks in any particular level. It also
 * serves as a container that manages redraws
 */
public class BrickList {
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;
    public static  int XOFFSET;
    public static  int YOFFSET;

    static {
        int w = Engine.PATTERN_WIDTH * Brick.WIDTH;
        int g = (Engine.PATTERN_WIDTH - 1) * Brick.GAP;

        XOFFSET = ((Screen.width - (w + g)) / 2) ;
        YOFFSET = Screen.height / 8;
    }
    
    private static final int MAX_MOVE = 2;
    private Brick[] list;
    private int columns;
    private int sleep_move;
    //private int bullet_freq;

    public BrickList(int[] typeList, int patternWidth, int level) {
        int x = XOFFSET;
        int y = YOFFSET - (Brick.HEIGHT + Brick.GAP);
        int n = -1;

        list = new Brick[typeList.length];
        //recent_collision = new int[list.length];
        sleep_move = MAX_MOVE;
        //below if level sensitive
        //bullet_freq = 20+level*10;
        fire_delay = (10-level/2 >0) ? (10-level/2) : 1;
        
        speed = level/2 > 0 ? (level/2) : 1;
        left_live_brick = 0;

        for (int i = 0; i < list.length; i++) {
            if ((i % patternWidth) == 0) {
                columns = (x - XOFFSET) / (Brick.WIDTH + Brick.GAP);
                x = XOFFSET;
                y += (Brick.HEIGHT + Brick.GAP);
                n++;
            }
            if(typeList[i] != Brick.ZOMBIE) {
                left_live_brick++;
            }
            list[i] = new Brick(this, x, y, i, typeList[i]);


            x += (Brick.WIDTH + Brick.GAP);
        }
    }

    public Brick getBrickAt(int n) {
        if ((n < 0) || (n >= list.length)) {
            return null;
        }

        return list[n];
    }

    public void moveBrick(int from, int to) {
        int to_x = list[to].x;
        int to_y = list[to].y;
        list[to] = new Brick(list[from]);
        list[to].moveTo(to_x, to_y);
        list[to].setPos(to);
        list[from].erase(Screen.GRAPHICS);
        list[from].clear();
        list[to].paintShadow(Screen.GRAPHICS);
        list[to].paint(Screen.GRAPHICS);
    }

    private int speed = -1;
    private int dummy = Screen.width/2;
    /** heart beat move*/
    public void move() {
        //int deteX = -speed;
        
        if(sleep_move-- > 0) {
            return;
        } 
        sleep_move = MAX_MOVE;
        if(left_live_brick < 6) {
            int s = (left_live_brick < 2 ? speed+10 :
                left_live_brick < 4 ? speed+8 :
                left_live_brick < 5 ? speed+5 : speed);    
           
            for (int i = 0; i < list.length; i++) {
                if (list[i].getType() != Brick.ZOMBIE) {
                    list[i].moveBy(s, 0);
                }
            }
        } else
        {
            dummy += speed;
            
            for (int i = 0; i < list.length; i++) {
                if (list[i].getType() != Brick.ZOMBIE) {
                    list[i].moveBy(speed, 0);
                }
            }
            if (((Screen.width/2 - dummy) > XOFFSET) ||  ((dummy-Screen.width/2) 
            >XOFFSET)) {
                speed = 0-speed;
            }
        }
        
    }
    
    public void fireBullet(BallList l) {
        Ball b;
        int type;
        int x, y;

        //if(!shouldFireBullet()) return;
        for (int i = 0; i < list.length; i++) {
            type = list[i].getType();   
            if (type != Brick.ZOMBIE) {
                if(list[i].shouldFire(fire_delay)) {
                    b = l.fire(list[i].x+list[i].width/2, 
                        list[i].y+list[i].height/2);
                    b.setType(list[i].getBtype());
                    b.moveTo(list[i].x+list[i].width/2-b.width/2,
                        list[i].y+list[i].height/2);
                }
            }
        }
           
    }
    
    public int checkForCollision(BallList balls, Hero h) {
        int x;
        int y;
        int score;
        int s;
        int width = Engine.PATTERN_WIDTH;
        int height = list.length / Engine.PATTERN_WIDTH;
        boolean intersects;

        score = 0;
        
    	int len = balls.list.size();
    	Ball ball;
      
        for (int i = 0; i < len; i++) {
          ball = (Ball)balls.list.elementAt(i);
          
          if (ball.y < 0 || ball.y>Screen.height) { 
            ball.setType(Ball.DIED);
            continue;
          }
          
          if (ball.type == Ball.DIED) {
            continue;
          }
          
          if (ball.type >= Ball.E_BULLET0) {
            if (ball.intersects(h)) {
                h.setBloodDeta(-ball.getScore());
                ball.setType(Ball.DIED);
            }
            continue;
          }
          //#if 0
          //here all the ball is for enemy
          x = (ball.getCenterX() - XOFFSET) / (Brick.WIDTH + Brick.GAP);
          
          if ((x < 0) || (x >= width)) {
              continue;
          }
          //#endif
          y = (ball.getCenterY() - YOFFSET) / (Brick.HEIGHT + Brick.GAP);
          
          if (( y < 0) || (y >= height)) {
              continue;
          }
          for (int j = 0; j < list.length; j++) {
              if(list[j].getType() == Brick.ZOMBIE) continue;
              intersects = ball.intersects(list[j]);
              if (intersects) {
                s = list[j].hit(ball);
                if (s > 0) 
                    left_live_brick--;
                score+=s;
                ball.setType(Ball.DIED);
                break;
              }
          }
        }
        return score;
        
    }
    public boolean isClean() {
        //#if 0
        for (int i = 0; i < list.length; i++) {
            if (list[i].getType() != Brick.ZOMBIE) {
                return false;
            }
        }
        //#endif

        return left_live_brick <= 0;
    }

    public void paint(Graphics g) {
        for (int i = 0; i < list.length; i++) {
            list[i].paint(g);
        }
    }

    
    /**********************

    *******************************/

    
    private int fire_delay;
    
    private int left_live_brick;
}
