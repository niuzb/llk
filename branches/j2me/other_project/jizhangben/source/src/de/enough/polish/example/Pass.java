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

import de.enough.polish.io.Serializable;

/**
 * <p>Represent the password when you login.</p>
 *
 * <p>Copyright Enough Software 2006</p>
 * <pre>
 * history
 *        2010/6/20 - niuzb creation
 * </pre>
 * @author niuzb
 */
public class Pass implements Serializable {

	public  String category;
    public  String sellcategory;
	public  boolean save_passward = false;
	//private long time;
	
	public Pass(String s, String b) {
	    this.category = s;
		this.sellcategory = b;
	}
}

