
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

    public class Shot {

     int timeToLive;
     
     Vector2D pos;
     Vector2D vel;
     
     int shotType;
     
     Image sprite;
     
     boolean collidedWithGround; // defaultValue = false
     
     int fromWhichPlayer;

     Engine engine;

     int draw_w;
     int draw_h;

     
     Shot(int shotType, int playerNr,Vector2D position, float angle ) {

      this.shotType = shotType;
      engine=Engine.ME;
      pos = position;
      collidedWithGround = false;
      fromWhichPlayer = playerNr;


      switch (shotType) {
        // primary shots
      case Global.SHOT_NORMAL:
        {
          vel = new Vector2D( Global.VEL_SHOT_NORMAL, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_NORMAL;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_NORMAL );
          break;
        }
      case Global.SHOT_NORMAL_HEAVY:
        {
          vel = new Vector2D( Global.VEL_SHOT_NORMAL_HEAVY, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_NORMAL_HEAVY;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_NORMAL_HEAVY );
          break;
        }
      case Global.SHOT_DOUBLE:
        {
          vel = new Vector2D(Global.VEL_SHOT_DOUBLE, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_DOUBLE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_DOUBLE );
          break;
        }
      case Global.SHOT_DOUBLE_HEAVY:
        {
          vel = new Vector2D(Global.VEL_SHOT_DOUBLE_HEAVY, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_DOUBLE_HEAVY;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_DOUBLE_HEAVY );
          break;
        }
      case Global.SHOT_TRIPLE:
        {
          vel = new Vector2D(Global.VEL_SHOT_TRIPLE, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_TRIPLE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_TRIPLE );
          break;
        }

        // secondary shots
      case Global.SHOT_DUMBFIRE:
        {
          vel = new Vector2D(Global.VEL_SHOT_DUMBFIRE, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_DUMBFIRE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_DUMBFIRE );
          break;
        }
      case Global.SHOT_DUMBFIRE_DOUBLE:
        {
          vel = new Vector2D(Global.VEL_SHOT_DUMBFIRE_DOUBLE, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_DUMBFIRE_DOUBLE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_DUMBFIRE_DOUBLE );
          break;
        }
      case Global.SHOT_KICK_ASS_ROCKET: 
        {
          vel = new Vector2D(Global.VEL_SHOT_KICK_ASS_ROCKET, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_KICK_ASS_ROCKET;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_KICK_ASS_ROCKET );
          break;
        }
      case Global.SHOT_HELLFIRE:
        {
          vel = new Vector2D(Global.VEL_SHOT_HELLFIRE, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_HELLFIRE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_HELLFIRE );
          break;
        }
      case Global.SHOT_MACHINE_GUN:
        {
          vel = new Vector2D(Global.VEL_SHOT_MACHINE_GUN, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_MACHINE_GUN;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_MACHINE_GUN );
          break;
        }
      case Global.SHOT_ENERGY_BEAM:
        {
          vel = new Vector2D(Global.VEL_SHOT_ENERGY_BEAM, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_SHOT_ENERY_BEAM;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_ENERGY_BEAM);
          break;
        }


        // special shots
      case Global.SPECIAL_SHOT_NUKE:
        {
          vel = new Vector2D(Global.VEL_SPECIAL_SHOT_NUKE, angle, Vector2D.POLAR );
          timeToLive = Global.LIFETIME_SPECIAL_SHOT_NUKE;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SPECIAL_SHOT_NUKE );
          break;
        }

        // enemy shots
      case Global.ENEMY_SHOT_NORMAL:
        {
          vel = new Vector2D(Global.VEL_ENEMY_SHOT_NORMAL, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_ENEMY_SHOT_NORMAL;
          sprite = engine.surfaceDB.loadSurface( Global.FN_ENEMY_SHOT_NORMAL );
          break;
        }
      case Global.ENEMY_SHOT_TANK_ROCKET:
        {
          vel = new Vector2D(Global.VEL_ENEMY_SHOT_TANK_ROCKET, angle, Vector2D.POLAR);
          timeToLive = Global.LIFETIME_ENEMY_SHOT_TANK_ROCKET;
          sprite = engine.surfaceDB.loadSurface( Global.FN_ENEMY_SHOT_TANK_ROCKET );
          break;
        }
        
      default: 
        {
          vel = new Vector2D(0,0);
          timeToLive = 0;
          sprite = engine.surfaceDB.loadSurface( Global.FN_SHOT_NORMAL );
          break;
        }
      }
      this.draw_w = sprite.getWidth();
      this.draw_h = sprite.getHeight();
    }


    void moveAndCollide( int dT ) {

      if ( fromWhichPlayer == 666 ) {
        moveAndCollideEnemyShot(dT);
      } else {
        moveAndCollidePlayerShot(dT);
      }


      timeToLive -= dT;
    }

    Vector2D posOld = new Vector2D(0, 0);

    void moveAndCollidePlayerShot( int dT ) {

      posOld.setVector2D((int)pos.getX(), (float)pos.getY(), Vector2D.CARTESIAN);

      // move the shot
      movePlayerShot( dT );

      if ( !isExpired() && collidePlayerShot( posOld ) ) {
        addExplosion();
      }
    }


    private void moveshot(int dT){
        pos.x += vel.x*dT/1000.0;
        pos.y += vel.y*dT/1000.0;
    }
    
    void movePlayerShot( int dT ) {
      switch (shotType) {
      case Global.SHOT_NORMAL:
      case Global.SHOT_NORMAL_HEAVY:
      case Global.SHOT_DOUBLE:
      case Global.SHOT_DOUBLE_HEAVY:
      case Global.SHOT_TRIPLE:
      case Global.SHOT_DUMBFIRE:
      case Global.SHOT_DUMBFIRE_DOUBLE:
      case Global.SHOT_KICK_ASS_ROCKET:
      case Global.SHOT_MACHINE_GUN:
      case Global.SHOT_ENERGY_BEAM:
      {
//#if 0
         pos.add(vel.multiNew(dT / 1000.0));
//#endif
         moveshot(dT);
         break;
      }
      case Global.SHOT_HELLFIRE:
        {
          moveshot(dT);
          if ( timeToLive < Global.LIFETIME_SHOT_HELLFIRE - 100 ) {
    	    vel = new Vector2D( 0, -Global.VEL_SHOT_HELLFIRE );
          }
          break;
        }
        

      case Global.SPECIAL_SHOT_NUKE:
        {
          moveshot(dT);
          // Nuke is in its place!
          if ( pos.minusNew(new Vector2D(Screen.width/2.0, Screen.height/2.0)).getLength()
    	   <= 10.0 ) {
    	   engine.nukeIsInPlace = true;
          }
          break;
        }

      default: 
        {
          break;
        }
      }

      // clip at the outside of the window
      if(isShotOutsideScreen()) {
        timeToLive = 0;
      }
    }

    boolean isShotOutsideScreen(){
        
       int x = (int)pos.x;
       int y = (int)pos.y;
       
       if(!(x>-50 &&
        x<(Screen.width+10) &&
        y > -50 &&
        y < (Screen.height+10))) {
            return true;
       } else {
            return false;
       }
        
    }
    BoundingBox box ;


    void processCollide(int i) {
        int type =engine.enemys.getEnemy(i).getType();
        engine.enemys.getEnemy(i).doDamage( shotType);
        timeToLive = 0;
        collidedWithGround = !Global.ENEMY_FLYING[type];
    }

    
    boolean collidePlayerShot( Vector2D posOld ) {
      switch (shotType) {
        // only against air
      case Global.SHOT_ENERGY_BEAM:
        {
          if(box == null) {
            box = new BoundingBox((int)Math.floor(posOld.getX()) - draw_w / 2,
    		       (int)Math.floor(pos.getY()) - draw_h / 2,
    		       draw_w,
    		       (int)Math.floor((posOld.minus(pos)).getY()) + draw_h );		
          } else {
            box.moveLeftBound((int)Math.floor(posOld.getX()) - draw_w / 2);
            box.moveUpperBound((int)Math.floor(pos.getY()) - draw_h / 2);
            box.setWidth(draw_w);
            box.setHeight((int)Math.floor((posOld.minus(pos)).getY()) + draw_h );
          }     
          int  d = engine.enemys.getNrEnemys();
          //System.out.println("xxxx"+d);
          for (int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
            int type =engine.enemys.getEnemy(i).getType();
        	if (/*Global.ENEMY_FLYING[type] &&*/
        	     engine.enemys.getEnemy(i).collidesWith( box ) ) {
        	  processCollide(i);
        	  return true;
        	}
          }
          break;
        }
      // against air and ground
      case Global.SHOT_NORMAL:
      case Global.SHOT_NORMAL_HEAVY:
      case Global.SHOT_DOUBLE:
      case Global.SHOT_DOUBLE_HEAVY:
      case Global.SHOT_TRIPLE:
        {
          for (int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
        	if (engine.enemys.getEnemy(i).collidesWith(posOld, pos ) ) {
        	  processCollide(i);
        	  return true;
        	}
          }
          break;
        }
        // against air and ground
      case Global.SHOT_DUMBFIRE:
      case Global.SHOT_DUMBFIRE_DOUBLE:
        {
          for (int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
        	if ( engine.enemys.getEnemy(i).collidesWith(new Circle(pos, 15) ) ) {
        	  
        	  processCollide(i);
        	  return true;
        	}
          }
          break;
        }
        // only against ground
      case Global.SHOT_KICK_ASS_ROCKET:
      case Global.SHOT_HELLFIRE:
        {
          for (int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
        	if ( /*(!Global.ENEMY_FLYING[ engine.enemys.getEnemy(i).getType() ]) && */
        	     engine.enemys.getEnemy(i).collidesWith( new Circle(pos, 15) ) ) {
        	  processCollide(i);
        	  return true;
    	    }
          }
          break;
        }
        //#if 0
       // only against ground, but has to hit more exactly than kickAssRocket
      case Global.SHOT_HELLFIRE:
        {
          for (int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
        	if ( (!Global.ENEMY_FLYING[ engine.enemys.getEnemy(i).getType()]) && 
        	     engine.enemys.getEnemy(i).collidesWith( new Circle(pos, 5) ) ) {
        	 processCollide(i);
        	  return true;
        	}
          }
          break;
        }
      //#endif
        // against air and ground
      case Global.SHOT_MACHINE_GUN:
        {
          for (  int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
        	if ( engine.enemys.getEnemy(i).collidesWith( posOld, pos ) ) {
        	  processCollide(i);
        	  return true;
        	}
          }
          break;
        }

      case Global.SPECIAL_SHOT_NUKE: break;
        
      default: 
        {
          return false;
        }
      }
      return false;
    }

    ///////////////////////////

    void moveAndCollideEnemyShot(int dT ) {
      posOld.setVector2D((int)pos.getX(), (float)pos.getY(), Vector2D.CARTESIAN) ;

      moveEnemyShot( dT );

      if ( !isExpired() && collideEnemyShot( posOld ) ) {
        addExplosion();
      }
    }


    void moveEnemyShot( int dT ) {
      //pos = pos + vel * dT / 1000.0;
      //pos.add(vel.multiNew(dT / 1000.0));
      moveshot(dT);

   
      if(isShotOutsideScreen()) {
        timeToLive = 0;
      }
    }


    boolean collideEnemyShot( Vector2D posOld ) {
    
      if (engine.racer.collidesWith( posOld, pos ) ) {
      	  engine.racer.doDamage( shotType );
      	  timeToLive = 0;
      	  collidedWithGround = false;
      	  return true;
  	  }
       
      return false;
    }

    /////////////

    void addExplosion() {
      Explosion explosion;
      switch (shotType) {
      default: 
        {
          if ( collidedWithGround ) {
    	    explosion = new Explosion(Global.FN_EXPLOSION_NORMAL, pos, 
    				   vel.div(10.0), Global.EXPLOSION_NORMAL_GROUND );
          } else {
        	  explosion = new Explosion( Global.FN_EXPLOSION_NORMAL, pos, 
        				      vel.div(10.0), Global.EXPLOSION_NORMAL_AIR );
          }
          break;
        }
      }
      engine.explosions.addExplosion( explosion );
    }

    ////////////////////
    //#if 0
    void drawShadow(Graphics screen) {
      switch (shotType) {
      case Global.SHOT_KICK_ASS_ROCKET:
      case Global.SHOT_HF_KICK_ASS_ROCKET:
      case Global.SHOT_HELLFIRE:
      case SPECIAL_SHOT_NUKE:
        {
          SDL_Rect shadowR;
          shadowR.x = lroundf(pos.getX()) - spriteShadow.w / 2 - 7;
          shadowR.y = lroundf(pos.getY()) - spriteShadow.h / 2 + 7;
          shadowR.w = spriteShadow.w;
          shadowR.h = spriteShadow.h;
          SDL_BlitSurface( spriteShadow, 0, screen, &shadowR );
          break;
        }
      case ENEMY_SHOT_TANK_ROCKET: 
        {
          SDL_Rect destR;
          SDL_Rect srcR;
          destR.x = lroundf(pos.getX()) - spriteShadow.w / 16 - 10;
          destR.y = lroundf(pos.getY()) - spriteShadow.h / 2 + 10;
          destR.w = spriteShadow.w / 8;
          destR.h = spriteShadow.h;
          float angle = vel.getDirection() + 202.5;
          int idx = lroundf(angle) % 360;
          idx = idx / 45;
          srcR.x = idx * spriteShadow.w / 8;
          srcR.y = 0;
          srcR.w = spriteShadow.w / 8;
          srcR.h = spriteShadow.h;

          SDL_BlitSurface( spriteShadow, &srcR, screen, &destR );
          break;
        }
      default: break;
      }
    }
    //#endif


    void drawSprite(Graphics screen) {
        int x2 = (int)Math.floor(pos.getX()) - draw_w/ (2);
        int y2 = (int)Math.floor(pos.getY()) - draw_h/ 2;
        
        int x1 = 0;
        int y1 = 0;
        screen.drawRegion(sprite, x1,y1,draw_w,draw_h,0,x2, y2 , Global.TL);

    }
    void drawGroundShot(Graphics screen) {
      switch (shotType) {
      case Global.SHOT_KICK_ASS_ROCKET:
      case Global.SHOT_HELLFIRE:
        {
          
          drawSprite(screen);
          break;
        }
      default: break;
      }
    }
     
    void drawGroundAirShot(Graphics screen) {
      switch (shotType) {
      case Global.SHOT_DUMBFIRE:
      case Global.SHOT_DUMBFIRE_DOUBLE:
        {
          
          int x2 = (int)Math.floor(pos.getX()) - draw_w/ (16);
          int y2 = (int)Math.floor(pos.getY()) - draw_h/ 2;
          
          float angle = (float)(vel.getDirection() + 202.5);
          int idx = (int)Math.floor(angle) % 360;
          idx = idx / 45;
          int x1 = idx * 8;
          int y1 = 0;
          screen.drawRegion(sprite, x1,y1,draw_w/8,draw_h,0,x2, y2 , Global.TL);
          break;
        }
      case Global.SHOT_MACHINE_GUN:
        {
          drawSprite(screen);
          break;
        }
      case Global.SHOT_ENERGY_BEAM:
        {
            drawSprite(screen);
          break;
        }
      case Global.ENEMY_SHOT_TANK_ROCKET:
        {
          
          int x2 = (int)Math.floor(pos.getX()) - draw_w/ (16);
          int y2 = (int)Math.floor(pos.getY()) - draw_h/ 2;
          
          float angle = (float)(vel.getDirection() + 202.5);
          int idx = (int)Math.floor(angle) % 360;
          idx = idx / 45;
          int x1 = idx * draw_w/8;
          int y1 = 0;
          screen.drawRegion(sprite, x1,y1,draw_w/8,draw_h,0,x2, y2 , Global.TL);
          break;
        }

      default: break;
      }
    }

    void drawAirShot(Graphics screen) {
      switch (shotType) {
      case Global.SHOT_NORMAL:
      case Global.SHOT_NORMAL_HEAVY:
      case Global.SHOT_DOUBLE:
      case Global.SHOT_DOUBLE_HEAVY:
      case Global.SHOT_TRIPLE:
      case Global.SPECIAL_SHOT_NUKE:
      case Global.ENEMY_SHOT_NORMAL:
        {
            drawSprite(screen);
          break;
        }
      default: break;
      }
    }

    int getShotType() {
      return shotType;
    }
    
    boolean isExpired() { 
      return (timeToLive <= 0);
    }

}


