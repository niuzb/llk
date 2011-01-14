

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Form;


public class SucessDisplay implements CommandListener,ItemStateListener
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
		score = gs.baseScore+gs.playTime;
		score2 = score;
		//#style gaugeHorizontalSpheres
		gaRond = new Gauge(Locale.get("SuccessDisplay.addrand"), true, score/20, 0);
		//gaRond.setPreferredSize(gs.ScrWidth-30, 20);
		//#style gaugeHorizontalSpheres
		gaHint = new Gauge(Locale.get("SuccessDisplay.addhint"), true, score/10, 0);
		//gaHint.setPreferredSize(gs.ScrWidth-30, 20);
		cmResume = new Command(Locale.get("Main_Menu.Resume_Game"), Command.OK, 1);
		// Create form, add commands, listen for events
		//#style playScreen
		fmMain = new Form(Locale.get("SuccessDisplay.congratulation")+gs.currentLevel);
		addMenu();
		fmMain.addCommand(cmResume);
		fmMain.setCommandListener(this);
		fmMain.setItemStateListener(this);
		llk.MIDLET.setCurrentDisplay(fmMain);
	}

	void addMenu() {
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.score")+
		gs.baseScore+Locale.get("SuccessDisplay.lefttime")+gs.playTime);
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.sumscore")+score2);
		gaRond.setLabel(Locale.get("SuccessDisplay.addrand")+m);
		fmMain.append(gaRond);
		gaHint.setLabel(Locale.get("SuccessDisplay.addhint")+n);
		fmMain.append(gaHint);
		
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.leftscore")+score);
	}
	int m = 0, n = 0;
	public void itemStateChanged(Item item) {
		m = gaRond.getValue();
		n = gaHint.getValue();
		score = score2-20*m-10*n;
				
		if ((item == gaRond) && (m>0)) {
			if(score<0){
				m--;
				gaRond.setValue(m);
			}
	    } else if ((item == gaHint) && (n>0)) {
			if(score<0){
				n--;
				gaHint.setValue(n);
			}
	    }
		/*
		if (score < 0) {
			if ((item == gaRond) && (m>0)) {
				m--;
				gaRond.setValue(m);
			} else if ((item == gaHint) && (n>0)) {
				n--;
				gaRond.setValue(n);
			} else {
				if (m>0){
					m--;
					gaRond.setValue(m);
				} else if (n>0){
					n--;
					gaRond.setValue(n);
				} else {
					//break;
				}
			}
			
			score = score2-20*m-10*m;
		}*/
		score = score2-20*m-10*n;
		gaRond.setLabel(Locale.get("SuccessDisplay.addrand")+m);
		
		gaHint.setLabel(Locale.get("SuccessDisplay.addhint")+n);
		fmMain.delete(4);
		//#style successCommand
		fmMain.append(Locale.get("SuccessDisplay.leftscore")+score);
	}

	
	public void commandAction(Command c, Displayable s)
	{
		if (c == cmResume)
		{
			
			gs.rondomTime +=m;
			gs.hintTime +=n;
			
			gs.baseScore = score2-20*m-10*n;
			
			gs.changeState(gs.PLAYING);
			gs.gotoNextLevel(++gs.currentLevel);
			llk.MIDLET.setCurrentDisplay(gs);
			
		}
	}
}

