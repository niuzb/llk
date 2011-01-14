import java.util.*;
import javax.microedition.lcdui.*;
import java.io.*;

/**
 * The class Board knows how the pieces move, handles undo, and
 * handles reading of screens.
 */

public class Board {
    // Move directions
    //当设置movedirection为下面几个变量中的一个，就会在消掉一个
    //node后，游戏块向产生的空缺移动
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int FIRE = 5;



    private byte[][] array = new byte [10][9];
    private int width =7;
    private int height=8;
    /** store the first piece's neighber piece*/
    Vector v1;
    /** store the second piece's neighber piece */
    Vector v2;
    Stack nodeList;
    int initialCapacity = 15;
    int capacityIncrement = 10;
    
    public Piece first, second, middle;
	int binggo = 0;
	int store = 28;
	GameScreen gs;
	int moveDirection = -1;
	/* store the two pot that can link*/
	public int x1, x2, y1, y2;
	/**
     * Creates new Board initialized to a simple puzzle.
     */
	
    public Board(GameScreen gs) {
    	this.gs = gs;
		middle = new Piece(1, 1, 1, gs);
		first = new Piece(1, 1, 0, gs);
		second = new Piece(1, 1, 0, gs);
		a  = new Piece(1, 1, 0, gs);
		b = new Piece(1, 1, 0, gs);
		v1 = new Vector();
		v2 = new Vector();
		
		nodeList = new Stack();
		for (int i = 0; i < initialCapacity; i++)
			nodeList.push(new node(0, 0));
		
    }
    
    void IncOfNodelist() {
    	for (int i = 0; i < capacityIncrement; i++)
			nodeList.push(new node(0, 0));
    }
    

    public void setMoveDir(int d) {
    	this.moveDirection = d;
    }
    
    public void changeArrayForMoveDir() {
    	
    	int i;
    	int x,y;
    	int m,n;
    	switch(this.moveDirection) {
    	case LEFT:
    	
    		if (x1 <= x2) {
    			x = x2;
    			y = y2;
    			m = x1;
    			n = y1;
    		} else {
    			x = x1;
    			y = y1;
    			m = x2;
    			n = y2;
    		}
    		
    		for (i = x; i<=(this.width); i++) {
    			if(array[y][i] == 0)
    				break;
    			array[y][i] = array[y][i+1];
    			gs.updateTiles(i-1, y-1, array[y][i]);
    		}

    		
    		for (i = m; i<=(this.width); i++) {
    			if(array[n][i] == 0)
    				break;
    			array[n][i] = array[n][i+1];
    			gs.updateTiles(i-1, n-1, array[n][i]);
    		}

    		
    	
    		break;
    	
    	case RIGHT:
    		if (x1 >= x2) {
    			x = x2;
    			y = y2;
    			m = x1;
    			n = y1;
    		} else {
    			x = x1;
    			y = y1;
    			m = x2;
    			n = y2;
    		}
    		
    		for (i = x; i>=1; i--) {
    			if(array[y][i] == 0)
    				break;
    			array[y][i] = array[y][i-1];
    			gs.updateTiles(i-1, y-1, array[y][i]);
    		}

    		
    		for (i = m;i>=1; i--) {
    			if(array[n][i] == 0)
    				break;
    			array[n][i] = array[n][i-1];
    			gs.updateTiles(i-1, n-1, array[n][i]);
    		}
    		break;
    	case UP:
    		if (y1 <= y2) {
    			x = x2;
    			y = y2;
    			m = x1;
    			n = y1;
    		} else {
    			x = x1;
    			y = y1;
    			m = x2;
    			n = y2;
    		}
    		
    		for (i = y; i<=this.height; i++) {
    			if(array[i][x] == 0)
    				break;
    			array[i][x] = array[i+1][x];
    			gs.updateTiles(x-1, i-1, array[i][x]);
    		}
	
    		for (i = n; i<=this.height; i++) {
    			if(array[i][m] == 0)
    				break;
    			array[i][m] = array[i+1][m];
    			gs.updateTiles(m-1, i-1, array[i][m]);
    		}
    		break;
    	case DOWN:
    		if (y1 >= y2) {
    			x = x2;
    			y = y2;
    			m = x1;
    			n = y1;
    		} else {
    			x = x1;
    			y = y1;
    			m = x2;
    			n = y2;
    		}
    		
    		for (i = y; i>=1; i--) {
    			if(array[i][x] == 0)
    				break;
    			array[i][x] = array[i-1][x];
    			gs.updateTiles(x-1, i-1, array[i][x]);
    		}

    		
    		for (i = n; i>=1; i--) {
    			if(array[i][m] == 0)
    				break;
    			array[i][m] = array[i-1][m];
    			gs.updateTiles(m-1, i-1, array[i][m]);
    		}
    		break;
    	default:
    		array[y1][x1] = 0;
    		array[y2][x2] = 0;
    		gs.updateTiles(x1-1, y1-1, 0);
    		gs.updateTiles(x2-1, y2-1, 0);
    		break;
    	
    	}
    }
    
    public void init() {
    	
    	middle.setx(1);
    	middle.sety(1);
    	this.issolved = false;
    	this.binggo = 0;
    }
    
    private boolean isVerticalBlank(int x,int y1, int y2){
    	int a = (y1 > y2)? y1 :y2;
		int b = (y1 > y2)? y2 :y1;

		for(b = b+1; b<a; b++){
			if(array[b][x] != 0)
				return false;
		}
		return true;

	}
	
    public void randomArray(byte[] a1) {
    /*	Random r = new Random();
    	int d = r.nextInt();
    	
    	synchronized (this) {
			for (int i = 1; i < width + 2; i++)
				for (int j = 1; j < (this.height + 2); j++) {
					if (array[j][i] != 0 && (array[j][i] % 2 == 1)) {
						array[j][i] = (byte) ((array[j][i] + d) % 35 + 1);
						gs.updateTiles(i - 1, j - 1, array[j][i]);
					}
				}
		}*/
    	Random r = new Random();
    	int d;
    	int l= a1.length;
    	byte t;
    	for(int i =1; i < l/2; i++) {
    		d = r.nextInt()%l;
    		if(d < 0){
    			d = (d+l)%l;
    		}
    		t= a1[d];
    		a1[d] = a1[l-1];
    		a1[l-1] = t;
    	}
    	
    }
    
    public void Random() {
    	byte[] a1 = new byte[(this.store-this.binggo)*2];
    	int index = 0;
    	for (int i = 1; i < width + 2; i++)
			for (int j = 1; j < (this.height + 2); j++) {
				if (array[j][i] != 0 ) {
					a1[index++]= array[j][i];
					
				}
	    }
    	
    	randomArray(a1);
    	
    	index = 0;
    	for (int i = 1; i < width + 2; i++)
			for (int j = 1; j < (height + 2); j++) {
				if (array[j][i] != 0 ) {
					array[j][i] = a1[index++];
					gs.updateTiles(i - 1, j - 1, array[j][i]);
				}
	    }
    }
    
    private boolean isHorizentalBlank(int y,int x1, int x2){
    	int a = (x1 > x2)? x1 :x2;
		int b = (x1 > x2)? x2 :x1;

		for(b = b+1; b<a; b++){
			if(array[y][b] != 0)
				return false;
		}
		return true;

	}

	/*判断P1 和P2是不是可以用直线联接起来*/
    private boolean canDirectLink(int x1, int y1, int x2, int y2){


		if(x1 == x2) {
		 return isVerticalBlank(x1, y1, y2);
		} else if(y1 == y2){
			return isHorizentalBlank(y1, x1, x2);
		} else {
			return false;
		}
	}
    node getNode(int x, int y) {
    	node n;
    	
    	if (this.nodeList.empty()) {
    		this.IncOfNodelist();
    	}
    	n = (node)nodeList.pop();
    	n.x = x;
    	n.y = y;
    	return n;
    }
	/**
	 * Compute the index in the array of the x, y location.
	 */
	private int index(int x, int y) {
	    if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
	        return -1;
	    }
	
	    return array[y+1][x+1];
	}

	void getList(Piece p1, int index){
		int x1 = p1.getx();
		int y1 = p1.gety();
		Vector v;
		
		if (index == 1){
			v = v1;
		} else {
			v = v2;
		}

        for(int i = x1+1; (i<=width+1); i++){		
				if (array[y1][i] != 0)
					break;
				v.addElement(getNode(i,y1));
		}
        for(int i = x1-1;(i>=0); i--){	
			if (array[y1][i] != 0)
				break;
			v.addElement(getNode(i,y1));
		}	
        for(int i = y1-1;(i>=0); i--){	
			if (array[i][x1] != 0)
				break;
			v.addElement(getNode(x1,i));
		}
		for(int i = y1+1;(i<=height+1); i++){	
			if (array[i][x1] != 0)
				break;
			v.addElement(getNode(x1,i));
		}
		
	}

	
	private boolean canLink(Piece p1, Piece p2){
		boolean link = false;
		int x1 = p1.getx();
		int y1 = p1.gety();
		int x2 = p2.getx();
		int y2 = p2.gety();

		if ((x1==x2) && (y1==y2))
			return false;
		if((array[y1][x1] == 0) ||(array[y2][x2] == 0) )
			return false;
		if(array[y1][x1] != array[y2][x2]){
			return false;
		}
		if(canDirectLink(x1, y1, x2, y2))
			return true;
		getList(p1, 1);
		getList(p2, 2);
		
		if(v1.isEmpty() || v2.isEmpty()){
			giveupnode(v1);
			giveupnode(v2);
			return false;
		}
		//Enumeration e1 = v1.elements();
		//Enumeration e2 = v2.elements();
		int s1 = v1.size();
		int s2 = v2.size();
		node m,n;
		for (int i=0; i<s1; i++){
			//m = (Piece)e1.nextElement();
			m = (node)v1.elementAt(i);
			for(int j=0; j<s2; j++){
				n = (node)v2.elementAt(j);
				if(canDirectLink(m.x, m.y, n.x, n.y)){
					giveupnode(v1);
					giveupnode(v2);
					return true;
				}
				//v2.removeElement(n);
			}
			//v1.removeElement(m);		
		}
		/*
		for (m = (Piece)e1.nextElement();e1.hasMoreElements(); m = (Piece)e1.nextElement()){
			for (n = (Piece)e2.nextElement();e2.hasMoreElements(); n = (Piece)e2.nextElement()){
				if(canDirectLink(m.getx(), m.gety(), n.getx(), n.gety()))
					return true;
			}
		}
        */
		giveupnode(v1);
		giveupnode(v2);
		/*v1.removeAllElements();
		v2.removeAllElements();*/
		return false;
		
	}
	public Piece a, b;

	public boolean giveUsrHint() {

		for (int i = 0; i< 56; i++) {
			if(array[i/7+1][i%7+1] == 0)
				continue;
			//System.out.println("i"+i);
		    for (int j = i+1; j< 56; j++) {
				if(array[j/7+1][j%7+1] == 0)
					continue;
				//System.out.println("j"+j);
				a.x=i%7+1;
				a.y = i/7+1;
				b.x=j%7+1;
				b.y=j/7+1;
				if (array[a.y][a.x] != array[b.y][b.x])
					continue;

				if (canLink(a,b)){
					//System.out.println("success to link");
					gs.setExploreTiles(i%7, i/7, j%7, j/7);
					return true;
				} 
		   }
		}
		return false;
	}
	void giveupnode(Vector v) {
		
		while (!v.isEmpty()) {
			nodeList.push(v.firstElement());
		
			v.removeElementAt(0);
		}
	}
	
    class node {
    	public int x,y;
    	
    	node(int x, int y) {
    		
    		this.x =x;
    		this.y =y;
    	}
    	
    }
    
	public Piece getMiddle(){
		return middle;
	}

	public void setMiddleX(int x){
		if(x > 0 && x <= width)
			middle.setx(x);
	}

	public int getStored(){
		return binggo;
	}
	
	public int getScore(){
		return binggo;
	}
	
	public void setMiddleY(int y){
		if (y>0 && y<=height)
			middle.sety(y);
	}
	boolean issolved = false;
	public boolean isSolved() {
		return this.issolved;
	}
	public void setSolved(boolean s) {
		this.issolved = s;
		return;
	}
	/** Succes to find two identical picture */
	private void updateTileAndArray(){
/*		int x1 = first.getx();
		int y1 = first.gety();
		int x2 = second.getx();
		int y2 = second.gety();*/
		
		
		//changeArrayForMoveDir();
		//gs.updateTiles(x1-1, y1-1, 0);
		//gs.updateTiles(x2-1, y2-1, 0);
		
		first.setcolor(0);	
		second.setcolor(0);	
		binggo++;
		issolved = true;
    	if (store <= binggo ){	
    		gs.changeState(gs.PLAYING_SUCCESS);
    	}
	}
    /**
     * Move the pusher in the direction indicated.
     * If there is a wall, don't move.
     * if there is a packet in that direction, try to move it.
     * @param move the direction; one of LEFT, RIGHT, UP, DOWN
     * @return the direction actually moved, -1 if not moved
     */
	
    public void move(int move) {
	    int a;
		synchronized (this) {
			switch (move) {
			case FIRE:
				if (first.getcolor() == 0) {
					first.setx(middle.getx());
					first.sety(middle.gety());
					first.setcolor(2);
				} else if (second.getcolor() == 0) {
					second.setx(middle.getx());
					second.sety(middle.gety());
					//second.setcolor(2);
					/** judge first can link to second */
					if (canLink(first, second)) {
						savePot(first.getx(), first.gety(), second.getx(), second.gety());
						updateTileAndArray();
					} else {
						/** if not link, change the second station to be the first station*/
						first.setx(second.getx());
						first.sety(second.gety());
						//second.setcolor(0);
					}

				}

				break;
			case LEFT:
				a = middle.getx();
				if (a == 1) {
					middle.setx(width);
				} else {
					middle.setx(a - 1);
				}
				break;
			case RIGHT:
				a = middle.getx();
				if (a == width) {
					middle.setx(1);
				} else {
					middle.setx(a + 1);
				}
				break;
			case UP:
				a = middle.gety();
				if (a == 1) {
					middle.sety(height);
				} else {
					middle.sety(a - 1);
				}

				break;
			case DOWN:

				a = middle.gety();
				if (a == height) {
					middle.sety(1);
				} else {
					middle.sety(a + 1);
				}
				break;
			}
		}
    }

    private void savePot(int getx, int gety, int getx2, int gety2) {		// TODO Auto-generated method stub
		x1 = getx;
		y1 = gety;
		x2 = getx2;
		y2 = gety2;
		return;
	}

	/**
     * Determine if the screen has been solved.
     */
    public boolean solved() {

        return binggo >= store;
    	//return true;
    }

	public void paint(Graphics g) {

		if(middle != null)
			middle.paint(g);
		if(first.getcolor() != 0)
			first.paint(g);
	}

    /**
     * Return the pieces at the location.
     * @param x location in the board.
     * @param y location in the board.
     * @return flags indicating what pieces are in this board location.
     * Bit flags; combinations of WALL, PUSHER, STORE, PACKET.
     */
    public byte get(int x, int y) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
            return -1;
        }

        return array[y+1][x+1];
    }

    private String readString(InputStream is, byte terminator)
    {
       try
       {
          StringBuffer sb = new StringBuffer();
          DataInputStream di = new DataInputStream(is);
          byte b = di.readByte();
          while (b != -1)
          {
             if (b == terminator)
             {
                return sb.toString();
             } else
                sb.append((char)b);

             b = di.readByte();
          }

          return null;
       } catch(IOException e)
       {
          System.out.println("IOException: " + e);
          e.printStackTrace();
          return null;
       }

    }
	
	Random r = new Random();
    public int getRandom(int min, int max) { //生成随机数
	  	int m = Math.abs(r.nextInt());
	  	return (m % ( (max - min + 1))) + min;
    } 
    
    /**
     * Read a board from a stream.
     * Read it into a fixed size array and then shrink to fit.
     */
    public void read(String levelName) {

//#if 0
		boolean foundLevel = false;
  
      
        
        try {
        	InputStream is = getClass().getResourceAsStream("/levels.dat");
            int b = is.read();
            while (b != -1 && !foundLevel)
            {
               // Level name starts with a ! and terminates with a ~ character.
               // The readString method wraps up reading the string from the
               // stream.
               if (b == '!')
               {
                  // Got a start of level name char, read the name string.
                  String ln = readString(is, (byte) '~').toLowerCase();

                  if (ln.equals(levelName.toLowerCase()))
                     foundLevel = true;
               }

               // If the level hasn't been found yet you continue reading.
               if (!foundLevel)
                  b = is.read();
            }

            // Test if the end of the file was reached, in which case the level
            // load failed (possibly because of the wrong level name being used.
            if (b == -1)
               throw new Exception("unknown level: " + levelName);

            // Load the level. Start by reading the height and width from the file.

            byte[] buffer = new byte[2];
            is.read(buffer);
            String tws = new String(buffer, 0, 2).trim();
            is.read(buffer);
            String ths = new String(buffer, 0, 2).trim();
        
            //width = Byte.parseByte(tws);
            width = Integer.parseInt(tws);
            height = Integer.parseInt(ths);
            store = (width*height)/2;
		 
         // Load the level. Start by reading the height and width from the file.
		    array = new byte[height+2][width+2];
		    
	     // Next you read all the tiles into the tilemap. Each tile is
	     // represented by 2 bytes (tile numbers can go up to a maximum of 99).
	     // The data is read into a 2 byte buffer and then added to the tilemap.
		   int bytesRead=0;
           for (int ty=1; ty < height+1; ty++)
           {
              for (int tx = 1; tx < width+1; tx++)
              {//add 1 becaulse image's index is begin 0,but tiles's begin from 1.
              
            	bytesRead = is.read(buffer);
                if (bytesRead > 0)
                {
                   tws = new String(buffer, 0, 2).trim();
                   if (tws.indexOf("-") != -1)
                      // If this starts throwing exceptions when loading the map
                      // check that the tile indecies in your map file doesn't
                      // have -1's in it. This is the default index for the
                      // empty tile in TileStudio. Always fill your levels with
                      // blank (NO_TILE) entries before editing it.
                      System.out.println("Oops, read a - at " + tx + ", " + ty);

                   int c = Integer.parseInt(tws);
                
                   array [ty][tx] =  (byte)(c + 1);
            	}
               
             }
         }
        } catch (EOFException ex) {
            ex.toString();
        } catch (NumberFormatException ex) {
        	ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//#endif
	Random r = new Random();
    byte pic;
	int m;


	
	for (int ty=1; ty < height+1; ty++)
	{
	   for (int tx = 1; tx < width+1; tx++){
			array [ty][tx] =  0;
			
	   }
	}
	
	for(int i = 0; i< 28; i++) {
		//20代表可以消的图片有20种，如果图片种类增加，
		//这里可以改大一些，增加难度
		pic = (byte)getRandom(1,20);
		m = getRandom(0,56);
		m = get_valid_index(m);
		array[m/7+1][m%7+1] = pic;
		m = getRandom(0,56);
		m = get_valid_index(m);
		array[m/7+1][m%7+1] = pic;
	}
    
}

    public int get_valid_index(int random) {
	    int i = 0, j = 0;

		int b = random;

		while( (b < 0) || (b > 55) || array[b/7+1][b%7+1] != 0) {
			if (i <= j) {
				i++;
				b = random - i;
			} else {
			    j++;
				b = random + j;
			}
		}
		return b;
		

	} 
	
    /**
     * Get the width of the game board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the board.
     */
    public int getHeight() {
        return height;
    }
}
