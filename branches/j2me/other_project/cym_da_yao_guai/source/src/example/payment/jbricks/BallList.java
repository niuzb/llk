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


/**
 * This is a container class that some of the
 * initialization, drawing and collision detection
 * for the ball in any particular level. It also
 * serves as a container that manages redraws
 */
public class BallList {
    public Vector list;
    //private int[] recent_collision;
    private Stack ballList;
    private Engine engine;
    private EngineState state;

    public BallList() {
        list = new Vector();
        ballList = new Stack();
        for (int i = 0; i < 7; i++) {
            ballList.push(new Ball(0, 0, 0, -1));
        }
        
        state = new EngineState();
    }

    /**
     * add a ball in ball list 
     */
    public Ball fire(int x, int y) {
        Ball b;

        b = getBall(x, y);
        list.addElement(b);
        return b;
    }
    
    public void deleteBall(Ball b) {
        
    }


    public void clearAllBall() {
        while (!list.isEmpty()) {
    			releaseBall(list.firstElement());
    			list.removeElementAt(0);
		    }
    }
    
    public void move() {
        
        recycalBall();
        
    	int len = list.size();
    	Ball ball;
      
        synchronized (this) {
          for (int i = 0; i < len; i++) {
            ball = (Ball)list.elementAt(i);
                  ball.move();
          }
        }
    }

    /**
     * this function auto create bullet for hero and enemy
     */
    public void createBullets() {
      //getheroaddress
      //generate the bullet for hero
      //get  enemy address and random generete bullet for enemy
      Ball b;
      synchronized (this) {
        
        engine.getState(state);

        Hero h = state.paddle;
        if (h.shouldFireBullet()) {
            b = fire(h.x, h.y);
            b.setType(h.getBulletType());
            b.moveTo(h.x+h.width/2-b.width/2,h.y);
        }

        //now for enemy bullet
        BrickList blist = state.bricks;
        blist.fireBullet(this);
      }
    }
    /**
     * Sets game business logic
     *
     * @param game engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
        engine.getState(state);
    }

    public void paint(Graphics g) {
        synchronized (this) {
          for (int i = 0; i < list.size(); i++) {
               if( ((Ball)list.elementAt(i)).type != Ball.DIED)
                   ((Ball)(list.elementAt(i))).paint(g);
          }
        }
    }
    
    
    /*********************************************
     * private method
     *********************************************/


    private void increaseBallcapacity() {
        for (int i = 0; i < 5; i++)
            ballList.push(new Ball(0, 0, 0, -1));
    }

    /**
     * get a ball from Ball List
     * this x y is 
     */
    private Ball getBall(int x, int y) {
        Ball n;
        
        if (this.ballList.empty()) {
            increaseBallcapacity();
        }
        n = (Ball)ballList.pop();
        return n;
    }
    
    /**
     * release a ball into Ball List
     */
    private void releaseBall(Object n) {
        ballList.push(n);
    }

    
    private void recycalBall() {
       int len = list.size();
       Ball ball;
       
       synchronized (this) {
         for (int i = 0; i < len; i++) {
           ball = (Ball)list.elementAt(i);
           if (ball.type == Ball.DIED) {
               list.removeElement((Object)ball);
               releaseBall((Object)ball);
               len--;
               i--;
           }
         }
       }
       
    }
}

