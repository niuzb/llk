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
    VolumeControl vc;
    String[] store = new String[] {
        Global.FN_SOUND_BOSS_ALARM,
        Global.FN_SOUND_EXPLOSION_NORMAL,
         Global.FN_SOUND_LEVEL_0,
         Global.FN_SOUND_LEVEL_1,
         Global.FN_SOUND_LEVEL_2,
         Global.FN_SOUND_LEVEL_3
    };
    Hashtable <String, Player> musicDB;
    Hashtable <String, Integer> fn2num;

    public MusicManager() {
     // musicDB  = new  Hashtable <String, Player>();
      //fn2num  = new  Hashtable <String, Integer>();
     // createAudioPlayer();
     try {
        InputStream is = getClass().getResourceAsStream("/explosion.mid");
        expPlayer = Manager.createPlayer(is, "audio/midi");
        expPlayer.prefetch();
     } catch (Exception e) {
         e.printStackTrace();
     }

     
     
    }

    
    /**
     * Create all player.
     */
    void createAudioPlayer() {
        InputStream is;
        Player mp;
        
        try {
            for(int i = 0; i< store.length; i++) {
                is = getClass().getResourceAsStream(store[i]);
                mp = Manager.createPlayer(is, "audio/midi");
                mp.prefetch();
                musicDB.put(store[i], mp);
                fn2num.put(store[i], new Integer(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    int loadSample(String fileName) {
        //#if 0
        if (fn2num.get(fileName) != null) 
            return fn2num.get(fileName).intValue();
        //#endif
        return -1;
    }
    Player  expPlayer;

    boolean playSample( int sampleId, int loop) {
        if(sampleId == 100) {
            
            if (Engine.ME.rsm.music == Engine.ME.rsm.OFF)
                return false;
             try {
                    expPlayer.stop();
                    expPlayer.setMediaTime(0);
                    expPlayer.start();
            } catch (MediaException e) {
              e.printStackTrace();
            }

        }
   //#if 0
        Player mp;
        
        if (sampleId == -1 ||
            Engine.ME.rsm.music == Engine.ME.rsm.OFF)
            return false;
        mp  = musicDB.get(store[sampleId]);
        if(mp == null) {
            //#debug
            //System.out.println("the music can't find"+sampleId);
            return false;
        }
        //#if 0
        if(mp.getState() == Player.STARTED) {
            return true;
        }
        //#endif
        try {
              
              if (loop != -1)
              mp.setLoopCount(loop);
              mp.setMediaTime(0);
              mp.start();
        } catch (MediaException e) {
          e.printStackTrace();
        }
//#endif
        return true;
    }
    Player  musicPlayer;
    boolean playMusic(String name, int level) {
        
        VolumeControl vc;

        if (Engine.ME.rsm.music == Engine.ME.rsm.OFF)
            return false;
      try {
        InputStream is = getClass().getResourceAsStream(name);
        musicPlayer = Manager.createPlayer(is, "audio/midi");
        musicPlayer.prefetch();
        vc = (VolumeControl)musicPlayer.getControl("VolumeControl");
        if (vc != null)
            vc.setLevel(50);
       
                musicPlayer.stop();
                musicPlayer.setLoopCount(-1);
                musicPlayer.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
         return false;
    }

    
    /**
     * Destroy the audio player. Call this destroy the audio player.
     */
    public void destroyAudioPlayer() {

      Player mp;
       try {
            for(int i = 0; i< store.length; i++) {
                mp = musicDB.get(store[i]);
                mp.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stopAudioPlayer() {
        Player mp;
       try {
        //#if 0
            for(int i = 0; i< store.length; i++) {
                mp = musicDB.get(store[i]);
                mp.stop();
            }
        //#endif
             musicPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
