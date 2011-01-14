
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Hashtable;

import javax.microedition.rms.*;

import java.io.*;
import java.util.*;
import java.lang.*;




/**
 * The application class for the game llk.
 */

public class llk extends MIDlet 
{
   protected Display display;
   private DynaMenu mainMenuScreen;
   private GameScreen gameScreen=null; 

   private static final String RSMName = "llk_xianle";

   public RecordStoreManager rsm;
   public static llk MIDLET;
   Hashtable ht;

   Enumeration em;
   public llk() {
	   super();
	   MIDLET = this;
	   rsm = RecordStoreManager.getInstance();
	   rsm.CreateRecordStore(RSMName);
	   try {
		   	ht = rsm.getRecordPairs_key_value(RSMName);
		   em = ht.keys();
		   while (em.hasMoreElements()) {
				String s = (String)em.nextElement();
				if (s.equals(keyLevel)){
					valueLevel = Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyMusic)) {
					valueMusic = Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyScore)) {
					valueScore= Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyrand)) {
					rand = Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyhint)) {
					hint = Integer.parseInt((String)ht.get(s));
				} 
		   } 
	   }catch (Exception e){
			e.printStackTrace();
	   }
   }
   boolean show_splash = false;
   public void startApp() throws MIDletStateChangeException {
      try {
	      display = Display.getDisplay(this);
		  //style mainScreen
	      //mainMenuScreen = new DynaMenu(this);

      } catch(Exception ex) {
    	 ex.printStackTrace();
      }
      if (!show_splash) {
	  	show_splash = true;
      	display.setCurrent(new Splash(this));
		
	  } else {
	    if (gameScreen !=null) {
			gameScreen.changeState(GameScreen.PLAYING);
			display.setCurrent(gameScreen);
			gameScreen.start_bg_music();
		}
		
	  }
   }

  
   public Display getDisplay() {
	   return display;
   }
   
   public boolean hasAGameRunning() {
		return (gameScreen != null);
   }
   
   public void pauseApp() {
   	 if (gameScreen !=null) {
     	gameScreen.changeState(GameScreen.NOT_STARTED);
	    gameScreen.stopAudioPlayer();
     }
   }

   public void destroyApp(boolean unconditional) {
    	 System.gc();
		 
         //notifyDestroyed();
	   
   }


   /*创建新的游戏界面,*/
   public void createNewGame() {
	   
	   gameScreen =  new GameScreen(this);
	   gameScreen.start();
	   display.setCurrent(gameScreen); 
   }
   
   /*显示游戏界面*/
   public void resumeGameScreen() {
   		
		gameScreen.rondomTime =rand;
		gameScreen.hintTime =hint;
		
		gameScreen.baseScore = valueScore;
		//llk.MIDLET.setCurrentDisplay(gs);
		gameScreen.changeState(gameScreen.PLAYING);
		gameScreen.gotoNextLevel(valueLevel);
		display.setCurrent(gameScreen); 
   }

   /*从RSM中读取游戏当前关数*/
   public void loadGame() {
    	gameScreen =  new GameScreen(this);
    
    	gameScreen.rondomTime =rand;
    	gameScreen.hintTime =hint;
    
    	gameScreen.baseScore = valueScore;
		gameScreen.start();
	    display.setCurrent(gameScreen); 
   }
   
   //下面是保存在RSM中数据对应的关键字，保存格式为关键字：数据
   String keyMusic="music";
   String keyLevel="level";
   String keyScore="score";
   String keyrand="rand";
   String keyhint="hint";
   public static final int MUSIC_ON = 1;
   public static final int MUSIC_OFF =0;
   public int valueMusic=1;
   public int valueLevel=1;
   public int valueScore=0;
   public int rand=3, hint = 3;
   
   /*保存音乐开或者关的状态到RSM
    * param: data 1代表音乐开，0代表音乐关 
    */
   public void updateoption(int data, String key) {	
   
        //valueMusic = data;
		
		if (key.equals(keyLevel)){
			valueLevel = data;
		} else if (key.equals(keyMusic)) {
			valueMusic = data;
		} else if (key.equals(keyScore)) {
			valueScore= data;
		} else if (key.equals(keyrand)) {
			rand = data;
		} else if (key.equals(keyhint)) {
			hint = data;
		} 
		try {
        	String[] s = rsm.getRecordById(RSMName, key);
		
			if (s==null) {
				rsm.createRecord(RSMName,key + ":" + data);
			} else {
	        	rsm.updateRecord(RSMName, key + ":" + data, Integer.parseInt(s[0]));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return;
   }

   
	public void setCurrentDisplay(Displayable nextDisplayable) {
		display.setCurrent(nextDisplayable); 
	}
    public void createNewMenu(){
		mainMenuScreen = new DynaMenu(this);
		setCurrentDisplay(mainMenuScreen.menuScreen);
	}
	
   /*显示主目录*/
	public void mainMenuScreenShow() {
	    mainMenuScreen.goMainMenu();
		//setCurrentDisplay(mainMenuScreen.menuScreen);

	}

	
}




