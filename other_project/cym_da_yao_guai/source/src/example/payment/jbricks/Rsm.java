package example.payment.jbricks;

import java.io.*; 
import java.util.*;


/** 
 * Main game wrapper 
 */
public class Rsm {
    /**
     * Creates game wrappper
     */
    public Rsm() {
      initRsm();
    }
    
   //�����Ǳ�����RSM�����ݶ�Ӧ�Ĺؼ��֣������ʽΪ�ؼ��֣�����
   String keyMusic="music";
   String keyLevel="level";
   String keyScore="score";
    String keyblood="blood";
   
   public static final int MUSIC_ON = 1;
   public static final int MUSIC_OFF =0;
   public int valueMusic=1;
   public int valueLevel=0;
   public int valueScore=0;
   public int valueblood=0;
   
   /*�������ֿ����߹ص�״̬��RSM
    * param: data 1�������ֿ���0�������ֹ� 
    */
   public void updateoption(int data, String key) {	
   
        //valueMusic = data;
		
		if (key.equals(keyLevel)) {
			valueLevel = data;
		} else if (key.equals(keyMusic)) {
			valueMusic = data;
		} else if (key.equals(keyScore)) {
			valueScore= data;
		} else if (key.equals(keyblood)) {
			valueblood= data;
		} 
        
		try {
        	String[] s = rsm.getRecordById(RSMName, key);
		
			if (s==null) {
				rsm.createRecord(RSMName,key + ":" + data);
			} else {
	        	rsm.updateRecord(RSMName, key + ":" + data, Integer.parseInt(s[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
   }
   
   private static final String RSMName = "mifen_xianle";
   private RecordStoreManager rsm;

   private void initRsm() {
	   rsm = RecordStoreManager.getInstance();
	   rsm.CreateRecordStore(RSMName);
       
       Hashtable ht;
       Enumeration em;
	   try {
		   	ht = rsm.getRecordPairs_key_value(RSMName);
		    em = ht.keys();
		   while (em.hasMoreElements()) {
				String s = (String)em.nextElement();
				if (s.equals(keyLevel)){
					valueLevel = Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyMusic)) {
					valueMusic = Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyScore)) {
					valueScore= Integer.parseInt((String)ht.get(s));
				} else if (s.equals(keyblood)) {
					valueblood= Integer.parseInt((String)ht.get(s));
				} 
		   } 
	   } catch (Exception e){
			e.printStackTrace();
	   }

   }
}

