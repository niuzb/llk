/* Copyright © 2006 - Fabien GIGANTE */

import java.io.IOException;
import javax.microedition.lcdui.*;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;

/**
 * Game font using sprites as characters
 */
class GameFont
{
  /** Characters represented by each frame of the sprite (in order) */
  public static final BitMapFont font1;
  
  static {
     font1 = BitMapFont.getInstance("/example.bmf" );
  }
  private BitMapFontViewer viewer;

  private int frameHeight =5;
  private int frameWidth =5;
  /** Constructor from a resource name and a character set */
  public GameFont()
  {
    
  }

  /** Draw a character string, using this font */
  public void drawString(Graphics g, String str, int x, int y, int anchor)
  {
//#if 0
    int strLength = str.length();
    if (strLength == 0) return;
    // Adjust position according to anchor
    if ((anchor & Graphics.BOTTOM) != 0) y -= frameHeight;
    else if ((anchor & Graphics.VCENTER) != 0) y -= frameHeight / 2;
    if ((anchor & Graphics.RIGHT) != 0) x -= (frameWidth + 1) * strLength - 1;
    else if ((anchor & Graphics.HCENTER) != 0) x -= ((frameWidth + 1) * strLength - 1) / 2;
    // Draw each character
    for (int i = 0; i < strLength; i++)
    {
      int frameNumber = characterSet.indexOf(str.charAt(i));
      if (frameNumber >= 0) paint(g, frameNumber, x, y);
      x += frameWidth + 1;
    }
//#endif
    int strLength = str.length();

    if ((anchor & Graphics.BOTTOM) != 0) y -= frameHeight;
    else if ((anchor & Graphics.VCENTER) != 0) y -= frameHeight / 2;
    if ((anchor & Graphics.RIGHT) != 0) x -= (frameWidth + 1) * strLength - 1;
    else if ((anchor & Graphics.HCENTER) != 0) x -= ((frameWidth + 1) * strLength - 1) / 2;

    this.viewer = this.font1.getViewer(str);
    this.viewer.paint(x, y, g );
  }

}
