/**
 * @author varun kumar
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;



public class Client_CassandraDB {

	/**
	 * @param args
	 */
	static int num_ops = 10;
	static String array[][] = new String[num_ops][2];
	Random rnd = new Random();
	private static Logger logger = Logger.getLogger("CassandraDB_TEST");
	
	public static void main(String[] args) {
		
		Cluster cluster = null;
	    cluster = Cluster.builder().addContactPoint("localhost").build();
	    Session session = cluster.connect();

	    try
	    {
	    	FileHandler fh;
	        fh = new FileHandler("CassandraLog.log");
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();
	        fh.setFormatter(formatter);
	        logger.info("Logger started");
	    	
	    	String key=null;
	    	String value=null;
	    	populate_array();
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
					key = array[i][0];
					value= array[i][1];
					//System.out.println("key: "+key+" value: "+ value);
					session.execute(("INSERT INTO test1.user1 (key, value) VALUES ('"+key+"','"+value+"')"));
					System.out.println(i+1);
				}
				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Total time for PUT = " + totalTime + "msec");
				logger.info("Total time for PUT = " + totalTime + "msec");
				
				break;
			case 2:
				//get
				 System.out.println("--------------GET Operation----------------");
			        long startTime_get = System.currentTimeMillis();
					for(int i=0;i<num_ops;i++)
					{
						key = array[i][0];	
				        ResultSet result_get =  session.execute(("SELECT * FROM test1.user1 WHERE key ='"+key+"'"));
						for (Row row : result_get) 
						{
							System.out.println(i+1+" value: "+row.getString("value"));
						}
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
					key = array[i][0];
					session.execute("DELETE FROM test1.user1 WHERE key='"+key+"'");	
					System.out.println(i+1);
				}
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
		System.out.println("------Cassandra DB Operations------");
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