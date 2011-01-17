package de.enough.polish.sample.table;

import java.io.IOException;


import de.enough.polish.ui.ChartItem;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.TableItem;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.ui.backgrounds.SimpleBackground;
import javax.microedition.lcdui.Canvas;
import de.enough.polish.ui.Style;

public class EditTable 
extends TableItem
{ 

    public EditTable(int a, int b, Style s) {
        super(a, b, s);
    }


    protected boolean handleKeyPressed(int keyCode,
                                       int gameAction){

        
        
        if (gameAction == Canvas.FIRE) {
            return super.handleKeyPressed(Canvas.KEY_NUM2, gameAction) ;
        } else {
            return super.handleKeyPressed(keyCode, gameAction) ;
        }
       
        
    }
    protected boolean handlePointerPressed(int relX,
                                       int relY){
        super.handlePointerPressed(relX, relY);
        return super.handleKeyPressed(Canvas.KEY_NUM2, Canvas.FIRE) ;
    }

}

