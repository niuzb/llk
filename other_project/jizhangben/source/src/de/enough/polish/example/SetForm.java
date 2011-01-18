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
import de.enough.polish.util.Locale;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;
import java.util.*;

import de.enough.polish.ui.StringItem;

import de.enough.polish.ui.ListItem;
import de.enough.polish.ui.FramedForm;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.*;
import de.enough.polish.util.*;



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
public class SetForm extends FramedForm implements CommandListener  {
	         
    private TextField addText;
    
    StringItem addButton;

    int currentCategory;
    String Title;//store the string we edit, eithor the buy list, or sell list
	Command quitCmd = new Command( Locale.get("General.ret"), Command.EXIT, 10 );
	Command login = new Command(Locale.get("category.cmd.add"), Command.SCREEN, 2);
	Command delete = new Command(Locale.get("General.Delete"), Command.SCREEN, 4);
	//Command nothing = new Command(Locale.get("General.OK"), Command.SCREEN, 6);

    /**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public SetForm(String title, int catogory) {
		//#style gradientFramedForm
		super(title);
        currentCategory = catogory;
        //#style input
        addText = new TextField( Locale.get( "category.addone"), 
        null, 30, TextField.ANY);
        append( Graphics.TOP, this.addText );  
        
        //#style searchbutton
        addButton = new StringItem( null, Locale.get("category.cmd.add"));       
        addButton.setDefaultCommand( this.login);
        append( Graphics.TOP, addButton);
        
        //append( Graphics.BOTTOM, new StringItem("",""));
        //category = new ListItem(Locale.get("category.list"));
        if(currentCategory == 0) {
            Title = MenuMidlet.MIDLET.passward.category;
        } else {
            Title = MenuMidlet.MIDLET.passward.sellcategory;
        }
        
        int index;
        StringItem item;
        while ((index=Title.indexOf("-"))>0) {
			//category.append(new StringItem("",Title.substring(0,index)), null);
			//#style string_list
			item = new StringItem("",Title.substring(0,index));
            item.setDefaultCommand( this.delete);
			append(item);
            Title=Title.substring(index+1);
        }
        //append(this.category);
        UiAccess.focus(this, addText);
        addCommand(quitCmd);
        addCommand(login);
        //addCommand(delete);
        setCommandListener(this);
        //addText.setString(""+size());
         //MenuMidlet.MIDLET.display.setCurrent(this);
	}
	
	
	public void commandAction(Command cmd, Displayable screen) 
    {
		if (cmd == login) {
            String s1 = addText.getString();
          
            if(s1.trim().equals("")== true) {
              MenuMidlet.MIDLET.showAlert("",Locale.get("category.sorry1")); 
              return;
            } 
            if(size() > 13) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("category.sorry4"));
                return;
            }
            int index;
            if(currentCategory == 0) {
                Title = MenuMidlet.MIDLET.passward.category;
            } else {
                Title = MenuMidlet.MIDLET.passward.sellcategory;
            }
            while ((index=Title.indexOf("-"))>0) {
                if (Title.substring(0,index).compareTo(s1) == 0 ) {
                    MenuMidlet.MIDLET.showAlert("",Locale.get("category.sorry2")); 
                    return;
                }
                Title=Title.substring(index+1);
            }
			//category.insert(0, new StringItem("",s1,null));
            {
              //save the user set passward
              //MenuMidlet.MIDLET.savePassward(s1, false);
              //MenuMidlet.MIDLET.showAlert("", Locale.get("General.setok")); 
            }
            //#style string_list
            StringItem item = new StringItem("",s1);
            item.setDefaultCommand(this.delete);
            insert(0,item);

            saveDate();
            
		} else if (cmd == delete) {
		    if(size() == 1) {
                MenuMidlet.MIDLET.showAlert("",Locale.get("category.sorry3"));
                return;
            }
         
		    //#if 0
		    for(int i = 0; i< size(); i++) {
                if(get(i) == getCurrentItem()) {
                    //#debug debug
			        System.out.println("#setform delte item"+ i);
                    this.delete(i);
                    saveDate();
                }
            }
            //#endif
		    this.delete(getCurrentIndex());
            saveDate();
            //MenuMidlet.MIDLET.setNoteFormAsCurrentDisplay();
        } else if (cmd == quitCmd) {
            //MenuMidlet.MIDLET.createNoteForm.category.clear();
            if(MenuMidlet.MIDLET.createNoteForm == null) {
                MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
                return;
            }
            ChoiceGroup cg = MenuMidlet.MIDLET.createNoteForm.category;
            cg.clear();
            
            if(currentCategory == 0) {
                Title = MenuMidlet.MIDLET.passward.category;
            } else {
                Title = MenuMidlet.MIDLET.passward.sellcategory;
            }
            int index;
            while ((index=Title.indexOf("-"))>0) {
                //#style popupChoiceItem
    			cg.append(Title.substring(0,index), null);
                Title=Title.substring(index+1);
            }
            MenuMidlet.MIDLET.setNoteFormAsCurrentDisplay();
        }
    } 

    private void saveDate() {
        String ca = ((StringItem)get(0)).getText().trim().replace('-','_')+"-";
        for(int i = 1; i< size(); i++) {
            ca = ca + ((StringItem)get(i)).getText().trim().replace('-','_')+"-";
        }
        MenuMidlet.MIDLET.savePassward(ca, this.currentCategory);
        
    }
}



/**
 * show a list of all state
 */
class FlowWaterForm extends Form implements CommandListener ,ItemStateListener {
	         
    private TextField addText;
    
    StringItem addButton;

    int currentCategory;
    String Title;//store the string we edit, eithor the buy list, or sell list
	Command quitCmd = new Command( Locale.get("General.ret"), Command.EXIT, 10 );
	Command login = new Command(Locale.get("category.cmd.add"), Command.SCREEN, 2);
	Command okCmd = new Command(Locale.get("General.Yes"), Command.SCREEN, 4);
	//Command nothing = new Command(Locale.get("General.OK"), Command.SCREEN, 6);
    ChoiceGroup year, month;
    String y;
    int m;
    int default_year_index=0;
    int default_month_index=0;
    
    private Calendar calendar;

    /**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public FlowWaterForm(String title) {
		//#style gradientFramedForm
		super(title);
        
        calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        //#style popupChoice
        year = new ChoiceGroup(Locale.get("ChartForm.year"),
        ChoiceGroup.POPUP);
        year.setItemStateListener(this);


        //#style popupChoice
        month = new ChoiceGroup(Locale.get("ChartForm.month"),
        ChoiceGroup.POPUP);
		

        Vector v = MenuMidlet.MIDLET.getAllYear();
        if (v == null || v.size() == 0) { 
            MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
            MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s1"));
            return;
        }
        String y1 = ""+calendar.get(Calendar.YEAR);
        int m1 = (calendar.get(Calendar.MONTH));
       

        
        int l = v.size();
        for (int j=0; j<l; j++) {
           String ss=(String)v.elementAt(j);
            if (y1.compareTo(ss) == 0)
                this.default_year_index=j;
           //#style choiceItem
           year.append(ss, null );
        }
        
        for (int j=1; j<13; j++) {
             if (m1 == j ) 
                this.default_month_index = j;
           //#style choiceItem
           month.append(j+"ิย", null );
        }
        year.setSelectedIndex(this.default_year_index, true);
        year.setItemStateListener(this);
        month.setSelectedIndex(this.default_month_index, true);
        month.setItemStateListener(this);
        append(year);
        append(month);
        //#style notesList
        list = new ListItem("");
        append(list);
        
        this.y = year.getString(default_year_index);
        this.m = (default_month_index);
        
        CreateList();
        addCommand(quitCmd);
        //addCommand(login);
        //addCommand(delete);
        setCommandListener(this);
        //addText.setString(""+size());
         //MenuMidlet.MIDLET.display.setCurrent(this);
	}
	ListItem list;
    long income , outcome ;
	/**
	 * create a list of all 
	 */
	private void CreateList() {
        
        boolean binggo = false;
        
        income = 0;
        outcome = 0;

        int s = size();
        if(s > 3) {
            for(int i = 3; i < s; i++)
                delete(3);
        }
        //deleteAll();
        list.removeAll();
        
        Vector notes = MenuMidlet.MIDLET.getNoteList();
        int size = notes.size();
       for (int i = 0; i < size; i++) {
             Note note = (Note) notes.elementAt(i);
             calendar.setTime(note.getDate());
		     String y1 = ""+calendar.get(Calendar.YEAR);
             int m1 =(calendar.get(Calendar.MONTH));
             if (y1.compareTo(y) == 0 && 
                m1 == (m)) {
                binggo = true;
                 if(note.sell == 0) {
                    outcome += (long)note.money;
                 } else {
                    income += (long)note.money;
                    
                 }
                appendItem(note.getSmallText());
             }
       }
       if(binggo == false)  {
            //#style roaditem
            StringItem s1 = new StringItem("",  Locale.get("flowwaterform.s1"));
            append(s1);
       } else {
            //#style roaditem
            StringItem s1 = new StringItem(Locale.get("ChartForm.allincome"),
            this.income + "ิช");
            append(s1); 
            //#style roaditem
            s2 = new StringItem(Locale.get("ChartForm.alloutcome"),
            this.outcome + "ิช");
            append(s2); 
       }
    }
    StringItem s1, s2;

    private void appendItem(String s) {
        StringItem si = new StringItem( null, s);
        //si.setDefaultCommand(this.okCmd);
        //#style notesItem
        list.append(si);
    }
	public void itemStateChanged(Item item) {
        
        int y,m;
        
        y = this.year.getSelectedIndex();
        m = month.getSelectedIndex();
        if (y < 0 || m < 0) {
            MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s2"));
            return;
        }
        if(m != this.default_month_index ||
            y != this.default_year_index) {
            default_month_index = m;
            default_year_index = y;
            this.y = year.getString(y);
            this.m = m;
            CreateList();
        }
	}


    
	public void commandAction(Command cmd, Displayable screen) 
    {
		if (cmd == login) {
            
            
		} else if (cmd == this.okCmd) {
		   
        } else if (cmd == quitCmd) {
            MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
            return;
        }
    } 

}


