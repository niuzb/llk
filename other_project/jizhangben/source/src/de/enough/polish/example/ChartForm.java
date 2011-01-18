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
import java.lang.Integer;

import java.util.Date;
import java.util.Vector;
import java.util.Calendar;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;
import de.enough.polish.util.Locale;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.ListItem;
import de.enough.polish.ui.Form;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.ChartItem;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.DateField;

import de.enough.polish.util.*;
import de.enough.polish.ui.*;



/**
 * <p>Provides a form for show chart</p>
 *
 * <p>Copyright Enough Software 2006</p>
 * <pre>
 * history
 *        30-June-2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class ChartForm extends Form implements CommandListener  {
	         
	private Command exitCommand = new Command( Locale.get("General.ret"), Command.BACK, 10 );
	private Command okCommand = new Command( Locale.get("General.Yes"), Command.OK, 10 );
    
    private Command styleLines = new Command(Locale.get("ChartForm.line"), Command.SCREEN, 1 );  
    private Command styleVerticalBar = new Command(Locale.get("ChartForm.bar"), Command.SCREEN
    , 1 );  
    private Command stylePieChart = new Command(Locale.get("ChartForm.pie"), Command.SCREEN
    , 1 );

    //Command nothing = new Command(Locale.get("General.OK"), Command.SCREEN, 6);
    ChoiceGroup year, month;
    String y, m;
    
	private Calendar calendar, before, after;
    DateField firstDate, lastDate;
    Date d1, d2;
    /**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public ChartForm(String title) {
		//#style gradientFramedForm
		super(Locale.get("ChartForm.title"));

        calendar=Calendar.getInstance();
        before=Calendar.getInstance();
        after=Calendar.getInstance();
//#if 0
        //#style popupChoice
        year = new ChoiceGroup(Locale.get("ChartForm.year"),
        ChoiceGroup.POPUP);


        //#style popupChoice
        month = new ChoiceGroup(Locale.get("ChartForm.month"),
        ChoiceGroup.POPUP);
		

        Vector v = MenuMidlet.MIDLET.getAllYear();
        if (v == null || v.size() == 0) { 
            MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s1"));
            return;
        }

        
        int l = v.size();
        for (int j=0; j<l; j++){
           String ss=(String)v.elementAt(j);
            //#style choiceItem
           year.append(ss, null );
        }

        for (int j=1; j<13; j++){
           //#style choiceItem
           month.append(""+j, null );
        }
        append(year);
        append(month);
  //#endif
          Date d=new Date();
          //#debug error
          System.out.println("now time"+ d.getTime()+" "+30*24*3600000);
          long time = d.getTime()-30L*24L*3600000L; //a moth earlier
          //#debug error
          System.out.println("eli time"+ time);
         //#style input
		firstDate = new DateField( Locale.get( "ChartForm.firstdate" ), DateField.DATE_TIME);
		firstDate.setDate(new Date(time));
		append(firstDate);
         //#style input
		lastDate = new DateField( Locale.get( "ChartForm.lastdate" ), DateField.DATE_TIME);
		lastDate.setDate( d );
		append(lastDate);
        //#style searchbutton
        StringItem addButton = new StringItem( null, Locale.get("General.Yes"));       
        addButton.setDefaultCommand( this.okCommand);
        append(addButton);
        
     
        UiAccess.focus(this, year);
        addCommand(exitCommand);
        addCommand(okCommand);
        setCommandListener(this);
	}
	
	
	public void commandAction(Command cmd, Displayable screen) 
    {
		if (cmd == okCommand) {
            //#if 0
            int i= year.getSelectedIndex();
            int j = month.getSelectedIndex();
            if (i < 0 || j < 0) {
                MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s2"));
                return;
            }

            y = year.getString(i);
            m = month.getString(j);
            //#endif
            d1 = this.firstDate.getDate();
            d2 = this.lastDate.getDate();
            before.setTime(d1);
            after.setTime(d2);
            if (before.after(after)) {
                MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s2"));
                return;
            }
            removeCommand(okCommand);
            showChart();
            
		} else if (cmd == this.styleLines) {
			//#style lineChart
			updateChart();			
		} else if (cmd == this.styleVerticalBar) {
			//#style verticalBarChart
			updateChart();
		} else if (cmd == this.stylePieChart) {
			//#style pieChart
			updateChart();		
		} else if (cmd == exitCommand) {
           
            MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
        }
    } 

    private void showChart() {
        
        if (initDate() < 0) {
            return;
        }
        //#if 0
        Command parent = new Command( Locale.get("ChartForm.choose"), Command.SCREEN, 2);
		addCommand( parent);
        UiAccess.addSubCommand(this.styleLines, parent, this );
		UiAccess.addSubCommand(this.styleVerticalBar, parent, this );
		UiAccess.addSubCommand(this.stylePieChart, parent, this );
        //#endif
        
        //#style pieChart
		updateChart();
    }

    int[][] buySequences;
    int[][] sellSequences;
    String bs, ss;
    HashMap sparameters = new HashMap();
    HashMap bparameters = new HashMap();

    
    private int initDate() {
        int ret = 0;
        Vector notes = MenuMidlet.MIDLET.getNoteList();
        int size = notes.size();
        buySequences = null;
        sellSequences = null;

        sparameters.clear();
        bparameters.clear();
        for (int i = 0; i < size; i++) {
             Note note = (Note) notes.elementAt(i);
             calendar.setTime(note.getDate());
             //#if 0
		     String y1 = ""+calendar.get(Calendar.YEAR);
             String m1 = ""+(calendar.get(Calendar.MONTH)+1);
             if (y1.compareTo(y) == 0 && 
                m1.compareTo(m) == 0) 
              //#endif
              if(calendar.after(before) &&
                calendar.before(after)){
                if(note.sell == 0) {//buy
                    if(!bparameters.containsKey(note.category)) {
                        bparameters.put((Object)note.category, new Integer(note.money));
                        int oldvalue = ((Integer)(bparameters.get((Object)note.category)))
                                                    .intValue();
                        //#debug error
                        System.out.println("add outcome value:"+ oldvalue);

                    } else {
                        int oldvalue = ((Integer)(bparameters.get((Object)note.category)))
                            .intValue();
                        //#debug error
                        System.out.println("old value:"+ oldvalue);
                        bparameters.put((Object)note.category, new Integer(note.money+oldvalue
                            ));
                        int newvalue = ((Integer)(bparameters.get((Object)note.category)))
                            .intValue();
                        //#debug error
                        System.out.println("new value:"+ newvalue);
                    }
                } else if(note.sell == 1) {//sell
                    if(!sparameters.containsKey(note.category)) {
                        sparameters.put((Object)note.category, new Integer(note.money));
                        int oldvalue = ((Integer)(sparameters.get((Object)note.category)))
                                                   .intValue();
                       //#debug error
                       System.out.println("*add in value:"+ oldvalue);

                    } else {
                        int oldvalue = ((Integer)(sparameters.get((Object)note.category)))
                            .intValue();
                        //#debug error
                        System.out.println("*old value:"+ oldvalue);
                        sparameters.put((Object)note.category, new Integer(note.money+oldvalue
                            ));
                        int newvalue = ((Integer)(sparameters.get((Object)note.category)))
                            .intValue();
                        //#debug error
                        System.out.println("*new value:"+ newvalue);
                    }
                }
             }
            
       }//end for

       
       if(bparameters.size() <= 0 &&
            sparameters.size() <= 0) {
            MenuMidlet.MIDLET.showAlert("", Locale.get("ChartForm.s3"));
            return -1;
       }
       
       //buySequences = new int[bparameters.keys().length][];
       btotal = 0;

       buySequences = new int[1][];
       buySequences[0] = new int[bparameters.keys().length];
       Object[] keys = this.bparameters.keys();
       for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            Object value = this.bparameters.get(key);
            btotal += ((Integer)value).intValue();
            buySequences[0][i]= ((Integer)value).intValue();
       }
       stotal= 0;
       sellSequences = new int[1][];
       sellSequences[0] = new int[sparameters.keys().length];
       keys = this.sparameters.keys();
       for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            Object value = this.sparameters.get(key);
            stotal += ((Integer)value).intValue();
            sellSequences[0][i]=((Integer)value).intValue();
            //#debug error
           System.out.println("sellSequences[0][i]"+ sellSequences[0][i]);
       }

       return 0;
    }
    long btotal, stotal;
    
    int[] colors = new int[]{ 0xFF0000, 0x00FF00, 0x0000FF, 0x660033, 0x009999, 
        0xFFCC33, 0x00CC99, 0xFFFF33, 0x996600,
        0xFF6633, 0xbbCC99, 0xF55F33, 0x476600};

    private void addbuy( Style style) {
        if (bparameters.size() > 0) { 
             //#debug error
            System.out.println("xxxbparameters"+buySequences.length);
            //#style titleitem
             StringItem ss = new StringItem("", Locale.get("ChartForm.outcome"));
             append(ss);
             //#style roaditem
             StringItem all = new StringItem(Locale.get("ChartForm.alloutcome"), 
             btotal+"Ԫ");
             append(all);
            if(bparameters.size() > 1) {
            ChartItem chart = new ChartItem("", 
                buySequences, colors, style );
            append( chart );
            }
            Object[] keys = this.bparameters.keys();
            TableItem table = null;
            long total = 0;
            for (int i = 0; i < keys.length; i++) {
               Object key = keys[i];
               Object value = this.bparameters.get(key);
               //#if 0
               if(bparameters.size() > 1) {
                append(new GradientItem( colors[i%colors.length] ));
               }
               StringBuffer buffer = new StringBuffer();
               buffer.append(key);
               buffer.append('\t');
               buffer.append(value.toString()+"Ԫ  ");
               buffer.append(Locale.get("ChartForm.zhanbi"));
               buffer.append(((Integer)value).intValue()*100/btotal+"%");
               //#style roaditem
               StringItem si = new StringItem("", buffer.toString());
               append(si);
               //#endif
               
               if(i%3 == 0) {
                    //#style defaultTable
                   table = new TableItem(4, 3);
                   append(table);
               }
               table.set( 0, i%3, new GradientItem( colors[i%colors.length] ));
               table.set( 1, i%3,key.toString());
               table.set( 2, i%3,value.toString()+"Ԫ");
               long zhanbi = (long)((Integer)value).intValue()*100/btotal;
               if (i != keys.length-1) {
                    total += zhanbi;
               } else {
                    zhanbi = 100-total;
               }
               table.set( 3, i%3, (String.valueOf(zhanbi)+"%"));
           }
        }

    }

    private void addSell( Style style) {
        if (sparameters.size() > 0) { 
           //#debug error
           System.out.println("xxxsparameters");
            //#style titleitem
             StringItem ss = new StringItem("", Locale.get("ChartForm.income"));
             append(ss);
              //#style roaditem
             StringItem all = new StringItem(Locale.get("ChartForm.allincome"), 
             stotal+"Ԫ");
             append(all);
           if (sparameters.size() > 1) {
           ChartItem chart = new ChartItem("", 
               sellSequences, colors, style );
           append( chart );
           }
           Object[] keys = this.sparameters.keys();
      
            TableItem table =null;
            long total = 0;
           for (int i = 0; i < keys.length; i++) {
               Object key = keys[i];
               Object value = this.sparameters.get(key);
               //#if 0
               if (sparameters.size() > 1) {
                append(new GradientItem( colors[i%colors.length] ));

               } 
               StringBuffer buffer = new StringBuffer();
               buffer.append(key);
               buffer.append('\t');
               buffer.append(value.toString()+"Ԫ  ");
               buffer.append(Locale.get("ChartForm.zhanbi"));
               buffer.append(((Integer)value).intValue()*100/stotal+"%");
               //#style roaditem
               StringItem si = new StringItem("", buffer.toString());
               append(si);
               //#endif
               if(i%3 == 0) {
                    //#style defaultTable
                   table = new TableItem(4, 3);
                   append(table);
               }
               table.set( 0, i%3, new GradientItem( colors[i%colors.length] ));
               table.set( 1, i%3, new StringItem("",key.toString()));
               table.set( 2, i%3, new StringItem("",value.toString()+"Ԫ"));
               long zhanbi = (long)((Integer)value).intValue()*100/stotal;
               if (i != keys.length-1) {
                    total += zhanbi;
               } else {
                    zhanbi = 100-total;
               }
               table.set( 3, i%3, (String.valueOf(zhanbi)+"%"));
          
          }
      }

    }
    private void updateChart( Style style ) {
        deleteAll();
        addbuy(style);
        addSell(style);
    }
}



