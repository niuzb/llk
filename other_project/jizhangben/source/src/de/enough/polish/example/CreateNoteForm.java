//#condition polish.usePolishGui
/*
 * Created on 08-Mar-2006 at 01:32:28.
 * 
 * Copyright (c) 2006 Robert Virkus / Enough Software
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
import java.util.Date;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;


import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
import de.enough.polish.io.RmsStorage;
import de.enough.polish.ui.UiAccess;

import de.enough.polish.util.Locale;
import de.enough.polish.ui.*;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.Item;


/**
 * <p>Provides a form for creating a new note.</p>
 *
 * <p>Copyright Enough Software 2006</p>
 * <pre>
 * history
 *        30-June-2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class CreateNoteForm extends Form implements CommandListener,ItemStateListener{
	         


	private final Command okCommand = new Command( Locale.get("General.Save"), Command.OK, 1 );
	private final Command abortCommand = new Command( Locale.get("General.Back"), Command.BACK, 2 );
	private final Command addCategory = new Command( Locale.get("noteform.class_add"), Command.BACK, 3 );

    ChoiceGroup group;
    ChoiceGroup category;
    private final TextField money;
    //private final DateField field;
    private final TextField remark;
    DateField date;
	/**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public CreateNoteForm(String title ) {
		//#style createNoteForm
		super( title );
        
        //#style popupChoice
        group = new ChoiceGroup(Locale.get("noteform.categery_title"),
        ChoiceGroup.EXCLUSIVE);
        
        //#style choiceItem
        group.append( Locale.get("noteform.categery_buy"), null );
        //#style choiceItem
        group.append( Locale.get("noteform.categery_sell"), null );
        group.setSelectedIndex(0, true);
        group.setItemStateListener(this);
        append(group);
        //#style input
		date = new DateField( Locale.get( "noteform.date_title" ), DateField.DATE_TIME);
		date.setDate( new Date() );
		append(date);

        //#style popupChoice
		category = new ChoiceGroup(null, ChoiceGroup.POPUP);
        String Title = MenuMidlet.MIDLET.passward.category;
        int index;
        while ((index=Title.indexOf("-"))>0) {
             //#style choiceItem
			category.append(Title.substring(0,index), null);
            Title=Title.substring(index+1);
        }
		append(category);

        
        //#style hyperlink
        StringItem addclass = new StringItem( null, Locale.get("noteform.class_add"));       
        addclass.setDefaultCommand( this.addCategory);
        append(addclass);
        
		//#style input
		this.money = new TextField(Locale.get( "noteform.money" ), null, 6,TextField.NUMERIC);
		append(this.money);

		//#style input
		this.remark = new TextField( Locale.get("noteform.note" ), null, 50, TextField.ANY );
		append( this.remark);

        
        //addCommand(addCategory);
        addCommand(this.okCommand);
        addCommand(this.abortCommand);
        //addCommand(login);
        setCommandListener(this);
	}
	//#if 0
	public Note getNote() {
		return new Note( this.money.getString(), remark.getString(),);
	}
    
   
	public void clearText() {
		text.setString("");
	}
    
	public void setText(String s){
		text.setString(s);
	}
    
	public void setDate(Date d){
		date.setDate(d);
	}
    //#endif
    public void setNote(Note n) {
    //#if 0
		text.setString(n.text);
        user.setString(n.user);
    //#endif
	}

    
    public boolean fieldIsvalid() {
        //#if 0
        if(text.equals("") || 
            user.equals("") || 
          ) 
          return false;
        //#endif
        return true;
    }
	
	public void itemStateChanged(Item item) {
        
        int select1;
        select1 = this.group.getSelectedIndex();
        if (select1 < 0 ) {
            return;
        }
        category.clear();
        if(select1 == 0) { //buy
            String Title = MenuMidlet.MIDLET.passward.category;
            int index;
            while ((index=Title.indexOf("-"))>0) {
                //#style popupChoiceItem
                category.append(Title.substring(0,index), null);
                Title=Title.substring(index+1);
            }

        } else { //sell
            String Title = MenuMidlet.MIDLET.passward.sellcategory;
            int index;
            while ((index=Title.indexOf("-"))>0) {
                //#style popupChoiceItem
                category.append(Title.substring(0,index), null);
                Title=Title.substring(index+1);
            }
        }
	}
	public void commandAction(Command cmd, Displayable screen) 
    {
        if (cmd == this.okCommand) {
               //if (!createNoteForm.fieldIsvalid()) {
               //    showAlert("",Locale.get("General.sorry4")); 
               //}
               
   			//Note note = this.createNoteForm.getNote();
   			//this.createNoteForm.clearText();
            int select1, select2;
            select1 = this.group.getSelectedIndex();
            select2 = this.category.getSelectedIndex();
            if (select1 < 0 ) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("noteform.sorry1")); 
                return;
            }
   			if (select2 < 0) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("noteform.sorry2"));
                return;
            }
            if (money.getString().equals("")) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("noteform.sorry3"));
                return;
            }
            String ca = MenuMidlet.MIDLET.getCategory(select2,group.getSelectedIndex());
            if (ca == null) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("noteform.sorry5"));
                return;
            }
            //ok, save it
            Note n = new Note(Integer.parseInt(money.getString()), remark.getString(),
            select1, ca, date.getDate());
            if (n == null) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("noteform.sorry4"));
                return;
            }
   			MenuMidlet.MIDLET.saveNote(n);
        }
   	    else if (cmd == addCategory) {
           int s = this.group.getSelectedIndex();

           String title = s==0?Locale.get("category.title"):Locale.get("category.stitle" );
           MenuMidlet.MIDLET.display.setCurrent(new SetForm(title,s));
   	    } 
        else if (cmd == abortCommand) {
           MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
        }
    } 
}
