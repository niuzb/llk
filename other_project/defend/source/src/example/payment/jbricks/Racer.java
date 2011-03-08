
package example.payment.jbricks;
import java.util.*;
import javax.microedition.lcdui.*;

import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Racer {

    Image spriteRacerBase;
    Image spriteShadow;
    Image spriteHPStat;
    Image shieldGlow;
    int draw_w;
    int draw_h;
    Vector2D pos;
    long timeLastShotPrimary;
    long timeLastShotSecondary;
    long timeLastNukeUsed;

    boolean lastDumbfireWasLeft = false;
    int specialsAvailability = 0;
    boolean []secondaryWeaponsAvailability = new boolean[Global.NR_SECONDARY_WEAPONS];

    int maxShield,maxDamage;
    int shield,damage;

    float maxVel;
    float pixPerHP;
    int  primaryShotType;
    int activeSecondary;
    Engine engine;
    private Font fontMedium;
    public  Racer(String fnSprite, Vector2D startpos) {
        engine = Engine.ME;
        spriteRacerBase = Engine.ME.surfaceDB.loadSurface( fnSprite );
        draw_w = spriteRacerBase.getWidth();
        draw_h = spriteRacerBase.getHeight();
        pos = startpos;
        activeSpecial =Global.SPECIAL_NUKE;
        specialsAvailability = 1;
        timeLastShotPrimary   = System.currentTimeMillis();
        timeLastShotSecondary = System.currentTimeMillis();

        
        fontMedium = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
      
        for(int i=0; i<Global.NR_SECONDARY_WEAPONS; i++)
            secondaryWeaponsAvailability[i] = true;
        maxShield = Global.LIGHT_FIGHTER_MAX_SHIELD;
        maxDamage = Global.LIGHT_FIGHTER_MAX_DAMAGE;
        shield = maxShield;
        damage = maxDamage;
        maxVel = Global.LIGHT_FIGHTER_VEL_MAX;
        shieldGlow = Engine.ME.surfaceDB.loadSurface("/lightFighterShieldDamaged.png");
        //#if 0
        spriteShadow = Engine.ME.surfaceDB.loadSurface( "/lightFighterShadow.png");
        shieldGlow = new ShieldGlow( LIGHT_FIGHTER );
   
  	    spriteFighterIcon = surfaceDB.loadSurface( FN_LIGHT_FIGHTER_1_SMALL );
        
        //#endif
  	    secondaryWeaponsAvailability[ 0 ] = false; // Dumbfire is always available
  	    primaryShotType = Global.SHOT_NORMAL;
  	    activeSecondary = Global.SHOT_DUMBFIRE;
        spriteHPStat = Engine.ME.surfaceDB.loadSurface( "/hpStat.png" );
        pixPerHP = spriteHPStat.getWidth()*1F / (maxDamage + maxShield);
        
        sndShotPrimary = engine.musicManager.loadSample(Global.FN_SOUND_SHOT_PRIMARY );
        sndShotSecondary = engine.musicManager.loadSample( Global.FN_SOUND_SHOT_SECONDARY );
        spriteSpecials = Engine.ME.surfaceDB.loadSurface( Global.FN_ICONS_SPECIALS );
        spriteSecondaryWeapons = Engine.ME.surfaceDB.loadSurface( Global.FN_ICONS_SECONDARY_WEAPONS );
        boundingBox  = new BoundingBox((int)Math.floor(pos.x-draw_w*0.45),
            (int)Math.floor(pos.y-draw_h*0.45),(int)Math.floor(draw_w*0.9),
            (int)Math.floor(draw_h*0.9));
        shipAngle = -90;
        vel = new Vector2D(0,0);
        shieldDamageActive = false;
        thrust = false;
        backwards = false;
        left = false;
        right = false;
        useSpecial = false;
        
        points = 0;
        timeLastMove = System.currentTimeMillis();
    }

    void reset() {
        timeLastMove = System.currentTimeMillis();
        thrust = false;
        backwards = false;
        left = false;
        right = false;
        pos =  new Vector2D(120,250);;
    }

    void resetControl() {
        thrust = false;
        backwards = false;
        left = false;
        right = false;
    }
    
int sndShotPrimary,sndShotSecondary;
    Image spriteSpecials, spriteSecondaryWeapons;
    BoundingBox boundingBox;
    float shipAngle; // the orientation
    Vector2D vel; // the velocity vector
    boolean thrust;
    boolean backwards;
    boolean left;
    boolean right;
    boolean useSpecial;
    float points;
    long timeLastMove;



    void move( int dT ) {


      if ( thrust ) {
        pos.y += -maxVel  * dT / 1000.0;
      }
      if ( backwards ) {
        pos.y  += maxVel  * dT / 1000.0;;
      }
      if ( left ) {
        //System.out.println("x: beore"+pos.x+"vel"+maxVel+dT);
        pos.x += -maxVel  * dT / 1000.0;
        //System.out.println("x:"+pos.x);
      } 
      if ( right ) {
        pos.x += maxVel  * dT / 1000.0;;
      }
      
      
      clipWorld();
      updateBoundingBox();

    }

    Vector2D getPos(){
        return new Vector2D(pos.getX(), pos.getY());
    }

    void drawStats(Graphics screen ) {
      int x1,y1, x2, y2;
      int indent = 5;
      int top = 1;
      int secondwidth = spriteSecondaryWeapons.getWidth()/10;
      int secondheight = spriteSecondaryWeapons.getHeight();

      

    
      int x = Screen.width/2+indent;
    
      // draw Secondary Weapon
      x1 = (activeSecondary - Global.SHOT_DUMBFIRE) * secondwidth;
      y1 = 0;
      
      x2 = x;
      y2 = top;
      x += secondwidth + 5;
      
      //SDL_BlitSurface( spriteSecondaryWeapons, &srcR, screen, &destR );
      screen.drawRegion( spriteSecondaryWeapons, x1,y1,
        secondwidth,secondheight,0,x2, y2 , Global.TL);
      // draw active special and amount of it
      if ( activeSpecial !=Global.SPECIAL_NONE ) {
        int w =(spriteSpecials.getWidth() / (Global.NR_SPECIALS-1));
        int h = spriteSpecials.getHeight();
        
        screen.drawRegion(spriteSpecials, 0,0,
          w,h,0,x, y2 , Global.TL);
        x += w+2;
        screen.setFont(fontMedium);
        screen.setColor(ThreeDColor.red.getRGB());
        screen.drawString(""+specialsAvailability, x,
            top, Global.TL);
        //#if 0
        font->drawInt( screen, x, 25,
               specialsAvailability[ activeSpecial ], digitCnt, 0);
        //#endif
      }
    
      // draw damage
 
      int w = (int)Math.floor(damage * pixPerHP);
      int h = spriteHPStat.getHeight();
      //System.out.println("sssss"+w+"h:"+h);
      
      screen.drawRegion(spriteHPStat, 0,0,
        w,h,0,indent, top , Global.TL);
      // draw shield
      x1 = spriteHPStat.getWidth() / 2;
      x2 = w+indent;
      w = (int)Math.floor(shield * pixPerHP);
      screen.drawRegion(spriteHPStat, x1,0,
        w,h,0,x2, top , Global.TL);
    }


    
    void drawRacer( Graphics screen ) {
    
    
      int x = (int)Math.floor(pos.getX()) - (draw_w / (2));
      int y = (int)Math.floor(pos.getY()) - (draw_h / 2);
      
      
      screen.drawRegion( spriteRacerBase, 0,0,draw_w,draw_h,0,x, y , Graphics.TOP | Graphics.LEFT);
      if ( shieldDamageActive ) {
        int w = 48;
        long shieldGlowTimeLeft = shieldDamageEndTime - System.currentTimeMillis();
        if ( shieldGlowTimeLeft < 0 ) {
          shieldDamageActive = false;
        } else {
         int actFrame = (int)(shieldGlowTimeLeft / 50);
         if(actFrame > 3) actFrame = 3;
         if (actFrame < 0)actFrame = 0;
         int x1 = actFrame*w;
         x = (int)Math.floor(pos.getX()) - (w / (2));
         y = (int)Math.floor(pos.getY()) - (w / 2);
         screen.drawRegion(shieldGlow, x1,0,w,w,0,x, y , Graphics.TOP | Graphics.LEFT);
        }
      }
    
    }

    void drawShadows(Graphics screen) {
      //#if 0
      vector<Shot >::iterator i;
      for (i = engine.shots.begin(); i != engine.shots.end(); ++i) {
        (*i).drawShadow(screen);
      }
      //#endif
    }

    void clipWorld() {
      int left = ((int)pos.getX() - draw_w/ (2));
      int top = ((int)pos.getY() - draw_h / 2);
      int right = ((int)pos.getX() + draw_w / (2));
      int bottom = ((int)pos.getY() + draw_h / 2);

      //System.out.println("draw_w:"+draw_w/ (2));

      //System.out.println("(draw_w/ (2:"+(draw_w/2);
      if ( left < 0 ) {
        //System.out.println("left:"+left);
        pos.x = ( 0 + draw_w/ (2) );
      } else if ( right > Screen.width ) {
        //System.out.println("right:"+right);
        pos.x =( Screen.width - draw_w /(2));
      }
      if ( top <= 1 ) {
        //System.out.println("top:"+top);
        pos.y = ( 1 + draw_h/ 2 );
      } else if ( bottom >= Screen.height- 1 ) {
        //System.out.println("bottom:"+bottom);
        pos.y = (  Screen.height - 1 - draw_h/ 2 );
      }
    }


    void updateBoundingBox() {
      boundingBox.moveUpperBound( (int)Math.floor(pos.getY()- draw_h * 0.45
    ) );
      boundingBox.moveLeftBound( (int)Math.floor(pos.getX() - (draw_w) * 0.45) );
    }

    void moveAndCollide( int dT ) {
        move( dT );
        collideWithEnemys();
    }
    
    
    void receivePoints( float amount ) {
      points += amount;
    }

    void doDamage( int shotType ) {
    
      switch (shotType) {
      case Global.ENEMY_SHOT_NORMAL:
        {
          receiveDamage( Global.DAMAGE_ENEMY_SHOT_NORMAL );
          break;
        }
      case Global.ENEMY_SHOT_TANK_ROCKET:
        {
          receiveDamage( Global.DAMAGE_ENEMY_SHOT_TANK_ROCKET );
          break;
        }
      default: 
        {
          break;
        }
      }
    }
    boolean shieldDamageActive;
    long    shieldDamageEndTime;
    int activeSpecial;
    void receiveDamage( int amount ) {
      if ( (shield) >= amount ) {
        shield -= amount;
        shieldDamageActive = true;
        shieldDamageEndTime = System.currentTimeMillis() + Global.RACER_SHIELD_DAMAGE_LIFETIME;
        return;
      }
      amount -= shield;
      shield = 0;
      damage -= amount;
      if (damage < 0)
        damage = 0;
    }

    void collideWithEnemys() {
        for (  int j = 0; j < engine.enemys.getNrEnemys(); ++j ) {

          // wurde noch nicht von einem anderen Racer gerammt und fliegt ebenfalls
          if ( !(engine.enemys.getEnemy( j ).isExpired()) &&
    	  Global.ENEMY_FLYING[ engine.enemys.getEnemy( j ).getType() ] ) {
        	// kollidiert
        	if ( collidesWithAsCircle( engine.enemys.getEnemy( j ).getBoundingCircle() ) ) {
        	  // explosion
            Explosion newExplosion = 
           	new Explosion(Global.FN_EXPLOSION_ENEMY, engine.enemys.getEnemy( j ).getPos(), engine.enemys
        .getEnemy( j ).getVel(), Global.EXPLOSION_NORMAL_AIR );
            engine.explosions.addExplosion( newExplosion );
        	  engine.enemys.getEnemy( j ).expire();
        	  this.receiveDamage(Global.ENEMY_COLLISION_DAMAGE[ engine.enemys.getEnemy( j ).getType() ]);
        	}
          }
          
        }
    }
    
    void shoot() {
     
        shootPrimary();
        shootSecondary();
        shootSpecial();
      
    }

    
    void rechargeShield ( int dT ) {
      float amount;
      amount =(float)(Global.LIGHT_FIGHTER_SHIELD_RECHARGE * dT / 100000.0);
      if (shield + amount < maxShield) shield += amount;
      else shield = maxShield;
    }
    
    
    void pickUpItems() {
      for (  int i = 0; i < engine.items.getNrItems(); i++ ) {
        Item  item = engine.items.getItem(i);
        if ( !item.isExpired() && boundingBox.overlaps( item.getBoundingBox() ) ) {
          switch (item.getType()) {
          case Global.ITEM_PRIMARY_UPGRADE: 
        {
          int newPrimaryShotType = primaryShotType + 1;
          if (newPrimaryShotType <=Global.SHOT_TRIPLE ) 
            primaryShotType = newPrimaryShotType;
          item.pickedUp();
          addBannerItemCollected(Global.BANNER_ITEM_PRIMARY_UPGRADE_COLLECTED );
          break;
        }
          case Global.ITEM_DUMBFIRE_DOUBLE:
        {
          if ( true ) {
            secondaryWeaponsAvailability[ Global.SHOT_DUMBFIRE_DOUBLE - 100 ] = true;
            if (activeSecondary == Global.SHOT_DUMBFIRE ) activeSecondary = Global.SHOT_DUMBFIRE_DOUBLE;
          } 
          item.pickedUp();
          addBannerItemCollected( Global.BANNER_ITEM_DUMBFIRE_DOUBLE_COLLECTED );
          break;
        }
          case Global.ITEM_KICK_ASS_ROCKET:
        {
          if ( true )
            secondaryWeaponsAvailability[ Global.SHOT_KICK_ASS_ROCKET - 100 ] = true;
          item.pickedUp();
          addBannerItemCollected( Global.BANNER_ITEM_KICK_ASS_ROCKET_COLLECTED );
          break;
        }
          case Global.ITEM_HELLFIRE:
        {
          if ( true ) {
            secondaryWeaponsAvailability[ Global.SHOT_HELLFIRE - 100 ] = true;
            item.pickedUp();
            addBannerItemCollected( Global.BANNER_ITEM_HELLFIRE_COLLECTED );
          }
          break;
        }
          case Global.ITEM_MACHINE_GUN:
        {
          if ( true ) {
            secondaryWeaponsAvailability[ Global.SHOT_MACHINE_GUN - 100 ] = true;
            item.pickedUp();
            addBannerItemCollected( Global.BANNER_ITEM_MACHINE_GUN_COLLECTED );
          }
          break;
        }
          case Global.ITEM_HEALTH:
        {
          {
            if ( true )
              repair( Global.ITEM_HEALTH_REPAIR_AMOUNT );
          }
          item.pickedUp();
          addBannerItemCollected( Global.BANNER_ITEM_HEALTH_COLLECTED );
          break;
        }
          case Global.ITEM_NUKE:
        {
          specialsAvailability += Global.ITEM_NUKE_AMMO;
          if ( activeSpecial == Global.SPECIAL_NONE ) {
            activeSpecial = Global.SPECIAL_NUKE;
          }
          item.pickedUp();
          addBannerItemCollected( Global.BANNER_ITEM_NUKE_COLLECTED );
          break;
        }
          case Global.ITEM_ENERGY_BEAM:
        {
          if ( true ) {
            secondaryWeaponsAvailability[ Global.SHOT_ENERGY_BEAM - 100 ] = true;
            item.pickedUp();
            addBannerItemCollected( Global.BANNER_ITEM_ENERGY_BEAM_COLLECTED );
          }
          break;
        }
          
          default:
        {
          break;
        }
          }
        }
      }
    }
    
    void handlePlayerEvent(int keycode, int gameAction, boolean keyDown ) {
        //System.out.println("keycode:" + keycode);
        
        resetControl();
            
        switch(keycode) {
           case -6://left
           {
             if ( !keyDown ) { 
           switchSecondary();
           return;
             }
             break;
           }    
           case -7:
           {
             if ( keyDown ) {
                useSpecial = true; 
                specialKeyDown();
             } else {
                useSpecial = false; 
                specialKeyUp();
           
             }
           return;
           }
           
           case (Canvas.KEY_NUM2):
             if ( keyDown ) thrust = true; else thrust = false; break;
           case (Canvas.KEY_NUM8):
               if ( keyDown ) backwards = true; else backwards = false; break;
           case (Canvas.KEY_NUM4):
               if ( keyDown ) left = true; else left = false; break;
           case (Canvas.KEY_NUM6):
               if ( keyDown ) right = true; else right = false; break;

           default: break;    
         }

      switch (gameAction) {
      case Canvas.UP:
        if ( keyDown ) thrust = true; else thrust = false; break;
      case Canvas.DOWN: 
        if ( keyDown ) backwards = true; else backwards = false; break;
      case Canvas.LEFT:
        if ( keyDown ) left = true; else left = false; break;
      case Canvas.RIGHT: 
        if ( keyDown ) right = true; else right = false; break;
      default: break;
      }
 
    }
    
    void switchSecondary() {
      int newActive = (activeSecondary-100 + 1) %Global.NR_SECONDARY_WEAPONS;
      while ( newActive != activeSecondary-100 &&
          secondaryWeaponsAvailability[ newActive ] == false ) {
        newActive = (newActive + 1) % Global.NR_SECONDARY_WEAPONS;
      }
      activeSecondary = (newActive+100);
      
      if ( activeSecondary == Global.SHOT_DUMBFIRE && 
           secondaryWeaponsAvailability[ Global.SHOT_DUMBFIRE_DOUBLE - 100 ] ) {
        activeSecondary = Global.SHOT_DUMBFIRE_DOUBLE;
      }
    
      // TODO: ugly workaround...
      if ( activeSecondary == Global.SHOT_KICK_ASS_ROCKET ) {
        timeLastShotSecondary -= Global.RACER_COOLDOWN_KICK_ASS_ROCKET / 2;
      }
    }
    
    void switchSpecials() {
      //#if 0
      int newActive = activeSpecial + 1;
      if ( newActive ==Global.NR_SPECIALS ) newActive = 1;
      for(int i=0; i < NR_SPECIALS-1; ++i) {
        // Only Sönke knows what the following does ;-)
        if (specialsAvailability[ ((activeSpecial + i) % (NR_SPECIALS-1))+1 ] != 0) {
          activeSpecial = (SpecialTypes)(((activeSpecial + i) % (NR_SPECIALS-1))+1);
          return;
        }
      }
      //#endif
      activeSpecial =Global.SPECIAL_NONE;
    }
    
    
    void shootPrimary() {
    
        switch ( primaryShotType ) {
        case Global.SHOT_NORMAL:
          {
        if ( (System.currentTimeMillis() - Global.RACER_COOLDOWN_SHOT_NORMAL) > timeLastShotPrimary ) {
          Shot shot = 
            new Shot( Global.SHOT_NORMAL, 0,new Vector2D(pos.getX(), pos.getY()-draw_h/2),
                  -90 +Util.getRandomInt(0,Global.SPREAD_ANGLE_SHOT_NORMAL) -
                 Global.SPREAD_ANGLE_SHOT_NORMAL / 2 );
          engine.shots.addShot( shot );
          engine.musicManager.playSample( sndShotPrimary, 1 );
          timeLastShotPrimary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_NORMAL_HEAVY:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_SHOT_NORMAL_HEAVY > timeLastShotPrimary ) {
          Shot shot = 
            new Shot( Global.SHOT_NORMAL_HEAVY, 0,
                  new Vector2D(pos.getX(), pos.getY()-draw_h/2), -90 );
          engine.shots.addShot( shot );
          engine.musicManager.playSample( sndShotPrimary, 1 );
          timeLastShotPrimary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_DOUBLE:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_SHOT_DOUBLE > timeLastShotPrimary ) {
          Shot shot1 = 
            new Shot( Global.SHOT_DOUBLE, 0, 
            new Vector2D(pos.getX()-8, pos.getY()-2), -95 );
          Shot shot2 = 
            new Shot( Global.SHOT_DOUBLE, 0, 
            new Vector2D(pos.getX()+8, pos.getY()-2), -85 );
          engine.shots.addShot(shot1);
          engine.shots.addShot(shot2);
          engine.musicManager.playSample( sndShotPrimary, 1 );
          timeLastShotPrimary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_DOUBLE_HEAVY:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_SHOT_DOUBLE_HEAVY > timeLastShotPrimary ) {
          Shot shot1 = 
            new Shot( Global.SHOT_DOUBLE_HEAVY, 0, new Vector2D(pos.getX()-8, pos.getY()-2), -95 );
          Shot shot2 = 
            new Shot( Global.SHOT_DOUBLE_HEAVY, 0, new Vector2D(pos.getX()+8, pos.getY()-2), -85 );
          engine.shots.addShot(shot1);
          engine.shots.addShot(shot2);
          engine.musicManager.playSample( sndShotPrimary, 1 );
          timeLastShotPrimary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_TRIPLE:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_SHOT_TRIPLE > timeLastShotPrimary ) {
          Shot shot1 = 
            new Shot( Global.SHOT_TRIPLE, 0,new Vector2D(pos.getX()-8, pos.getY()-2), -100 );
          Shot shot2 = 
            new Shot( Global.SHOT_TRIPLE, 0, 
            new Vector2D(pos.getX(), pos.getY()-draw_h / 2), -90 );
          Shot shot3 = 
            new Shot( Global.SHOT_TRIPLE, 0, new Vector2D(pos.getX()+8, pos.getY()-2), -80 );
          engine.shots.addShot(shot1);
          engine.shots.addShot(shot2);
          engine.shots.addShot(shot3);
          engine.musicManager.playSample( sndShotPrimary, 1 );
          timeLastShotPrimary = System.currentTimeMillis();
        }
        break;
          }
          
    
        default: 
          {
        break;
          }
        }
    }
    
    
    void shootSecondary() {
    
        switch ( activeSecondary ) {
    
        case Global.SHOT_DUMBFIRE:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_DUMBFIRE > timeLastShotSecondary ) {
          Shot shot = 
            new Shot( Global.SHOT_DUMBFIRE, 0, pos.addNew(new Vector2D(0, -5)), 
            -90F);
          engine.shots.addShot(shot);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_DUMBFIRE_DOUBLE:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_DUMBFIRE_DOUBLE > timeLastShotSecondary ) {
          Shot shot;
          if ( lastDumbfireWasLeft ) {
            shot = new Shot( Global.SHOT_DUMBFIRE_DOUBLE, 0, 
               new Vector2D(pos.getX()+15, pos.getY()-3), -90 );
          } else {
            shot = new Shot( Global.SHOT_DUMBFIRE_DOUBLE, 0, new Vector2D(pos.getX()-15, pos.getY()-3), -90 );
          }
          lastDumbfireWasLeft = !lastDumbfireWasLeft;
          engine.shots.addShot(shot);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_KICK_ASS_ROCKET:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_KICK_ASS_ROCKET > timeLastShotSecondary ) {
          Shot shot = 
            new Shot( Global.SHOT_KICK_ASS_ROCKET, 0,  new Vector2D(pos.getX(), pos.getY()), -90 );
          engine.shots.addShot(shot);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_HELLFIRE:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_HELLFIRE > timeLastShotSecondary ) {
          Shot shot1 = 
            new Shot( Global.SHOT_HELLFIRE, 0,  new Vector2D(pos.getX()-15, pos.getY()-3), -180 );
          Shot shot2 = 
            new Shot( Global.SHOT_HELLFIRE, 0,  new Vector2D(pos.getX()+15, pos.getY()-3), 0 );
          engine.shots.addShot(shot1);
          engine.shots.addShot(shot2);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_MACHINE_GUN:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_MACHINE_GUN > timeLastShotSecondary ) {
          //#if 0
          int idxNearestEnemy1 = 0;
          float distFarestY1 = -1;
          int idxNearestEnemy2 = 0;
          float distFarestY2 = -1;
          float angle1 = -90;
          float angle2 = -90;
          float maxDistY = pos.getY() + 40; // only engine.enemys in or already in the screen
          for (  int i = 0; i < engine.enemys.getNrEnemys(); i++ ) {
            // angle of the vector from the racer position to the enemy
            float angleEnemy = (engine.enemys.getEnemy(i).getPos().minusNew(pos)).getDirection();
            if ( -90 <= angleEnemy && angleEnemy < -60 ) {
              float distY = pos.getY() - engine.enemys.getEnemy(i).getPos().getY();
              if ( distFarestY1 < distY && distY < maxDistY ) {
                distFarestY1 = distY;
                idxNearestEnemy1 = i;
                angle2 = angleEnemy + 5;
              }
            } else {
              if ( -120 < angleEnemy && angleEnemy < -90 ) {
                float distY = pos.getY() - engine.enemys.getEnemy(i).getPos().getY();
                if ( distFarestY2 < distY && distY < maxDistY ) {
                  distFarestY2 = distY;
                  idxNearestEnemy2 = i;
                  angle1 = angleEnemy - 5;
                }
              }
            }
          }
          //#endif
          
          float angle1 = -Util.getRandomInt(65, 95);
          float angle2 = -Util.getRandomInt(95, 125);
          Shot shot1 = 
            new Shot( Global.SHOT_MACHINE_GUN, 0, 
            new Vector2D(pos.getX()-3, pos.getY()-draw_h/2), angle1 );
          Shot shot2 = 
            new Shot( Global.SHOT_MACHINE_GUN, 0, 
            new Vector2D(pos.getX()+3, pos.getY()-draw_h/2), angle2 );
          engine.shots.addShot(shot1);
          engine.shots.addShot(shot2);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
        case Global.SHOT_ENERGY_BEAM:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_ENERGY_BEAM > timeLastShotSecondary ) {
          Shot shot = 
            new Shot( Global.SHOT_ENERGY_BEAM, 0, 
            new Vector2D(pos.getX()-1, pos.getY()-10), -90 );
          engine.shots.addShot(shot);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastShotSecondary = System.currentTimeMillis();
        }
        break;
          }
          
    
        default: 
          {
        break;
          }      
        }
    
    }
    
    
    void shootSpecial() {
        //#if 0
      if ( useSpecial ) {
    
        switch ( activeSpecial ) {
    
        case SPECIAL_HEATSEEKER:
          {
        if ( System.currentTimeMillis() - Global.RACER_COOLDOWN_SPECIAL_HEATSEEKER > timeLastHeatseekerUsed &&
             specialsAvailability[ SPECIAL_HEATSEEKER ] > 0 ) {
          Shot shot;
          if ( lastHeatseekerWasLeft ) {
            shot = new Shot( SPECIAL_SHOT_HEATSEEKER, 0, pos + Vector2D(15,-3), -90 );
          } else {
            shot = new Shot( SPECIAL_SHOT_HEATSEEKER, 0, pos + Vector2D(-15,-3), -90 );
          }
          lastHeatseekerWasLeft = !lastHeatseekerWasLeft;
          engine.shots.addShot(shot);
          engine.musicManager.playSample( sndShotSecondary, 1);
          timeLastHeatseekerUsed = System.currentTimeMillis();
          specialsAvailability[ SPECIAL_HEATSEEKER ]--;
        }
        break;
          }
        default: break;
        }
      }
      //#endif
    }
    
    
    boolean collidesWith(  Vector2D shotPosOld,  Vector2D shotPosNew ) {
      return boundingBox.overlaps(shotPosOld, shotPosNew);
    }
    
    boolean collidesWith( BoundingBox box ) {
      return boundingBox.overlaps( box );
    }
    
    boolean collidesWith(  Circle circle ) {
      return boundingBox.overlaps( circle );
    }
    
    boolean collidesWithAsCircle(  Circle circle ) {
      return ( (circle.getRadius() + (draw_w)/2) > circle.getCenter().distanceTo( pos ) );
    }
    
    
    BoundingBox getBoundingBox() {
      return boundingBox;
    }
    
    
    boolean isDead() {
      return (damage <= 0);
    }
    
    
    
    
    Vector2D setVel(  Vector2D newVel ) {
      Vector2D temp = vel;
      vel = newVel;
      if ( vel.getLength() >= getActVelMax() ) {
        vel.setLength(getActVelMax());
      }
      return temp;
    }
    
    
    void setPos(  Vector2D newPos ) {
      pos = newPos;
      updateBoundingBox();
    }
    
    
    float getActVelMax() {
      return maxVel;
    }
    
    
    void repair( float amount ) {
      float possibleDamageAdd = maxDamage - damage;
      if ( possibleDamageAdd >= amount ) {
        damage += amount;
      } else {
        damage += possibleDamageAdd;
        amount -= possibleDamageAdd;
        shield += amount;
      }
      if ( shield > maxShield ) shield = maxShield;
    }
    
    
    void specialKeyDown() {
        
      switch ( activeSpecial ) {
      case Global.SPECIAL_NUKE: 
        {
          if ( timeLastNukeUsed + Global.RACER_COOLDOWN_SPECIAL_NUKE < System.currentTimeMillis() ) {
        engine.shots.addShot( new Shot( Global.SPECIAL_SHOT_NUKE,
                      0,new Vector2D(pos.getX(), pos.getY()), 
                       (float)(new Vector2D( Screen.width/ 2.0-pos.getX(), 
                             Screen.height/ 2.0-pos.getY()) ).getDirection() ) );
        specialsAvailability--;
        timeLastNukeUsed = System.currentTimeMillis();
          }
          break;
        }
      default:
        {
          break;
        }
      }
      if ( specialsAvailability == 0 ) {
        switchSpecials();
      }
    }
    
    void specialKeyUp() {
      if ( specialsAvailability == 0 ) {
        switchSpecials();
      }
    }
    
    
    
    void addBannerItemCollected(int text ) {
        //#if 0
        banners.addBanner( text,
                BANNER_MODE_ITEM_COLLECTED_SINGLE_PLAYER,
                BANNER_BONUS_NONE );
        //#endif
    }

}

