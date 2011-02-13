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

import java.util.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;


/**
 * This class drives the games (hence the name engine ). It has all the level 
 * data, animates the objects, keeps track of state, and triggers
 * screen redraws.
 */
public class Engine implements Runnable {
    public static final int PATTERN_WIDTH = 5;
    
    // engine states
    public static final int TITLE = 0;
    public static final int PLAY = 1;
    public static final int OVER = 2;
    public static final int MENU = 3;
    public static final int FEEDBACK = 4;
    public static final int MIDDLE_SUCCESS = 5;


    /**
     * Creates Engine object
     *
     * @param screen the screen used for rendering
     */
    public Engine(Screen screen) {
        int i;

        rsm = new Rsm();
        musicManager = new MusicManager();
        musicManager.setEngine(this);
        menu = new DynaMenu();
        menu.setEngine(this);

        this.screen = screen;
        screen.setEngine(this);

        paddle = new Hero(this, (Screen.width / 2), (Screen.height-55));
        availableLevels = MAX_LEVELS;
        availableLives = MAX_LIVES;

        level = 0;
        state = TITLE;
        done = false;
        //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
        balls = new BallList();
        balls.setEngine(this);
        lastKeyPress = System.currentTimeMillis();
        
        timer = new Timer();
        //增加一个定时器，定期生成子弹
        timer.schedule(new BallHeartbeatTask(), 0, BallHeartbeatTask.PERIOD);

        Thread runner = new Thread(this);
        runner.start();
    }

    /**
     * Resets the game
     */
    private void reset() {
        score = 0;
        paddle.reset();
        startLevel();
    }

    /**
     * Restarts a level
     */
    private void restartLevel() {
        state = PLAY;
        //paused = true;
        setPaused(true);
        synchronized (this) {
            levelStarted = true;
        }

        paddle.moveTo((Screen.width / 2) - (Brick.WIDTH / 2), (Screen.height * 8) / 10);
        paddle.setIsHit(false);
        balls.clearAllBall();
        //musicManager.start_bg_music();
        screen.repaint();
        screen.serviceRepaints();
    }

    /**
     * Starts a new level
     */
    private void startLevel() {
        bricks = new BrickList(pattern_list[level], PATTERN_WIDTH, level);
        restartLevel();
    }

    /**
     * Starts next level
     */
    public void nextLevel() {
        level++;

        if ((level == pattern_list.length)) {
            level = 0;
        }

        startLevel();
    }

    /**
     * Called when a key is pressed
     *
     * @param keycode key code of the key that was pressed
     * @param gameAction game action associated with the given key code of the device
     */
    public void keyPressed(int keycode, int gameAction) {
        key = gameAction;
        lastKeyPress = System.currentTimeMillis();

        if ((state == OVER) || (state == TITLE)) {
           showMenu();
            //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
        } 
        if ((state == FEEDBACK)) {
            state = PLAY;
        }
        if ((state == PLAY)) {
            //paused = false;
            setPaused(false);
            //musicManager.start_bg_music();
            if(keycode == -7) {
                 musicManager.destroyAudioPlayer();
                 Main.MIDLET.destroyApp(false);
                 Main.MIDLET.notifyDestroyed();
                 return;
            }
        }  
    }

    /**
     * Called when a key is released
     *
     * @param keycode key code of the key that was pressed
     * @param gameAction game action associated with the given key code of the device
     */    
    public void keyReleased(int keycode, int gameAction) {
        key = 0;
    }

    /**
     * Retrives state of the game
     *
     * @param engineState holder of the state
     */
    public void getState(EngineState engineState) {
        engineState.bricks = bricks;
        engineState.balls = balls;
        engineState.paddle = paddle;
        engineState.state = state;
        engineState.score = score;
        engineState.blood = blood;
        engineState.level = level;
        engineState.menu = menu;
        engineState.rsm = rsm;
    }

    /**
     * Starts the game
     *
     * @param level a level to be started
     */
    protected void startGame(int level) {
        this.level = level;
        Main.DISPLAY.setCurrent(screen);
        reset();
    }

    /**
     * Sets time of a last pressed key
     *
     * @param time time when the key was pressed
     */
    public void setLastKeyPressed(long time) {
        lastKeyPress = time;
    }

    /**
     * Returns a flag indicating whether the game was started or not
     *
     * @return true if the game was started, false otherwise
     */
    public synchronized boolean levelStarted() {
        boolean x = levelStarted;
        levelStarted = false;

        return x;
    }

    /**
     * Stops the game
     */
    public void stop() {
        done = true;
    }

    
    private void drawSucessScr() {
        int data =score|(paddle.getSpeed()<<16)|(paddle.getBulletType()<<24);
        rsm.updateoption((level+1)%pattern_list.length,rsm.keyLevel);
        rsm.updateoption(data,rsm.keyScore);
        rsm.updateoption(paddle.getBlood(),rsm.keyblood);
        new SucessDisplay(this);
    }

    /**
     * Sets a flag indicating whether the game was paused
     *
     *@param pause true if the game is paused, false otherwise
     */
    public void setPaused(boolean pause) {
        //if (timer != null) { timer.cancel(); timer = null; }
        if(pause == true &&
            paused == false) {
            musicManager.stopAudioPlayer();
        } else if(pause == false &&
            paused == true) {
           musicManager.start_bg_music();
        }
        paused = pause;
        
    }

    /**
     * Executes the game
     */
    public void run() {
        boolean recentCollision;
        long then;
        int px;
        int pw;
        int delta;
        int paddleSpeed = 0;
        int s;
        
        recentCollision = false;
        then = System.currentTimeMillis();
        try {
        while (!done) {
            if (((state == TITLE) || (state == OVER)) &&
                    ((System.currentTimeMillis() - lastKeyPress) > LAST_KEY_DELTA)) {
                showMenu();
                lastKeyPress = System.currentTimeMillis();
                //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
            }
            if(state == FEEDBACK) {
                //do nothing , for pause and resume game
                Thread.sleep(30);
            }
            
            px = paddle.x;
            pw = paddle.width;
            
            blood = paddle.getBlood();
            if ((state == PLAY) && !paused) {
                if (key == Canvas.LEFT) {
                    paddleSpeed = Math.min(-1, -paddle.getSpeed());
                } else if (key == Canvas.RIGHT) {
                    paddleSpeed = Math.max(1, paddle.getSpeed());
                } else if (key == Canvas.FIRE) {
                    paddleSpeed = Math.max(1, paddle.getSpeed());
                } else {
                    paddleSpeed = 0;
                }

                if (((paddleSpeed < 0) && (px > 0)) ||
                        ((paddleSpeed > 0) && ((px + pw) < Screen.width))) {
                    paddle.moveBy(paddleSpeed, 0);
                }
                balls.move();
                s = bricks.checkForCollision(balls, paddle);
                                
                if (s > 0) 
                    musicManager.start_fire_music();
                score += s;
                bricks.move();


               
                
                if (bricks.isClean()) {
                    //nextLevel();
                    state = MIDDLE_SUCCESS;
                    setPaused(true);
                    drawSucessScr();
                }

                //check if we die:the enemy beat us
                if(paddle.getBlood() <= 0) {
                    musicManager.stopAudioPlayer();
                     state = MENU;
                    menu.showResumeItem(rsm.valueLevel>0 ? true : false);
                    menu.goMainMenu();
                    Util.showAlert( "",Locale.get( "messages.gameover" ), 
                        menu.menuScreen );
                    //showMenu();
                }
            }

            screen.repaint();

            delta = (int)(System.currentTimeMillis() - then);

            if (delta < 30) {
                try {
                    Thread.sleep(30 - delta);
                } catch (InterruptedException e) {
                }
            }

            then = System.currentTimeMillis();
        }
        } catch (Exception e) {
	         
	         e.printStackTrace();
	      }
    }

    /******************************************************************
     * protected methed
     ******************************************************************/
    /**
     * Sets engine state
     *
     * @param state a code of the engine state
     */
    protected void setState(int state) {
        this.state = state;
    }
    
    /**
     * Sets engine score
     *
     * @param state a code of the engine state
     */
    protected void setScore(int score) {
        this.score = score;
    }

    
    /**
     * Returns number of current level
     */
    protected int getCruuentLevel() {
        return level + 1;
    }
    
    /**
     * Returns current rsm cb
     */
    protected Rsm getRsm() {
        return rsm;
    }
    
    /**
     * Returns current music manager
     */
    protected MusicManager getMusicManager() {
        return musicManager;
    }


    /**
     * Shows menu
     */
    protected void showMenu() {
        state = MENU;
        menu.showResumeItem(rsm.valueLevel>0 ? true : false);
        menu.goMainMenu();
        menu.show_menu();
        //oldBricks = bricks;
        //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
        //paused = true;
        screen.repaint();
    }

    /**
     * Resumes paused game
     */
    protected void resumeGame() {
        this.level = rsm.valueLevel;
        this.score = rsm.valueScore&0xFFFF;
        
        paddle.setBlood(rsm.valueblood);
        paddle.setSpeed((rsm.valueScore>>16)&0xFF);
        paddle.setBulletType((rsm.valueScore>>24)&0xFF);
        //state = PLAY;
        Main.DISPLAY.setCurrent(screen);
        startLevel();
    }

    protected class BallHeartbeatTask extends TimerTask
    {
      /** Scheduled every 1.2 seconds temply */
      public final static int PERIOD = 1000;
      /** Heartbeat */
      public void run()
      {
        // Loop on every hole
        if (!done && (state == PLAY) && !paused) {
          balls.createBullets();
        }
      }
    }



/*********************************************************
 * private metheds 
 *********************************************************/

  private static final int LAST_KEY_DELTA = 7000;

  // brick types
  private static final int STD = Brick.STANDARD;
  private static final int FIX = Brick.FIXED;   //can fire bullet
  private static final int SLI = Brick.SLIDE;
  private static final int ZOM = Brick.ZOMBIE;

  private static final int MAX_LEVELS = 12;
  private static final int MAX_LIVES = 1;
//#if 0
  private Ball ball;
//#endif
  private Hero paddle;
  private BrickList bricks;
  private BrickList oldBricks;
  private BallList balls;

  private Screen screen;
  private int score;
  private int blood;
  private int level;
  private int availableLevels;
  private int availableLives;
  private int state;
  private int key;
  private boolean paused = true;
  private long lastKeyPress;
  private boolean levelStarted;
  private boolean done;
  private DynaMenu menu;
  private Rsm rsm;
  MusicManager musicManager;
  
  private Timer timer = null;

  // Note that the width of each pattern has to be equal to the PATTERN_WIDTH
  // variable above
  private int[] title_pattern =
      {
          STD, STD, STD, STD, STD
      };
  private int[][] pattern_list =
      {
          {
              1, 1, 1, 1, 1, 
              2, 2, 2, 2, 2, 
              1, 1, 1, 1, 1, 
          },
          {
              SLI, SLI, SLI, SLI, SLI,
              FIX, FIX, FIX, FIX, FIX,
              2, 2, 2, 2, 2,
          },
          {
              3, 3, 3, 3, 3,
              3, 3, 3, 3, 3,
              2, 2, 2, 2, 2,
          },
          {
              4, 4, 4, 4, 4,
              3, 3, 3, 3, 3,
              2, 2, 2, 2, 2,
          },
          {
              4, 4, 4, 4, 4,
              4, 4, 4, 4, 4, 
              SLI, SLI, SLI, SLI, SLI, 
              
          },
          {
              4, 4, 4, 4, 4,
              3, 3, 3, 3, 3,
              2, 2, 2, 2, 2,
          },
           {
              4, 4, 4, 4, 4,
              3, 3, 3, 3, 3,
              3, 3, 3, 3, 3,
          },
          {
              6, 6, 6, 6, 6,
              4, 4, 4, 4, 4,
              4, 4, 4, 4, 4,
          },
          
          {
              6, 6, 6, 6, 6,
              5, 5, 5, 5, 5, 
              4, 4, 4, 4, 4,
              4, 4, 4, 4, 4,
          },
          {
              6, 6, 6, 6, 6,
              5, 5, 5, 5, 5, 
              4, 4, 4, 4, 4,
              2, 2, 2, 2, 2,
          },
          {
              6, 6, 6, 6, 6, 
              2, 2, 2, 2, 2,
              5, 5, 5, 5, 5, 
          },
          {
              6, 6, 6, 6, 6, 
              2, 2, 2, 2, 2,
              7, 7, 7, 7, 7, 
              6, 6, 6, 6, 6, 
          },
          {
              6, 6, 6, 6, 6, 
              5, 5, 5, 5, 5, 
              7, 7, 7, 7, 7, 
          },
          
              {
                  8, 8, 8, 8, 8, 
                  7, 7, 7, 7, 7, 
                  7, 7, 7, 7, 7, 
              },
          {
              6, 6, 6, 6, 6, 
              7, 7, 7, 7, 7, 
              7, 7, 7, 7, 7, 
              4, 4, 4, 4, 4,
          },
          {
              6, 6, 6, 6, 6, 
              7, 7, 7, 7, 7, 
              5, 5, 5, 5, 5, 
              4, 4, 4, 4, 4,
          },
          
          {
                  2, 2, 2, 2, 2,
              7, 7, 7, 7, 7, 
              5, 5, 5, 5, 5, 
              4, 4, 4, 4, 4,
          },
          
              {
                  6, 6, 6, 6, 6, 
                  7, 7, 7, 7, 7, 
                  5, 5, 5, 5, 5, 
                  4, 4, 4, 4, 4,
              },
          {
              7, 7, 7, 7, 7, 
              8, 8, 8, 8, 8, 
              7, 7, 7, 7, 7, 
          },
          
          {
              8, 8, 8, 8, 8,
              8, 8, 8, 8, 8, 
              9, 9, 9, 9, 9, 
          },
          {
              9, 9, 8, 9, 9, 
              8, 8, 8, 8, 8, 
              8, 8, 8, 8, 8, 
              9, 9, 9, 9, 9, 
          },
          {
              9, 9, 9, 9, 9, 
              8, 8, 8, 8, 8, 
              8, 8, 8, 8, 8, 
              9, 9, 9, 9, 9, 
          }
      };

    
}
