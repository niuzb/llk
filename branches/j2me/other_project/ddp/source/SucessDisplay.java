
import java.lang.Thread;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import de.enough.polish.util.Locale;


public class SucessDisplay implements CommandListener
{
	private Form fmMain; // The main form
	private Command cmResume; // Exit the form
	private Gauge  gaRond, gaHint; // Volume adjustment
	GameScreen gs;
	int score, score2;
	public SucessDisplay (GameScreen gs)
	{
		this.gs = gs;
		//this.l = l;
		score = gs.baseScore;
		score2 = score;
    //#if 0
		//#style gaugeHorizontalSpheres
		gaRond = new Gauge(Locale.get("SuccessDisplay.addrand"), true, score/20, 0);
		//gaRond.setPreferredSize(gs.ScrWidth-30, 20);
		//#style gaugeHorizontalSpheres
		gaHint = new Gauge(Locale.get("SuccessDisplay.addhint"), true, score/10, 0);
		//gaHint.setPreferredSize(gs.ScrWidth-30, 20);
		//#endif
		cmResume = new Command(Locale.get("Main_Menu.Resume_Game"), Command.OK, 1);
		// Create form, add commands, listen for events
		//#style playScreen
		fmMain = new Form(Locale.get("SuccessDisplay.gg"));
		addMenu();
		fmMain.addCommand(cmResume);
		fmMain.setCommandListener(this);
//		fmMain.setItemStateListener(this);
		llk.MIDLET.setCurrentDisplay(fmMain);
	}

	void addMenu() {
    	StringItem introText = new StringItem( null, 
				Locale.get("SuccessDisplay.congratulation",gs.currentLevel));
      //#style successCommand
		fmMain.append(introText);
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.score")+
		gs.baseScore);
    //#if 0
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.sumscore")+score2);
		gaRond.setLabel(Locale.get("SuccessDisplay.addrand")+m);
		fmMain.append(gaRond);
		gaHint.setLabel(Locale.get("SuccessDisplay.addhint")+n);
		fmMain.append(gaHint);
		
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.leftscore")+score);
    //#endif
	}
	int m = 0, n = 0;

	
	public void commandAction(Command c, Displayable s)
	{
		if (c == cmResume)
		{
			//#if 0
			gs.rondomTime +=m;
			gs.hintTime +=n;
			
			gs.baseScore = score2-20*m-10*n;
			//#endif
			//gs.changeState(gs.PLAYING);
			//temp delete this for ddp 
			//gs.gotoNextLevel(++gs.currentLevel);
			gs.gotoNextLevel(++gs.currentLevel);
      Thread.yield();
			llk.MIDLET.setCurrentDisplay(gs);
			
		}
	}
}

