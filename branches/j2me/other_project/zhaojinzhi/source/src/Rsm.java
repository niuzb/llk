/*
 * Created on 30-Jun-2006 at 23:58:19.
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

import java.util.Date;

import de.enough.polish.io.Serializable;
import de.enough.polish.io.RmsStorage;

/**
 * <p>Represent the password when you login.</p>
 *
 * <p>Copyright Enough Software 2006</p>
 * <pre>
 * history
 *        2010/6/20 - niuzb creation
 * </pre>
 * @author niuzb
 */
public class Rsm implements Serializable {

    public static int ON = 1;
    public static int OFF= 0;
    
    public  int music = ON;
    public  int level = 0;
    public int life = 6;
    public int autowalk = ON;
	public Rsm() {
	
	}
}
class RsmManager {
    Rsm rsm;
    public RsmManager() {
        this.storage = new RmsStorage();
        initRsm();
    }

    
    private void initRsm() {
         try {
             rsm = (Rsm)this.storage.read(this.RSM_NAME);
         } catch (Exception e) {
            //#debug
            System.out.println("storage read Exception");
             rsm = new Rsm();
         }

    }

    public void saveRsm() {
        
        try {
            this.storage.save(rsm, this.RSM_NAME);
        } catch (Exception e) {
           //#debug
           System.out.println("storage save Exception");
           
        }
    }
    private static String RSM_NAME ="xianle_loderunner";
    private RmsStorage storage;
}
