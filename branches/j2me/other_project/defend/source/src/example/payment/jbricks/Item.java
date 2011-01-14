
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Item {
     Image sprite;
     BoundingBox boundingBox;
     
     Vector2D pos;
     Vector2D vel;
     int itemType;
     
     int timeLived;
    

     // a sprite, that contains horizontally all animationframes of the explosion.
     // it is assumed, that every frame is quadratic.
     Engine engine;

     int draw_w;
     int draw_h;

     public Item(Vector2D position,  Vector2D velocity, int itemType) {
     
       this.itemType = itemType;
     
       pos = position;
       vel = velocity;
       timeLived = 0;
        engine = Engine.ME;
       switch (itemType) {
       case Global.ITEM_PRIMARY_UPGRADE:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_PRIMARY_UPGRADE );
           break;
         }
       case Global.ITEM_DUMBFIRE_DOUBLE:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_DUMBFIRE_DOUBLE );
           break;
         }
       case Global.ITEM_KICK_ASS_ROCKET:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_KICK_ASS_ROCKET );
           break;
         }
       case Global.ITEM_HELLFIRE:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_HELLFIRE );
           break;
         }
       case Global.ITEM_MACHINE_GUN:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_MACHINE_GUN );
           break;
         }
       case Global.ITEM_HEALTH:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_HEALTH );
           break;
         }
       case Global.ITEM_NUKE:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_NUKE );
           break;
         }
       case Global.ITEM_ENERGY_BEAM:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_ENERGY_BEAM );
           break;
         }
           
       default:
         {
           sprite = engine.surfaceDB.loadSurface( Global.FN_ITEM_PRIMARY_UPGRADE );
           break;
         }
       }
       draw_w = sprite.getWidth();
       draw_h = sprite.getHeight();
       
       boundingBox = new BoundingBox( (int)Math.floor(pos.getX() - draw_w / 2.0),
                      (int)Math.floor(pos.getY() - draw_h / 2.0),
                      draw_w, draw_h );
     }
     
 
     void update( int dT ) {
       pos.add(vel.multiNew(dT / 1000.0));
       updateBoundingBox();
       timeLived += dT;
     }
     
     void deleteItem() {
       timeLived = Global.ITEM_LIFETIME;
     }
     
     void updateBoundingBox() {
           boundingBox.moveUpperBound( (int)Math.floor(pos.getY()- draw_h * 0.45
         ) );
           boundingBox.moveLeftBound( (int)Math.floor(pos.getX() - (draw_w) * 0.45) );
     }
     
     void draw(Graphics screen) {
       int x = (int)Math.floor(pos.getX()) - (draw_w / (2));
       int y = (int)Math.floor(pos.getY()) - (draw_h / 2);
       
       screen.drawRegion( sprite, 0,0,draw_w,draw_h,0,x, y , Global.TL);
     }
     
     BoundingBox getBoundingBox() {
       return boundingBox;
     }
      boolean isExpired() { return (timeLived >=Global.ITEM_LIFETIME); }
      Vector2D getPos() { return pos; }
      Vector2D getVel() { return vel; }
      void pickedUp() { timeLived = Global.ITEM_LIFETIME; }
      int getType() { return itemType; }


}


