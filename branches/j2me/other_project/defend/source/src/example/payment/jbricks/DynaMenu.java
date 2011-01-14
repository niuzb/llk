/**
 * Dynamically constructed menu class.
 */
package example.payment.jbricks;

import javax.microedition.lcdui.*;
import java.util.Vector;
import de.enough.polish.util.*;
import de.enough.polish.util.Locale;
import javax.microedition.lcdui.Choice;

public class DynaMenu  
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
   private Main midlet;
   public List menuScreen = null;
   private Rsm rsm;

   private Font font;
   Image item;
   int fontHeight;
   Object[] helpArray;
   int helpHeight;
   Object[] aboutArray;
   int aboutHeight;
   public DynaMenu()
   {
      //super("", Choice.IMPLICIT);
      
	  this.midlet = Main.MIDLET;
      if(item == null)
           item = Util.createImage("/item.png");
       items = new Vector();
       items.addElement(Locale.get("Main_Menu.New_Game"));
       items.addElement(Locale.get("Main_Menu.Settings"));
       items.addElement(Locale.get("Main_Menu.Help"));
       items.addElement(Locale.get("Main_Menu.About"));
       items.addElement(Locale.get("General.Exit"));
       engine = Engine.ME;
       
       font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
       fontHeight = font.getHeight();
       currentMenu = MAIN_MENU;
       rsm = engine.rsm;
       String value = Locale.get("General.Help");
      ArrayList lines = new ArrayList();
      wrap( value, font, painwidth, painwidth, lines, 100);
      helpArray = lines.toArray();
      helpHeight = fontHeight*helpArray.length;
      
      value = Locale.get("General.About");
      lines = new ArrayList();
      wrap( value, font, painwidth, painwidth, lines, 100);
      aboutArray = lines.toArray();
      aboutHeight = fontHeight*aboutArray.length;
      
      ok = Util.createImage("/ok.png");
      
      ret = Util.createImage("/ret.png");
      //goMainMenu();
      //setCommandListener(this);
   }

   int painwidth = 140;
   
   /**
    * Wraps the given string so that the substrings fit into the the given line-widths.
    * It is expected that the specified lineWidth >= firstLineWidth.
    * The resulting substrings will be added to the given ArrayList.
    * When the complete string fits into the first line, it will be added
    * to the list.
    * When the string needs to be splited to fit on the lines, it is tried to
    * split the string at a gab between words. When this is not possible, the
    * given string will be splitted in the middle of the corresponding word. 
    * 
    * 
    * @param value the string which should be splitted
    * @param font the font which is used to display the font
    * @param completeWidth the complete width of the given string for the specified font.
    * @param firstLineWidth the allowed width for the first line
    * @param lineWidth the allowed width for all other lines, lineWidth >= firstLineWidth
    * @param maxLines the maximum number of lines 
    * @param maxLinesAppendixPosition either MAXLINES_APPENDIX_POSITION_AFTER or MAXLINES_APPENDIX_POSITION_BEFORE
    * @param list the list to which the substrings will be added.
    */
   public static void wrap( String value, Font font, 
            int firstLineWidth, int lineWidth, 
           ArrayList list,
           int maxLines ) 
   {
       
       int lastLineIndex = maxLines - 1;
       char[] valueChars = value.toCharArray();
       int startPos = 0;
       int lastSpacePos = -1;
       int lastSpacePosLength = 0;
       int currentLineWidth = 0;
       for (int i = 0; i < valueChars.length; i++) {
           char c = valueChars[i];
           currentLineWidth += font.charWidth( c );
           if (c == '\n') {
               list.add( new String( valueChars, startPos, i - startPos ) );
               lastSpacePos = -1;
               startPos = i+1;
               currentLineWidth = 0;
               firstLineWidth = lineWidth; 
               i = startPos;
           } else if (currentLineWidth >= firstLineWidth && i > 0) {
               if(list.size() == lastLineIndex)
               {
                   // add the remainder of the value
                   list.add( new String( valueChars, startPos, valueChars.length - startPos ) );
                   break;
               }
               
               // need to create a line break:
               if (c == ' ' || c == '\t') { 
                   String line = new String( valueChars, startPos, i - startPos );
                   if (lastSpacePos != -1 && (font.stringWidth(line) > currentLineWidth) ) {
                       // adding the widths of characters does not always yield a correct result,
                       // so using stringWidth here ensures that we really break at the correct position:
                       if (i > startPos + 1) {
                           i--;
                       }
                       //System.out.println("value=" + value + ", i=" + i + ", startPos=" + startPos);
                       list.add( new String( valueChars, startPos, lastSpacePos - startPos ) );
                       startPos =  lastSpacePos;
                       currentLineWidth -= lastSpacePosLength;
                       lastSpacePos = i;
                       lastSpacePosLength = currentLineWidth;
                   } else {
                       //line += font.stringWidth(line) + "[" + currentLineWidth + "]";
                       list.add( line );
                       startPos =  ++i;
                       currentLineWidth = 0;
                       lastSpacePos = -1;
                   }
               } else if ( lastSpacePos == -1) {
                   /**/
                   //System.out.println("value=" + value + ", i=" + i + ", startPos=" + startPos);
                   list.add( new String( valueChars, startPos, i - startPos ) );
                   startPos =  i;
                   currentLineWidth = font.charWidth(valueChars[i]);
               } else {
                   currentLineWidth -= lastSpacePosLength;
                   String line = new String( valueChars, startPos, lastSpacePos - startPos );
                   //line += font.stringWidth(line) + "<" + lastSpacePosLength + ">";
                   list.add( line );
                   startPos =  lastSpacePos + 1;
                   lastSpacePos = -1;
               }
               
               firstLineWidth = lineWidth; 
           } else if (c == ' ' || c == '\t') {
               lastSpacePos = i;
               lastSpacePosLength = currentLineWidth;
           }
           
       } 
       
       if(list.size() != maxLines)
       {
           // add tail:
           list.add( new String( valueChars, startPos, valueChars.length - startPos ) );
       }
       
   }
   
   int activeChoice = 0;
   Vector items;
   
   int start = 85;
   Image ret;
   Image ok;
   int imageSize = 32;
   
   void paintMainMenu(Graphics g) {
    Font font1 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_LARGE);
    g.setFont(font1);
    int w = font1.stringWidth((String)items.elementAt(0));
    int anchor = (Screen.width-w)/2-20;
       for (int i = 0; i < items.size(); i++) {
            if(activeChoice ==  i) {
                g.drawImage(this.item, 
                anchor, start+30*i, Global.TL);
            }
            g.setColor(ThreeDColor.white.getRGB());
            g.drawString((String)items.elementAt(i), Screen.width/2, start+i*30, Screen.TH_ANCHOR);
       }
   
    g.drawImage(ok, Screen.width-imageSize,Screen.height-imageSize, Graphics.TOP|Graphics.LEFT);
   }

   void paintSettingMenu(Graphics g) {
      String s = (rsm.music == rsm.ON) ? Locale.get("Settings_Menu.music_on"):
                           Locale.get("Settings_Menu.music_off");
      g.drawString(s, 38, 160, Global.TL);
      g.drawImage(ret,0,Screen.height-imageSize, Graphics.TOP|Graphics.LEFT);
      g.drawImage(ok, Screen.width-imageSize,Screen.height-imageSize, Graphics.TOP|Graphics.LEFT);

   }

   int curHelpPos = start;
   int curAboutPos = start;
   void paintHelpMenu(Graphics g) {
      int y  = curHelpPos;
      g.setClip(30,start,160,paintheight);
      for (int i = 0; i < helpArray.length; i++) {
		String line = (String)helpArray[i];
		g.drawString( line, 38, y, Global.TL);
		y += fontHeight;
	  }
      g.setClip(0,0,Screen.width, Screen.height);
      
      g.drawImage(ret,0,Screen.height-imageSize, Graphics.TOP|Graphics.LEFT);
   }
   
   int paintheight = 117;

   void paintAboutMenu(Graphics g) {
         
      int y  = curAboutPos;
      g.setClip(30,start,160,paintheight);
      for (int i = 0; i < aboutArray.length; i++) {
		String line = (String)aboutArray[i];
		g.drawString( line, 38, y, Global.TL);
		y += fontHeight;
	  }
      g.setClip(0,0,Screen.width, Screen.height);
      
      g.drawImage(ret,0,Screen.height-imageSize, Graphics.TOP|Graphics.LEFT);
   }
   Image menubg = Util.createImage("/menu.png");
   
  // Image blueplain = Util.createImage("/blueplain.png");
    
   void paint(Graphics g) {
        g.setFont(font);
        g.setColor(ThreeDColor.black.getRGB());
        g.fillRect(0,0,Screen.width,Screen.height);
        g.drawImage(menubg,0,35,Global.TL);
        g.setColor(ThreeDColor.white.getRGB());
     //   g.drawImage(blueplain,(Screen.width-blueplain.getWidth())/2,
       //     80,Global.TL);
        switch(currentMenu) {
         case MAIN_MENU:
            paintMainMenu(g);
            break;
         case SETTINGS_MENU:
            paintSettingMenu(g);
            break;
         case HELP_MENU:
            paintHelpMenu(g);
            break;
         case ABOUT_MENU:
            paintAboutMenu(g);
            break;
         default:
            break;
        }
   }

  boolean ishelpAboutMenu(){
    return (currentMenu == HELP_MENU ||
       currentMenu == ABOUT_MENU);
  }
  void processOkcmd() {
    if (currentMenu == SETTINGS_MENU 
    ) {
     rsm.music=(rsm.music == rsm.ON)?rsm.OFF:rsm.ON;
    }
    if (currentMenu == MAIN_MENU ) {
      switch(activeChoice) {
       case 0: 
        engine.startGame(0);
        break;
       case 1:
           currentMenu = SETTINGS_MENU;
           break;
       case 2:
        currentMenu = HELP_MENU;
        break;
       case 3:
        currentMenu = ABOUT_MENU;
        break;
       case 4:
        midlet.destroyApp(false);
        midlet.notifyDestroyed();
        break;
       default:
        break;
       }              
    }
   
  }
  
   void keyPressed(int code, int gameaction) {
       switch(code) {
       case -6: //left back
         if (currentMenu == SETTINGS_MENU ||
    		 	currentMenu == HELP_MENU ||
    		 	currentMenu == ABOUT_MENU) {
            currentMenu = MAIN_MENU;
         }
        break;
       case -7://right
        if (currentMenu == SETTINGS_MENU) {
           rsm.music=(rsm.music == rsm.ON)?rsm.OFF:rsm.ON;
        } else if (currentMenu == MAIN_MENU) {
           processOkcmd();
        } 
       break;
     
       default:
        break;
       }
       
       switch(gameaction) {
      
       case Canvas.FIRE: //fire
        processOkcmd();
        break;
       case Canvas.UP: 
       {
        if(currentMenu == MAIN_MENU) {
            activeChoice--;
            if(activeChoice < 0) activeChoice = 4;
        } else if(currentMenu == HELP_MENU) {
            if(helpHeight < paintheight) return;
            if(curHelpPos<=83 && curHelpPos >= (paintheight-helpHeight)){
                curHelpPos+=2;
            }
        } else if(currentMenu == ABOUT_MENU){
            if(aboutHeight< paintheight) return;
            if(curAboutPos<=83 && curAboutPos >= (paintheight-aboutHeight)){
                curAboutPos+=2;
            }

        }
       }
        break;
       case Canvas.DOWN: 
        {
        if(currentMenu == MAIN_MENU) {
           activeChoice++;
            if(activeChoice > 4) activeChoice = 0;
        } else if(currentMenu == HELP_MENU){
            //System.out.println("curHelpPos:"+curHelpPos+"helpHeight:"+ helpHeight);
            if(helpHeight < paintheight) return;
            if(curHelpPos<=85 && curHelpPos >= (paintheight-helpHeight+2)){
                curHelpPos-=2;
            }
        } else if(currentMenu == ABOUT_MENU){
            if(aboutHeight< paintheight) return;
            if(curAboutPos<=85 && curAboutPos >= (paintheight-aboutHeight+2)){
                curAboutPos-=2;
            }

        }
       }
        
        break;
       default:
        break;
       }     
   }

   
   Engine engine;
   
   public void setEngine(Engine engine) {
       this.engine = engine;
   }

}
