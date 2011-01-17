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



    
    private int width =5;
    private int height=300;
	  private byte[][] array = new byte [height+2][width+2];
    /** store the first piece's neighber piece*/
    Vector v1;
    /** store the second piece's neighber piece */
    Vector v2;
    Stack nodeList;
    int initialCapacity = 15;
    int capacityIncrement = 10;
    
    public Piece first, second, middle;
	  int binggo = 0;
  	int store ;
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

    
    
    public int getStore(int s) {
      return this.store;
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
        /* process bomber event*/
        //#debug
        System.out.println("***x1"+x1+"y1"+y1);
        //#debug
        System.out.println("***x2"+x2+"y2"+y2);
        if((x1 == x2) && (y1 == y2)) {
          if(array[y1][x1] == 10) {
            //#debug
            System.out.println("10");
            for(i=y1-1; i<=y1+1; i++) 
              for(int j=x1-1; j<=x1+1; j++) {
                if((i>0) && (i<=this.height) && 
                  (j>0) && (j<=width) && 
                  (array[i][j] != 0)) {
                  //#debug
                  System.out.println("10xx");
                  array[i][j] = 0;
                  gs.updateTiles(j-1, i-1, 0);
                }
              }
          } else if(array[y1][x1] == 11) {
          //#debug
            System.out.println("11");
            //#if 0
            for (i = this.width; i>=1; i--) {
              //#debug
              System.out.println("11**");
    			    if(array[y1][i] != 0) {
                  array[y1][i] = 0;
                  gs.updateTiles(i-1, y1-1, 0);
              }
            }
            //#endif
            array[y1][x1]=0;
            gs.updateTiles(x1- 1, y1- 1, 0);
            //int row = y1;
            int row  = (2+gs.ScrHeight- gs.game_tile_y_postion) / gs.TILE_HEIGHT;
            if(row>=this.height) row=this.height;
            int count = 0;
            while(true) {
              if(is_the_row_empty(row)){
                row--;
                continue;
              }
              for(i=1; i<=this.width; i++){
                if(array[row][i]!=0){
                  array[row][i]=0;
                  gs.updateTiles(i - 1, row - 1, 0);
                }
              }
              row--;
              count++;
              if((count>=2) || (row<y1)) break;
            }
          } 
        } else {
      		array[y1][x1] = 0;
      		array[y2][x2] = 0;
      		gs.updateTiles(x1-1, y1-1, 0);
      		gs.updateTiles(x2-1, y2-1, 0);
        }
    		break;
    	
    	}
    }
    int levelOfgame = 0;
    public void init(int level) {
    	
    	middle.setx(1);
    	middle.sety(1);
      first.setcolor(0);
      second.setcolor(0);
    	this.issolved = false;
      if(level != -1) {
        this.binggo = 0;
        store = 10+level*10;
      } else {
        //#debug
        System.out.println("same level, not clear binggo");
      }
    	levelOfgame = level;
    }

    
    public boolean is_the_row_empty(int row) {
      int i;
      if(row > this.height) return true;
      for (i = this.width; i>=1; i--) {
        //#debug
        System.out.println("is_the_row_empty");
        if(array[row][i] != 0) {
           return false;
           
        }
      }
      return true;
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
      //#if 0
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
      //#endif
      int row  = (2 - gs.game_tile_y_postion) / gs.TILE_HEIGHT;
      //we random from row+1 until blank row
      row++;
      while(!is_the_row_empty(row)) {
        for(int i=1; i<=this.width; i++){
          if(array[row][i]!=0){
            array[row][i]=(byte)getRandom(1,11);
            gs.updateTiles(i - 1, row - 1, array[row][i]);
          }
        }
        row++;
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

	
  //to see if select index is bomber 
  private boolean  process_bomber(int x, int y) {
    if((array[y][x] != 10 ) &&
      (array[y][x] != 11 )) {
      //not bomber,return;
      return false;
    }
    //#debug
    System.out.println("process_bomber:"+x+y);
    this.x1 = x;
    this.y1 = y;
    
    this.x2 = x;
    this.y2 = y;
    issolved = true;
    return true;
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
		if(array[y1][x1] + array[y2][x2] == 10){
			return true;
		}
		return false;
		//#if 0
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
		giveupnode(v1);
		giveupnode(v2);
		/*v1.removeAllElements();
		v2.removeAllElements();*/
		return false;
		//#endif
	}
	public Piece a, b;

	public boolean giveUsrHint() {

		for (int i = 0; i< 56; i++) {
			if(array[i/width+1][i%width+1] == 0)
				continue;
			//System.out.println("i"+i);
		    for (int j = i+1; j< 56; j++) {
				if(array[j/width+1][j%width+1] == 0)
					continue;
				//System.out.println("j"+j);
				a.x=i%width+1;
				a.y = i/width+1;
				b.x=j%width+1;
				b.y=j/width+1;
				if (array[a.y][a.x] != array[b.y][b.x])
					continue;

				if (canLink(a,b)){
					//System.out.println("success to link");
					gs.setExploreTiles(i%width, i/width, j%width, j/width);
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
		first.setcolor(0);	
		second.setcolor(0);	
		
    if(array[y1][x1] == 10 ) {
      binggo+=2;
    } else if(array[y1][x1] == 11 ){
      binggo+=4;
    } else {
      binggo++;
    }
		issolved = true;
    if (store <= binggo ){	
        binggo = store;
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
        if(process_bomber(middle.getx(), middle.gety())) {
          updateTileAndArray();
          return;
        } 
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

    private void savePot(int x, int y, int getx2, int gety2) {		// TODO Auto-generated method stub
		x1 = x;
		y1 = y;
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
    public void read(int levelName) {

  		Random r = new Random();
  	  byte pic;
  		//#if 0
  		for (int ty=1; ty < height+1; ty++)
  		{
  		   for (int tx = 1; tx < width+1; tx++){
  				array [ty][tx] =  0;
  				
  		   }
  		}
      //#endif
      
      if(levelName == -1) levelName=this.levelOfgame;
  		int total = this.width*this.height;
  		for(int i = 0; i< total; i++) {
  			//9代表可以消的图片有9种，如果图片种类增加，
  			//这里可以改大一些，增加难度
  			if(array[i/width+1][i%width+1] != 0) continue;
  			pic = (byte)getRandom(1,9);
  		  
  			array[i/width+1][i%width+1] = pic;

       
        int j = i+(byte)getRandom(1,45);
        j = get_valid_index(j);
        if(j>0 && j<total) {
          array[j/width+1][j%width+1] = (byte)(10-(int)pic);
        }
        
  		}
      
      //file some bomber and clear screen killer
      int aa;
      int num_of_killer = getRandom(1,Math.max(30+levelName*2, 1));
      for(int i=0; i < num_of_killer; i++) {
        aa = getRandom(1, total-1);
        array[aa/width+1][aa%width+1] = 10;
      }
      num_of_killer = getRandom(1,Math.max(20+levelName*3, 1));
      for(int i=0; i < num_of_killer; i++) {
        aa = getRandom(1, total-1);
        array[aa/width+1][aa%width+1] = 11;
      }
   }

	
    public int get_valid_index(int random) {
    	  int i = 0, j = 0;

    		int b = random;
        int t =this.width*this.height;
    		while( (b < 0) || (b > t) || array[b/width+1][b%width+1] != 0) {
    			if (i <= j) {
    				i++;
    				b = random - i;
    			} else {
    			    j++;
    				b = random + j;
    			}
          if(i>=10) break;
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
