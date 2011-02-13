package example.payment.jbricks;


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Form;
import de.enough.polish.util.Locale;

public class SucessDisplay implements CommandListener,ItemStateListener
{
    private static final int S1=30;
    private static final int S2=50;
    private static final int S3=15;
	private Form fmMain; // The main form
	private Command cmResume, cmExit; // Exit the form
	private Gauge  shield, gun_power, ship_speed; // Volume adjustment
	private Engine ower;    
    private EngineState state;
	private int score, score2;
    private Displayable dsplybl;
	public SucessDisplay (Engine ower)
	{
		this.ower = ower;
        state = new EngineState();
        ower.getState(state);

		score = state.score;
		score2 = score;


        addMenu();
        dsplybl = Main.DISPLAY.getCurrent(); 
		Main.DISPLAY.setCurrent(fmMain);
	}
	private int sh, gu, ship; 

	void addMenu() {
        String s = " " +ower.getCruuentLevel();
        s = Locale.get("SuccessDisplay.congratulation", s);
		//#style playScreen
		fmMain = new 
		    Form(s);
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.score")+ score);

		//#style gaugeHorizontalSpheres
		shield = new Gauge(Locale.get("SuccessDisplay.addrand"), 
		    true, 
		    10, 0);
		shield.setLabel(Locale.get("SuccessDisplay.addrand")+sh);
		fmMain.append(shield);

        //#style gaugeHorizontalSpheres
		gun_power = new Gauge(Locale.get("SuccessDisplay.addhint"), 
		    true, 
		    3, 0);
		gun_power.setLabel(Locale.get("SuccessDisplay.addhint")+gu);
		fmMain.append(gun_power);

        //#style gaugeHorizontalSpheres
		ship_speed = new Gauge(Locale.get("SuccessDisplay.speed"), 
		    true, 
		    10, 0);
		ship_speed.setLabel(Locale.get("SuccessDisplay.speed")+ship);
		fmMain.append(ship_speed);
        		
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.leftscore")+score);

        cmResume = new Command(Locale.get("Main_Menu.Resume_Game"), Command.OK, 
        2);
		fmMain.addCommand(cmResume);
        cmExit = new Command(Locale.get("General.Exit"), Command.EXIT, 1);
		fmMain.addCommand(cmExit);

        fmMain.setCommandListener(this);
		fmMain.setItemStateListener(this);
	}
    
	public void itemStateChanged(Item item) {
        
		sh = shield.getValue();
		gu = gun_power.getValue();
		ship = ship_speed.getValue();

		score = score2 - S1*sh - S2*gu - S3*ship;
				
		if ((item == shield) && (sh > 0)) {
			if(score < 0) {
				sh--;
				shield.setValue(sh);
			}
	    } else if ((item == gun_power) && (gu > 0)) {
			if(score<0){
				gu--;
				gun_power.setValue(gu);
			}
	    } else if ((item == ship_speed) && (ship > 0)) {
			if(score<0){
				ship--;
				ship_speed.setValue(ship);
			}
	    }
		score = score2 - S1*sh - S2*gu - S3*ship;
        
		shield.setLabel(Locale.get("SuccessDisplay.addrand")+sh);
		
		gun_power.setLabel(Locale.get("SuccessDisplay.addhint")+gu);
        
		ship_speed.setLabel(Locale.get("SuccessDisplay.speed")+ ship);

		fmMain.delete(4);
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.leftscore")+score);
	}

	
	public void commandAction(Command c, Displayable s)
	{
		if (c == cmResume)
		{
			Hero h= state.paddle;
            h.setBloodDeta(sh);
            h.setBulleTypeDeta(gu);
            h.setSpeedDeta(ship);
			
			ower.setScore(score);
            
			Main.DISPLAY.setCurrent(dsplybl);
            //ower.setState(Engine.PLAY);
            ower.nextLevel();
		} else if(c == cmExit) {
            ower.musicManager.destroyAudioPlayer();
            Main.MIDLET.destroyApp(false);
            Main.MIDLET.notifyDestroyed();
        }
	}
}

