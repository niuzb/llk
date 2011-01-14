
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Items {

    Vector <Item> items;
    int timeNextItemAppear;
    Engine engine;
    public  Items() {
        items = new Vector<Item>();
        
        timeNextItemAppear = 
          Global.ITEM_APPEAR_DELAY + Util.getRandomInt(0, 
          Global.ITEM_APPEAR_RAND_DELAY);
    }
    
    void addItem(Item item) {
      if (item != null) {
        items.addElement(item);
      }
    }
    
    void expireItems() {
      int i = 0;
      while ( i < items.size() ) {
        if ( items.elementAt(i).isExpired() ) {
            items.removeElementAt(i);
        } else {
          i++;
        }
      }
    }
    
    void update( int dT ) {
      for (Item  e: items) {
        e.update(dT);
      }
    }
    
    void draw(Graphics screen) {
      for (Item  e: items) {
        e.draw(screen);
      }
    }
    
    Item getItem(int idx) {
      return items.elementAt(idx);
    }
    int getNrItems() {
        return items.size();
    }
    void generateItemNow( Vector2D pos, Vector2D vel ) {
      if ( pos.getX() < 10 ) pos.setX( 10 );
      if ( pos.getX() > Screen.width-10 ) pos.setX( Screen.width-10 );
      if ( pos.getY() < 100 && vel.getY() < 5 ) vel.setY( 5 );
      
      int itemType;
      // 10 tries for a correct item
      for ( int i = 0; i < 10; i++ ) {
        itemType = Global.getRandValue( Global.ITEM_APPEAR_CHANCES, Global.NR_ITEM_TYPES );
        if ( 
         ( 
           ( itemType == Global.ITEM_PRIMARY_UPGRADE ||
             itemType == Global.ITEM_DUMBFIRE_DOUBLE ||
             itemType == Global.ITEM_KICK_ASS_ROCKET ||
             itemType == Global.ITEM_HELLFIRE ||
             itemType == Global.ITEM_MACHINE_GUN ||
             itemType == Global.ITEM_HEALTH ||
             itemType == Global.ITEM_NUKE ||
             itemType == Global.ITEM_ENERGY_BEAM ) ) ) {
          Item item = new Item( pos, vel, itemType );
          addItem( item );
          break;
        }
      }
    }
    
    void generate( int dT ) {
      timeNextItemAppear -= dT;
      
      if ( timeNextItemAppear < 0 ) {
        timeNextItemAppear = Global.ITEM_APPEAR_DELAY + 
         Util.getRandomInt(0, Global.ITEM_APPEAR_RAND_DELAY);
        generateItemNow(new  Vector2D( 75 + Util.getRandomInt(0,150), -20 ),
               new Vector2D(Util.getRandomInt(0,Global.SCROLL_SPEED)-Global.SCROLL_SPEED/2,Global.SCROLL_SPEED + 
               Util.getRandomInt(0,Global.SCROLL_SPEED) ) );
      }
    }
    
    void deleteAllItems() {
    
      for (Item  e: items) {
        e.deleteItem();
      }
      expireItems();
    }
 
}

