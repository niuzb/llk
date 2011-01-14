
package example.payment.jbricks;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

class MyRect {
     int x, y, w, h;
     public MyRect(){

     }
};

public class BoundingBox {

    MyRect box;
    public  BoundingBox(int x, int y, int width, int height) {
        box = new MyRect();
        box.x = x;
    	box.y = y;
    	box.w = width;
    	box.h = height;
    }
    int getUpperBound() {
        return box.y;
    }
    
    int getLowerBound() {
        return box.y + box.h;
    }
    
    int getLeftBound() {
        return box.x;
    }
    
    int getRightBound() {
        return box.x + box.w;
    }

    
    boolean overlaps(BoundingBox other) {
      return ( !(getUpperBound() > other.getLowerBound()) &&
           !(getLowerBound() < other. getUpperBound()) &&
           !(getLeftBound() > other.getRightBound()) &&
           !(getRightBound() < other.getLeftBound()) );
    }
    
    boolean overlaps(Circle circle) {
//#if 0
      return (
          circle.isInside(new Vector2D( box.x, box.y )) ||
          circle.isInside(new Vector2D( box.x+ box.w, box.y + box.h )) ||
          circle.isInside(new Vector2D( box.x, box.y + box.h )) ||
          circle.isInside(new Vector2D( box.x + box.w, box.y )));
//#endif
       double x = circle.posCenter.getX();
       double y = circle.posCenter.getY();
       double r = circle.radius;
       return ( !(getUpperBound() > (y+r)) &&
           !(getLowerBound() < (y)) &&
           !(getLeftBound() > (x+r)) &&
           !(getRightBound() < (x)) );
    }
    
    boolean isInside(Vector2D v) {
      int x = (int)v.getX();
      int y = (int)v.getY();

      if((x>box.x &&
        x<(box.x+box.w) &&
        y > box.y &&
        y < (box.y+box.h))) {
        return true;
      }
      return false;
   }

   
    boolean overlaps(Vector2D startOfLine, Vector2D endOfLine) {
    
     
      boolean overlaps = false;
    
      overlaps = isInside( endOfLine );
      if ( overlaps ) return true;
      overlaps = isInside( startOfLine );
      if ( overlaps ) return true;
      //#if 0
      // check some points between the two end points
      Vector2D delta((endOfLine.getX() - startOfLine.getX()) / 4.0,
             (endOfLine.getY() - startOfLine.getY()) / 4.0);
      Vector2D actPoint = startOfLine + delta;
      int i = 1;
      while (!overlaps && i <= 3 ) {
        overlaps = rect.isInside(actPoint);
        actPoint += delta;
        i++;
      }
      //#endif
      return overlaps;
    }

    
    void moveUpperBound(int upperBound) {
        box.y = upperBound;
    }
    
    void moveLowerBound(int lowerBound) {
        box.y = lowerBound - box.h;
    }
    
    void moveLeftBound(int leftBound) {
        box.x = leftBound;
    }
    
    void moveRightBound(int rightBound) {
        box.x = rightBound - box.w;
    }
    void setWidth(int w) {
        box.w = w;
    }

    void setHeight(int h) {
        box.h = h;
    }
}

