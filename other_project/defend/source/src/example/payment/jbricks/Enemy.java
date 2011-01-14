
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Enemy {


     Image spriteEnemy;
     Image spriteShadow;
     // for collision with racers or shots. 
     // A rectangle with racersize * 0.9 is used.
     BoundingBox boundingBox;
     
     // Movement-System
     Vector2D pos; // absolute position
     Vector2D vel; // the velocity vector
     
     Vector2D relTargetPos;
     
     // needed for cooldown
     int nextShotPrimary;
     int nextShotSecondary;
     
     int sndShotPrimary;
     int sndShotSecondary;
     
     float hitpoints;
     
     int  enemyType;
     
     boolean isInFormation;
     boolean fireByFormation;
     boolean boss2PointReached;
     Vector2D boss2TargetPos;


     Engine engine;

     int draw_w;
     int draw_h;

     
     public Enemy( Vector2D pos, Vector2D vel, int  whichEnemyType) {
        this(pos, vel,whichEnemyType, false, false);
     }

     
     public Enemy( Vector2D pos, Vector2D vel, int  whichEnemyType, 
               boolean isInFormation, boolean fireByFormation ) {
     
       this.isInFormation = isInFormation;
       this.fireByFormation = fireByFormation;
       enemyType = whichEnemyType;
       hitpoints =Global.ENEMY_HITPOINTS[ enemyType ];
     
       this.pos = pos;
       this.vel = vel;
       relTargetPos = new Vector2D(0,0);
       engine = Engine.ME;
       switch ( enemyType ) {
       case Global.FIGHTER: 
         {
           spriteEnemy = engine.surfaceDB.loadSurface( "/fighter.png" );
           break;
         }
       case Global.BOMBER:
         {
           spriteEnemy = engine.surfaceDB.loadSurface( "/bomber.png" );
     
           break;
         }
       case Global.TANK:
         {
           if(engine.level == 2) {
               spriteEnemy = engine.surfaceDB.loadSurface( "/tank.png" );
           } else {
               spriteEnemy = engine.surfaceDB.loadSurface( "/hovercraft.png" );
           }
           break;
         }
     
       case Global.BOSS_1_MAIN_GUN:
         {
           spriteEnemy = engine.surfaceDB.loadSurface( "/boss1MainGun.png" );
           break;
         }
       case Global.BOSS_1_ROCKET_LAUNCHER:
         {
             
             switch(engine.level) {
               case 1:
                  spriteEnemy = engine.surfaceDB.loadSurface( "/boss1RocketLauncher.png" );
                  break;
               case 2:
                  spriteEnemy = engine.surfaceDB.loadSurface("/boss3_2.png" );
                  break;
               default:
                  break;
             }
             
             break;
         }
       case Global.BOSS_1_SHOT_BATTERY_LEFT:
         {
           
           switch(engine.level) {
             case 1:
                spriteEnemy = engine.surfaceDB.loadSurface( "/boss1ShotBatteryLeft.png" );
                break;
             case 2:
                spriteEnemy = engine.surfaceDB.loadSurface("/boss3_2.png" );
                break;
             case 3:
                spriteEnemy = engine.surfaceDB.loadSurface("/boss4_1.png" );
                break;
             default:
                break;
           }
           
           break;
         }
       case Global.BOSS_1_SHOT_BATTERY_RIGHT:
         {
           spriteEnemy = engine.surfaceDB.loadSurface( "/boss1ShotBatteryRight.png" );
           break;
         }
       case Global.BOOS_1_NOGUN:
         { 
           switch(engine.level) {
             case 1:
                spriteEnemy = engine.surfaceDB.loadSurface("/wreckBossBackground.png" );
                break;
             case 2:
                spriteEnemy = engine.surfaceDB.loadSurface("/boss3.png" );
                break;
             case 3:
                spriteEnemy = engine.surfaceDB.loadSurface("/boss4.png" );
                break;
             default:
                break;
           }
           break;
         }
       case Global.BOSS_2:
         {
           spriteEnemy = engine.surfaceDB.loadSurface("/boss2.png");
           boss2PointReached = false;
           boss2TargetPos = new Vector2D(Screen.width/2.0, 150.0);
           break;
         }
       case Global.BOSS_3:
       {
           spriteEnemy = engine.surfaceDB.loadSurface("/boss3_1.png");
           break;
       }
     
       default: 
         {
             spriteEnemy = engine.surfaceDB.loadSurface( "/fighter.png" );
           break;
         }
       }
       draw_w = spriteEnemy.getWidth();
       draw_h = spriteEnemy.getHeight();
      
       boundingBox = new BoundingBox((int)Math.floor(pos.x-draw_w*0.45),
            (int)Math.floor(pos.y-draw_h*0.45),(int)Math.floor(draw_w*0.9),
            (int)Math.floor(draw_h*0.9));
       nextShotPrimary = Util.getRandomInt(0,
        (Global.ENEMY_RAND_WAIT_PRIMARY[ enemyType ]+1));
       nextShotSecondary = Util.getRandomInt(0,
        (Global.ENEMY_RAND_WAIT_SECONDARY[ enemyType ]+1));

       sndShotPrimary =engine.musicManager.loadSample( Global.FN_SOUND_SHOT_PRIMARY );
       sndShotSecondary = engine.musicManager.loadSample( Global.FN_SOUND_SHOT_SECONDARY );
     }
     
     
     
     boolean isExpired() {
       return ( hitpoints <= 0 || 
            pos.getY() < -200 ||
            pos.getY() > Screen.height+ 40 ||
            pos.getX() < -50 || 
            pos.getX() > Screen.width+ 40 );
     }
     
     boolean isDead() { return ( hitpoints <= 0 ); }
     
     int getType() { return enemyType; }
     
     void expire() {
       hitpoints = -1;
     }
     
     Circle getBoundingCircle() {
       return new Circle( pos, (float)Math.min(draw_w/ 2, draw_h/ 2) );
     }
     
     BoundingBox getBoundingBox() {
       return boundingBox;
     }
     
     boolean collidesWith(  Vector2D shotPosOld,  Vector2D shotPosNew ) {
       if(enemyType == Global.BOOS_1_NOGUN)
         return false;
       return boundingBox.overlaps(shotPosOld, shotPosNew);
     }
     
     boolean collidesWith( BoundingBox box ) {
        if(enemyType == Global.BOOS_1_NOGUN)
         return false;

        return boundingBox.overlaps( box );
     }
     
     boolean collidesWith(Circle circle ) {
         if(enemyType == Global.BOOS_1_NOGUN)
         return false;
         return boundingBox.overlaps( circle );
     }
     
     boolean collidesWithAsCircle(Circle circle ) {
        if(enemyType == Global.BOOS_1_NOGUN)
         return false;
         return ( circle.getRadius() + draw_w/ 2 > circle.getCenter().distanceTo( pos ) );
     }
     
     
     
     void update( int dT ) {
       move( dT );
       
       if(enemyType == Global.BOOS_1_NOGUN)
         return;
       if ( !fireByFormation ) {
         shootPrimary( dT );
         shootSecondary( dT );
       }
     }
     
     void move( int dT ) {
       switch ( enemyType ) {
       case Global.FIGHTER:
       case Global.BOMBER:
         {
            //#if 0
           if (engine.scrollingOn) 
              pos.add(vel.multiNew(dT / 1000.0));
           else 
              pos.add(vel.minusNew((double)Global.SCROLL_SPEED));
           //#endif
            pos.add(vel.multiNew(dT / 1000.0));
              //pos += (vel - SCROLL_SPEED) * dT / 1000.0;
           if ( isInFormation && relTargetPos.getLength() > 0.5) {
             Vector2D addMovement = new Vector2D(0,0);
             addMovement.setVector2D(40, (float)relTargetPos.getDirection(), Vector2D.POLAR );
             addMovement.x *= dT / 1000.0;
             addMovement.y *= dT / 1000.0;
             
             if ( addMovement.getLength() > relTargetPos.getLength() ) {
               addMovement.setLength((float) relTargetPos.getLength() );
             }
             pos.add(addMovement);
             relTargetPos.minus(addMovement);
           }
           updateBoundingBox();
           break;
         }
       case Global.TANK: {
         if ( engine.scrollingOn ) pos.add(vel.multiNew(dT / 1000.0));
         updateBoundingBox();
         break;
       }
       case Global.BOOS_1_NOGUN:
       case Global.BOSS_3:
       case Global.BOSS_1_MAIN_GUN:
       case Global.BOSS_1_ROCKET_LAUNCHER:
       case Global.BOSS_1_SHOT_BATTERY_LEFT:
       case Global.BOSS_1_SHOT_BATTERY_RIGHT: {
         if ( engine.scrollingOn ) {
           int position =20;
           pos.add( new Vector2D( 0.0, Global.SCROLL_SPEED * dT / 1000.0 ));
           updateBoundingBox();
           switch(engine.level) {
           case 1:
            position = Global.BOSS_1_END_Y;
            break;
           case 2:
            position = 80;
            break;
           case 3:
             position = 160;
            break;
            default:
                break;
           }
           if ( pos.getY() >= position ) {
             engine.scrollingOn = false;
             pos.setY( (double)position );
           }
         }  
         break;
       }    
       case Global.BOSS_2: {
         if (boss2PointReached ) {
           boss2TargetPos = new Vector2D( Util.getRandomInt(0,(Screen.width- draw_w))+ draw_w/ 2,
            Util.getRandomInt(0, 150) + draw_h/2);
           boss2PointReached = false;
         } else {
            pos.add(boss2TargetPos.minusNew(pos).div(20.0));
         }
         if ( pos.distanceTo(boss2TargetPos) < 10.0 ) boss2PointReached = true;
         updateBoundingBox();
         break;
       }
       default: 
        break;
       }
     }
     
     void updateBoundingBox() {
       boundingBox.moveUpperBound( (int)Math.floor(pos.getY() - draw_h* 0.45) );
       boundingBox.moveLeftBound(  (int)Math.floor(pos.getX() - draw_w* 0.45) );
     }
     
     
     void firePrimary() {
       switch (enemyType) {
       case Global.FIGHTER: 
         {
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_NORMAL, 666, 
               new Vector2D( pos.getX(), pos.getY()+draw_h / 2 ),
               90F );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.BOMBER:
         {
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX()-4, pos.getY()+draw_h / 2 ),
               100F );
           engine.shots.addShot( shot );
           shot = new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX()+4, pos.getY()+draw_h / 2 ),
               80F);
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.TANK:
         {
           Shot shot = new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX(), pos.getY()),
               (Util.getRandomInt(0 ,360)) - 180 );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.BOSS_1_MAIN_GUN:
         {
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX(), pos.getY()),
               (Util.getRandomInt(0 ,20)) + 80 );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.BOSS_1_SHOT_BATTERY_LEFT:
         {
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX(), pos.getY()),
               (Util.getRandomInt(0 ,120)) + 30 );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.BOSS_1_SHOT_BATTERY_RIGHT:
         {
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_NORMAL, 666,
               new Vector2D(pos.getX(), pos.getY()),
               (Util.getRandomInt(0 ,120)) + 30 );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
       case Global.BOSS_1_ROCKET_LAUNCHER:
         {
           float angle = (float)(engine.racer.getPos().minus(pos)).getDirection();
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_TANK_ROCKET, 666, 
            new Vector2D(pos.getX(), pos.getY()), angle );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotSecondary, 1);
           break;
         }
       case Global.BOSS_2:
         {
           float angle = (float)(engine.racer.getPos().minus(pos)).getDirection();
           Shot shot = new Shot( Global.ENEMY_SHOT_NORMAL, 666, 
            new Vector2D(pos.getX(), pos.getY()), angle );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotPrimary, 1 );
           break;
         }
      case Global.BOSS_3:
         {
           float angle;
           for (angle = 30F;angle < 170F ;angle+=20F) {
                Shot shot = new Shot( Global.ENEMY_SHOT_NORMAL, 666, 
                new Vector2D(pos.getX(), pos.getY()), angle );
               engine.shots.addShot( shot );
               //engine.musicManager.playSample( sndShotPrimary, 1 );
           }
           break;
         }
       default:
         {
           break;
         }
       }
     }
     Vector2D getPos() {
        return new Vector2D(pos.getX(), pos.getY());
     }
     Vector2D  getVel() {
        return new Vector2D(vel.getX(), vel.getY());
     }
    
     void shootPrimary( int dT ) {
       nextShotPrimary -= dT;
       if ( nextShotPrimary < 0 ) {
         firePrimary();
         
         nextShotPrimary = Util.getRandomInt(0,
            (Global.ENEMY_RAND_WAIT_PRIMARY[ enemyType ]+1)) +
            Global.ENEMY_COOLDOWN_PRIMARY[ enemyType ];
       }
     }
     
     
     void fireSecondary() {
       switch (enemyType) {
       case Global.TANK:
         {
             float angle = (float)(engine.racer.getPos().minus(pos)).getDirection();
           
           Shot shot = 
         new Shot( Global.ENEMY_SHOT_TANK_ROCKET, 666, 
         new Vector2D(pos.getX(), pos.getY()), angle );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotSecondary, 1);
           break;
         }
       case Global.BOSS_2:
         {
             float angle = (float)(engine.racer.getPos().minus(pos)).getDirection();
           Shot shot = new Shot( Global.ENEMY_SHOT_TANK_ROCKET, 666, 
           new Vector2D(pos.getX()-40, pos.getY()), angle );
           engine.shots.addShot( shot );
           shot = new Shot( Global.ENEMY_SHOT_TANK_ROCKET, 666, 
            new Vector2D(pos.getX()+40, pos.getY()), angle );
           engine.shots.addShot( shot );
           engine.musicManager.playSample( sndShotSecondary, 1);
           break;
         }
       default:
         {
           break;
         }
       }
     }
     
     void shootSecondary( int dT ) {
       nextShotSecondary -= dT;
       if ( nextShotSecondary < 0 ) {
         fireSecondary();
          nextShotSecondary = Util.getRandomInt(0,
            (Global.ENEMY_RAND_WAIT_SECONDARY[ enemyType ]+1)) +
            Global.ENEMY_COOLDOWN_SECONDARY[ enemyType ];
       }
     }
     
     
     void doDamage( int shotType) {
       boolean allreadyDead = isExpired();

       if(enemyType == Global.BOOS_1_NOGUN)
         return;
       switch (shotType) {
       case Global.SHOT_NORMAL: hitpoints -= Global.DAMAGE_SHOT_NORMAL; break;
       case Global.SHOT_NORMAL_HEAVY: hitpoints -= Global.DAMAGE_SHOT_NORMAL_HEAVY; break;
       case Global.SHOT_DOUBLE: hitpoints -= Global.DAMAGE_SHOT_DOUBLE; break;
       case Global.SHOT_DOUBLE_HEAVY: hitpoints -= Global.DAMAGE_SHOT_DOUBLE_HEAVY; break;
       case Global.SHOT_TRIPLE: hitpoints -= Global.DAMAGE_SHOT_TRIPLE; break;
         
         
       case Global.SHOT_DUMBFIRE: hitpoints -= Global.DAMAGE_SHOT_DUMBFIRE; break;
       case Global.SHOT_DUMBFIRE_DOUBLE: hitpoints -= Global.DAMAGE_SHOT_DUMBFIRE_DOUBLE; break;
       case Global.SHOT_KICK_ASS_ROCKET: hitpoints -= Global.DAMAGE_SHOT_KICK_ASS_ROCKET; break;
       case Global.SHOT_HELLFIRE: hitpoints -= Global.DAMAGE_SHOT_HELLFIRE; break;
       case Global.SHOT_MACHINE_GUN: hitpoints -= Global.DAMAGE_SHOT_MACHINE_GUN; break;
       case Global.SHOT_ENERGY_BEAM: hitpoints -= Global.DAMAGE_SHOT_ENERGY_BEAM; break;
     
     
       case Global.SPECIAL_SHOT_NUKE: hitpoints -= Global.DAMAGE_SPECIAL_SHOT_NUKE; break;
       default: 
         {
           break;
         }
       }
     
       if ( enemyType < Global.NR_ENEMY_TYPES_NORMAL ) {
         // the enemy just died . explode, generate item, 
         if ( (!allreadyDead) && isExpired() ) {
           // the player gets points, who did the final shot
           engine.racer.receivePoints( Global.ENEMY_POINTS_FOR_DEST[ enemyType ]);
           // explode
           Explosion newExplosion = 
             new Explosion( Global.FN_EXPLOSION_ENEMY, 
             new Vector2D(pos.getX(), pos.getY()), 
             new Vector2D(vel.getX(), vel.getY()), 
             Global.EXPLOSION_NORMAL_AIR );
           engine.explosions.addExplosion( newExplosion );
            //#if 0
           // generate wreck
           Wreck *newWreck = new Wreck( pos, WRECK_FOR_ENEMYTYPE[ enemyType ]);
           wrecks.addWreck( newWreck );
           //#endif
           // generate item
           if ( Util.getRandomInt(0,
            Global.ENEMY_DIES_ITEM_APPEAR_CHANCE[ enemyType ])== 1 ) {
             engine.items.generateItemNow( new Vector2D(pos.getX(), pos.getY()), 
                new Vector2D(vel.getX(), vel.getY()).div(10.0));
           }
            
         }
       } 
       // it's a boss!!!
       else {
         if ( (!allreadyDead) && isExpired() ) {
           // explode
           Explosion newExplosion;
           for(int i = 0; i < 4; i++){
             newExplosion = new Explosion(
               Global.FN_EXPLOSION_ENEMY,
               new Vector2D(pos.getX()+Util.getRandomInt(0, draw_w)- draw_w / 2,
                pos.getY()+Util.getRandomInt(0, draw_h) - draw_h / 2),
               new Vector2D(vel.getX()+Util.getRandomInt(0, 100)-50,
               vel.getY()+Util.getRandomInt(0, 100)-50).div(5.0),
               Global.EXPLOSION_NORMAL_GROUND );
             engine.explosions.addExplosion( newExplosion );
           }
//#if 0
           // generate wreck
           Wreck *newWreck = new Wreck( pos, WRECK_FOR_ENEMYTYPE[ enemyType ]);
           wrecks.addWreck( newWreck );     
//#endif
         }
       }  
     }
     
     
     void drawGroundEnemy( Graphics screen ) {
       if ( Global.ENEMY_FLYING[ enemyType ] ) 
        return;
     
       int x = (int)Math.floor(pos.getX()) - (draw_w / (2));
       int y = (int)Math.floor(pos.getY()) - (draw_h / 2);
     
     
       screen.drawRegion( spriteEnemy, 0,0,draw_w,draw_h,0,x, y , Global.TL);
       
     }
     
     void drawAirEnemy( Graphics screen ) {
       if ( !Global.ENEMY_FLYING[ enemyType ] ) return;
     
     int x = (int)Math.floor(pos.getX()) - (draw_w / (2));
     int y = (int)Math.floor(pos.getY()) - (draw_h / 2);
     
     
     screen.drawRegion( spriteEnemy, 0,0,draw_w,draw_h,0,x, y , Global.TL);
     }
    
     void drawShadow( Graphics screen ) {
         //#if 0
       if ( !ENEMY_FLYING[ enemyType ] ) return;
     
       SDL_Rect destR;
     
       destR.x = lroundf(pos.getX()) - spriteShadow.w / 2 - ENEMY_FLYING_HEIGHT[ enemyType ];
       destR.y = lroundf(pos.getY()) - spriteShadow.h / 2 + ENEMY_FLYING_HEIGHT[ enemyType ];
       destR.w = spriteShadow.w;
       destR.h = spriteShadow.h;
     
       SDL_BlitSurface( spriteShadow, 0, screen, &destR );
       //#endif
     }
     
     
     void drawStats( Graphics screen ) {
       //#if 0
       // draw status of the bosses
       float pixPerHP = spriteEnemy.w / (float)(ENEMY_HITPOINTS[ enemyType ]);
       SDL_Rect destR;
       // draw damage
       destR.x = lroundf(pos.getX()) - spriteEnemy.w / 2;
       destR.y = lroundf(pos.getY()) - draw_h / 2 - 4;
       float damageToDraw = min( (float)ENEMY_HITPOINTS[ enemyType ] / 2, hitpoints );
       destR.w = lroundf(pixPerHP * damageToDraw);
       destR.h = 3;
       SDL_FillRect( screen, &destR, SDL_MapRGB(screen.format, 255, 0, 0) );
       // draw shields
       destR.x = lroundf(pos.getX());
       destR.y = lroundf(pos.getY()) - draw_h / 2 - 4;
       float shieldToDraw = min( (float)ENEMY_HITPOINTS[ enemyType ] / 2, 
                     hitpoints - ENEMY_HITPOINTS[ enemyType ] / 2 );
       if ( shieldToDraw < 1 ) destR.w = 0;
       else destR.w = lroundf(pixPerHP * shieldToDraw);
       destR.h = 3;
       SDL_FillRect(screen, &destR, SDL_MapRGB(screen.format, 0, 255, 0) );
       //#endif
     }

}


