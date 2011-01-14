
package example.payment.jbricks;

import java.util.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import de.enough.polish.util.*;


/**
 * This class drives the games (hence the name engine ). It has all the level 
 * data, animates the objects, keeps track of state, and triggers
 * screen redraws.
 */
public class Engine implements Thread {
    public static final int PATTERN_WIDTH = 5;
    public static Engine ME;
    // engine states
    public static final int TITLE = 0;
    public static final int PAUSE = 1;
    public static final int PLAY = 2;
    public static final int OVER = 3;
    public static final int MENU = 4;
    public static final int FEEDBACK = 5;
    public static final int MIDDLE_SUCCESS = 6;
    public static final int GS_ROUNDFINISHED = 7;
    public static final int LOADGAME = 9;

    
    private Screen screen;


    int state;
    int tickCnt;
    long sdlTicks;
    int gameActRuntime;
    long timePauseOn;
    long timeMinibossOn;
    long timeLastUpdate;
    long timeNukeEnd;
    
    boolean paused;

    boolean scrollingOn=true;
    int actBackgroundPos=0;

    
    Background background;
    SurfaceDB surfaceDB;
    Items items;
    Racer racer;
    Explosions explosions ;
    Enemys enemys ;
    Shots shots;


    boolean bossTime;
    boolean minibossTime;
    boolean minibossAlreadyKilled;
    boolean bossNukeEffect;
    boolean nukeIsInPlace;
    int bossExplosion;
    int bossAlarm;
    
    int level;
    
    long lastKeyPress;
    int progress;
    protected Timer timer = null;
    /**
     * Creates Engine object
     *
     * @param screen the screen used for rendering
     */
    public Engine(Screen screen) {
        int i;
        ME = this;
        
        this.screen = screen;
        paused = true;
        sdlTicks = System.currentTimeMillis();
        timeNukeEnd = 0;
        timePauseOn = 0;
        timeMinibossOn = 0;

        state = TITLE;

        scrollingOn = true;

        rsmManager = new RsmManager();

        rsm = rsmManager.rsm;
        musicManager = new MusicManager();
        menu = new DynaMenu();
        menu.setEngine(this);
        

        bossAlarm = musicManager.loadSample(Global.FN_SOUND_BOSS_ALARM );
        surfaceDB = new SurfaceDB();
        lastKeyPress = System.currentTimeMillis();
        Thread runner = new Thread(this);
        runner.start();
    }

    boolean bossDead =false;
    void playOn() {
       updateGameState();
       drawPlayOn();
       if(state == PLAY) {
         if(bossDead == true)
            return;
         switch (level) {
            case 0:
                
                if ( !paused && !minibossTime && 
                (int)Global.GAME_LENGTH  <= gameActRuntime) {
                  generateMiniboss();
                }  
                if ( minibossTime && enemys.bossDead() ) {
                    //minibossKilled();
                    //state = GS_BOSS_KILLED;
                    bossDead = true;
                   
                } 
                break;
            case 1:
                
                if (!paused && !bossTime && 
                (int)Global.GAME_LENGTH < gameActRuntime) {
                  enemys.bossTime(1); // generates the boss
                  bossTime = true;
                }
                if ( bossTime && enemys.bossDead() ) bossDead = true;;
                break;
            case 2:
                if (!paused && !bossTime && 
                (int)Global.GAME_LENGTH < gameActRuntime) {
                  enemys.bossTime(3); // generates the boss
                  bossTime = true;
                }
                if ( bossTime && enemys.bossDead() ) bossDead = true;;
                break;
            case 3:
                if (!paused && !bossTime && 
                (int)Global.GAME_LENGTH < gameActRuntime) {
                  enemys.bossTime(4); // generates the boss
                  bossTime = true;
                }
                if ( bossTime && enemys.bossDead() ) bossDead = true;;
                break;
         }
         if(bossDead) {
            if(timer == null)
              timer = new Timer();
            timer.schedule(new GameTask(), GameTask.DELAY);

         }
        // miniboss
        // endboss
        lastKeyPress = System.currentTimeMillis();
        if ( racer.isDead() )
        {state = GS_ROUNDFINISHED;
         setPaused(true);
        }
        
      }

    }

    void updateGameState() {
      //int dT = (int)(System.currentTimeMillis() - timeLastUpdate);
      //timeLastUpdate += dT;
      int dT=30;
      if ( !minibossTime ) gameActRuntime += dT;
      
      if ( nukeIsInPlace ) handleNuke();
      
      enemys.generateEnemys( dT );
      explosions.updateExplosions( dT );
      //#if 0
      smokePuffs.update( dT );
      wrecks.updateWrecks( dT );
      //#endif
      shots.moveAndCollide( dT );
      enemys.updateEnemys( dT );
      items.generate( dT );
      items.update( dT );
      racer.moveAndCollide( dT );
      racer.pickUpItems();
      racer.shoot();
      racer.rechargeShield( dT );

      enemys.deleteExpiredEnemys();
      shots.expireShots();
      items.expireItems();
      explosions.expireExplosions();
      //wrecks.expireWrecks();

//#if 0
      racer.expireRacers();
//#endif
      //smokePuffs.expireSmokePuffs();
      //sonicDeflectorEffect();

      //banners.update( dT );
      //banners.expireBanners();
      if (scrollingOn) {
        //actBackgroundPos -= (int)Math.floor(Global.SCROLL_SPEED * dT/1000.0);
        actBackgroundPos -= (Global.SCROLL_SPEED);
      }

//#if 0
      if (scrollingOn) {
        bb += 20 * dT % 1000;
        int c = bb/1000;
        if(c >= 1) {
            bb = bb-c*1000;
        }
        actBackgroundPos -= 10 * (dT / 1000+c);
      }
//#endif
    }


    void drawPlayOn() {
        Graphics screen=Screen.GRAPHICS;
        if (tempNukeImage == null) {
        background.draw(screen,actBackgroundPos);

        enemys.drawGroundEnemys(screen);
        shots.drawShadows(screen);
        racer.drawShadows(screen);
        enemys.drawShadows(screen);
        
        
        //smokePuffs.draw(screen);
        
        //sonic1.draw(screen);
        //sonic2.draw(screen);
        
        shots.drawGroundShots(screen);
        shots.drawGroundAirShots(screen);
        items.draw(screen);
        enemys.drawAirEnemys(screen);
        shots.drawAirShots(screen);
        
        racer.drawRacer(screen);
        racer.drawStats(screen);
        explosions.drawGroundExplosions(screen);
        explosions.drawAirExplosions(screen);
        }
       if ( System.currentTimeMillis() < timeNukeEnd ) {
          drawNukeEffect( screen);
        } else {
            tempNukeImage = null;
        }
 //#if 0
        banners.draw(screen);
        if ( !arcadeGame && !bossTime && !minibossTime ) drawTime();
        else {
          if ( bossTime || minibossTime ) {
          fontTime.drawStr( screen, (screen.w / 2), 5, "BOSS", FONT_ALIGN_CENTERED );
          }
          else {
            drawPointsArcadeMode();
          }
        }   
        
        if (paused) drawPaused();
        
        SDL_Flip( screen );
         
        frameCnt++;
   //#endif
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
        int s = 0;

        int bb=0;     
        recentCollision = false;
        then = System.currentTimeMillis();
      

        
        try {
        while (!done) {
            switch (state) {
                case TITLE:
                    
                case OVER:
                    if((System.currentTimeMillis() - lastKeyPress) > 4000){
                    showMenu();
                    lastKeyPress = System.currentTimeMillis();
                    }
                    break;
                
                case MENU:
                    if(key != 0 && menu.ishelpAboutMenu()) {
                        menu.keyPressed(screen.getKeyCode(key), key);
                         screen.repaint();
                    }
                    break;
                case PLAY:
                if(!paused) {
                    playOn();
                }
                screen.repaint();
                break;
                case LOADGAME:
                   initAllSurfaces();
                   progress = 30;
                   if(nukeEffectSurface == null)
                     nukeEffectSurface = surfaceDB.loadSurface(Global.FN_NUKE_EFFECT );
                   initNewGame();   
                   progress = 100;
                   state = PLAY;
                   setPaused(false);
                   
                   timeLastUpdate = System.currentTimeMillis();
                break;
                case MIDDLE_SUCCESS:
                    screen.repaint();
                    break;
                case GS_ROUNDFINISHED:
                    if((System.currentTimeMillis() - lastKeyPress) > 2500){
                        showMenu();
                    }
                    
                    break;
                default:
                    break;
            }
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

    /**
     * Starts the game
     *
     * @param level a level to be started
     */
    protected void startGame(int level) {
        this.level = level;
        //Main.DISPLAY.setCurrent(screen);
        rsmManager.saveRsm();
        switch (level) {
        case 0:
            racer=null;
            initLevel0();
            break;
        case 1:
            initLevel1();
            break;
         case 2:
        initLevel2();
            break;
        default:
            break;
        }
        
        state = LOADGAME;
        
        loadLevel( level );
        progress = 0;
        //paused = true;
        //setPaused(true);
        screen.repaint();
        screen.serviceRepaints();
    } 

    /** load a new level , now we just save level file 
     * in the mesages_cn.txt, since we only have one level*/
    void loadLevel(int level ) {
      // load background tiles
      
      background = new Background();
      background.clearTileList();
      for(int i=0; i < backgroundImage[level].length; i++) {
        //String aa = "BACKG_TILE"+i;
       // String tilename = Locale.get("BACKG_TILE"+i);
        background.addTile( backgroundImage[level][i] );
      }
      //int len = Integer.parseInt(Locale.get("BACKG_LENGTH"));
      background.generateBackground(1000);
    }

    
    void initNewGame() {

//#if 0
      banners = new Banners();
      smokePuffs = new SmokePuffs();
      wrecks = new Wrecks();
//#endif
      Racer tempracer;

      if(racer != null) {
         racer.reset();
      } else {
          racer = new Racer("/lightFighter1.png",
                       new Vector2D(120,250) );

      }
      
      
      explosions = new Explosions();
      progress = 70;
      enemys = new Enemys();
      shots = new Shots();
      progress = 80;
      items = new Items();
      
      gameActRuntime = 0;
      paused = true;
      bossTime = false;
      bossNukeEffect = false;
      bossDead = false;

      minibossTime = false;
      timeMinibossOn = System.currentTimeMillis();
      timeNukeEnd = System.currentTimeMillis();
      timePauseOn = System.currentTimeMillis();
      actBackgroundPos = 0;

      scrollingOn = true;
      
      nukeIsInPlace = false;
//#if 0
      sonic1.setActive( false );
      sonic2.setActive( false );
//#endif
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

        if ((state == OVER) || (state == TITLE) || 
            (state == GS_ROUNDFINISHED)) {
           showMenu();
            //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
        } 
        else if ((state == MIDDLE_SUCCESS)) {
            level++;
            startGame(level);
        } else if ((state == PLAY)) {
            //paused = false;
            setPaused(false);
            //musicManager.start_bg_music();
            racer.handlePlayerEvent(keycode, gameAction, true);
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
        if ((state == PLAY)) {
            racer.handlePlayerEvent(keycode, gameAction, false);
        }  
        
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



    

    /**
     * Sets a flag indicating whether the game was paused
     *
     *@param pause true if the game is paused, false otherwise
     */
    public void setPaused(boolean pause) {
        //if (timer != null) { timer.cancel(); timer = null; }
        if(pause == true &&
            paused == false) {
            racer.resetControl();
            musicManager.stopAudioPlayer();
        } else if(pause == false &&
            paused == true) {
            timeLastUpdate = System.currentTimeMillis();
            racer.resetControl();
            play_bg_music();
        }
        paused = pause;
        
    }

    private void play_bg_music() {

        switch(level) {
        case 0:
            musicManager.playMusic(Global.FN_SOUND_LEVEL_0, -1);
            break;
        case 1:
            musicManager.playMusic(Global.FN_SOUND_LEVEL_1, -1);
            break;
        case 2:
            musicManager.playMusic(Global.FN_SOUND_LEVEL_2, -1);
            break;
        case 3:
            musicManager.playMusic(Global.FN_SOUND_LEVEL_3, -1);
            break;
        }
    }

    
    void handleNuke() {
      //#if 0
      sonic1.setActive( false );
      sonic2.setActive( false );
      //#endif
      enemys.doNukeDamage();
      shots.deleteAllShots();
      timeNukeEnd = System.currentTimeMillis() + Global.NUKE_EFFECT_DURATION;
      nukeIsInPlace = false;
     
    }
    
    Image nukeEffectSurface;
    Image tempNukeImage = null;
    
    void drawNukeEffect(Graphics s) {
      // effect-process: transparent -> nearly opaque -> transparent
//#if 0

      long timeFromMaximum = (Global.NUKE_EFFECT_DURATION / 2) - (timeNukeEnd - 
    System.currentTimeMillis());
      timeFromMaximum = Math.abs(timeFromMaximum);
      //screen.drawImage(nukeEffectSurface, 0,0,Global.TL);
      //RgbImage rgbimage = new RgbImage(nukeEffectSurface, true);
      //RgbImage aimage = ImageUtil.changeColorBalance(rgbimage, 20,20,20,100);
      
      (int)Math.floor(((Global.NUKE_EFFECT_DURATION / 2.0) - timeFromMaximum) * 
              100.0 / (Global.NUKE_EFFECT_DURATION / 2.0))

      SDL_SetAlpha( nukeEffectSurface, SDL_SRCALPHA | SDL_RLEACCEL,
            (int)Math.floor(((NUKE_EFFECT_DURATION / 2) - timeFromMaximum) * 128.0 / 
                (NUKE_EFFECT_DURATION / 2)) );
      SDL_BlitSurface( nukeEffectSurface, 0, screen, 0 );
    
      int randRange = (int)
        (( ((NUKE_EFFECT_DURATION / 2.0) - timeFromMaximum) * NUKE_QUAKE_EFFECT /
             (NUKE_EFFECT_DURATION / 2.0 ) ) + 0.5);
//#endif
      if(tempNukeImage == null) {
        //aimage.paint(0,0,s);
        s.drawImage(nukeEffectSurface,0,0,Global.TL);
        tempNukeImage = Image.createImage(screen.buf);
      }
      int randRange = 10;

      int randX, randY;
      //if ( randRange == 0 ) randRange = 1;
      randX = Util.getRandomInt(0, randRange)- randRange / 2;
      randY = Util.getRandomInt(0, randRange) - randRange / 2;
    
      //MyRect src, dest;
      int x1,y1,x2,y2;
      int w,h;
      if ( randX < 0 ) {
        x1 = -randX;    
        w = screen.width + randX;
        x2 = 0;
        //dest.w = screen.width + randX;
      } else {
        x1 = 0;
        w = screen.width - randX;
        x2 = randX;
        //dest.w = screen.width - randX;
      }
      if ( randY < 0 ) {
        y1 = -randY;    
        h = screen.height+ randY;
        y2 = 0;
        //dest.h = screen.height + randY;
      } else {
        y1 = 0;
        h = screen.height - randY;
        y2 = randY;
        //dest.h = screen.height - randY;
      }
        
      s.drawRegion(tempNukeImage, x1,y1,w,h,0,x2, y2 , Graphics.TOP | Graphics.LEFT);
      //SDL_BlitSurface( screen, &src, screen, &dest );
    }




    
    void generateMiniboss() {
      scrollingOn = false;
      minibossTime = true;
      enemys.bossTime(2); // generates the miniboss
     
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

    
    /**
     * Returns number of current level
     */
    


    /**
     * Shows menu
     */
    protected void showMenu() {
        state = MENU;
        //menu.showResumeItem(true);
        //menu.goMainMenu();
        //menu.show_menu();
        //oldBricks = bricks;
        //bricks = new BrickList(title_pattern, PATTERN_WIDTH, -1);
        //paused = true;
        screen.repaint();
    }

void initAllSurfaces() {
    switch(level) {
    case 0:
        surfaceDB.loadSurface("/boss2.png");
        break;
    case 1:
        
        surfaceDB.loadSurface("/boss1MainGun.png");
        surfaceDB.loadSurface("/boss1RocketLauncher.png");
        surfaceDB.loadSurface("/boss1ShotBatteryLeft.png");
        surfaceDB.loadSurface("/boss1ShotBatteryRight.png");
        break;
    case 2:
        surfaceDB.loadSurface("/boss3.png");
        surfaceDB.loadSurface("/boss3_1.png");
        surfaceDB.loadSurface("/boss3_2.png");
        surfaceDB.loadSurface("/tank.png");
        break;
    case 3:
        surfaceDB.loadSurface("/boss4.png");
        surfaceDB.loadSurface("/boss4_1.png");
        break;

    }
    surfaceDB.loadSurface("/bomber.png");
    surfaceDB.loadSurface("/dumbfire.png");
    surfaceDB.loadSurface("/explosion.png");
    surfaceDB.loadSurface("/explosionEnemy.png");
    surfaceDB.loadSurface("/energyBeam.png");
    surfaceDB.loadSurface("/enemyShotNormal.png");
    surfaceDB.loadSurface("/fighter.png" );
    surfaceDB.loadSurface("/kickAssRocket.png");
    surfaceDB.loadSurface("/hellfire.png");
    surfaceDB.loadSurface("/itemPrimaryUpgrade.png");
    surfaceDB.loadSurface("/itemDumbfireDouble.png");
    surfaceDB.loadSurface("/itemKickAssRocket.png");
    surfaceDB.loadSurface("/itemHellfire.png");
    surfaceDB.loadSurface("/itemMachineGun.png");
    surfaceDB.loadSurface("/itemHealth.png");
    surfaceDB.loadSurface("/itemHeatseeker.png");
    surfaceDB.loadSurface("/itemNuke.png");
    surfaceDB.loadSurface("/itemDeflector.png");
    surfaceDB.loadSurface("/itemEnergyBeam.png");
    surfaceDB.loadSurface("/itemLaser.png");
    surfaceDB.loadSurface("/lightFighterShieldDamaged.png");
    surfaceDB.loadSurface("/machineGun.png");
    surfaceDB.loadSurface("/normalShot.png");
    surfaceDB.loadSurface("/nukeEffect.png");
    surfaceDB.loadSurface("/shotNuke.png");
    surfaceDB.loadSurface("/tankRocket.png");
    //surfaceDB.loadSurface("/background.png");
    return;
}

/**
 * Stops the game
 */
public void stop() {
    done = true;
}

private void initLevel0() {
   Global.GENERATE_ENEMY_RAND_DELAY =3000;
   Global.ENEMY_APPEAR_CHANCES =new int[] {40, 40, 10, 0, 0, 0, 0, 0,0};
   Global.ENEMY_COOLDOWN_PRIMARY =new int[] {500, 500, 1500, 70, 500, 1500, 1500, 1500,0};
   Global.ENEMY_RAND_WAIT_PRIMARY = new int[]{1500, 1000, 1000, 80, 1000, 200, 200, 
    2000,5000};
}

private void initLevel1() {
   Global.GENERATE_ENEMY_RAND_DELAY =2000;
   Global.ENEMY_APPEAR_CHANCES = new int[]{40, 40, 10, 0, 0, 0, 0, 0,0,0};
   Global.ENEMY_COOLDOWN_PRIMARY = new int[]{400, 400, 400, 1000, 1000, 1000, 1000, 1000,300,00};
   Global.ENEMY_RAND_WAIT_PRIMARY = new int[]{1500, 1000, 300, 1000, 1000, 1000, 200, 
    2000,2000,5000};
}

private void initLevel2() {
   Global.GENERATE_ENEMY_RAND_DELAY =2000;
   Global.ENEMY_APPEAR_CHANCES = new int[]{40, 40, 10, 0, 0, 0, 0, 0,0,0};
   Global.ENEMY_COOLDOWN_PRIMARY = new int[]{400, 400, 400, 400, 400, 400, 400, 1000,300,00};
   Global.ENEMY_RAND_WAIT_PRIMARY = new int[]{1500, 1000, 300, 400, 400, 400, 400, 
    400,2000,5000};
}

class GameTask extends TimerTask { 
       final static int DELAY = 2500;
       /** Heartbeat */
       public void run()
       {
         setPaused(true);
         if(level == 3) {
            state = OVER;
            lastKeyPress = System.currentTimeMillis();
         }
         else
            state = MIDDLE_SUCCESS;
       }
}


/*********************************************************
 * private metheds 
 *********************************************************/

  private int key;
  private boolean done;
   DynaMenu menu;
  public Rsm rsm;
  private RsmManager rsmManager;
  MusicManager musicManager;
  static String[][] backgroundImage=new  String[][] {
      {
          "/tile-water.png" ,     
          "/tile-water.png" ,     
          "/tile-oilplatform.png",
          "/tile-island2.png" ,   
          "/tile-island1.png" ,   
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png",      
          "/tile-water.png"    
      },        
      {   "/tile-water.png" ,     
          "/bg4.png" ,    
          "/tile-water.png" ,    
          "/bg3.png" ,   
          "/tile-water.png",  
          "/tile-water.png" ,    
          "/tile-water.png" ,    
          "/bg3.png" ,    
          "/tile-water.png" ,    
          "/tile-water.png" , 
          "/tile-island1.png" , 
           "/tile-water.png" ,    
          "/tile-water.png" , 
           "/tile-water.png" ,    
          "/tile-water.png" , 
           "/tile-water.png" ,    
          "/tile-water.png" , 
           "/tile-water.png" ,    
          "/tile-water.png" , 
          "/tile-water.png" 
          
      },// 
      {"/background.png"},
      {   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" ,   
        "/bg6.png" , 
        "/bg5png",      
        "/bg6.png" 
      }
    };

    
}
