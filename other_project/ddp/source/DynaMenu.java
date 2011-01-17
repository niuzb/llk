/**
 * Dynamically constructed menu class.
 */

import javax.microedition.lcdui.*;
import java.util.Vector;
import de.enough.polish.util.Debug;
import de.enough.polish.util.Locale;
import javax.microedition.lcdui.Choice;

public class DynaMenu  implements CommandListener
{
   private static final int MAIN_MENU = 100;
   private static final int SETTINGS_MENU = 110;
   private static final int MUSIC_ON_MENU = 111;
   private static final int VIBRATE_TOGGLE_MENU = 112;
   private static final int STARFIELD_TOGGLE_MENU = 113;
   private static final int HELP_MENU = 120;
   
   private static final int ABOUT_MENU = 121;
   private static final int RIGHTKEY_MENU = 122;
   private static final int FIREKEY_MENU = 123;
   private static final int CONFIRM_NEW_GAME_MENU = 130;

   private Command back;
   private int currentMenu;
   private llk midlet;
   public List menuScreen = null;
   public DynaMenu(llk midlet)
   {
      //super("", Choice.IMPLICIT);
      
	  this.midlet=midlet;
      goMainMenu();
      //setCommandListener(this);
   }

   public void goMainMenu()
   {
      //#style mainScreen 
	  menuScreen = new List(Locale.get("Main_Menu.Title"), Choice.IMPLICIT);

      Vector items = new Vector();
      items.addElement(Locale.get("Main_Menu.New_Game"));
	  if (midlet.hasAGameRunning() || midlet.valueLevel > 1)
        items.addElement(Locale.get("Main_Menu.Resume_Game"));
      
      items.addElement(Locale.get("Main_Menu.Settings"));
      //items.addElement(Locale.get("Main_Menu.Change_Keys"));
      items.addElement(Locale.get("Main_Menu.Help"));
      items.addElement(Locale.get("Main_Menu.About"));
	  items.addElement(Locale.get("General.Exit"));

      String[] s = new String[items.size()];
      for (int i = 0; i < items.size(); i++)
         s[i] = (String) items.elementAt(i);

      setupMenu(Locale.get("Main_Menu.Title"), s,
                null);
	  menuScreen.setCommandListener(this);

      currentMenu = MAIN_MENU;
	  midlet.setCurrentDisplay(menuScreen);
   }

   public void goSettingsMenu()
   {
      setupMenu(Locale.get("Settings_Menu.Title"),
                new String[]
                {
                   (midlet.valueMusic == midlet.MUSIC_ON)?Locale.get("Settings_Menu.music_on"):
				   	Locale.get("Settings_Menu.music_off")
				   	/* delete by niuzb for no useful*/
                   //Locale.get("Settings_Menu.Shake"),
                   //Locale.get("Settings_Menu.Star_field")
                },
                Locale.get("General.Back"));
      currentMenu = SETTINGS_MENU;
   }
   
    public Displayable getDisplay() {
        return menuScreen;
    }

   public void goToggleMenu(String title, int menuId, boolean on)
   {
      setupMenu(title,
                new String[]
                {
                   Locale.get("General.On"),
                   Locale.get("General.Off")
                }, null);
      if (on)
         menuScreen.setSelectedIndex(0, true);
      else
         menuScreen.setSelectedIndex(1, true);

      currentMenu = menuId;
   }

   public void goYesNoMenu(String title, int menuId, boolean on)
   {
      setupMenu(title,
                new String[]
                {
                   Locale.get("General.Yes"),
                   Locale.get("General.No")
                }, null);
      if (on)
         menuScreen.setSelectedIndex(0, true);
      else
         menuScreen.setSelectedIndex(1, true);

      currentMenu = menuId;
   }


   public void setupMenu(String title, String[] choices, String backCommandName)
   {
      // clear the current list & command
      int s = menuScreen.size();
      for (int i = 0; i < s; i++)
         menuScreen.delete(0);
	  if (back != null) {
		  menuScreen.removeCommand(back);
		  back = null;
	  }
      //menuScreen.removeCommand(back);
	  menuScreen.setTitle(title);

      // add the new choices
      for (int i = 0; i < choices.length; i++){
	     //#style mainCommand
		 menuScreen.append(choices[i], null);

	  }

      // add the back command, if they wanted one
      if (backCommandName != null)
      {
         back = new Command(backCommandName, Command.BACK, 1);
         menuScreen.addCommand(back);
      }
   }
   
   Form fm;
   Command back1;
   public void setupForm(String title, String content, String backCommandName)
   {
      if (fm == null) {
	  	//#style mainScreen 
      	fm = new Form(title);
	  } else {
	    if(back1 != null) {
		    fm.removeCommand(back1);
			back1 = null;
		}
		fm.setTitle(title);
	  }
	  fm.deleteAll();
	  //menuScreen.removeCommand(back);
	  //#style mainCommand
      fm.append(content);
      // add the back command, if they wanted one
      if (backCommandName != null)
      {
         back1 = new Command(backCommandName, Command.BACK, 1);
         fm.addCommand(back1);
      }
	  fm.setCommandListener(this);
	  //System.out.println("setupForm_2");
	  midlet.setCurrentDisplay(fm);
	  //System.out.println("setupForm_3");
   }

   public void commandAction(Command command, Displayable displayable)
   {
      if ((command == back) ||(command == back1))
      {
         if (currentMenu == SETTINGS_MENU ||
		 	currentMenu == HELP_MENU ||
		 	currentMenu == ABOUT_MENU) goMainMenu();
      }

      if (command == List.SELECT_COMMAND)
      {
         String selected = menuScreen.getString(menuScreen.getSelectedIndex());
		 switch (currentMenu)
         {
            case MAIN_MENU:
               if (selected.equals(Locale.get("Main_Menu.New_Game")))
               {
                  // if they already have a game running the confirm that
                  // they want to create a new game
                  if (midlet.hasAGameRunning())
                  {
                     goYesNoMenu(Locale.get("General.Are_you_sure"), CONFIRM_NEW_GAME_MENU, false);
                  }
                  else
                     midlet.createNewGame();
               } else if (selected.equals(Locale.get("Main_Menu.Resume_Game")))
               {
                  if (midlet.hasAGameRunning())
                     midlet.resumeGameScreen();
                  else
                     midlet.loadGame();
               } else if (selected.equals(Locale.get("Main_Menu.Settings")))
               {
                  goSettingsMenu();
			   } else if (selected.equals(Locale.get("Main_Menu.Help")))
               {
				   //System.out.println("help**");
				   currentMenu = HELP_MENU;
                   setupForm(Locale.get("Main_Menu.Help"),
				  	Locale.get("General.Help"),
				  	Locale.get("General.Back"));
               }else if  (selected.equals(Locale.get("Main_Menu.About")))
               {
					currentMenu = ABOUT_MENU;
				  setupForm(Locale.get("Main_Menu.About"),
				  	Locale.get("General.About"),
				  	Locale.get("General.Back"));
               } else if  (selected.equals(Locale.get("General.Exit")))
               {
               		midlet.destroyApp(false);
					midlet.notifyDestroyed();
               }
               break;

            case CONFIRM_NEW_GAME_MENU:
               if (getBool()) {
                  midlet.createNewGame();
			   } else {
					goMainMenu();
			   }
               break;

               // SETTINGS...
            case SETTINGS_MENU:
				/*
               if (selected.equals(Locale.get("Settings_Menu.music_on")))
                  goToggleMenu(Locale.get("Settings_Menu.music_on"), MUSIC_ON_MENU, true);
				*/
				
				midlet.updateoption((midlet.valueMusic == midlet.MUSIC_ON)?midlet.MUSIC_OFF:
				midlet.MUSIC_ON, midlet.keyMusic);
				goSettingsMenu();
               break;

           

         }
      }

   }

   public  boolean getBool()
   {
     return (menuScreen.getSelectedIndex() == 0);
      
   }

   public static final int getKeyNum(String s)
   {
      String n = s.substring(4, 5);
      return Integer.parseInt(n);
   }
}
