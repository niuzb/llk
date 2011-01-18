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
public class SetForm extends Form  implements CommandListener  {
	         
    private TextField p1, p2 ;
     private TextField p3;
    StringItem iinterchage;

    
	Command quitCmd = new Command( Locale.get("General.ret"), Command.EXIT, 10 );
	Command login = new Command(Locale.get("Main_Menu.login"), Command.SCREEN, 2);

    /**
	 * Creates a new form for writing an email.
	 * 
	 * @param title the title of the frame
	 */
	public SetForm(String title ) {
		//#style createNoteForm
		super( title );
        
            
        //first login
        //#style input
		this.p1 = new TextField( Locale.get( "setform.set2" ), 
		null, 30, TextField.PASSWORD );
        //#style input
        this.p2 = new TextField( Locale.get( "setform.set3" ), 
		null, 30, TextField.PASSWORD );
        //#style input
		this.p3 = new TextField( Locale.get( "setform.set1" ), 
		null, 30, TextField.PASSWORD );
        append( this.p3 );  
		append( this.p1 );
        append( this.p2 );
          
        
        //#style searchbutton
        iinterchage = new StringItem( null, Locale.get("Main_Menu.login"));       
        
        iinterchage.setDefaultCommand( this.login);
        append(iinterchage);
        addCommand(quitCmd);
        addCommand(login);
        setCommandListener(this);
	}
	
	
	public void commandAction(Command cmd, Displayable screen) 
    {
		if (cmd == login) {
            String s1 = p1.getString();
            String s2 = p2.getString();
            String s3 = p3.getString();
            
            if(s3.equals("") || 
                s3.compareTo(MenuMidlet.MIDLET.passward.passward) != 0) {
              MenuMidlet.MIDLET.showAlert("",Locale.get("General.sorry2")); 
              return;
            }
             if(s1.compareTo(s3) == 0) {
            
              MenuMidlet.MIDLET.showAlert("",Locale.get("General.sorry3")); 
              return;
            } 
            if(s1.equals("") || s1.compareTo(s2) != 0) {
            
              MenuMidlet.MIDLET.showAlert("",Locale.get("General.sorry")); 
              return;
            } 
            {
              //save the user set passward
              MenuMidlet.MIDLET.savePassward(s1, false);
              MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
              MenuMidlet.MIDLET.showAlert("", Locale.get("General.setok")); 
            }
                
            
		} else if (cmd == quitCmd) {
            MenuMidlet.MIDLET.setMenuAsCurrentDisplay();
        }
    } 
}


