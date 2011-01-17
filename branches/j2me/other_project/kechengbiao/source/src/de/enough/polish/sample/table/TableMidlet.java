package de.enough.polish.sample.table;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.ui.ChartItem;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.TableItem;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.backgrounds.SimpleBackground;
import de.enough.polish.util.TableData;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
import de.enough.polish.io.RmsStorage;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.StringItem;
import java.util.Vector;
import java.util.Enumeration;

public class TableMidlet 
extends MIDlet
implements CommandListener
{
	
	
	private Form form;
	private Command cmdExit = new Command( Locale.get("General.Exit"), 
        Command.EXIT, 3 );
    
	private Command cmdAddrow = new Command( Locale.get("Main_Menu.addrow"), 
	Command.ITEM, 4 );
	private Command cmdDeleterow = new Command( Locale.get("Main_Menu.deleterow"), 
        Command.ITEM, 5 );

    private Command cmdEdititem = new Command( Locale.get("Main_Menu.edititem"), 
        Command.ITEM, 4 );

	private Display display;

	public TableMidlet() {
		//#style mainScreen
		this.form = new Form( Locale.get("Application.Title"));
		this.form.addCommand( this.cmdExit );
		this.form.addCommand( this.cmdAddrow );	
        this.form.addCommand( this.cmdDeleterow );	
		this.form.setCommandListener( this );
        this.display = Display.getDisplay( this );
        this.storage = new RmsStorage();
        initDate();
        try {
      	  demoInteractiveTableElements();
        } catch (Exception e) {
          e.printStackTrace();
            //#debug error
      	  System.out.println("Unable to initialize table completely");
        }
          this.display.setCurrent( this.form );
	}

     protected void startApp() throws MIDletStateChangeException {
       
          
     }

     protected void pauseApp(){
          // ignore
          save_rsm();
     }

     private void save_rsm() {
          this.courseList = "";
          for (int j = 1; j< row+1; j++) 
              for (int i = 0; i< 8; i++) {
                 TextField tf = (TextField)table.get(i, j);
                 //if null, fill a #
                 System.out.println(tf.getString());
                 if(tf.getString().trim().length() == 0) {
                     if(i == 0 && j == 1) {
                         courseList="#";

                     } else {
                     courseList = courseList + "-"+"#";
                     }
                 } else {
                     if(i == 0 && j == 1) {
                         courseList=tf.getString().
                         trim().replace('-', '_');
                     } else {
                     courseList = courseList + "-" + tf.getString().
                         trim().replace('-', '_');
                     }
                 }
          }
          courseList = courseList + "-";
          System.out.println(courseList);
          try {
             
             note = new Note(courseList, this.row);
             this.storage.save( this.note, "kcb_xianle");
         } catch (IOException e) {
             // storage does not yet exist
         }

     }
     //when we exit, save all data in RSM
     protected void destroyApp(boolean unconditional) throws MIDletStateChangeException{

        save_rsm();
     }

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable disp) {
        save_rsm();
        if (cmd == this.cmdExit) {
			notifyDestroyed();
		} else if (cmd == this.cmdAddrow) {
            this.row += 1;
            demoInteractiveTableElements();
        } else if (cmd == this.cmdDeleterow) {
            if(row > 0) {
                row--;
                demoInteractiveTableElements();
                save_rsm();
            }
        }
        
       
	}



    /**
     * draw the table
     */
	private void demoInteractiveTableElements()
	{
		this.form.deleteAll();
        createTable();
		this.form.append( table );
	}

    private boolean firstCreate=false;
	private  Note note;
	private RmsStorage storage;
    private String courseList = "";
    private int row = 7;
    private void initDate() {
		try {
			note = (Note) this.storage.read("kcb_xianle");
			
		} catch (IOException e) {
            System.out.println("***note does not exest");
			note = new Note("", 7);
            firstCreate = true;
		}
        courseList = note.keMu;
        row = note.row;
    }

    
    TableItem table;
    
    Vector  courses = new Vector();
    private void createTable() {
        //#style defaultTable
		table = new EditTable(8, row+1);//int columns, int rows
//#if 0		
		int index;
		String x;
		Vector  down_road = new Vector();

		while((index=stations.indexOf("-"))>0) { 
			x = stations.substring(0,index);
			down_road.addElement(x);
			stations=stations.substring(index+1);

		}
//#endif

        String Title =  Locale.get("General.rowtitle");
        int index;
        for (int i = 1; i< 8; i++) {
            if ((index=Title.indexOf("-"))>0) {
                //#style centeredCell
                table.set( i, 0, Title.substring(0,index));
                Title=Title.substring(index+1);
            }
        }

        if(firstCreate) {
            Title =  Locale.get("General.coltitle");
            
            for (int i = 1; i< row+1; i++) {
                if ((index=Title.indexOf("-"))>0) {
                    //#style centeredCell
                    //table.set( 0, i, Title.substring(0,index));
                    table.set( 0, i, new 
                    TextField(null,Title.substring(0,index),20, TextField.ANY));
                    Title=Title.substring(index+1);
                } else {
                    table.set( 0, i,  new TextField(null, "", 
                               20, TextField.ANY));
                }
            }
        }
        
        String cc = this.courseList;
        int startCol=firstCreate?1:0;
        
        for (int j = 1; j< row+1; j++) 
            for (int i = startCol; i< 8; i++) {
                
            if ((index=cc.indexOf("-")) > 0) {
                //table.set( i, j, cc.substring(0,index));
                //courses.addElement(cc.substring(0,index));
                 String content = cc.substring(0,index);
                 if(content.equals("#")) {
                    content="";
                 }
                //#style textinput
                 TextField tf = new TextField( null,content, 
                20, TextField.ANY);
                  table.set( i, j, tf);
                //courses.addElement(tf);
                cc=cc.substring(index+1);
            } else {
                //#style textinput
                TextField tf = new TextField(null, "", 
                               20, TextField.ANY);
                table.set( i, j, tf);
                //courses.addElement(tf);
            }
           
        }
        if(firstCreate) {
            save_rsm();
            firstCreate = false;
        }
        table.setSelectionMode( TableItem.SELECTION_MODE_CELL | TableItem.
        SELECTION_MODE_INTERACTIVE );
    }

}
