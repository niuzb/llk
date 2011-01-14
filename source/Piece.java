import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;
public class Piece {

	  /** the  actual x subindex of  coordinate */
	  public int x;
	  /** the actual y subindex of  coordinate */
	  public int y;
      /** color can be 0,1,2, 0 :do nothing,1:paint black rectangle;2:paint red rectangle */
	  int color;
	  
	  GameScreen gs;
	  public Piece(int _x, int _y, int _c, GameScreen gs) {
	    x = _x;
		y = _y;
		color = _c;
		this.gs = gs;
      }
	  
	  public int getx() {
		return x;
	  }
	  
	  public void setx(int _x) {
		x = _x;
	  }
	  
	  public int gety() {
		return y;
		
	  }
	  
	  public void sety(int _y) {
		y = _y;
	  }
	  
	  public void setcolor(int color) {
		this.color = color;
	  }
	  
	  public int getcolor() {
			return color;
	  }
      // assumes background is white
      public void paint(Graphics g) {
    	
	     int px = (x-1) * gs.TILE_WIDTH + gs.offsetX; 
	     int py = (y-1) * gs.TILE_HEIGHT+ gs.offsetY;
  
    	 if (color == 1) {//black
    	  	g.setColor(0);          
          	g.drawRect(px, py, gs.TILE_WIDTH - 1, gs.TILE_HEIGHT - 1);
          	g.drawRect(px+1, py+1, gs.TILE_WIDTH - 2, gs.TILE_HEIGHT - 2);
			
		 } else if (color == 2){//red
    		g.setColor(new Random().nextInt());          
          	g.drawRect(px, py, gs.TILE_WIDTH - 1, gs.TILE_HEIGHT - 1);
			g.drawRect(px+1, py+1, gs.TILE_WIDTH - 2, gs.TILE_HEIGHT - 2);
    	 }
      }

}
