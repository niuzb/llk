
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Shots {

    Vector <Shot> shots;

    public  Shots() {
        shots = new Vector<Shot>();
    }
    
    void addShot(Shot shot) {
      if (shot!=null) {
        shots.addElement(shot);
      }
    }
    
    void moveAndCollide( int dT ) {
      for (Shot  e: shots) {
        e.moveAndCollide(dT);
      }
    }
    
    void deleteAllShots() {
      
      shots.removeAllElements();
    }
    
    void expireShots() {
  
      int i = 0;
      while ( i < shots.size() ) {
        if ( shots.elementAt(i).isExpired() ) {
          
            shots.removeElementAt(i);
        } else {
          i++;
        }
      }
    }
    
    void drawShadows(Graphics screen) {
      //#if 0
      vector<Shot *>::iterator i;
      for (i = shots.begin(); i != shots.end(); ++i) {
        (*i)->drawShadow(screen);
      }
      //#endif
    }
    
    void drawGroundShots(Graphics screen) {
     
      for (Shot  e: shots) {
        e.drawGroundShot(screen);
      }
    }
    
    void drawAirShots(Graphics screen) {
     
       for (Shot  e: shots) {
        e.drawAirShot(screen);
      }
    }
    
    void drawGroundAirShots(Graphics screen) {
     
      for (Shot  e: shots) {
        e.drawGroundAirShot(screen);
      }
    }
    
    

}

