/*
 * Created on 30-Jun-2006 at 23:58:19.
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
import de.enough.polish.util.Locale;
import java.util.Calendar;

import de.enough.polish.io.Serializable;

/**
 * <p>Represents a simple note.</p>
 *
 * <p>Copyright Enough Software 2006</p>
 * <pre>
 * history
 *        01-Jul-2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class Note implements Serializable {

	public String remark;
    
    public int sell, money;
    public String category;
	private final Date date;
	//private long time;
	
	public Note( int money, String remark,
	int sell, String c, Date d) {
		this.money = money;
		this.remark = remark;
        this.sell = sell;
        this.category = c;
        this.date = d;
		//this.time = System.currentTimeMillis();
	}

	public String getSimpleText() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
		String s = calendar.get(Calendar.YEAR)+"/"+
			       (calendar.get(Calendar.MONTH)+1)+"/"+
			       calendar.get(Calendar.DAY_OF_MONTH);
        String ss = sell==0 ?"-" :"+";
        return s+" "+category+" "+ ss +money+"ิช";
    }

    
    public String getSmallText() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
		String s = ""+calendar.get(Calendar.DAY_OF_MONTH);
        String ss = sell==0 ?"-" :"+";
        return s+"บล "+category+" " + ss + money+"ิช";
    }
	public String getText() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
		String s = calendar.get(Calendar.YEAR)+"/"+
			       (calendar.get(Calendar.MONTH)+1)+"/"+
			       calendar.get(Calendar.DAY_OF_MONTH);
       
		return (sell==0?Locale.get("noteform.categery_buy"):
            Locale.get("noteform.categery_sell")) + ": "+category+"\n"+
             Locale.get("noteform.date_title")
            +" "+ s+"\n"+
            Locale.get("noteform.money")
                +" "+ money+"ิช\n"+
           Locale.get("noteform.note")
                +" "+remark;
	}
    public Date getDate(){
        return date;
    }
}
