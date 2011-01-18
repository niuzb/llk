package net.xylophones.micro.game.mb;

import java.util.Date;

import de.enough.polish.io.Serializable;
import de.enough.polish.io.RmsStorage;
import net.xylophones.micro.game.mb.*;

public class RsmManager {
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
    private static String RSM_NAME ="xianle_teric";
    private RmsStorage storage;
}

