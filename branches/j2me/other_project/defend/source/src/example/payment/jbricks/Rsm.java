/*
 * Created on 30-Jun-2006 at 23:58:19.
 * 
 * Copyright (c) 2006 niuzb
 *
 * This file is part of  project 
 * niuzb
 *
 */
package example.payment.jbricks;

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
    
    public  int music = OFF;
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
            //System.out.println("storage read Exception");
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
    private static String RSM_NAME ="xianle_baoweinanhai";
    private RmsStorage storage;
}

