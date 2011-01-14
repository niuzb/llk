	/**
	 * The central game control class managing: rendering, cycling of world (which
	 * includes actors) and other game state.
	 */
	
  import java.io.*;
	import java.util.*;
	import javax.microedition.lcdui.*;
	import javax.microedition.lcdui.game.*;
	import javax.microedition.media.Manager;
  import javax.microedition.media.MediaException;
	import javax.microedition.media.Player;
	import javax.microedition.media.control.*;
	
	import de.enough.polish.midp.ui.GameCanvas;
	

	
	public class GameScreen extends GameCanvas implements Runnable, CommandListener
	{
	   private static GameScreen theGameScreen;	// "there can be only one..."
	
	   private static final int t = 0;
	   public static final int NOT_STARTED = 0;	// game state
	   public static final int STARTING_UP = 1;   // delay prior to action
	   public static final int PLAYING = 2;       // game in progress
	   public static final int PLAYING_SUCCESS = 3; // game in progress
	   public static final int PLAYING_FAILURE = 4; // game in progress
	   public static final int PAUSED = 5;        // paused
	   public static final int GAME_OVER = 6;		// showing "game over..."
	   public static final int GAME_DONE = 7;		// game is finished
	   public static final int MAX_STATE_NUM = GAME_DONE + 1;	
	   
		//#ifdef polish.ScreenWidth:defined
			//#= int ScrWidth = ${ polish.ScreenWidth };
		//#else
			int ScrWidth = this.getWidth();
		//#endif
		
		//#ifdef polish.ScreenHeight:defined
			//#= int ScrHeight = ${ polish.ScreenHeight };
		//#else
			int ScrHeight = this.getHeight();
		//#endif
	   public int TILE_WIDTH = 30;
	   public int TILE_HEIGHT = 30;
	   public static final int GAME_TIME = 120;		// game is finished
	   
	   int defaultFontHeight;
	   private Font defaultFont;
	  
	   // Game state
	   private boolean running = false;						// thread controller
	   private  Board board;
	   /** Board width */
	   int  bwidth;  
	   /** Board height */
	   int	bheight;
	   
	   /** Tiles forming the background */
	   private TiledLayer tiles;  
	   private TiledLayer bgtiles;  
	   private TiledLayer exptiles; 
	   /** Layer manager */
	   private LayerManager layers;
	
	   

	   llk theMidlet;

	   //int ScrWidth;
	   //int ScrHeight;
	
	   /** total game time :second*/
	   int totalGameTime;
	   int currentLevel;
	   int baseScore;
	   
	   private Command exitCommand = new Command(Locale.get("General.Exit"), Command.EXIT, 60);
   //private Command randomCommand = new Command("random", Command.SCREEN, 26);
	   //private Command hintCommand = new Command("random", Command.SCREEN, 26);
	   private Command menuCommand = new Command(Locale.get("Main_Menu.Title"), Command.SCREEN, 30);

	   
	   public GameScreen(llk midlet) {
		  super(false);
		  setFullScreenMode(true);
		  //ScrWidth = getWidth();
		  //ScrHeight = getHeight();
		  TILE_WIDTH = 30*ScrHeight/320;
		  TILE_HEIGHT = 30*ScrHeight/320;
		  currentLevel = 1;
		  baseScore = 0;
		  rondomTime = 3;
		  hintTime = 3;
		  move = 0;
		  /*end*/
		  theMidlet = midlet;
		  board = new Board(this);
		  
		  layers = new LayerManager();
		  
	
	     // menu = new Command("Menu", Command.SCREEN, 1);
	      //addCommand(menu);
	     // setCommandListener(this);
		  initResources();
		  /*建立菜单*/
		  //this.addCommand(exitCommand);
		  //this.addCommand(menuCommand);
		  this.createAudioPlayer();
		  setCommandListener(this);
	   }
	   
	   public void start() {
		  this.gotoNextLevel(currentLevel);
		  //readScreen("level" + currentLevel);
		  //changeState(this.PLAYING);
		  running = true;
	      
	      // create the game thread
	      Thread t = new Thread(this);
	      t.start(); 
	   }
	   
	   //Image Success;
	   //Image Failure;
	   //Image source;
	   //Image TimeBar;
	   /* 背影图片，包含很多30*30的小图片，可用于不同的关卡*/
	   public Image BackGround;
	   /*explorer graphics 30*30 */
	   public Image explore;
	   Image tileImage1;
	   Image tileImage2;
	   Image tileImage3;
	   Image tileImage4;
	   Image frameImage;
	   private void initResources() {
	      try {
	    	 //Success = createImage("/End_Success.png");
			 //Failure = createImage("/End_Failed.png");
	    	// source = createImage("/Statusbar.png");
	    	 BackGround= createImage("/000.png");
			 explore = createImage("/explosion.png");
			 frameImage = createImage("/bg.png");
	    	 //TimeBar = Image.createImage(source.getWidth(), source.getHeight());        
	    	 //Graphics g = TimeBar.getGraphics();    
	    	 //g.drawImage(source, 0, 0, Graphics.TOP|Graphics.LEFT);       
	         tileImage1 = createImage("/tile1.png");
			 tileImage2 = createImage("/tile2.png");
			 tileImage3 = createImage("/tile3.png");
			 tileImage4 = createImage("/tile4.png");
			 
			
	         defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	         defaultFontHeight = defaultFont.getHeight();
	         
	      } catch (Exception e) {
	         System.out.println("App exception: " + e);
	         e.printStackTrace();
	      }
	   }

	   
	   /**
		* Read and setup the next level.
		* Opens the resource file with the name "/Screen.<lev>"
		* and tells the board to read from the stream.
		* <STRONG>Must be called only with the board locked.</STRONG>
		*
		* @param lev the level number to read.
		* @return true if the reading of the level worked, false otherwise.
		*/
	   boolean shouldAppend = false;
	   private boolean readScreen(String lev) {
		   board.read(lev);
		   board.init();
		   bwidth = board.getWidth();
		   bheight = board.getHeight();
		   setupBgTiles();
		   setupExploreTiles();
		   
		   if (!shouldAppend) {
			   try {
				    layers.append(exptiles);	
					//layers.insert(exptiles,0);
		   			//layers.append(tiles);	
				    //layers.insert(bgtiles,2);
		   			layers.append(this.bgtiles);
					
		   	   } catch (Exception e){
		   		   	e.toString();	
		   	   }	
			   shouldAppend = true;
		   	}
		   setupTiles();
		   return true;
	   }
	   
	   
	   private void setupBgTiles() {
	   	    //if (bgtiles != null) {
			//   layers.remove(bgtiles); 
		    //}
		    int tile_x = ScrWidth/TILE_WIDTH-bwidth+1;
			int tile_y = ScrHeight/TILE_HEIGHT -bheight+1;
	   	    if(bgtiles == null) {
				try {
					bgtiles =new TiledLayer(bwidth+tile_x, bheight+tile_y, BackGround, TILE_WIDTH, TILE_HEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (int i = 0; i< bwidth+tile_x; i++) 
				for (int j = 0; j< bheight+tile_y; j++) {
				bgtiles.setCell(i, j, currentLevel%7+1);//使地图被景在7个中轮循
			}
			//bgtiles.setPosition(10,25);
	   }
	   
	   /*创建显示爆炸效果的layer*/
	   private void setupExploreTiles() {
	   	    if(exptiles == null) {
				try {
					exptiles =new TiledLayer(bwidth, bheight, explore, TILE_WIDTH, TILE_HEIGHT);
					exptiles.move(tileoffsetx,tileoffsety);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	   }
	   
	   /** Create the Tiled layer to represent the current board. */
	   private void setupTiles() {
	       Image ima;
		   int m;

		   
		   if (tiles != null){
			  layers.remove(tiles);
		   } 
		   m = currentLevel%4+1;
		   switch(m) {
			case 1:
				ima=tileImage1;
				break;
			case 2:
				ima=tileImage2;
				break;
			case 3:
				ima=tileImage3;
				break;
			case 4:
				ima=tileImage4;
				break;
			default:
				System.out.println("no image found"+m);	
				ima= tileImage1;
				break;
		   }
			  
		   try {
		   	 tiles = new TiledLayer(bwidth, bheight, ima,TILE_WIDTH, TILE_HEIGHT);
				tiles.move(tileoffsetx,tileoffsety);
		   } catch (NullPointerException e){
				e.toString();
				System.out.println("tiles is NullPointerException");	
		   } catch (IllegalArgumentException e){
				e.toString();
				System.out.println("tiles is IllegalArgumentException");	
		   }
		   if (tiles == null){
			  System.out.println("tiles is null");	  
		   } 
		   // Initialize the background tileset
		   for (int y = 0; y < bheight; y++) {
			   for (int x = 0; x < bwidth; x++) {
				   tiles.setCell(x, y, board.get(x, y));
			   }
		   }
		   try {
		   		layers.insert(tiles, 1);
		   }
		   catch (Exception e){
				e.toString();
		   }
		   
	   }
	
	   public void updateTiles(int x, int y, int index) {
		   tiles.setCell(x, y, index);
	   }
	   /*指示现在显示的爆炸图片索引*/
		int currentExploreTile=0;
	    //int animate;
		int x1,y1,x2,y2;
	   public void setExploreTiles(int x1, int y1, int x2, int y2) {
		   /*把原来的图片清掉*/
		   for (int y = 0; y < bheight; y++) {
			   for (int x = 0; x < bwidth; x++) {
				   exptiles.setCell(x, y, 0);
			   }
		   }
		   currentExploreTile = 1;
		   this.x1 = x1;
		   this.y1 = y1;
		   this.x2 = x2;
		   this.y2 = y2;
		   exptiles.setCell(x1, y1, 1);
		   exptiles.setCell(x2, y2, 1);
		   //animate = exptiles.createAnimatedTile(4);
		   
	   }

	   private void setNextExploreTiles(){
		//显示的图片索引如果到了最后一个清空
		if (currentExploreTile == 8) {
			//exptiles.setAnimatedTile(-1, 0);
			currentExploreTile = 0;
			
			exptiles.setCell(x1, y1, currentExploreTile);
			exptiles.setCell(x2, y2, currentExploreTile);
			return;
		}
        //System.out.println("currentExploreTile:"+currentExploreTile);
		if (currentExploreTile != 0) {
			++currentExploreTile;
			exptiles.setCell(x1, y1, currentExploreTile);
			exptiles.setCell(x2, y2, currentExploreTile);
			//exptiles.setAnimatedTile(-1, );
			
		}
	   }
	   
	   /*记录可使用的随机次数和可使用的提示次数*/
	   int rondomTime;
	   int hintTime;
	   boolean shouldChangeTile=false;
	   public void randomTiles() {
		   if(rondomTime-- > 0){
			   //board.Random();
			   shouldChangeTile=true;
		   }
		   
	   }
	   
	   public static Image createImage(String filename) {
		   Image image = null;
		   try {
			   image = Image.createImage(filename);
		   } catch (Exception e) {
			   e.printStackTrace();
			   
		   } 	
		   return image;
	   }
	   public void start_bg_music(){
			/*打开背景音乐*/
		   if((theMidlet.valueMusic == theMidlet.MUSIC_ON) && 
			   (musicPlayer != null)) {
				 try {
					 //gameoverPlayer.stop();
					 musicPlayer.setLoopCount(-1);
					 musicPlayer.start();
					// if(squishPlayer!=null)
					// 	squishPlayer.start();
				 } catch (MediaException e) {
				   e.printStackTrace();
				 }
		   }
	   }
	   int move = 0;
	   public void gotoNextLevel(int level) {
	   	
			start_bg_music();

		   board.setMoveDir(currentLevel%board.FIRE);
		  /* if (move < board.FIRE) {
			   board.setMoveDir(move);
			   move++;
			   
		   } else {
			   
			   move = 0;
			   board.setMoveDir(move);
			   
		   }*/
		   //currentLevel++;
		   totalGameTime = GAME_TIME;	
		   escape_time = 0;
		   lastCPSTime = 0;
		   readScreen("level" + currentLevel);
		  
		   changeState(this.PLAYING);
	   }
	   
	   /**
	    * update the time bar when time minus 1 second.
	    
	   private void updateTimeBar() {
		   int length = TimeBar.getWidth();
		   int width =  (int) (TimeBar.getWidth()*escape_time/totalGameTime);
		   
		   if (escape_time == totalGameTime) {
			   changeState(this.PLAYING_FAILURE);
		   }
		   Graphics g = TimeBar.getGraphics();
		   g.setColor(0xaaaaaa);   
		   g.fillRect(length-width, 0, width, TimeBar.getHeight());
	 
	   }*/
	
	  private void paintStatusBar(Graphics g) {
		  int width = 0;
		  int ts = this.baseScore + board.getScore()*2;
		  //#if 0
		  g.setColor(0);
		  g.setFont(defaultFont);
		  g.drawString("rd:"+(this.rondomTime<0?0:rondomTime), this.ScrWidth-20, 0, Graphics.TOP|Graphics.LEFT);
		  g.drawString("time:", 0, ScrHeight - defaultFontHeight, Graphics.TOP|Graphics.LEFT);
		  width += defaultFont.stringWidth("time:");
		  g.drawImage(TimeBar, width, 
				  ScrHeight- TimeBar.getHeight(), Graphics.TOP|Graphics.LEFT);
		  width += TimeBar.getWidth();
		  if (width < ScrWidth-defaultFont.stringWidth("score:xxxx")) {
			  g.drawString("score:" + ts, width, 
					  ScrHeight - defaultFontHeight, Graphics.TOP|Graphics.LEFT);
		  } else {
			  g.drawString("score:" + ts, 0, 
					  ScrHeight - defaultFontHeight*2, Graphics.TOP|Graphics.LEFT);
	  	  }
		  //#endif
		  
		  g.drawImage(frameImage,0,0,g.TOP|g.LEFT);
		  //下面是显示时间，得分的代码，x,y,坐标需要在frameImage变化时，相应变化
		  g.setColor(0);
		  g.setFont(defaultFont);
		  int y = 6*ScrHeight/320;
		  g.drawString(""+(totalGameTime-escape_time),40*ScrWidth/240,y,Graphics.TOP|Graphics.LEFT);
		  
		  g.drawString(""+(this.rondomTime<0?0:rondomTime),98*ScrWidth/240,y,Graphics.TOP|Graphics.LEFT);
		  g.drawString(""+(this.hintTime<0?0:hintTime),150*ScrWidth/240,y,Graphics.TOP|Graphics.LEFT);
		  g.drawString(""+ts,198*ScrWidth/240,y,Graphics.TOP|Graphics.LEFT);
		  g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		  g.drawString(""+currentLevel,ScrWidth/2,ScrHeight-30*ScrHeight/320,Graphics.TOP|Graphics.HCENTER);

	   }
	  /*
	  private void drawStatisticNumber(Graphics g) {
		  int x = (ScrWidth+Success.getWidth())/2 - 35;
		  int y = (ScrHeight-Failure.getHeight())/2;
		  
		  g.setColor(0);
		  g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
		  g.drawString("" + this.currentLevel, x, 
				  y+62, Graphics.TOP|Graphics.LEFT);
		  
		  g.drawString("" + this.baseScore, x, 
				  y+80, Graphics.TOP|Graphics.LEFT);
		  
		  g.drawString("" + this.playTime, x, 
				  y+110 - defaultFontHeight, Graphics.TOP|Graphics.LEFT);
	  }
	  */
	  /*this function is not called yes.because we want to show this only after user 
	  finish all levels*/
	  private void drawHighScore(Graphics g) {
		   {
			   g.setColor(0x00ff00);
			   g.setFont(this.defaultFont);
				 
			   g.drawString("Your score is TOP 4", 
					   ScrWidth/2, ScrHeight-40, Graphics.TOP|Graphics.HCENTER);
			   g.drawString("do you want to save it?", 
					   ScrWidth/2, ScrHeight-30, Graphics.TOP|Graphics.HCENTER);
			  // theMidlet.gameScreenAddCmd();
		   }
	  /*delete templetely*/
	  //else {
		//	   theMidlet.mainMenuScreenShow(null);
		  // }
	  }
	  SucessDisplay sd;
	  private void drawSucessScr(Graphics g) {

		   /*g.drawImage(Success, (ScrWidth-Success.getWidth())/2,
				   (ScrHeight-Success.getHeight())/2, Graphics.TOP|Graphics.LEFT);
		   drawStatisticNumber(g);
		   if(state == this.GAME_OVER) {
			   //drawHighScore(g);
			   stopThread();
		   }*/

		   stopAudioPlayer();
		   theMidlet.updateoption(hintTime,theMidlet.keyhint);
		   theMidlet.updateoption(rondomTime,theMidlet.keyrand);
		   theMidlet.updateoption(currentLevel+1,theMidlet.keyLevel);
		   theMidlet.updateoption(totalGameTime-escape_time+baseScore,theMidlet.keyScore);
		   this.changeState(NOT_STARTED);
		   sd = new SucessDisplay(this);
	  }
	  FailureDisplay fd;
	  private void drawFailureScr(Graphics g) {
	  	   /*
		   g.drawImage(Failure,(ScrWidth-Failure.getWidth())/2,
				   (ScrHeight-Failure.getHeight())/2, Graphics.TOP|Graphics.LEFT);
		   drawStatisticNumber(g);
		   //drawHighScore(g);
		   stopThread();*/
		   stopAudioPlayer();
		   this.changeState(NOT_STARTED);
		   fd = new FailureDisplay(this);
		   //theMidlet.mainMenuScreenShow();
	  }
	  
	  private void stopThread(){
		  
		  running = false;
	  }

	
	   public int getScore() {
		   return this.baseScore;
	   }

	   
	   // tiles should paint offsetX offset in x axis and offsetY offset
	   //in y axis on the graphics by niuzb 2009/10/1
	   //and layermanager paint -2 position
	  int tileoffsetx=7*ScrHeight/320;
	  int tileoffsety=7*ScrHeight/320;
	  int layermanager_offsetx = 10*ScrHeight/320;
	  int layermanager_offsety = 30*ScrHeight/320;
      int offsetX = layermanager_offsetx+tileoffsetx;
	  int offsetY = layermanager_offsety+tileoffsety;
	  private void drawPlayScr(Graphics g) {
		  	
		   synchronized (board) {
			// Draw all the layers and flush
			   if(board.issolved) {
			   	   setExploreTiles(board.x1-1,board.y1-1,board.x2-1,board.y2-1);
				   board.changeArrayForMoveDir();
				   board.setSolved(false);
			   }
			   if (this.shouldChangeTile) {
				board.Random();
				shouldChangeTile=false;
			   }
			   if(!hint_suc) {
				   board.Random();
				   hint_suc = true;

			   }
		   }
		   //System.out.println("x:::"+ offsetX);
		   setNextExploreTiles();
		   //bgtiles.paint(g);
		   layers.paint(g,layermanager_offsetx, layermanager_offsety);
		   board.paint(g);
		   
		   paintStatusBar(g);
	  }
	  
	  private void statisticTime() {
          // update the CPS and escape time
          if (System.currentTimeMillis() - lastCPSTime > 1000)
          {
             lastCPSTime = System.currentTimeMillis();
             cps = cyclesThisSecond;
             cyclesThisSecond = 0;
             escape_time++;
             if (escape_time >= totalGameTime)
			 	changeState(this.PLAYING_FAILURE);
          }
          else {
             cyclesThisSecond++;
          }
	  }

	   
	   /* follow variable is for time statistic*/
	   private int cps;
	   private static final int MAX_CPS = 12;
	   private static final int MS_PER_FRAME = 1000 / MAX_CPS;
	   private long cycleStartTime;
	   private long timeSinceStart;
	   private long lastCycleTime;
	   private int cyclesThisSecond;
	   private long lastCPSTime = 0;
	   private int escape_time = 0;
	   
	   /* the state of current game */
	   private int state = NOT_STARTED;
	   boolean hint_suc = true;
	   
	   public void run() {
		   Graphics g = getGraphics(); // Of the buffered screen image
	
	
		   // Loop handling events
		   while (running) {
			 cycleStartTime = System.currentTimeMillis();
	           //msSinceLastCycle = cycleStartTime - lastCycleTime;
			 //g.setColor(255,191,253);
			 
			 //g.fillRect(0, 0, ScrWidth, ScrHeight);
			 switch(state) {
			 case NOT_STARTED:
			 	 Thread.yield();
				 //changeState(PLAYING);
				 break;
				 
			 case PLAYING:
			 	 if(this.should_hint && (hintTime>0)) {
					should_hint=false;
					hint_suc=board.giveUsrHint();
					if(hint_suc) {
						hintTime--;
				    }
				 }
				 drawPlayScr(g);
				 statisticTime();
				 break;
				 
			 case PLAYING_SUCCESS:
				/*  if((theMidlet.valueMusic == llk.MUSIC_ON) && (celebratePlayer != null)) {
					  try {
						  musicPlayer.stop();
						  celebratePlayer.setMediaTime(0);
						  celebratePlayer.start();
					} catch (MediaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }*/
				 drawSucessScr(g);
				 break;
				 
			 case PLAYING_FAILURE:
				/*  if((theMidlet.valueMusic == llk.MUSIC_ON) && (gameoverPlayer != null)) {
					  try {
						  musicPlayer.stop();
						  gameoverPlayer.setMediaTime(0);
						  gameoverPlayer.start();
					} catch (MediaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }*/
				 drawFailureScr(g);
				 break;	 
			 case GAME_OVER:
				 drawSucessScr(g);
			 default:
				 break;
			 }

			 flushGraphics();

	           lastCycleTime = System.currentTimeMillis();
	           // sleep if we've finished our work early
	           timeSinceStart = lastCycleTime - cycleStartTime;
	           if (timeSinceStart < MS_PER_FRAME)// && MS_PER_FRAME - timeSinceStart > 5)
	           {
	              synchronized (this)
	              {
	            	 try{
	            		 wait(MS_PER_FRAME - timeSinceStart);
	            	 } catch(Exception e){
	            		 e.printStackTrace();
	            	 }
	              }
	           }
	           else {
	              Thread.yield();
	           }
		   }//end while
	   }


	   public int playTime = 0;
	   public void changeState(int state) {
		   this.state = state;  
		   if (state == PLAYING_SUCCESS) {
			   playTime = totalGameTime-this.escape_time;
			   //calculate the total score for the next level
			   
				   this.baseScore += board.getScore() * 2;
		   }
	   }
	   
	   public  int getTileX() {
		   return tiles.getX();
	   }
	   
	   public  int getTileY() {
		   return tiles.getY();
	   }
	   /** The target cell for runTo */
	   private int targetx;   
	   /** The target cell for runTo */
	   private int targety;
	   /**
		* Called when the pointer is pressed.
		* Record the target for the pusher.
		*
		* @param x location in the Canvas
		* @param y location in the Canvas
		*/
	   protected void pointerPressed(int x, int y) {
	       if (y>(ScrHeight-35*ScrHeight/320)){
			  if (x<(ScrWidth/2-60*ScrHeight/320)){
				processKeyEvent(-6);
			  } else if(x>(ScrWidth/2+60*ScrHeight/320)){
				processKeyEvent(-7);
			  }
			  return;
		   }
		   
		   if (state != this.PLAYING)
			   return;
		   targetx = (x - offsetX) / TILE_WIDTH;
		   targety = (y - offsetY) / TILE_HEIGHT;
		   if(targetx>=0 && targetx < bwidth && targety>=0 && targety<bheight){
			   board.setMiddleX(targetx + 1);
			   board.setMiddleY(targety + 1);
			  
			   int stored = board.getStored();
			   board.move(board.FIRE);
	   
			   if (stored < board.getStored()) {
				   processBingo();
				
			   }
		   }
	   }
	   
	  void processKeyEvent(int key) {
		   /*if (state == PLAYING_SUCCESS ||
				   state == this.PLAYING_FAILURE) {
			   this.gotoNextLevel();
			   return;
		   }*/
		   
		   if(key == -6)
    	   {
    			destroyAudioPlayer();
    			theMidlet.destroyApp(false);
    			theMidlet.notifyDestroyed();
       		    return;
    		}
		   
			   
			if(key == -7)
			{
				changeState(this.NOT_STARTED);
				setupList();
				theMidlet.setCurrentDisplay(menuScreen);
				return;
			}
		   
			   
	  }
	  
	  void processBingo() {
		  if((theMidlet.valueMusic == llk.MUSIC_ON) && (squishPlayer != null)) {
			try {
				  //squishPlayer.setMediaTime(0);
				squishPlayer.start();
			} catch (MediaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		  }
		//add some time for user
		  totalGameTime += 1;
		   if (totalGameTime > GAME_TIME)
			   totalGameTime = GAME_TIME;
		  
	  }
	  
	/**
		* Handle a single key event.
		* The LEFT, RIGHT, UP, and DOWN keys are used to
		* move the pusher within the Board.
		* Other keys are ignored and have no effect.
		* Repaint the screen on every action key.
		*/
	   protected void keyPressed(int keyCode) {
	   
		   processKeyEvent(keyCode);
		   
		   if (state != this.PLAYING)
			   return;
		   synchronized (board) {
	   
			   int action = getGameAction(keyCode);
			   int move;
	   
			   switch (action) {
			   case Canvas.LEFT:
				   move = Board.LEFT;
	   
				   break;
	   
			   case Canvas.RIGHT:
				   move = Board.RIGHT;
	   
				   break;
	   
			   case Canvas.DOWN:
				   move = Board.DOWN;
	   
				   break;
	   
			   case Canvas.UP:
				   move = Board.UP;
	            
				   break;
	           case Canvas.FIRE:
			   	   move = Board.FIRE;
	
			   	   break;
				   // case 0: // Ignore keycode that don't map to actions.
			   default:
			   	
				   return;
			   }
	   
			   // Tell the board to move the piece
			   int stored = board.getStored();
			   board.move(move);
	   
			   if (stored < board.getStored()) {
				   processBingo();
			   }
	   
			  
		   } // End of synchronization on the Board.
	   }
	   
	    /**
	     * Destroy the audio player. Call this destroy the audio player.
	     */
	    public void destroyAudioPlayer() {
	        synchronized (this) {
	            if (musicPlayer != null) {
	            	musicPlayer.close();
	            	musicPlayer = null;
	            }

	            if (squishPlayer != null) {
	            	squishPlayer.close();
	            	squishPlayer = null;
	            }
	            
	        }
	    }
		
	    public void stopAudioPlayer() {
	        try  {
	            if (musicPlayer != null) {
	            	musicPlayer.stop();
	            }

	            if (squishPlayer != null) {
	            	squishPlayer.stop();
	            }
	            
	        } catch (Exception e) {
					e.printStackTrace();
			}
	    }
		Player	squishPlayer;
		Player	musicPlayer;

		
	    /**
	     * Create all player.
	     */
	    public void createAudioPlayer() {
	    	VolumeControl vc;
			try {
					
				InputStream is = getClass().getResourceAsStream("/beach.mid");
				musicPlayer = Manager.createPlayer(is, "audio/midi");
				musicPlayer.prefetch();
				vc = (VolumeControl)musicPlayer.getControl("VolumeControl");
   				if (vc != null)
      				vc.setLevel(20);
				
				is = getClass().getResourceAsStream("/fire.WAV");
				squishPlayer = Manager.createPlayer(is, "audio/x-wav");
				squishPlayer.prefetch();
				vc = (VolumeControl)squishPlayer.getControl("VolumeControl");
   				if (vc != null)
      	          vc.setLevel(100);
				
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
		}

		boolean should_hint = false;		
		public void commandAction(Command c, Displayable d) {
		
			if (c == exitCommand ) {
				destroyAudioPlayer();
				theMidlet.destroyApp(false);
				theMidlet.notifyDestroyed();
				
	        } else if (c == menuCommand) {
	        	changeState(this.NOT_STARTED);
				setupList();
				theMidlet.setCurrentDisplay(menuScreen);
	        } else if (c == List.SELECT_COMMAND) {
				switch(menuScreen.getSelectedIndex()){
					case 0://random
					
						changeState(PLAYING);
						theMidlet.setCurrentDisplay(this);
						randomTiles();					
						break;
					case 1://
						changeState(PLAYING);
						theMidlet.setCurrentDisplay(this);
						should_hint = true;
						//board.giveUsrHint();				
						break;
            
          case 2://resume game
            changeState(PLAYING);
            theMidlet.setCurrentDisplay(this);
            break;
					case 3:
						if(theMidlet.valueMusic == 1) {
							theMidlet.updateoption(0, theMidlet.keyMusic);
							stopAudioPlayer();
						} else {
							theMidlet.updateoption(1, theMidlet.keyMusic);
							try {
				   			   musicPlayer.setLoopCount(-1);
				   			   musicPlayer.start();
				   		   } catch (MediaException e) {
				   			 	e.printStackTrace();
				   		   }		
						}
						menuScreen.delete(3);
						//#style playCommand
						menuScreen.append((this.theMidlet.valueMusic == 1)?
						  Locale.get("Settings_Menu.music_on"):Locale.get("Settings_Menu.music_off"), null);
						break;
						
				}
			} else if (c == backCommand){
				changeState(PLAYING);
				theMidlet.setCurrentDisplay(this);

			}
		}
	   
	   private Command backCommand = new Command(Locale.get("General.Back"), Command.BACK, 30);
	   List menuScreen = null;
	   public void setupList()
	   {
          //#style playScreen
	      menuScreen = new List(Locale.get("Main_Menu.Title"), Choice.IMPLICIT);
		  //#style playCommand
		  menuScreen.append(Locale.get("Settings_Menu.random"), null);
		  //#style playCommand
		  menuScreen.append(Locale.get("Settings_Menu.hint"), null);
		  
		  //#style playCommand
     	  menuScreen.append(Locale.get("Main_Menu.Resume_Game"), null);
		  //#style playCommand
		  menuScreen.append((this.theMidlet.valueMusic == 1)?
		  	Locale.get("Settings_Menu.music_on"):Locale.get("Settings_Menu.music_off"), null);
		  menuScreen.addCommand(backCommand);
		  menuScreen.setCommandListener(this);
		  	
	    
	   }
	}
