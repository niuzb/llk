
package example.payment.jbricks;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;

public class SurfaceDB {

    Hashtable <String, Image> surfaceDB;

    public  SurfaceDB() {
        surfaceDB = new Hashtable<String, Image>();
    }

    public Image loadSurface( String fn ) {

      Image searchResult = getSurface( fn );
      if ( searchResult != null ) {
        return searchResult;
      }

      Image newSurface = Util.createImage(fn);
      surfaceDB.put(fn, newSurface);


      return newSurface;
    }

    public Image getSurface( String fn ) {

        return surfaceDB.get(fn);
    }

}
