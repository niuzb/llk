/*
 * Created on Mar 30, 2006 at 10:55:47 AM.
 * 
 * Copyright (c) 2005 Robert Virkus / Enough Software
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


import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.util.DrawUtil;

/**
 * <p>Just displays a gradient.</p>
 *
 * <p>Copyright Enough Software 2005</p>
 * <pre>
 * history
 *        Mar 30, 2006 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class GradientItem extends CustomItem {
	
	//#message cfg.Name=${ cfg.Name }


    private int color;

	private Image image;

	/**
	 * Creates a new GradientItem.
	 * 
	 * @param width the width of this item.
	 */
	public GradientItem( int color ) {
		super(null);
		//this.width = width;
		this.color = color;
		//this.height = h;
		
		updateImage();
		//#if true
			//# this.appearanceMode = PLAIN;
		//#endif
	}
    /* (non-Javadoc)
         * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
         */
        protected int getMinContentWidth() {
            return 15;
        }
    
        /* (non-Javadoc)
         * @see javax.microedition.lcdui.CustomItem#getMinContentHeight()
         */
        protected int getMinContentHeight() {
            return 15;
        }

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
	 */
        /* (non-Javadoc)
             * @see javax.microedition.lcdui.CustomItem#getPrefContentWidth(int)
             */
            protected int getPrefContentWidth(int maxHeight) {
                return 15;
            }
        
            /* (non-Javadoc)
             * @see javax.microedition.lcdui.CustomItem#getPrefContentHeight(int)
             */
            protected int getPrefContentHeight(int grantedWidth) {
                return 15;
            }


	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
	 */
	protected void paint(Graphics g, int w, int h) {
		g.drawImage( this.image, 0, 0, Graphics.LEFT | Graphics.TOP );
	}



	/**
	 * @param topColor The topColor to set.
	 */
	public void setColor(int topColor) {
		this.color = topColor;
		updateImage();
	}

	private void updateImage() {
		if (this.image == null) {
			this.image = Image.createImage( 20, 20 );
		}
		Graphics g = this.image.getGraphics();
        //#if 0
		int steps = this.height/2;
		int[] gradient = DrawUtil.getGradient(this.topColor, this.midColor, steps);
		for (int i = 0; i < gradient.length; i++) {
			int color = gradient[i];
			g.setColor(color);
			g.drawLine( 0, i, this.width, i );
		}
		DrawUtil.getGradient(this.midColor, this.bottomColor, gradient);
		for (int i = 0; i < gradient.length; i++) {
			int color = gradient[i];
			g.setColor(color);
			g.drawLine( 0, i + steps, this.width, i + steps );
		}
        //#endif
        g.setColor(this.color);
        g.fillRect(0, 0, 15, 15)  ;
        //g.fillArc(0,0,15,15,0,360);
		repaint();
	}

}
