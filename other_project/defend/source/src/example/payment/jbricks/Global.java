
package example.payment.jbricks;


import java.util.*;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.*;

import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;
import de.enough.polish.java5.Enum;

public class Global {
    private static Random r;
    static {
        r = new Random();
    }
    public  Global() {


    }
    static int  MAX_LEVEL = 4;

    static int TL=Graphics.TOP | Graphics.LEFT;     
    static int   SCROLL_SPEED = 2;

    static int GAME_LENGTH = 100000; // ms
    static int ITEM_LIFETIME = 20000; // ms
    static int ITEM_APPEAR_DELAY = 10000; // ms
    static int ITEM_APPEAR_RAND_DELAY = 20000; // ms
    static int ITEM_HEALTH_REPAIR_AMOUNT = 100;
    
    static int ITEM_HEATSEEKER_AMMO = 70;
    static int ITEM_NUKE_AMMO = 3;
    static int ITEM_DEFLECTOR_AMMO = 1;
    static int ITEM_DEFLECTOR_ACTIVATION_DIST = 200;
    static int ITEM_DEFLECTOR_POWER = 100;
    static int ITEM_DEFLECTOR_DURATION = 15000;
    
    static int ITEM_APPEAR_CHANCES[] = { 50, 20, 30, 30, 30, 50, 20, 20, 30, 20, 20 };
    
    static int GENERATE_ENEMY_DELAY = 100;
    static int GENERATE_ENEMY_RAND_DELAY = 2000;
    
    static int LIGHT_FIGHTER_MAX_SHIELD = 200;
    static int LIGHT_FIGHTER_MAX_DAMAGE = 200;
    
    static int LIGHT_FIGHTER_SHIELD_RECHARGE = 80;
    
    static int RACER_DEFLECTOR_ACTIVATION_DIST = 150;
    static int RACER_DEFLECTOR_POWER = 50;
    static int RACER_SONIC_ACTIVATION_DIST = 200;
    static int RACER_SONIC_POWER = 50;
    
    static int ENEMY_HITPOINTS[] = { 80, 120, 150, 2000, 1500, 1000, 1000, 3000,4000, 200};
    static int ENEMY_COLLISION_DAMAGE[] = {20, 40, 0, 0, 0, 0, 0, 2000,2000,0};
    static int ENEMY_DIES_ITEM_APPEAR_CHANCE[] = {15, 12, 8, 0, 0, 0, 0, 1,10,0};
    static int ENEMY_APPEAR_CHANCES[] = {40, 40, 10, 0, 0, 0, 0, 0,0,0};
    static int ENEMY_COOLDOWN_PRIMARY[] = {400, 400, 400, 50, 500, 100, 100, 
    300,400,0};
    static int ENEMY_COOLDOWN_SECONDARY[] = {0, 0, 2000, 0, 0, 0, 0, 600,600,0};
    static int ENEMY_RAND_WAIT_PRIMARY[] = {1500, 1000, 300, 80, 1000, 200, 200, 
    2000,2000,8000};
    static int ENEMY_RAND_WAIT_SECONDARY[] = {0, 0, 10000, 0, 0, 0, 0, 400,2000,8000};
    
    static int GENERATE_FORMATION_DELAY = 5000;
    static int GENERATE_FORMATION_RAND_DELAY = 20000;
    
    static int FORMATION_MAX_NR_ENEMYS[] = {7,7,7,6};
    static int FORMATION_SP_CHANCES[] = { 10, 10, 5, 5, 20, 10, 5, 20, 10, 20, 10 };
    static int FORMATION_SP_PRIMARY_DELAY[] = {0, 80, 120, 160,
                        500, 800, 1100,
                        150, 250, 150, 250};
    static int FORMATION_SP_PRIMARY_RAND_DELAY[] = { 0, 80, 120, 160,
                          1000, 1500, 2000,
                          0, 0, 0, 0 };
    
    /********************************** ITEMS ****************************************/
   final static int	ITEM_PRIMARY_UPGRADE=0;
   final static int	ITEM_DUMBFIRE_DOUBLE=1;
   final static int	ITEM_KICK_ASS_ROCKET=2;
   final static int	ITEM_HELLFIRE=3;
   final static int	ITEM_MACHINE_GUN=4;
   final static int	ITEM_HEALTH=5;
   final static int	ITEM_HEATSEEKER=6;
   final static int	ITEM_NUKE=7;
   final static int	ITEM_DEFLECTOR=8;
   final static int	ITEM_LASER=9;
   final static int	ITEM_ENERGY_BEAM= 10; 

    static int NR_ITEM_TYPES = 11;
    
    final static int EXPLOSION_NORMAL_AIR = 0;
    final static int  EXPLOSION_NORMAL_GROUND = 1;
    final static int NR_EXPLOSION_TYPES = 2;
     static int LIFETIME_EXPL_NORMAL = 1500;
    
     static int NUKE_EFFECT_DURATION = 1000;
     static int NUKE_QUAKE_EFFECT = 40;
     static int LIGHT_FIGHTER = 0;
    static int  HEAVY_FIGHTER = 1;
     static int NR_SHIPS = 2;

    /*********************************** SHOTS ********************************/


    final static int SHOT_NORMAL=0;
    final static int SHOT_NORMAL_HEAVY=1;
    final static int SHOT_DOUBLE= 2;     
    final static int SHOT_DOUBLE_HEAVY=3; 
    final static int SHOT_TRIPLE= 4; 
    final static int SHOT_DUMBFIRE=100;
    final static int SHOT_DUMBFIRE_DOUBLE=101 ;
    final static int SHOT_KICK_ASS_ROCKET= 102;
    final static int SHOT_HELLFIRE= 103;
    final static int SHOT_MACHINE_GUN= 104;
    final static int SHOT_ENERGY_BEAM= 105;
    final static int ENEMY_SHOT_NORMAL=1000;
    final static int ENEMY_SHOT_TANK_ROCKET=1001 ;
    final static int SPECIAL_SHOT_HEATSEEKER=10000;
    final static int SPECIAL_SHOT_NUKE= 100001;
final static int NR_SECONDARY_WEAPONS = 6;
final  static int SPECIAL_NONE = 0;
final static int  SPECIAL_NUKE = 1;
final  static int SPECIAL_HEATSEEKER = 2;
final static int  SpecialTypes = 3;

 static int NR_SPECIALS = 4;

// after that many ms the shot is expired
 static int LIFETIME_SHOT_NORMAL = 5000;
 static int LIFETIME_SHOT_NORMAL_HEAVY = 5000;
 static int LIFETIME_SHOT_DOUBLE = 5000;
 static int LIFETIME_SHOT_DOUBLE_HEAVY = 5000;
 static int LIFETIME_SHOT_TRIPLE = 5000;
 static int LIFETIME_SHOT_HF_NORMAL = 6000;
 static int LIFETIME_SHOT_HF_DOUBLE = 6000;
 static int LIFETIME_SHOT_HF_TRIPLE = 6000;
 static int LIFETIME_SHOT_HF_QUATTRO = 6000;
 static int LIFETIME_SHOT_HF_QUINTO = 6000;

 static int LIFETIME_SHOT_DUMBFIRE = 6000;
 static int LIFETIME_SHOT_DUMBFIRE_DOUBLE = 6000;
 static int LIFETIME_SHOT_KICK_ASS_ROCKET = 7000;
 static int LIFETIME_SHOT_HELLFIRE = 6000;
 static int LIFETIME_SHOT_MACHINE_GUN = 5000;
 static int LIFETIME_SHOT_ENERY_BEAM = 5000;

 static int LIFETIME_SHOT_HF_DUMBFIRE = 6000;
 static int LIFETIME_SHOT_HF_DUMBFIRE_DOUBLE = 6000;
 static int LIFETIME_SHOT_HF_KICK_ASS_ROCKET = 7000;
 static int LIFETIME_SHOT_HF_LASER = 2000;

 static int LIFETIME_SPECIAL_SHOT_HEATSEEKER = 10000;
 static int LIFETIME_SPECIAL_SHOT_NUKE = 10000;

 static int VEL_SHOT_NORMAL = 150;
 static int VEL_SHOT_NORMAL_HEAVY = 150;
 static int VEL_SHOT_DOUBLE = 150;
 static int VEL_SHOT_DOUBLE_HEAVY = 150;
 static int VEL_SHOT_TRIPLE = 150;

 static int VEL_SHOT_HF_NORMAL = 180;
 static int VEL_SHOT_HF_DOUBLE = 180;
 static int VEL_SHOT_HF_TRIPLE = 180;
 static int VEL_SHOT_HF_QUATTRO = 180;
 static int VEL_SHOT_HF_QUINTO = 180;

 static int VEL_SHOT_DUMBFIRE = 100;
 static int VEL_SHOT_DUMBFIRE_DOUBLE = 100;
 static int VEL_SHOT_KICK_ASS_ROCKET = 80;
 static int VEL_SHOT_HELLFIRE = 55;
 static int VEL_SHOT_MACHINE_GUN = 130;
 static int VEL_SHOT_ENERGY_BEAM = 105;

 static int VEL_SHOT_HF_DUMBFIRE = 160;
 static int VEL_SHOT_HF_DUMBFIRE_DOUBLE = 160;
 static int VEL_SHOT_HF_KICK_ASS_ROCKET = 80;
 static int VEL_SHOT_HF_LASER = 600;

 static int VEL_SPECIAL_SHOT_HEATSEEKER = 130;
 static int VEL_SPECIAL_SHOT_NUKE = 180;

 static int DAMAGE_SHOT_NORMAL = 5;
 static int DAMAGE_SHOT_NORMAL_HEAVY = 8;
 static int DAMAGE_SHOT_DOUBLE = 5;
 static int DAMAGE_SHOT_DOUBLE_HEAVY = 8;
 static int DAMAGE_SHOT_TRIPLE = 7;

 static int DAMAGE_SHOT_HF_NORMAL = 20;
 static int DAMAGE_SHOT_HF_DOUBLE = 20;
 static int DAMAGE_SHOT_HF_TRIPLE = 20;
 static int DAMAGE_SHOT_HF_QUATTRO = 20;
 static int DAMAGE_SHOT_HF_QUINTO = 20;

 static int DAMAGE_SHOT_DUMBFIRE = 40;
 static int DAMAGE_SHOT_DUMBFIRE_DOUBLE = 30;
 static int DAMAGE_SHOT_KICK_ASS_ROCKET = 151; // should kill a tank/turret 
 static int DAMAGE_SHOT_HELLFIRE = 50;
 static int DAMAGE_SHOT_MACHINE_GUN = 8;
 static int DAMAGE_SHOT_ENERGY_BEAM = 80;

 static int DAMAGE_SHOT_HF_DUMBFIRE = 40;
 static int DAMAGE_SHOT_HF_DUMBFIRE_DOUBLE = 40;
 static int DAMAGE_SHOT_HF_KICK_ASS_ROCKET = 151;
 static int DAMAGE_SHOT_HF_LASER = 70;

 static int DAMAGE_SPECIAL_SHOT_HEATSEEKER = 20;
 static int DAMAGE_SPECIAL_SHOT_NUKE = 250;

 static int SPREAD_ANGLE_SHOT_NORMAL = 6;

 static int LIFETIME_ENEMY_SHOT_NORMAL = 6000;
 static int LIFETIME_ENEMY_SHOT_TANK_ROCKET = 10000;

 static int VEL_ENEMY_SHOT_NORMAL = 130;
 static int VEL_ENEMY_SHOT_TANK_ROCKET = 70;

 static int DAMAGE_ENEMY_SHOT_NORMAL = 8;
 static int DAMAGE_ENEMY_SHOT_TANK_ROCKET = 25;
    /********************************** ENEMIES ********************************/
    
    final    static int BOOS_1_NOGUN = 9;
    final    static int FIGHTER = 0;
    final    static int BOMBER = 1;
    final    static int TANK = 2;
    final    static int BOSS_1_MAIN_GUN = 3;
    
    final    static int BOSS_1_ROCKET_LAUNCHER = 4;
    final    static int BOSS_1_SHOT_BATTERY_RIGHT = 5;
    final    static int BOSS_1_SHOT_BATTERY_LEFT = 6;
    final    static int BOSS_2 = 7;
    final    static int BOSS_3 = 8;
    final    static int NR_ENEMY_TYPES = 9;
    final    static int NR_ENEMY_TYPES_NORMAL = 3;
    final    static int NR_ENEMY_TYPES_BOSS_1 = 4;

    
     static int BOSS_1_END_Y = 50;
    
    static int WRECK_FIGHTER = 0;
    static int  WRECK_BOMBER = 1;
    static int WRECK_TANK = 2;
    static int  WRECK_BOSS_1_MAIN_GUN = 3;
    
    static int WRECK_BOSS_1_ROCKET_LAUNCHER = 4;
    static int  WRECK_BOSS_1_BATTERY_RIGHT = 5;
    static int WRECK_BOSS_1_BATTERY_LEFT = 6;
    static int  WRECK_BOSS_1_BACKGROUND = 7;
    
    static int  WRECK_BOSS_1_DESTROYED = 8;
    static int WRECK_BOSS_2_DESTROYED = 9;
     static int NR_WRECK_TYPES = 10;
     int WRECK_FOR_ENEMYTYPE[] = new int[] {
                 WRECK_FIGHTER,
                 WRECK_BOMBER, 
                 WRECK_TANK, 
                 WRECK_BOSS_1_MAIN_GUN,
                 WRECK_BOSS_1_ROCKET_LAUNCHER,
                 WRECK_BOSS_1_BATTERY_RIGHT,
                 WRECK_BOSS_1_BATTERY_LEFT,
                 WRECK_BOSS_2_DESTROYED };

    static boolean ENEMY_FLYING[] = {true, true, false, false, false, false, false, 
    true,false,false};
    // determines the difference between shadow and enemy plane
     static int ENEMY_FLYING_HEIGHT[] = {10, 15, 0, 0, 0, 0, 0, 10,0,0};
     static int ENEMY_POINTS_FOR_DEST[] = {10,20,20,0,0,0,0,0,0,0};
    /******************************** FORMATION 
***************************************/

    static int FORMATION_V = 0;
    static int  FORMATION_REVERSE_V = 1;
    static int FORMATION_BLOCK = 2;
    static int  FORMATION_LINE = 3;


 static int NR_FORMATION_TYPES = 4;

 static int FORMATION_MAX_NR_ENEMYS_HARD_LIMIT[] = {7,7,7,6};


 static int FORMATION_ENEMY_SET_DEFAULT = 0;
 static int  FORMATION_ENEMY_SET_FIGHTER = 1;
 static int FORMATION_ENEMY_SET_BOMBER = 2;
 static int  FORMATION_ENEMY_SET_FIGHTER_BOMBER = 3;
 

 static int NR_FORMATION_ENEMY_SETS = 4;

 static int FORMATION_CHANGE_ON_KILL = 1;
 static int FORMATION_CHANGE_SPONTANEOUS = 2;
 static int FORMATION_CHANGE_SELDOM = 4;
 static int FORMATION_CHANGE_OFTEN = 8;

 static int FORMATION_CHANGE_OFTEN_DELAY = 3000;
 static int FORMATION_CHANGE_OFTEN_RAND_DELAY = 8000;
 static int FORMATION_CHANGE_SELDOM_DELAY = 4000;
 static int FORMATION_CHANGE_SELDOM_RAND_DELAY = 15000;

static int FORMATION_SP_NONE = 0;
static int  FORMATION_SP_RAND_FAST = 1;
static int FORMATION_SP_RAND_MEDIUM = 2;
static int  FORMATION_SP_RAND_SLOW = 3;
static int FORMATION_SP_VOLLEY_FAST = 4;
static int  FORMATION_SP_VOLLEY_MEDIUM = 5;
static int FORMATION_SP_VOLLEY_SLOW = 6;


    static int  FORMATION_SP_LEFT_RIGHT_FAST = 7;
    static int  FORMATION_SP_LEFT_RIGHT_MEDIUM = 8;
    static int FORMATION_SP_RIGHT_LEFT_FAST = 9;
    static int  FORMATION_SP_RIGHT_LEFT_MEDIUM = 10;


 static int NR_FORMATION_SP = 11;

    /************************* RACER *********************************/
    
    
    // max speed of the racer in pixels per second
     static int LIGHT_FIGHTER_VEL_MAX = 90;
    
    
    // Cooldown rates (in ms) of the weapons
     static int RACER_COOLDOWN_SHOT_NORMAL = 100;
     static int RACER_COOLDOWN_SHOT_NORMAL_HEAVY = 100;
     static int RACER_COOLDOWN_SHOT_DOUBLE = 130;
     static int RACER_COOLDOWN_SHOT_DOUBLE_HEAVY = 130;
     static int RACER_COOLDOWN_SHOT_TRIPLE = 130;
    
     static int RACER_COOLDOWN_SHOT_HF_NORMAL = 300;
     static int RACER_COOLDOWN_SHOT_HF_DOUBLE = 300;
     static int RACER_COOLDOWN_SHOT_HF_TRIPLE = 300;
     static int RACER_COOLDOWN_SHOT_HF_QUATTRO = 350;
     static int RACER_COOLDOWN_SHOT_HF_QUINTO = 400;
    
     static int RACER_COOLDOWN_DUMBFIRE = 600;
     static int RACER_COOLDOWN_DUMBFIRE_DOUBLE = 300;
     static int RACER_COOLDOWN_KICK_ASS_ROCKET = 1500;
     static int RACER_COOLDOWN_HELLFIRE = 600;
     static int RACER_COOLDOWN_MACHINE_GUN = 150;
     static int RACER_COOLDOWN_ENERGY_BEAM = 500;
    
     static int RACER_COOLDOWN_HF_DUMBFIRE = 600;
     static int RACER_COOLDOWN_HF_DUMBFIRE_DOUBLE = 300;
     static int RACER_COOLDOWN_HF_KICK_ASS_ROCKET = 1300;
     static int RACER_COOLDOWN_HF_LASER = 700;
    
     static int RACER_COOLDOWN_SPECIAL_HEATSEEKER = 400;
     static int RACER_COOLDOWN_SPECIAL_NUKE = 3000;
    
    
    // how long (in ms) does the shield glow, when the racer is hit 
     static int RACER_SHIELD_DAMAGE_LIFETIME = 200;
    //banner

    final static int   BANNER_EXCELLENT=0;
    final static int   BANNER_YOU_RULE=1; 
    final static int   BANNER_HEIHO=2;
    final static int   BANNER_HEALTH=3; 
    final static int  BANNER_ENEMYS_KILLED=15; 
    final static int   BANNER_ITEM_HEALTH_COLLECTED=4;
    final static int   BANNER_ITEM_PRIMARY_UPGRADE_COLLECTED=5;
    final static int   BANNER_ITEM_DUMBFIRE_DOUBLE_COLLECTED=6;
    final static int   BANNER_ITEM_KICK_ASS_ROCKET_COLLECTED=7;
    final static int   BANNER_ITEM_HELLFIRE_COLLECTED=8;
    final static int   BANNER_ITEM_MACHINE_GUN_COLLECTED=9;
    final static int   BANNER_ITEM_HEATSEEKER_COLLECTED=10;
    final static int   BANNER_ITEM_NUKE_COLLECTED=11;
    final static int   BANNER_ITEM_DEFLECTOR_COLLECTED=12;
    final static int   BANNER_ITEM_ENERGY_BEAM_COLLECTED=13;
    final static int           BANNER_ITEM_LASER_COLLECTED=14; 

    
    // shields
    static  String FN_SOUND_SHOT_PRIMARY = "/shotPrimary.wav";
    static String FN_SOUND_SHOT_SECONDARY = "/shotSecondary.wav";
    static String FN_SOUND_EXPLOSION_NORMAL = "/explosion.mid";
    static String FN_SOUND_EXPLOSION_BOSS = "/explosionBoss.wav";
    static String FN_SOUND_BOSS_ALARM = "/2.mid";
    static String FN_SOUND_ARCADE_CONFIRM = "/playon.wav";
    static String FN_SOUND_LEVEL_0 ="/5.mid";
    static String FN_SOUND_LEVEL_1 ="/6.mid";
    static String FN_SOUND_LEVEL_2 ="/7.mid";
    static String FN_SOUND_LEVEL_3 ="/1.mid";
    
    static String FN_ICONS_SPECIALS = "/iconsSpecials.png";
    static String FN_ICONS_SECONDARY_WEAPONS = "/iconsSecondaryWeapons.png";
    static String FN_HITPOINTS_STAT = "/hpStat.png";
    static String FNINTRO_SHOW_CHOICE = "/menuIcon.png";

    static String FN_EXPLOSION_NORMAL = "/explosion.png";
    static String FN_EXPLOSION_ENEMY = "/explosionEnemy.png";
//#if 0   


//#endif
    static String FN_SHOT_NORMAL = "/normalShot.png";
    static String FN_SHOT_NORMAL_HEAVY = "/heavyShot.png";
    static String FN_SHOT_DOUBLE = "/normalShot.png";
    static String FN_SHOT_DOUBLE_HEAVY = "/heavyShot.png";
    static String FN_SHOT_TRIPLE = "/heavyShot.png";
    
    
    
    static String FN_SHOT_DUMBFIRE = "/dumbfire.png";
    static String FN_SHOT_DUMBFIRE_DOUBLE = "/dumbfire.png";
    static String FN_SHOT_KICK_ASS_ROCKET = "/kickAssRocket.png";
    static String FN_SHOT_KICK_ASS_ROCKET_SHADOW = "/kickAssRocketShadow.png";
    static String FN_SHOT_HELLFIRE = "/hellfire.png";
    static String FN_SHOT_HELLFIRE_SHADOW = "/hellfireShadow.png";
    static String FN_SHOT_MACHINE_GUN = "/machineGun.png";
    static String FN_SHOT_ENERGY_BEAM = "/energyBeam.png";
    
    
    
    static String FN_ENEMY_SHOT_NORMAL = "/enemyShotNormal.png";
    static String FN_ENEMY_SHOT_TANK_ROCKET = "/tankRocket.png";
    
    
    
    static String FN_SPECIAL_SHOT_NUKE = "/shotNuke.png";
    static String FN_NUKE_EFFECT = "/nukeEffect.png";
    
    final static String FN_ITEM_PRIMARY_UPGRADE = "/itemPrimaryUpgrade.png";
    final static String FN_ITEM_DUMBFIRE_DOUBLE = "/itemDumbfireDouble.png";
    final static String FN_ITEM_KICK_ASS_ROCKET = "/itemKickAssRocket.png";
    final static String FN_ITEM_HELLFIRE = "/itemHellfire.png";
    final static String FN_ITEM_MACHINE_GUN = "/itemMachineGun.png";
    final static String FN_ITEM_HEALTH = "/itemHealth.png";
    final static String FN_ITEM_NUKE = "/itemNuke.png";
    final static String FN_ITEM_ENERGY_BEAM = "/itemEnergyBeam.png";




    static  int getRandValue(int []choicesWeights,  int nrChoices ) {
       int sum = 0;
     // if ( sumWeights != 0 ) sum = sumWeights;
       for (  int i = 0; i < nrChoices; i++ ) sum += choicesWeights[ i ];
      
      if ( sum == 0 ) {
         return -1;
      }
      
       int val = Math.abs(r.nextInt()) % sum;
    
       int idx = 0;
       int tmpSum = 0;
      while ( idx < nrChoices ) {
        tmpSum += choicesWeights[ idx ];
        if ( val < tmpSum ) {
          return idx;
        }
        idx++;
      }
      return(-1);
    }
    
}

