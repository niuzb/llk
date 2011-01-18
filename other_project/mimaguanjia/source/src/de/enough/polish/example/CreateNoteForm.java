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
public class CreateNoteForm extends Form  {
	         
	private final TextField text;
    //private final DateField field;
    private final TextField user, passward;
    private TextField  remark;
	/**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public CreateNoteForm(String title ) {
		//#style createNoteForm
		super( title );
//#if 0
		//#style input
		field = new DateField( Locale.get( "General.Time" ), DateField.DATE_TIME);
		field.setDate( new Date() );
		append( field );
//#endif

		//#style input
		this.text = new TextField( Locale.get( "General.Input" ), null, 40,TextField.ANY );
		append( this.text );

        
		//#style input
		this.user = new TextField( Locale.get( "General.User" ), null, 40, TextField.ANY );
		append( this.user );
		//#style input
		this.passward = new TextField( Locale.get( "General.Pass" ), null, 40, TextField.ANY );
		append( this.passward );
        //#style input
		this.remark = new TextField( Locale.get( "General.Remark" ), null, 60, TextField.ANY );
		append( this.remark );
	}
	
	public Note getNote() {
		return new Note( this.text.getString(), user.getString(), 
		passward.getString(),remark.getString());
	}
    
	public void clearText(){
		text.setString("");
	}
//#if 0	
	public void setText(String s){
		text.setString(s);
	}
	public void setDate(Date d){
		field.setDate(d);
	}
//#endif
    public void setNote(Note n) {
		text.setString(n.text);
        user.setString(n.user);
        passward.setString(n.passward);
        remark.setString(n.remark);
	}
    public boolean fieldIsvalid() {
       
        return true;
    }
}
