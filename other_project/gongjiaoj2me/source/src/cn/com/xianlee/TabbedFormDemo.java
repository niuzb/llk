/*
 * Created on Nov 23, 2009 at 10:53:20 AM.
 * 
 * Copyright (c) 2009 zhibin niu / XianLe software
 *
 * This file is part of xian le workgroup.
 *
 * 
 * jiaotong is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * 
 */
package cn.com.xianlee;
import java.util.Vector;
import java.util.Stack;
import java.util.Enumeration;


import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.StringItem;

import de.enough.polish.ui.ImageItem;
import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.TableItem;
import de.enough.polish.ui.ScreenStateListener;
import de.enough.polish.ui.ItemStateListener;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.TabbedForm;
import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;
import de.enough.polish.ui.splash.InitializerSplashScreen;
import de.enough.polish.ui.splash.ApplicationInitializer;
import de.enough.polish.ui.Gauge;




/**
 * <p>provide a form for traffic search city</p>
 *
 * <p>Copyright Enough Software 2009</p>
 * <pre>
 * history
 *        Nov-04, 2009 - niuzb creation
 * </pre>
 * @author zhibin niu, niuzb1@gmail.com
 */
public class TabbedFormDemo 
extends MIDlet
implements ScreenStateListener, CommandListener, ApplicationInitializer, Runnable
{

	public static final int DO_NOTHING = 0; 		// nothing to do
	public static final int INTER_STATION_S = 1;   	// do intersation search
	public static final int ROAD_S = 2;	     		// do road search
	public static final int STATION_S = 3; 			// do station search
	public static final int ONE_ROAD_S = 4; 			// do station search

	int current_state=DO_NOTHING;
	private Command backCmd = new Command( Locale.get("cmd.back"), Command.BACK, 2 );
	private Command exitCmd = new Command( Locale.get("cmd.exit"), Command.BACK, 2 );
	private Command sss = new Command(Locale.get("cmd.sec"), Command.SCREEN, 2);
	private InitializerSplashScreen splashScreen;

	private TabbedForm tabbedForm;
	private int lastTabIndex;
	TextField t1,t2,t3,t4;
	//Image search;
	Image inter;
	Model m;
	Display display;
	List filterlist;
	private boolean running = false;
	Gauge 	busyIndicator;
	/**
	 * Creates a new MIDlet.
	 */
	public TabbedFormDemo() {
		super();
		initResource();
		m = new Model();
		display = Display.getDisplay( this );
		init_splash_screen();
		running = true;
		Thread t = new Thread(this);
	    t.start(); 
		//#debug
		System.out.println("Creating TabbedFormDemo MIDlet.");
	}

	  private void init_splash_screen(){
		  Image splashImage = null;
				 
		  splashImage = createImage( "/splash.png" );
		 
		  int backgroundColor =  0xebe8e0;
		  this.splashScreen = new InitializerSplashScreen( 
				  this.display, 
				  splashImage,
				  backgroundColor, 
				  null, // no message, so we proceed to the initial screen as soon as possible
				  0,	// since we have no message, there's no need to define a message color
				  this );
		  this.display.setCurrent( this.splashScreen );

	  }
	  Alert waitAlert;
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.splash.ApplicationInitializer#initApp()
	 */
	public Displayable initApp() {
		
		//#style filterPopupStyle
		filterlist = new List(Locale.get("General.choose"),Choice.IMPLICIT);
		
		filterlist.addCommand( this.backCmd );
		filterlist.setCommandListener(this);
		
		//#debug
		System.out.println("Starting FramedFormDemo MIDlet.");
		String[] headings = new String[]{
				Locale.get("title.introduction"), 
				Locale.get("title.road"), 
				Locale.get("title.station"),
				Locale.get("title.out")
		};
		//#style tabbedForm
		tabbedForm = new TabbedForm( Locale.get("title.main"), headings, null );
		
		initT1();
		initT2();
		initT3();
		tabbedForm.addCommand( this.exitCmd );
		tabbedForm.addCommand( this.sss );
		tabbedForm.setScreenStateListener( this );
		tabbedForm.setCommandListener( this );
		//#style .busyIndicator
		busyIndicator = new Gauge( null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING );
		
		//#style messageAlert
		waitAlert = new Alert( null, "查询..", null, AlertType.INFO );
		//waitAlert.setIndicator(busyIndicator);
		return tabbedForm;
	}

	
	public Image createImage(String filename) {
	   Image image = null;
	   try {
		   image = Image.createImage(filename);
	   } catch (Exception e) {
		   e.printStackTrace();
		   
	   } 	
	   return image;
	}

	  
	private void initResource() {
		inter = createImage("/inter.png");
		//search = createImage("/button.png");
	}

	ImageItem i1;
	//,iroad,istation;
	//StringItem iinterchage;

	
	private void initT1() {
		//#style input
		t1 = new TextField( Locale.get("label.start_station"), "", 30, TextField.ANY );
		UiAccess.setInputMode(t1, UiAccess.MODE_NATIVE);
		tabbedForm.append( 0, t1 );
		//#style imageItem
		i1 = new ImageItem( null, this.inter, Item.LAYOUT_RIGHT, "x", Item.PLAIN );
		tabbedForm.append( 0, i1 );
		
		//#style input
		 t2 = new TextField( Locale.get("label.end_station"), "", 30, TextField.ANY );
		UiAccess.setInputMode(t2, UiAccess.MODE_NATIVE);
		tabbedForm.append( 0, t2 );

		
		////#style imageItem
		//iinterchage = new ImageItem( null, this.search, Item.LAYOUT_CENTER, "search", Item.BUTTON );
		//#style searchbutton
		StringItem iinterchage = new StringItem( null, Locale.get("cmd.sec"));       

		iinterchage.setDefaultCommand( this.sss);
		tabbedForm.append( 0, iinterchage );
	}
	
	private void initT2() {
		//#style input
		 t3 = new TextField( Locale.get("label.road"), "", 30, TextField.ANY );
		
		UiAccess.setInputMode(t3, UiAccess.MODE_NATIVE);
		tabbedForm.append( 1, t3 );

		
		//#style searchbutton
		StringItem iinterchage = new StringItem( null, Locale.get("cmd.sec"));       

		iinterchage.setDefaultCommand( this.sss);

		tabbedForm.append( 1, iinterchage );
	}

	
	private void initT3() {
		//#style input
		 t4 = new TextField( Locale.get("label.station"), "", 30, TextField.ANY );
		UiAccess.setInputMode(t4, UiAccess.MODE_NATIVE);
		tabbedForm.append( 2, t4 );

		
		//#style searchbutton
		StringItem iinterchage = new StringItem( null, Locale.get("cmd.sec"));       

		iinterchage.setDefaultCommand( this.sss);

		tabbedForm.append( 2, iinterchage );
	}
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		//#debug
		System.out.println("TabbedFormDemo MIDlet is up and running.");
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		// ignore
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		// nothing to free
		running=false;
	}

	private static final int MS_PER_FRAME = 1000 / 30;
	private long cycleStartTime;
	private long lastCycleTime;
	public void run() {
		  // Loop handling events
		  while (running) {
			cycleStartTime = System.currentTimeMillis();
			switch(current_state) {
			case DO_NOTHING:
				Thread.yield();
				break;
				
			case INTER_STATION_S:
				processinterSearch(t1.getText(),t2.getText());
				break;
				
			case ROAD_S:
			   	processRoadSearch(t3.getText());
				break;
				
			case STATION_S:
			  	processStationSearch(t4.getText());
				break;	
				
			case ONE_ROAD_S:
				
				this.draw_road_table((String)v.elementAt(selectedItem),false);
				this.display.setCurrent(tabbedForm);
				this.tabbedForm.setActiveTab(3);
				
				
				break;	
			
			default:
				break;
			}
			this.current_state = this.DO_NOTHING;
			lastCycleTime = System.currentTimeMillis();
			  // sleep if we've finished our work early
			 
			if ((lastCycleTime - cycleStartTime) < MS_PER_FRAME) {
				 synchronized (this)
				 {
					try{
						wait(MS_PER_FRAME - (lastCycleTime - cycleStartTime));
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


	
	/**
	 * @param string
	 */
	private void showAlert(String title,String message) {
			//#style messageAlert
			Alert alert = new Alert( title, message, null, AlertType.INFO );
			alert.setTimeout( Alert.FOREVER );
			this.display.setCurrent( alert );
	}
	/*使当前屏幕上显示过滤LIST*/
	private void draw_filter_list(String s) {
		Enumeration seq=v.elements(); 
		//ChoiceItem ci;
		filterlist.deleteAll();
		while(seq.hasMoreElements()) {
			
			//ci =new ChoiceItem((String)seq.nextElement(), null, List.IMPLICIT);
			this.filterlist.append((String)seq.nextElement(), null);
			
		}
		//we don't use filterlist,have some problem
		//#if 0
		filterlist.setFilterText(s.trim());
		//#endif
		this.display.setCurrent( filterlist );
	}

	
	
	/*显示一条路的信息*/
	private void draw_road_table(String road,boolean append) {
		String st = "";
		Enumeration seq;
		if(!append){
			this.tabbedForm.deleteAll(3);
		}
		//because j2mepolish table item has some problem
		//we donot use tableitem anymore,instead,we use string item.
		//#if 0
		//#style defaultTable
		TableItem table = new TableItem(2, 3);
		
		//#style heading
		table.set( 0, 0, Locale.get("road.name"));
		//#style heading
		table.set( 0, 1, Locale.get("road.info"));
		//#style heading
		table.set( 0, 2, Locale.get("road.info1"));
		//#style centeredCell
		table.set( 1, 0, road);
		String[] ss= m.get_stations_for_one_road(road);
		if(ss!= null)
			st=ss[0];
		//#style centeredCell
		table.set( 1, 1, st);
		//get backward station
		st=ss[1];
		//#style centeredCell
		table.set( 1, 2,st);
		table.setLineStyle(TableItem.LINE_STYLE_SOLID, 0x000000 ); // set the line color to black 
		

		this.tabbedForm.append(3, table);
		//#endif

		
	
		String[] ss= m.get_stations_for_one_road(road);
		if(ss!= null)
			st=ss[0];
		//#style roaditem
		StringItem rr = new StringItem(road+Locale.get("road.info"), st);       
		this.tabbedForm.append(3, rr);
		
		st=ss[1];
		//#style roaditem
		rr = new StringItem(road+Locale.get("road.info1"), st);       
		this.tabbedForm.append(3, rr);
	}

	

	private boolean checkTextValid(String s){
		if(s.trim().length() == 0)
			return false;
		if(!validateString(s)){
			return false;
		}
		return true;
	}

	
	/*显示经过某一站点的线路信息*/
	private void draw_station_road_table(String road) {
		String st = "";
		Enumeration seq;

		Vector vv=m.get_stations_for_one_road_not_care_direction(road);
		if (vv == null) return;
	    seq=vv.elements(); 
	    // get station for forward 
		while(seq.hasMoreElements()) {
			if(st.trim().equals("")){
				st = (String)seq.nextElement();
			} else {
				st = st + "-" + (String)seq.nextElement();
			}
		}
		//#style roaditem
		StringItem rr = new StringItem(road, st);       
		this.tabbedForm.append(3, rr);
	}
	
	/*存储从数据库中获得的路线信息*/
	Vector v;
	private void processRoadSearch(String s) {

        
		if (!validateString(s) ){
			this.display.setCurrent( tabbedForm);
			showAlert("",Locale.get("General.serarchnothing"));
			return;
		}
		//this.display.setCurrent( waitAlert);
		v = m.getSuggestRoads(s);
		//this.display.setCurrent( tabbedForm);
		if(v.size() > 1){
			draw_filter_list(s);
		} else if (v.size() == 1){
			draw_road_table((String)v.elementAt(0),false);
			this.tabbedForm.setActiveTab(3);
			//this.display.setCurrent(tabbedForm);
		} else {
			//no road match show alert
			showAlert("",Locale.get("General.serarchnothing"));
		}
		
	}

	/**
	 * @param s the station name
	 */
	private void processStationSearch(String s) {
		Vector mm;
		
		s=s.trim();
        if (!validateString(s)){
			this.display.setCurrent( tabbedForm);
			showAlert("",Locale.get("General.serarchnothing"));
			return;
		}
		//this.display.setCurrent( waitAlert);
		mm = m.get_road_from_station_key(s);
		
		if ((mm.size()==0)){
			//this.display.setCurrent( tabbedForm);
			showAlert("",Locale.get("General.serarchnothing"));
			return;
		}
	    Enumeration seq=mm.elements(); 
		String road;
		int first=1;

		String[] parameter=new String[]{s,String.valueOf(mm.size())};
		//#style introText1
		StringItem introText1 = new StringItem( null, Locale.get("label.stationresult",parameter));       
		
		tabbedForm.deleteAll(3);
        this.tabbedForm.setActiveTab(3);
		tabbedForm.append(3,introText1);
	    // get station for forward 
		while(seq.hasMoreElements()) {
			road = (String)seq.nextElement();
			draw_station_road_table(road);
		}
		//this.display.setCurrent( tabbedForm);
		
		return;
	}
    private boolean validateString(String s) {
		if(s.trim().equals("")) 
			return false;
		if(s.indexOf('?') != -1) {
            return false;
        }
        return true;
    }


	/**
	 * @param sa the first station name
	 * @param sb the second station name
	 */
	private void processinterSearch(String sa, String sb) {
		Vector[] vv;
		//this.display.setCurrent( waitAlert);
		vv = m.get_road_for_inter_station(sa, sb);
		

		
		if (vv == null){
			//this.display.setCurrent( tabbedForm);
			showAlert("",Locale.get("General.serarchnothing"));
			return;
		}
		
	    Enumeration seq=vv[2].elements(); 
		Enumeration seq1 = vv[0].elements(); 
		Enumeration seq2 = vv[1].elements(); 
		Enumeration firststationseq = vv[3].elements(); 
		Enumeration laststationseq = vv[4].elements(); 
		String road;
		String road2;
		String middleStation;
		String firstStation;//起始站
		String lastStation;
		String translation;
		String[] parameter=new String[]{sa.trim(),sb.trim(),String.valueOf(vv[0].size())};
		 translation = Locale.get("label.interstationresult",parameter);
		//#style introText1
		StringItem introText1 = new StringItem( null, translation);       
		
		tabbedForm.deleteAll(3);
		tabbedForm.append(3,introText1);
		
	    // get station for forward 
		while(seq.hasMoreElements()) {
			middleStation = (String)seq.nextElement();
			if(middleStation.compareTo("same") == 0){
			    //two station has in one road
			    
			    road = (String)seq1.nextElement();
				//#style introText2
				StringItem introText2 = new StringItem( null, 
				Locale.get("label.interoneroad2",road));	
				
				tabbedForm.append(3,introText2);
			    draw_road_table(road,true);
				//this.display.setCurrent( tabbedForm);
				this.tabbedForm.setActiveTab(3);
				return;
			} 
			//shoule print two road
			{
				road = (String)seq1.nextElement();
				road2 = (String)seq2.nextElement();
				firstStation = (String)firststationseq.nextElement();
				lastStation=(String)laststationseq.nextElement();
			}
			{
				parameter=new String[]{firstStation.trim(),road.trim()};
				translation = Locale.get("label.interoneroad",parameter);
                translation = translation.replace('?', ' ');
                //#style introText2
				StringItem introText = new StringItem( null, 
				translation);	
				
				tabbedForm.append(3,introText);
			}
			//draw_road_table(road,true);
			{
				
				parameter=new String[]{middleStation,road2,
					lastStation};
				translation = Locale.get("label.intertworoad1",parameter);
                translation = translation.replace('?', ' ');
                //#style introText
				StringItem introText = new StringItem( null, translation);	
				
				tabbedForm.append(3,introText);
			}
			//draw_road_table(road2,true);
		}
		this.display.setCurrent( tabbedForm);
		this.tabbedForm.setActiveTab(3);
	}

	int selectedItem;
	public void commandAction(Command cmd, Displayable disp) {
		if(disp == this.tabbedForm) {
			if(cmd == this.sss) {
				switch(this.tabbedForm.getActiveTab()) {
				
				case 0://search for interchange station
					String m1,m2;
					m1 = t1.getText().trim();
					m2 = t2.getText().trim();
					if (!checkTextValid(t1.getText()) ||
						!checkTextValid(t2.getText()) ||
						(m1.indexOf(m2) != -1) ||
						(m2.indexOf(m1) != -1)) {
						showAlert("", Locale.get("label.inputvalid"));
						return;
					}
					this.current_state = this.INTER_STATION_S;
					break;
				case 1://search for one road
					if (!checkTextValid(t3.getText()) || t3.getText().trim().equals("路")) {
						showAlert("", Locale.get("label.inputvalid"));
						return;
					}
					
					this.current_state = this.ROAD_S;
					break;
				case 2://search for one station
					if (!checkTextValid(t4.getText())) {
						showAlert("", Locale.get("label.inputvalid"));
						return;
					}
					
					this.current_state = this.STATION_S;
					break;
				}
				
			}else if(cmd == this.exitCmd){
				notifyDestroyed();
			} 
			
		} else if (disp == this.filterlist) {
		    //#debug
			System.out.println(cmd.getLabel());
			if (cmd == List.SELECT_COMMAND ) {		
				int si = this.filterlist.getSelectedIndex();
				if(si == -1) {
					//#debug
					System.out.println("***noting****");
				} else  {
					//process road info
					//#debug
					System.out.println("***info type 0****");
					this.selectedItem = si;
					this.current_state = ONE_ROAD_S;
				} 
				
			} else if (cmd == backCmd) {
				this.display.setCurrent( tabbedForm );
			}
		}
	}

	public void screenStateChanged(Screen screen) {
		
		if (screen == this.tabbedForm ) {
			int tabIndex = this.tabbedForm.getActiveTab();
			if (tabIndex != this.lastTabIndex ) {
				if (tabIndex == 3) {
					this.tabbedForm.removeCommand( this.sss );
				} else if (tabIndex != 3 && this.lastTabIndex == 3){
					this.tabbedForm.addCommand( this.sss );
				}
				this.lastTabIndex = tabIndex;
			}
		}	
		
	}



}
