
package example.payment.jbricks;


import javax.microedition.lcdui.Image;
import java.util.*;
import javax.microedition.lcdui.*;

import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.Locale;

public class Background{
    int minTileWidth;
    int minTileHeight;
    int tilesPerLine;
    int tilesPerColumn;
    int step;
    
    Vector< String > tileNames;
    Vector< Image > tileSurfaces;


public  Background() {
    minTileWidth   = 9999999;
    minTileHeight  = 9999999;
    tilesPerLine   = 0;
    tilesPerColumn = 0;
    step           = 0;
    tileNames = new Vector< String >();
    tileSurfaces = new Vector< Image >();

}

void clearTileList() {
  tileNames.removeAllElements();
  tileSurfaces.removeAllElements();
  tilesPerLine   = 0;
  tilesPerColumn = 0;
}


void addTile( String tilename ) {
  tileNames.addElement( tilename );
}


void generateBackground( int length ) {  
  tileSurfaces.removeAllElements();
  Vector< Image > tmpTiles = new Vector<Image>();
  // load all tiles
  for(String tilename : tileNames) {

    Image tile = Util.createImage(tilename);

    if (tile != null) {
      tmpTiles.addElement( tile );
      if (tile.getWidth() < minTileWidth) {
	    minTileWidth = tile.getWidth();
      } 
      if (tile.getHeight() < minTileHeight) {
	    minTileHeight = tile.getHeight();
      }
    } 
  }

  // calculate tiles per line and tiles per row
  tilesPerLine = Screen.width/ minTileWidth;
  if (Screen.width % minTileWidth>0) {
    tilesPerLine++;
  }
  tilesPerColumn = Screen.height/ minTileHeight;
  if (Screen.height % minTileHeight>0) {
    tilesPerColumn++;
  }

  int rows = length / minTileHeight;
  if (length % minTileHeight > 0) {
    rows++;
  }
  
  //   cout << "Background: minTileWidth=" << minTileWidth << "  minTileHeight=" << minTileHeight << "  rows=" << rows << endl;

  // generate random background
  for(int i=rows*tilesPerLine; i > 0; i--) {
    tileSurfaces.addElement( tmpTiles.elementAt(Util.getRandomInt(0,tmpTiles.size()) ));
  }
}


void draw( Graphics  screen ) {  
  step = (step+1) % (tilesPerColumn*minTileHeight);
  draw( screen, step );
}


void draw( Graphics screen, int step ) {

  int x1=0, y1=0;
  int w1,h1;
  int x2, y2;
  
  
  if (step < 0) {
    step *= -1;
  }
  int startLine = (step / minTileHeight);
  int offset    = (step % minTileHeight);

  x1 = 0;
  y1 = 0;


  for(int y = 0; y < tilesPerColumn+1; y++) {
    for(int x = 0; x < tilesPerLine; x++) {

      int diffX = Screen.width - x * minTileWidth;
      if ( diffX >= minTileWidth ) {
		w1 = minTileWidth;
      } else {
		w1 = diffX;
      }
	
      if (y==0) {
		int diffY = -(offset - minTileHeight);
		h1 = diffY;
      } else {
		h1 = minTileHeight;
      }
      
      x2 = x * minTileWidth;
      y2 = Screen.height+ offset - (y+1) * minTileHeight;
      //#if 0
      SDL_BlitSurface( tileSurfaces[ ((y+startLine)*tilesPerLine+x) % tileSurfaces.size()] , 
      &srcRect, screen, &dstRect );
      //#endif
      screen.drawRegion(tileSurfaces.elementAt(((y+startLine)*tilesPerLine+x) % tileSurfaces.size()), 
      x1, y1, w1, h1,
                0, x2, y2, Graphics.TOP | Graphics.LEFT);
    }
  }
}


}

