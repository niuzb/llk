package example.payment.jbricks;

import java.util.*;
import java.io.*;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.*;

/**
 * this class response for manage start or stop music
 */
public class MusicManager {
    Player  squishPlayer;
    Player  musicPlayer;
    Player  bangPlayer;
    Engine engine;
    Rsm rsm;
    VolumeControl vc;


    public MusicManager() {
      createAudioPlayer();
    }

    
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    /**
     * Create all player.
     */
    private void createAudioPlayer() {
        try {
                
            InputStream is = getClass().getResourceAsStream("/bg.mid");
            musicPlayer = Manager.createPlayer(is, "audio/midi");
            musicPlayer.prefetch();
            vc = (VolumeControl)musicPlayer.getControl("VolumeControl");
            if (vc != null)
                vc.setLevel(20);
           
            is = getClass().getResourceAsStream("/fire.wav");
            squishPlayer = Manager.createPlayer(is, "audio/x-wav");
             //#if 0
            is = getClass().getResourceAsStream("/explosion.mid");
            squishPlayer = Manager.createPlayer(is, "audio/midi");
            
            //#endif
            squishPlayer.prefetch();
            vc = (VolumeControl)squishPlayer.getControl("VolumeControl");
            if (vc != null)
              vc.setLevel(20);
            
            is = getClass().getResourceAsStream("/bang.mid");
            bangPlayer = Manager.createPlayer(is, "audio/midi");
            bangPlayer.prefetch();
            vc = (VolumeControl)bangPlayer.getControl("VolumeControl");
            if (vc != null)
              vc.setLevel(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start_bg_music() {
      if(rsm == null) 
        rsm = engine.getRsm();
       /*´ò¿ª±³¾°ÒôÀÖ*/
      if((rsm.valueMusic == rsm.MUSIC_ON) && 
          (musicPlayer != null)) {
            try {
                //gameoverPlayer.stop();
                musicPlayer.setLoopCount(-1);
                musicPlayer.start();
            } catch (MediaException e) {
              e.printStackTrace();
            }
      }
    }

    public void start_fire_music() {
        if(rsm == null) 
          rsm = engine.getRsm();
        
        if((rsm.valueMusic == rsm.MUSIC_ON) && (squishPlayer != null)) {
          try {
              squishPlayer.setMediaTime(0);
              squishPlayer.start();
          } catch (MediaException e) {
              e.printStackTrace();
          }
        }
    }
    
    public void start_bang_music() {
        if(rsm == null) 
          rsm = engine.getRsm();
        
        if((rsm.valueMusic == rsm.MUSIC_ON) && (bangPlayer != null)) {
          try {
            bangPlayer.setMediaTime(0);
            bangPlayer.start();
          } catch (MediaException e) {
              e.printStackTrace();
          }
        }
    }

    
    /**
     * Destroy the audio player. Call this destroy the audio player.
     */
    public void destroyAudioPlayer() {
        synchronized (this) {
            if (musicPlayer != null) {
                musicPlayer.close();
                musicPlayer = null;
            }
            if (squishPlayer != null) {
                squishPlayer.close();
                squishPlayer = null;
            }
            
            if (bangPlayer!= null) {
                bangPlayer.close();
                bangPlayer = null;
            }
        }
    }
    
    public void stopAudioPlayer() {
        try  {
            if (musicPlayer != null) {
                musicPlayer.stop();
            }
    
            if (squishPlayer != null) {
                squishPlayer.stop();
            }
            
            if (bangPlayer!= null) {
                bangPlayer.stop();
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }



}
