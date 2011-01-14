
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Formation {

     // a sprite, that contains horizontally all animationframes of the explosion.
     // it is assumed, that every frame is quadratic.
     Image sprite;
     
     // how many frames does this explosion have?
     int nrAnimStages;
     // which frame is now?
     int actAnimStage;
     // how long should one frame last (ms)
     int timePerStage;
     // how long is the current explosion already active
     int timeLived;
     // at what timeLived starts the next frame?
     int timeNextAnimStage;
     
     // the explosion can be deleted
     boolean expired;
     
     int sndExplosion;
     
     Vector2D pos;
     Vector2D vel;
     int explosionType;
     Engine engine;

     int draw_w;
     int draw_h;
     
     public Formation(String fn, Vector2D position, 
       		     Vector2D velocity,  int explosionType) {
         engine = Engine.ME;
         
         sprite = engine.surfaceDB.loadSurface(fn);
         draw_w = draw_h=sprite.getHeight();
         nrAnimStages = sprite.getWidth()/ sprite.getHeight();
         expired = false;
         sndExplosion = engine.musicManager.loadSample(Global.FN_SOUND_EXPLOSION_NORMAL );  


         engine.musicManager.playSample( sndExplosion, 1);
       
         this.explosionType = explosionType;
         pos = position;
         vel = velocity;
       
         switch (explosionType) {
         case Global.EXPLOSION_NORMAL_AIR: 
         case Global.EXPLOSION_NORMAL_GROUND:
           {
             timePerStage = Global.LIFETIME_EXPL_NORMAL / nrAnimStages;
             break;
           }
         default: 
           {
             timePerStage = 0;
             actAnimStage = nrAnimStages;
             break;
           }
         }
         
         actAnimStage = 0;
         timeLived = 0;
         timeNextAnimStage = timePerStage;
   }
   
   
   void update(int dT ) {
     if ( engine.scrollingOn ) {
        pos.add(vel.multiNew(dT / 1000.0));
     } 
     //pos += vel * dT / 1000.0;
     timeLived += dT;
     if ( timeLived > timeNextAnimStage ) {
       timeNextAnimStage += timePerStage;
       actAnimStage++;
       if (actAnimStage == nrAnimStages) expired = true;
     }
   }
   
   boolean isExpired() { 
     return expired;
   }

   void drawAirExplosion(Graphics screen) {
     if (expired) return;
     if ( ! ( explosionType == Global.EXPLOSION_NORMAL_AIR ) ) return;
     int x2 = (int)Math.floor(pos.getX()) - draw_w/ (2);
     int y2 = (int)Math.floor(pos.getY()) - draw_h/ 2;
   
     int x1 = actAnimStage * draw_w;
     int y1 = 0;
     screen.drawRegion(sprite, x1,y1,draw_w,draw_h,0,x2, y2 , Global.TL);
   
   }
   
   void drawGroundExplosion(Graphics screen) {
     if (expired) return;
     if ( ! ( explosionType == Global.EXPLOSION_NORMAL_GROUND ) ) return;
   
     int x2 = (int)Math.floor(pos.getX()) - draw_w/ (2);
     int y2 = (int)Math.floor(pos.getY()) - draw_h/ 2;
     
     int x1 = actAnimStage * draw_w;
     int y1 = 0;
     screen.drawRegion(sprite, x1,y1,draw_w,draw_h,0,x2, y2 , Global.TL);
   }


}


