

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Form;
import java.util.Vector;


public class FailureDisplay implements CommandListener
{
	private List menuScreen; // The main LIST
	private Command cmResume; // Exit the form
	GameScreen gs;

	
	public FailureDisplay (GameScreen gs)
	{
		this.gs = gs;

		//cmResume = new Command(Locale.get("Main_Menu.Resume_Game"), Command.OK, 1);
		// Create form, add commands, listen for events
		//#style mainScreen
		menuScreen = new List(Locale.get("Main_Menu.Title"), Choice.IMPLICIT);
		
		Vector items = new Vector();

		items.addElement(Locale.get("Main_Menu.Play_again"));
		//items.addElement(Locale.get("Main_Menu.Help"));
		//items.addElement(Locale.get("Main_Menu.About"));
		items.addElement(Locale.get("General.Exit"));
		
		String[] s = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
           //#style mainCommand
		   menuScreen.append((String) items.elementAt(i),null);
		}
		menuScreen.setTitle(Locale.get("Main_Menu.Settings"));
		
		menuScreen.setCommandListener(this);
		llk.MIDLET.setCurrentDisplay(menuScreen);
	}

	public void commandAction(Command command, Displayable displayable)
	{
	  if (command == List.SELECT_COMMAND)
      {
         String selected = menuScreen.getString(menuScreen.getSelectedIndex());
		 
         if (selected.equals(Locale.get("Main_Menu.Play_again")))
		 {
			llk.MIDLET.resumeGameScreen();
		 } else if	(selected.equals(Locale.get("General.Exit")))
		 {
			  llk.MIDLET.destroyApp(false);
			  llk.MIDLET.notifyDestroyed();
		 }
       
      }
	}
}


