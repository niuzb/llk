
package example.payment.jbricks;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import java.io.*;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Thilina
 * Date: Oct 19, 2007
 * Time: 9:47:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecordStoreManager {

    private static RecordStoreManager me = null;

    private RecordStoreManager() {

    }

    public static RecordStoreManager getInstance() {
        if (me == null)
            me = new RecordStoreManager();
        return me;
    }
	
    public RecordStore CreateRecordStore(String recStore) {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(recStore, true);
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
		return rs;
	}

    public String[] getRecordById(String recStore, String id) throws RecordStoreNotFoundException, Exception {
        try {
            InputStream stream = null;
            RecordStore rs = null;

            rs = RecordStore.openRecordStore(recStore, false);

            RecordEnumeration r = rs.enumerateRecords(null, null, true);

            while (r.hasNextElement()) {
                int rid = r.nextRecordId();
                byte[] adata = rs.getRecord(rid);
                stream = new ByteArrayInputStream(adata);
                InputStreamReader e = new InputStreamReader(stream);
                int a;
                String s = "";
                while ((a = e.read()) != -1) {
                    s += String.valueOf((char) a);
                }
                int x = s.indexOf(":");
                if (x != -1) {
                    if (s.substring(0, x).equals(id)) {
                        rs.closeRecordStore();
                        return new String[]{String.valueOf(rid), s.substring(x + 1, s.length())};
                    }
                }
            }

            rs.closeRecordStore();
            return null;
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
            throw e;


        } catch (Exception ex) {
            ex.printStackTrace();
            //return null;
            throw ex;

        }
    }


    public Hashtable getRecordPairs_id_value(String recStore) throws Exception {
        try {
            InputStream stream = null;
            RecordStore rs = RecordStore.openRecordStore(recStore, false);
            RecordEnumeration r = rs.enumerateRecords(null, null, true);
            Hashtable ht = new Hashtable();
            int i = 0;
            while (r.hasNextElement()) {
                int rid = r.nextRecordId();
                byte[] adata = rs.getRecord(rid);
                stream = new ByteArrayInputStream(adata);
                InputStreamReader e = new InputStreamReader(stream);
                int a;
                String s = "";
                while ((a = e.read()) != -1) {
                    s += String.valueOf((char) a);
                }
                int x = s.indexOf(":");
                if (x != -1) {
                    ht.put(String.valueOf(rid), s.substring(x + 1, s.length()));
                }
            }

            rs.closeRecordStore();
            return ht;
        } catch (Exception ex) {
            throw ex;
        }
    }
	
    public Hashtable getRecordPairs_key_value(String recStore) throws Exception {
        try {
            InputStream stream = null;
            RecordStore rs = RecordStore.openRecordStore(recStore, false);
            RecordEnumeration r = rs.enumerateRecords(null, null, true);
            Hashtable ht = new Hashtable();
            int i = 0;
            while (r.hasNextElement()) {
                int rid = r.nextRecordId();
                byte[] adata = rs.getRecord(rid);
                stream = new ByteArrayInputStream(adata);
                InputStreamReader e = new InputStreamReader(stream);
                int a;
                String s = "";
                while ((a = e.read()) != -1) {
                    s += String.valueOf((char) a);
                }
                int x = s.indexOf(":");
                if (x != -1) {
                    ht.put(s.substring(0,x), s.substring(x + 1, s.length()));
                }
            }

            rs.closeRecordStore();
            return ht;
        } catch (Exception ex) {
            throw ex;
        }
    }


    public String[] GetAllrecords(String recordStoreName) throws Exception {


        try {
            InputStream stream = null;
            RecordStore rs = RecordStore.openRecordStore(recordStoreName, false);
            RecordEnumeration r = rs.enumerateRecords(null, null, true);
            String [] strA = new String[rs.getNumRecords()];

            int i = 0;
            while (r.hasNextElement()) {
                byte[] adata = r.nextRecord();
                stream = new ByteArrayInputStream(adata);
                InputStreamReader e = new InputStreamReader(stream);
                int a;
                String s = "";
                while ((a = e.read()) != -1) {
                    s += String.valueOf((char) a);
                }
                strA[i] = s;
                i++;
            }
            rs.closeRecordStore();
            return strA;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Record Store not found");

        }
    }

    public String ReadRecord(String recordStoreName, int recordId) throws Exception {


        try {
            InputStream stream = null;
            RecordStore rs = RecordStore.openRecordStore(recordStoreName, false);

            byte[] adata = rs.getRecord(recordId);
            //System.out.println("char " + adata[0]);
            rs.closeRecordStore();
            stream = new ByteArrayInputStream(adata);
            InputStreamReader e = new InputStreamReader(stream);
            int a;
            String s = "";
            while ((a = e.read()) != -1) {
                s += String.valueOf((char) a);
            }
            return s;

        } catch (Exception ex) {
            throw new Exception("Record Store not found");
        }
    }

    public int createRecord(String recordStoreName, String data) {
        try {
            char[] tmp = data.toCharArray();
            byte[] d = new byte[tmp.length];

            for (int i = 0; i < tmp.length; i++) {
                d[i] = (byte) tmp[i];
            }
            RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
            int i = rs.addRecord(d, 0, d.length);

            rs.closeRecordStore();
            return i;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void deleteRecord(String recordStoreName, int recordNumber) {
        try {
            RecordStore rs = RecordStore.openRecordStore(recordStoreName, false);
            rs.deleteRecord(recordNumber);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public int updateRecord(String recordStoreName, String data, int recordId) {
        try {
            char[] tmp = data.toCharArray();
            byte[] d = new byte[tmp.length];

            for (int i = 0; i < tmp.length; i++) {
                d[i] = (byte) tmp[i];
            }
            RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
            rs.setRecord(recordId, d, 0, d.length);

            rs.closeRecordStore();
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }


}

