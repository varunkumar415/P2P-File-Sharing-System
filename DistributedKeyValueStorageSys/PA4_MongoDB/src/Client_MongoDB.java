/**
 * @author varun kumar
 *
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;





public class Client_MongoDB {

	static int num_ops = 10;
	static String array[][] = new String[num_ops][2];
	Random rnd = new Random();
	private static MongoClient sock = null;
	private static DB sockdb = null;
	private static DBCollection sockdbcoll = null;
	private static ArrayList<MongoClient> sockList;
	private static ArrayList<DB> sockListdb;
	private static ArrayList<DBCollection> sockListdbcoll;
	private static Logger logger = Logger.getLogger("MongoDB_TEST");
	
	public static void main(String[] args) throws IOException 
	{
		
		
			populate_array();		
	    	sockList = new ArrayList<MongoClient>();
	    	sockListdb = new ArrayList<DB>();
	    	sockListdbcoll = new ArrayList<DBCollection>();
	    	FileReader freader = new FileReader("config.properties");
			Properties ppt = new Properties();
			ppt.load(freader);
			int iserverCount = Integer.parseInt(ppt.getProperty("config.serverCount"));
			System.out.println("serverCount: "+ iserverCount);
			try {
				
				FileHandler fh;
		        fh = new FileHandler("MongoLog.log");
		        logger.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();
		        fh.setFormatter(formatter);
		        logger.info("Logger started");
					
				for (int i = 0; i < iserverCount; i++) 
				{
					
					String IP = ppt.getProperty("config.IP" + (i + 1));
					sock = new MongoClient(IP, 27017);
					sockdb = sock.getDB("chicago"+(i+1));
					sockdbcoll = sockdb.getCollection("kvtable"+(i+1));
					sockList.add(sock);
					sockListdb.add(sockdb);
					sockListdbcoll.add(sockdbcoll);
				}
			} catch (Exception e) {
				System.err.println("Cannot connect to the server, try again later.");
				e.printStackTrace();
				System.exit(1);
			}		
		
		//while
		while(true)
		{
		System.out.println("\n\t\t*******Please Enter Valid Option:******* \n");
		String strChoice = selectAction();
		int choice = Integer.parseInt(strChoice.trim());
		 
		switch (choice) {
		case 1:
			//put
			//System.out.println("[Before PUT] Size of Collection :" + collection.getCount());
			long startTime = System.currentTimeMillis();
			for(int i=0;i<num_ops;i++)
			{
				int hashcode = GetHashCode(array[i][0]);
				//System.out.println("Hash code: "+hashcode);
				sockdbcoll = sockListdbcoll.get(hashcode);
				BasicDBObject dbObj = new BasicDBObject();
				dbObj.put(array[i][0], array[i][1]);
				sockdbcoll.insert(dbObj);
				
				
			}
			//System.out.println("[After PUT] Size of Collection :" + sockdbcoll.getCount());
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Total time for PUT = " + totalTime + "msec");
			 logger.info("Total time for PUT = " + totalTime + "msec");
			
			//
			
			
			break;
		case 2:
			//get
			//System.out.println("[Before Get] Size of Collection :" + collection.getCount());
			long startTime_get = System.currentTimeMillis();
			DBObject dbObj1 = null ;
			for(int i=0;i<num_ops;i++)
			{
				int hashcode = GetHashCode(array[i][0]);
				//System.out.println("Hash code: "+hashcode);
				sockdbcoll = sockListdbcoll.get(hashcode);
				BasicDBObject obj = new BasicDBObject();
				obj.put(array[i][0], array[i][1]);
				DBCursor cursor1 = sockdbcoll.find(obj);
				while(cursor1.hasNext())
				  { 
					
					dbObj1=cursor1.next();        
				    String data=dbObj1.get(array[i][0]).toString();
				    System.out.println(i+1+" value: "+ data);
				  }
			}
			System.out.println("[After Get] Size of Collection :" + sockdbcoll.getCount());
			long endTime_get = System.currentTimeMillis();
			long totalTime_get = endTime_get - startTime_get;
			System.out.println("Total time for Get = " + totalTime_get + "msec");
			logger.info("Total time for Get = " + totalTime_get + "msec");
			
			
			break;
		case 3:
			//Delete
			//System.out.println("[Before Delete] Size of Collection :" + collection.getCount());
			long startTime_del = System.currentTimeMillis();
			DBObject dbObj2 = null ;
			for(int i=0;i<num_ops;i++)
			{
				
				int hashcode = GetHashCode(array[i][0]);
				//System.out.println("Hash code: "+hashcode);
				sockdbcoll = sockListdbcoll.get(hashcode);
				BasicDBObject obj_del = new BasicDBObject();
				obj_del.put(array[i][0], array[i][1]);
				DBCursor cursor_del = sockdbcoll.find(obj_del);
				while(cursor_del.hasNext())
				  { 
					
					dbObj2=cursor_del.next();        
					sockdbcoll.remove(dbObj2);
				  }
			}
			System.out.println("[After Delete] Size of Collection :" + sockdbcoll.getCount());
			long endTime_del = System.currentTimeMillis();
			long totalTime_del = endTime_del - startTime_del;
			System.out.println("Total time for Delete = " + totalTime_del + "msec");
			logger.info("Total time for Delete = " + totalTime_del + "msec");
			break;
		case 4:
			System.exit(1);
			break;
		default:
			System.err.println("Sorry :( Please Enter the Correct Choice..");
			choice = 0;
			break;
		}

		}
		
		
	}

	
	/*
	 * selectAction Method :
	 */
	public static String selectAction() throws IOException {
		BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("-----Mongo DB Operations-------");
		System.out.println("1. PUT    Operation");
		System.out.println("2. GET    Operation");
		System.out.println("3. DELETE Operation");
		System.out.println("4. EXIT\n");
		return r1.readLine();
	}

	
	
	// populate array
		public static void populate_array() {
			for (int i = 0; i < num_ops; i++) {
				for (int j = 0; j < 2; j++) {

					char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
					StringBuilder sb = new StringBuilder();
					Random random = new Random();
					if (j == 0) {
						for (int z = 0; z < 10; z++) {
							char k = chars[random.nextInt(chars.length)];
							sb.append(k);
						}
						String key = sb.toString();
						array[i][j] = key;

					}
					if (j == 1)
						for (int z = 0; z < 90; z++) {
							char k = chars[random.nextInt(chars.length)];
							sb.append(k);
						}
					String value = sb.toString();
					array[i][j] = value;

				}
			}
		}

		/*
		 * GetHashCode Method:
		 */
		public static int GetHashCode(String K) throws IOException {
			int hashcode = 0;
			int MOD = 8;
			int shift = 29;
			for (int i = 0; i < K.length(); i++) {
				hashcode = ((shift * hashcode) % MOD + K.charAt(i)) % MOD;
			}
			return hashcode;
		}
		
	

}
