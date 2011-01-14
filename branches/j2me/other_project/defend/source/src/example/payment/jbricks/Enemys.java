
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Enemys {

    Vector <Enemy> enemys;
    Vector <Formation> formations;
    int timeToNextEnemy;
    int timeToNextFormation;
    
    // if != 0, only the enemys for this boss are generated
    int bossActive;
    
    int enemysGenerated;
    int enemysKilled;

    
    public  Enemys() {
        
      enemys = new Vector<Enemy>();
      formations = new Vector<Formation>();
      timeToNextEnemy = Global.GENERATE_ENEMY_DELAY +
        Util.getRandomInt(0,Global.GENERATE_ENEMY_RAND_DELAY+1);

      timeToNextFormation = Global.GENERATE_FORMATION_DELAY +
        Util.getRandomInt(0,Global.GENERATE_FORMATION_RAND_DELAY+1);
      bossActive = 0;
      enemysKilled = 0;
      enemysGenerated = 0;
    }
    
    
    void addEnemy( Enemy newEnemy ) {
      if ( newEnemy != null ) {
        enemys.addElement( newEnemy );
        enemysGenerated++;
      }
    }
    
    boolean minibossDead() {
      for (Enemy  e: enemys) {
        if ( e.getType() == Global.BOSS_2 ) return false;
      }
      return true;
    }
      
    boolean bossDead() {
      boolean dead = true;

      for (Enemy e: enemys) {
        if ( e.getType() >= Global.NR_ENEMY_TYPES_NORMAL &&
            e.getType() < Global.NR_ENEMY_TYPES) {
            dead = false;
            break;
        }
      }
      if (dead) bossActive = 0;
      return dead;
    }
    
    
    void bossTime( int bossNr ) {
      bossActive = bossNr;
      Enemy boss;
      int yOffset = -70;
      //Wreck *wreck;
      switch (bossNr) {
      // Final boss
      case 1:
        {
          //wreck = new Wreck( new Vector2D( SCREEN_WIDTH / 2 , -120 ), WRECK_BOSS_1_BACKGROUND );
          //wrecks->addWreck( wreck );
          boss = new Enemy(new Vector2D(120, yOffset+35 ), new Vector2D( 0,
          Global.SCROLL_SPEED*2 ), Global.BOOS_1_NOGUN );
          addEnemy( boss );
          boss = new Enemy(new Vector2D(120, yOffset+50 ), new Vector2D( 0,
          Global.SCROLL_SPEED*2 ), Global.BOSS_1_MAIN_GUN );
          addEnemy( boss );
          boss = new Enemy( new Vector2D(92, yOffset+54 ), new Vector2D( 0,Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_SHOT_BATTERY_LEFT );
          addEnemy( boss );
          boss = new Enemy( new Vector2D( 148, yOffset+54 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_SHOT_BATTERY_RIGHT );
          addEnemy( boss );
          boss = new Enemy( new Vector2D( 18, yOffset+49 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_ROCKET_LAUNCHER );
          addEnemy( boss );
          boss = new Enemy( new Vector2D( 222, yOffset+49 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_ROCKET_LAUNCHER );
          addEnemy( boss );
          break;
        }
      // Miniboss  
      case 2: 
        {
          boss = new Enemy( new Vector2D( Screen.width/ 2 , -100 ),
           new Vector2D( 0, Global.SCROLL_SPEED*2 ),  Global.BOSS_2 );
          addEnemy( boss );
          break;
        }
      case 3:
        {
          yOffset = -100;
          //wreck = new Wreck( new Vector2D( SCREEN_WIDTH / 2 , -120 ), WRECK_BOSS_1_BACKGROUND );
          //wrecks->addWreck( wreck );
          boss = new Enemy(new Vector2D(120, yOffset+50 ), new Vector2D( 0,
          Global.SCROLL_SPEED*2 ), Global.BOOS_1_NOGUN );
          addEnemy( boss );
          boss = new Enemy(new Vector2D(120, yOffset+68 ), new Vector2D( 0,
          Global.SCROLL_SPEED*2 ), Global.BOSS_3 );
          addEnemy( boss );
          boss = new Enemy( new Vector2D(83, yOffset+35 ), new Vector2D( 0,Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_SHOT_BATTERY_LEFT );
          addEnemy( boss );
          boss = new Enemy( new Vector2D(157, yOffset+35 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_SHOT_BATTERY_LEFT );
          addEnemy( boss );
          boss = new Enemy( new Vector2D( 65, yOffset+85 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_ROCKET_LAUNCHER );
          addEnemy( boss );
          boss = new Enemy( new Vector2D( 175, yOffset+85 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                  Global.BOSS_1_ROCKET_LAUNCHER );
          addEnemy( boss );
          break;
        }
      case 4:
             {
               yOffset = -150;
               //wreck = new Wreck( new Vector2D( SCREEN_WIDTH / 2 , -120 ), WRECK_BOSS_1_BACKGROUND );
               //wrecks->addWreck( wreck );
               boss = new Enemy(new Vector2D(120, yOffset+75 ), new Vector2D( 0,
               Global.SCROLL_SPEED*2 ), Global.BOOS_1_NOGUN );
               addEnemy( boss );
               boss = new Enemy(new Vector2D(44, yOffset+70 ), new Vector2D( 0,
               Global.SCROLL_SPEED*2 ), Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D(199, yOffset+80 ), new Vector2D( 0,Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D(70, yOffset+32), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D( 180, yOffset+40 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D( 126, yOffset+17 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );

               boss = new Enemy( new Vector2D(63, yOffset+113), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D( 174, yOffset+121 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               boss = new Enemy( new Vector2D( 115 , yOffset+137 ), new Vector2D( 0, Global.SCROLL_SPEED*2 ), 
                       Global.BOSS_1_SHOT_BATTERY_LEFT );
               addEnemy( boss );
               break;
             }

      default: 
        {
          break;
        }
      }
    }
    
    
    void generateEnemys( int dT ) {
      if ( bossActive != 0 ||
        Engine.ME.bossDead == true) return;
    
//#if 0
//no formation now
      timeToNextFormation -= dT;
      if ( timeToNextFormation < 0 ) {
        int  whichFormation = Util.getRandomInt(0, Global.NR_FORMATION_TYPES);
        int flags = 0;
        if ( Util.getRandomInt(0, 2) == 0 ) flags = flags | Global.FORMATION_CHANGE_ON_KILL;
        if ( Util.getRandomInt(0, 2) == 0 ) { 
          flags = flags | Global.FORMATION_CHANGE_SPONTANEOUS;
          if ( Util.getRandomInt(0, 2) == 0 ) flags = flags | Global.FORMATION_CHANGE_OFTEN;
          else flags = flags | Global.FORMATION_CHANGE_SELDOM;
        }
        int shotPattern = 
         Global.getRandValue( Global.FORMATION_SP_CHANCES, Global.NR_FORMATION_SP );
        //#if 0
        //when formation is done, open this
        Formation newV = 
          new Formation( whichFormation,
                 Vector2D( 80 + (rand() % (SCREEN_WIDTH  - 160)), -100 ),
                 Vector2D( 0, 40 ),
                 FORMATION_MAX_NR_ENEMYS[ whichFormation ] - (rand() % 3),
                 (FormationEnemySets)(rand() % NR_FORMATION_ENEMY_SETS),
                 flags, shotPattern );
        formations.addElement( newV );
       //#endif
        timeToNextFormation = 
          Global.GENERATE_FORMATION_DELAY + 
          Util.getRandomInt(0, (Global.GENERATE_FORMATION_RAND_DELAY + 1));
      }
 //#endif
    
      timeToNextEnemy -= dT;
      if ( timeToNextEnemy < 0 ) {
        int enemyType = Global.getRandValue( Global.ENEMY_APPEAR_CHANCES, Global.NR_ENEMY_TYPES_NORMAL );
        Enemy newOne = null;
        switch (enemyType) {
        case Global.FIGHTER:
        {
            boolean collides = false;
            int xpos = 0;
            for ( int i = 0; i < 10; i++ ) {
              collides = false;
              xpos =  15 + Util.getRandomInt(0,Screen.width-30);
              //#if 0
              // check if collides with a formation
              for (int f = 0; f < formations.size(); f++ ) {
                Vector2D formationCenter = formations[ f ]->getCenter();
                collides =
                  formationCenter.getY() < 150 && 
                  fabs(xpos - formationCenter.getX()) < 150;
                if (collides) break;
              }
              //#endif
            }
            if ( !collides ) {
              newOne = new Enemy( new Vector2D( xpos, -15 ), // position
                          new Vector2D( Util.getRandomInt(0,5) - 2, 
                          Util.getRandomInt(0,7) + 3 ).div(0.1), // velocity
                          Global.FIGHTER );
            }
            break;
        }
        case Global.BOMBER:
          {
        boolean collides = false;
        int xpos = 0;
        for ( int i = 0; i < 10; i++ ) {
          collides = false;
          xpos =  15 + Util.getRandomInt(0,Screen.width-30);
          //#if 0
          // check if collides with a formation
          for ( unsigned int f = 0; f < formations.size(); f++ ) {
            Vector2D formationCenter = formations[ f ]->getCenter();
            collides =
              formationCenter.getY() < 150 && 
              fabs(xpos - formationCenter.getX()) < 150;
            if (collides) break;
          }
          //#endif
        }
        if ( !collides ) {
          newOne = new Enemy( new Vector2D( xpos, -20 ), // position
              new Vector2D( Util.getRandomInt(0,5) - 2, 
                                       Util.getRandomInt(0,3) + 3 ).div(0.1), // velocity
          Global.BOMBER );
        }
        break;
          }
        case Global.TANK:
          {
    //  Graphics spriteTank = surfaceDB.loadSurface( FN_ENEMY_TANK );
    //  Graphics spriteTankWreck = surfaceDB.loadSurface( FN_WRECK_TANK );
        int halfWidthTank = 20;
        int halfHeightTank = 20;
    
        int xPos;
        
        for ( int i = 0; i < 10; i++ ) {
          boolean collides = false;
          xPos =  20 + Util.getRandomInt(0,Screen.width-40);
          // check if collides with another tank
          for(Enemy e: enemys) {
              if ( e.getType() ==Global.TANK ) {
                  Vector2D enemyTankPos = e.getPos();
                  collides = 
                !(enemyTankPos.getX() - halfWidthTank > xPos + halfWidthTank) &&
                !(enemyTankPos.getX() + halfWidthTank < xPos - halfWidthTank) &&
                !(enemyTankPos.getY() - halfHeightTank > 0);
                  if (collides) break;
              }
          }
          
          // no collision -> generate this enemy
          if ( !collides ) {
            // the tank and the background have to be at the same pixel-fraction 
            // to avoid a waggle-effect
            newOne = new Enemy( new Vector2D( xPos, -halfHeightTank - 10 ),
                    new Vector2D (0, Global.SCROLL_SPEED), // tanks are not moving
                    Global.TANK );
            break;
          }
        }
        
        break;
          }
        default:
          {
        break;
          }
        }
        addEnemy( newOne );
        // +1 for security
        timeToNextEnemy = 
          Global.GENERATE_ENEMY_DELAY + 
          Util.getRandomInt(0, (Global.GENERATE_ENEMY_RAND_DELAY + 1));

      }
    }
    
    void updateEnemys( int dT ) {
      //#if 0
      vector<Formation *>::iterator f;
      for (f = formations.begin(); f != formations.end(); ++f) {
        (*f)->update( dT );
      }
      //#endif
      
      for(Enemy e: enemys) {
        e.update( dT );
      }
    }
    
    void doNukeDamage() {
      
      for(Enemy e: enemys) {
        e.doDamage( Global.SPECIAL_SHOT_NUKE);
      }
    }


    void deleteAll() {
        enemys.removeAllElements();

    }
    
    void deleteExpiredEnemys() {
      int i = 0;
      while ( i < enemys.size() ) {
        if ( enemys.elementAt(i).isExpired() ) {
          if ( enemys.elementAt(i).isDead() ) {
            enemysKilled++;
          }
          //#if 0
          for ( int f = 0; f < formations.size(); f++ ) {
            formations[f]->enemyKilled( enemys[i] );
          }
          //#endif
          enemys.removeElementAt(i);
          //enemys.erase(enemys.begin() + i);
        } else {
          i++;
        }
      }
      //#if 0
      int f = 0;
      while ( f < formations.size() ) {
        if ( formations[f]->isExpired() ) {
          // assert, that the formation is not deleted, because all
          // enemys in the formation flew out of the screen
          if ( arcadeGame && formations[f]->getCenter().getY() < SCREEN_HEIGHT + 400 ) {
        banners->addBanner( (BannerTexts)(rand() % NR_BANNER_TEXTS),
                    BANNER_MODE_RANDOM, 
                    ARCADE_BONUS_FOR_FORMATION_DESTRUCTION );
        racers->receivePointsArcade( ARCADE_POINTS_FOR_FORMATION_DESTRUCTION );
          }
          delete formations[f];
          formations.erase(formations.begin() + f);
        } else {
          f++;
        }
      }
      //#endif
    }
    
    void drawGroundEnemys(Graphics screen) {
      
      for(Enemy e: enemys) {
        e.drawGroundEnemy(screen);
      }
    }
    
    void drawAirEnemys(Graphics screen) {
      
      for(Enemy e: enemys) {
        e.drawAirEnemy(screen);
      }
    }
    void drawShadows(Graphics screen) {
      //#if 0
      vector<Enemy >::iterator i;
      for (i = enemys.begin(); i != enemys.end(); ++i) {
        (*i)->drawShadow(screen);
      }
      //#endif
    }

    //#if 0  
    void drawBossStats( Graphics screen ) {
      for ( unsigned int i = 0; i < enemys.size(); i++ ) {
        if ( enemys[ i ]->getType() >= NR_ENEMY_TYPES_NORMAL ) {
          enemys[ i ]->drawStats( screen );
        }
      }
    }
    //#endif
    
     Enemy getEnemy(int i) { return enemys.elementAt(i); }  
     int getNrEnemys() { return enemys.size(); }
     int getNrEnemysKilled() { return enemysKilled; }
     int getNrEnemysGenerated() { return enemysGenerated; }
}

