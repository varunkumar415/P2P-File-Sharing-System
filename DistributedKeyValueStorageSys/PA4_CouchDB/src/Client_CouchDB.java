/**
 * @author varun kumar
 *
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;


public class Client_CouchDB {
	
	static int num_ops = 10;
	private static Session sock = null;
	static String dbName = "CIT";
	static Database sockdb=null;
	private static int flag=0;
	private static boolean boolval1 = false;
	static String array[][] = new String[num_ops][2];
	Random rnd = new Random();
	private static ArrayList<Session> sockList;
	private static ArrayList<Database> sockListdb;
	
	private static Logger logger = Logger.getLogger("CouchDB_TEST");
	
	public static void main(String[] args) 
	{
	    try
	    {
	    	sockList = new ArrayList<Session>();
	    	sockListdb = new ArrayList<Database>();
	    	populate_array();
	    	FileReader freader = new FileReader("config.properties");
			Properties ppt = new Properties();
			ppt.load(freader);
			int iserverCount = Integer.parseInt(ppt.getProperty("config.serverCount"));
			try {
				
				FileHandler fh;
		        fh = new FileHandler("CouchDBLog.log");
		        logger.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();
		        fh.setFormatter(formatter);
		        logger.info("Logger started");
				
				for (int i = 0; i < iserverCount; i++) {
					String IP = ppt.getProperty("config.IP"+(i + 1));
					
					//Session dbSession = new Session("localhost", 5984);
					sock = new Session(IP, 5984);
					sockdb = sock.createDatabase(dbName+i);
					if(sockdb==null)
						sockdb = sock.getDatabase(dbName+i);
					sockList.add(sock);
					sockListdb.add(sockdb);
					
				}
				
			} catch (Exception e) {
				System.err.println("Cannot connect to the server, try again later.");
				e.printStackTrace();
				System.exit(1);
			}
	    	
	    	
	    	
	    	//start
	    	while(true)
			{
			System.out.println("\n\t\t*******Please Enter Valid Option:******* \n");
			String strChoice = selectAction();
			int choice = Integer.parseInt(strChoice.trim());
			 
			switch (choice) {
			case 1:
				//put
				System.out.println("------------PUT Operation-----------------");
				long startTime = System.currentTimeMillis();
				for(int i=0;i<num_ops;i++)
				{
					boolval1 = Put(array[i][0], array[i][1]);
					System.out.println(i+1+": "+boolval1);
				}
				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Total time for PUT = " + totalTime + "msec");
				logger.info("Total time for PUT = " + totalTime + "msec");
				
				
				break;
			case 2:
				//get-------------------------------------------------------------------------
				 System.out.println("--------------GET Operation----------------");
			        long startTime_get = System.currentTimeMillis();
					for(int i=0;i<num_ops;i++)
					{
						String getValue = Get(array[i][0]);
						//System.out.println(i+1+" Value:" + getValue);
					}
					long endTime_get = System.currentTimeMillis();
					long totalTime_get = endTime_get - startTime_get;
					System.out.println("Total time for Get = " + totalTime_get + "msec");
					logger.info("Total time for Get = " + totalTime_get + "msec");
				
				break;
			case 3:
				//Delete
				System.out.println("\n---------------Delete Operation---------------");
				long startTime_del = System.currentTimeMillis();
				for(int i=0;i<num_ops;i++)
				{
					boolval1 = Delete(array[i][0]);
					System.out.println(i+1+":"+boolval1);
					
				}
				long endTime_del = System.currentTimeMillis();
				long totalTime_del = endTime_del - startTime_del;
				System.out.println("Total time for Delete = " + totalTime_del + "msec");
				logger.info("Total time for Delete = " + totalTime_del + "msec");
		
				break;
			case 4:
				for (int i = 0; i < iserverCount; i++) 
				{
					sock.deleteDatabase(dbName);
				}
				//ps.println("4");
				//ps.flush();
				//Database.delete(DBname);
				//System.exit(1);
				break;
			default:
				System.err.println("Sorry :( Please Enter the Correct Choice..");
				choice = 0;
				break;
			}

		}//while end  
			
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }

	    System.exit(0);

	}
	
	
	/*
	 * selectAction Method :
	 */
	public static String selectAction() throws IOException {
		BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("-------Couch DB Operations---------");
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
	
	
	public static boolean Put(String k, String val) throws IOException 
	{
		boolean bval = false;
		try
		{
			int hashcode = GetHashCode(k);
			sockdb= sockListdb.get(hashcode);
			Document doc = new Document();
			doc.setId(k);
			//doc.put("key", k);
			doc.put("Value", val);
			sockdb.saveDocument(doc);
			bval = true;
			return bval;
		}catch(Exception e)
		{
			e.printStackTrace();
			bval = false;
			return bval;
		}
		
	}
	
	public static String Get(String k) throws IOException
	{
		String getValue = " ";
		
		int hashcode = GetHashCode(k);
		sockdb= sockListdb.get(hashcode);
		Document d =sockdb.getDocument(k);
		System.out.println("value:"+ d);
		if(d==null)
		{
			System.out.println("couch Db is empty");
			return null;	
		}
		//System.out.println(d);
		//getValue=d.toString();
		return getValue;
		
	}
	
	public static boolean Delete(String k) throws IOException 
	{
		boolean boolval = false;
		try{
			int hashcode = GetHashCode(k);
			sockdb= sockListdb.get(hashcode);
			Document d =sockdb.getDocument(k);
			sockdb.deleteDocument(d);
			boolval = true;
			return boolval;
		}catch(Exception e)
		{
			e.printStackTrace();
			boolval = false;
			return boolval;
		}
		
	}

}
