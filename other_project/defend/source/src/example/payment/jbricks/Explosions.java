
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class Explosions {

    Vector <Explosion> explosions;

    public  Explosions() {
        explosions = new Vector<Explosion>();
    }

    void addExplosion(Explosion explosion) {
      if (explosion != null) {
        explosions.addElement(explosion);
      }
    }
    
    void drawAirExplosions(Graphics screen) {
      for (Explosion  e: explosions) {
        e.drawAirExplosion(screen);
      }
    }
    
    void drawGroundExplosions(Graphics screen) {
      for (Explosion  e: explosions) {
        e.drawGroundExplosion(screen);
      }
    }
    
    void updateExplosions( int dT ) {
      
      for (Explosion  e: explosions) {
        e.update(dT );
      }
    }
    
    void expireExplosions() {
      int i = 0;
      while ( i < explosions.size() ) {
        if ( explosions.elementAt(i).isExpired() ) {
          
            explosions.removeElementAt(i);
        } else {
          i++;
        }
      }

      
    }

    int getNum(){
        return explosions.size();
    }

}

