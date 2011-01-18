/*
 * Created on 26-June-2007 at 16:14:27.
 * 
 * Copyright (c) 2007 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */
package de.enough.polish.example;
import java.io.IOException;
import java.util.Vector;
import java.util.Calendar;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
import de.enough.polish.io.RmsStorage;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.StringItem;
import de.enough.polish.midp.ui.Form;


//#ifdef polish.debugEnabled
import de.enough.polish.util.Debug;
//#endif
	
/**
 * <p>Shows a demonstration of the possibilities of J2ME Polish.</p>
 *
 * <p>Copyright Enough Software 2007</p>

 * <pre>
 * history
 *        26-June-2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class MenuMidlet extends MIDlet implements CommandListener {
	
	List menuScreen;
	//Command startGameCmd = new Command( Locale.get( "cmd.StartGame" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("General.Exit"), Command.EXIT, 10 );
	//#ifdef polish.debugEnabled
		Command showLogCmd = new Command( Locale.get("cmd.ShowLog"), Command.ITEM, 9 );
	//#endif
	
	private final Command okCommand = new Command( Locale.get("General.Save"), Command.OK, 1 );
	private final Command abortCommand = new Command( Locale.get("General.Back"), Command.BACK, 2 );
	private final Command deleteCommand = new Command( Locale.get("General.Delete"), Command.ITEM, 3 );
	private final Command editCommand = new Command( Locale.get("General.edit"), Command.ITEM, 3 );

	
	private List notesList;
	private CreateNoteForm createNoteForm;
	private CreateNoteForm editNoteForm;
	private Display display;
	private  Vector notes;
	private RmsStorage storage;
	private Calendar calendar;

	public MenuMidlet() {
		super();
		//#debug
		System.out.println("starting MenuMidlet");
			String title = Locale.get( "Main_Menu.Title" );
		//#style mainScreen
		this.menuScreen = new List(title, List.IMPLICIT);
		//#style mainCommand
		this.menuScreen.append( Locale.get( "Main_Menu.new_note" ), null);
		//#style mainCommand
		this.menuScreen.append(Locale.get( "Main_Menu.see_note" ), null);
		//#style mainCommand
		this.menuScreen.append(Locale.get( "Main_Menu.Help" ), null);
		//
		//this.menuScreen.append(Locale.get( "Main_Menu.About" ), null);
		
		this.menuScreen.setCommandListener(this);
		//this.menuScreen.addCommand( this.startGameCmd ); 
		this.menuScreen.addCommand( this.quitCmd );
		//#ifdef polish.debugEnabled
			this.menuScreen.addCommand( this.showLogCmd );
		//#endif
		calendar=Calendar.getInstance();
		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
		initNoteStore();
		//#debug
		System.out.println("initialisation done.");
	}

    protected void initNoteStore() {
		
		//#style notesList
		this.notesList = new List(Locale.get( "Main_Menu.see_note" ), List.IMPLICIT );
		this.notesList.setCommandListener( this );
		this.notesList.addCommand( this.abortCommand );
		this.notesList.addCommand( this.deleteCommand );
		this.notesList.addCommand( this.editCommand);
		// restore notes from record store:
		this.storage = new RmsStorage();
		Vector vector;
		try {
			vector = (Vector) this.storage.read("notes");
			// populate list:
			int size = vector.size();
			for (int i = 0; i < size; i++) {
				Note note = (Note) vector.elementAt(i);
				appendNoteToList(note);
			}
			if (size != 0) {
				UiAccess.focus( this.notesList, size-1 );
			}
		} catch (IOException e) {
			// storage does not yet exist
			vector = new Vector();
		}
		this.notes = vector;				

	}
	protected void startApp() throws MIDletStateChangeException {
		//#debug
		System.out.println("setting display.");
		this.display = Display.getDisplay(this);
		this.display.setCurrent( this.menuScreen );
		DeviceControl.lightOn();
		//DeviceControl.vibrate(20);
		//#debug
		System.out.println("sample application is up and running.");
	}

	protected void pauseApp() {
		DeviceControl.lightOff();
	}
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		try {
			this.storage.save( this.notes, "notes");
		} catch (IOException e) {
			//#debug error
			System.out.println("Unable to store notes" + e );
		}
		
	}
	
	public void commandAction(Command cmd, Displayable screen) {		
		if (screen == this.menuScreen) {
			//#ifdef polish.debugEnabled
				if (cmd == this.showLogCmd ) {
					Debug.showLog(this.display);
					return;
				}
			//#endif
			if (cmd == List.SELECT_COMMAND) {
				int selectedItem = this.menuScreen.getSelectedIndex();
				if (selectedItem == 0) { //quit has been selected
					NewNote();
				} else if(selectedItem == 1) {
					seeNote();
				} else if(selectedItem == 2) {
					showAlert( Locale.get( "Main_Menu.Help" ),Locale.get( "General.Help" ) );
				} else if(selectedItem == 3) {
					showAlert( Locale.get( "Main_Menu.About" ),Locale.get( "General.About" ) );
				} 
			} 
		} 
		else if(screen == this.createNoteForm ){
			if (cmd == this.okCommand) {
				Note note = this.createNoteForm.getNote();
				if(note.getText().length()==0){
                   return;
				}
				//this.createNoteForm.clearText();
				
				
				this.notes.addElement( note );
				appendNoteToList(note);
				//UiAccess.focus( this.createNoteForm, 0 );
				UiAccess.focus( this.notesList, this.notesList.size() - 1 );
				
				//#style mailAlert
				Alert alert = new Alert( "", Locale.get( "General.Save_OK" ), null, AlertType.INFO );
				alert.setTimeout( 800 );
				this.display.setCurrent( alert );
				this.createNoteForm = null;
				this.display.setCurrent( this.notesList );
			} else {
			    //this.createNoteForm = null;
				//#debug
				System.out.println("exit from create note form");
				this.display.setCurrent( this.menuScreen );
			}
		} 
		else if ( screen == this.notesList ) {
			if (cmd == this.abortCommand ) {
				//#debug
				System.out.println("exit from notesList");
				this.display.setCurrent( this.menuScreen );
			} else if (cmd == this.deleteCommand) { 
				int selectedIndex = this.notesList.getSelectedIndex();
				if (selectedIndex != -1) {
					this.notes.removeElementAt(selectedIndex);
					this.notesList.delete(selectedIndex);
				}
			}  else if (cmd == this.editCommand) { 
			    //#debug
				System.out.println("edit command");
				int selectedIndex = this.notesList.getSelectedIndex();
				if (selectedIndex != -1) {
					//#debug
				    System.out.println("index"+selectedIndex);
					itemIndexToBeEdit = selectedIndex;
					//this.notes.removeElementAt(itemIndexToBeEdit);
					//this.notesList.delete(itemIndexToBeEdit);
					editItem();
				}
			} else if (cmd == List.SELECT_COMMAND) { 
				int selectedIndex = this.notesList.getSelectedIndex();
				if (selectedIndex != -1) {
					Note note = (Note) this.notes.elementAt(selectedIndex);
				    showAlert("",getNoteString(note));
				}
			}
		}
		else if(screen == this.editNoteForm){
			if (cmd == this.okCommand) {
				Note note = this.editNoteForm.getNote();
				this.editNoteForm.clearText();

				this.notes.removeElementAt(itemIndexToBeEdit);
			    this.notesList.delete(itemIndexToBeEdit);
				this.notes.insertElementAt( note, itemIndexToBeEdit);
				//appendNoteToList(note);
				//#style notesItem
				notesList.insert(itemIndexToBeEdit,getNoteString(note),null);
				UiAccess.focus( this.notesList, itemIndexToBeEdit );
				
				
				this.display.setCurrent( notesList );
				//this.createNoteForm = null;
				//this.display.setCurrent( this.menuScreen );
			} 
		} 
		if (cmd == this.quitCmd) {
			quit();
		}
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

	private String getNoteString(Note n){
		calendar.setTime(n.getDate());
		String s = calendar.get(Calendar.YEAR)+"/"+
			       calendar.get(Calendar.MONTH)+"/"+
			       calendar.get(Calendar.DAY_OF_MONTH)+" "+
			       calendar.get(Calendar.HOUR_OF_DAY)+":"+
			       calendar.get(Calendar.MINUTE);
		
        return s+"\n"+n.getText();
	}
    private void appendNoteToList(Note n) {
		//#style notesItem
		this.notesList.append(getNoteString(n)
		,null);
	}
	private void NewNote() {
		CreateNoteForm form = new CreateNoteForm( Locale.get( "Main_Menu.new_note" ));
		form.setCommandListener( this );
		form.addCommand( this.okCommand );
		form.addCommand( this.abortCommand );
		this.createNoteForm = form;
		this.display.setCurrent( form );
	}
	
	private void seeNote() {
	    this.display.setCurrent( this.notesList );
	}
	private void quit() {
		try {
			destroyApp(false);
			notifyDestroyed();				
		} catch (MIDletStateChangeException e) {
			//#debug error
			System.out.println("Unable to quit app" + e );
		}
	}
	int itemIndexToBeEdit;
	private void editItem() {
		//#debug error
			System.out.println("editItem");
		if(editNoteForm == null) {
			editNoteForm = new CreateNoteForm( Locale.get( "Settings_Menu.edit" ));
			editNoteForm.addCommand( this.okCommand );
			editNoteForm.setCommandListener( this );
		}
		Note note = (Note) notes.elementAt(itemIndexToBeEdit);
	 	editNoteForm.setDate(note.getDate());
		//#debug error
			System.out.println("text is"+note.getText());
		this.editNoteForm.setText(note.getText());
	    this.display.setCurrent( editNoteForm );
	}
	
}
