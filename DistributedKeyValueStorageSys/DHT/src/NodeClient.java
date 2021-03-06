/**
 * Node1Server class implements Server Functionality for a peer when a peer acts as a server for other Peer
 * 
 * @author  Varun Kumar [A20365139]
 * @version 1.0
 * @since   2015-09-07 
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

class Node1Server implements Runnable {

	private static ServerSocket serverPSocket;
	private static Socket clientSocket = null;

	Node1Server() {
		try {
			Thread tp = new Thread(this);
			tp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* run function is called when a new thread is started */
	public void run() {
		try {
			FileReader freader = new FileReader("config.properties");
			Properties ppt = new Properties();
			ppt.load(freader);
			String P1port = ppt.getProperty("config.serverPort3");
			int P1servPort = Integer.parseInt(P1port);

			serverPSocket = new ServerSocket(P1servPort);
			System.out.println("Node1Server Server started");
		} catch (Exception e) {
			System.err.println("Port already in use.");
			System.exit(1);
		}

		while (true) {
			try {
				clientSocket = serverPSocket.accept();
				System.out.println("Accepted connection from Server1 : " + clientSocket);
				Thread t1 = new Thread(new NodeThread(clientSocket));
				t1.start();

			} catch (Exception e) {
				System.err.println("Error in connection attempt.");
			}
		}

	}
}

// -----------------------------------------------------------------------------------------------------------------------------------------------------

// CLIENT
/*
 * NodeClient Class implements a peer functionality like Register files, Search
 * File, Look Up Files Info and Download File
 */

public class NodeClient {

	private static Socket sock = null;
	private static boolean boolval1 = false;
	//private static String Header = "0001";
	private static PrintWriter ps;
	private static ArrayList<Socket> socList;

	static String array[][] = new String[100][2];
	Random rnd = new Random();

	static Logger debug = Logger.getLogger("[VARUN]");

	public static void main(String[] args) throws IOException {

		// new Node1Server();
		try {
			socList = new ArrayList<Socket>();
			populate_array(); //it will populate key and values
			FileReader freader = new FileReader("config.properties");
			Properties ppt = new Properties();
			ppt.load(freader);
			int iserverCount = Integer.parseInt(ppt.getProperty("config.serverCount"));
			try {
				for (int i = 0; i < iserverCount; i++) {
					String IP = ppt.getProperty("config.IP" + (i + 1));
					int Iport = Integer.parseInt(ppt.getProperty("config.serverPort" + (i + 1)));
					sock = new Socket(IP, Iport);
					socList.add(sock);
				}
			} catch (Exception e) {
				System.err.println("Cannot connect to the server, try again later.");
				e.printStackTrace();
				System.exit(1);
			}
			int choice = 0;
			String strChoice;
			do {

				System.out.println("\n\t\t*******Please Enter Valid Option:******* \n");
				//BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
				// Scanner input = new Scanner(System.in);
				strChoice = selectAction();
				choice = Integer.parseInt(strChoice.trim());
				 //System.out.println("choice:" + choice);
				switch (choice) {
				case 1:

					
					long startTime = System.currentTimeMillis();
					
					for(int i=0;i<100;i++)
					{
						boolval1 = Put(array[i][0], array[i][1]);
						System.out.println(i+": "+boolval1);
						
					}

					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					System.out.println("Total time for PUT = " + totalTime + "msec");
					break;
				case 2:
					//System.out.println("Enter Key");
					//String GK = r1.readLine();
					long startgetTime = System.currentTimeMillis();
					//if (GK.length() > 0) {
						//String getValue = Get(GK);
						//System.out.println("Value:" + getValue);
					//} else {
						//System.out.println("Not a valid key");
						//System.out.println("Value: null");
					//}
					for(int i=0;i<100;i++)
					{
						String getValue = Get(array[i][0]);
						System.out.println("Value:" + getValue);
						
					}
					
					long endgetTime = System.currentTimeMillis();
					long totalgetTime = endgetTime - startgetTime;
					System.out.println("Total time for GET = " + totalgetTime + "msec");
					break;
				case 3:
					//System.out.println("Enter Key to Delete");
					//String GK2 = r1.readLine();
					long startdeleteTime = System.currentTimeMillis();
					//if (GK2.length() > 0) {
						//boolval1 = Delete(GK2);
						//System.out.println(":" + boolval1);
					//} else {
						//System.out.println("Not a valid key");
						//System.out.println(boolval1);
					//}
					for(int i=0;i<100;i++)
					{
						boolval1 = Delete(array[i][0]);
						System.out.println(boolval1);
						
					}
					long enddeleteTime = System.currentTimeMillis();
					long totaldeleteTime = enddeleteTime - startdeleteTime;
					System.out.println("Total time for Delete = " + totaldeleteTime + "msec");
					break;
				case 4:
					//ps.println("4");
					//ps.flush();
					//System.exit(1);
					break;
				default:
					System.err.println("Sorry :( Please Enter the Correct Choice..");
					choice = 0;
					break;
				}

			} while (true);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/*
	 * selectAction Method :
	 */
	public static String selectAction() throws IOException {
		BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("1. PUT    Operation");
		System.out.println("2. GET    Operation");
		System.out.println("3. DELETE Operation");
		System.out.println("4. EXIT\n");
		return r1.readLine();
	}

	// populate array
	public static void populate_array() {
		for (int i = 0; i < 100; i++) {
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
	 * Get Method :
	 */
	public static boolean Put(String k, String val) throws IOException {
		boolean bval = false;
		try {
			//debug.info("");
			int hashcode = GetHashCode(k);
			//System.out.println(hashcode);
			sock = socList.get(hashcode);
			ps = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			BufferedReader breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            
			String message = ("1"+k+val);
            ps.println(message);
			ps.flush();
			//ps.println("1");
			//ps.flush();

			//ps.println(k);
			//ps.flush();

			//ps.println(val);
			//ps.flush();

			String boolval = breader.readLine();
			if (boolval.equals("true")) {
				bval = true;
			}

		} catch (IOException ex) {
			System.err.println(ex);
			ex.printStackTrace();
		}
		return bval;
	}

	/*
	 * Get Method :
	 */
	public static String Get(String k) throws IOException {
		String getValue = null;
		try {
			// debug.info("");
			int hashcode = GetHashCode(k);
			sock = socList.get(hashcode);
			ps = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			BufferedReader breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			String message = ("2"+k);
			ps.println(message);
			ps.flush();
			
			//ps.println("2");
			//ps.flush();

			//ps.println(k);
			//ps.flush();

			getValue = breader.readLine();

		} catch (IOException ex) {
			System.err.println(ex);
		}
		return getValue;
	}

	/*
	 * Delete Method:
	 */
	public static boolean Delete(String k) throws IOException {
		boolean boolval = false;
		try {
			//debug.info("");
			int hashcode = GetHashCode(k);
			sock = socList.get(hashcode);
			ps = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			BufferedReader breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			String message = ("3"+k);
			ps.println(message);
			ps.flush();
			//ps.println("3");
			//ps.flush();
			//ps.println(k);
			//ps.flush();
			String boolval1 = breader.readLine();
			if (boolval1.equals("true")) {
				boolval = true;
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
		return boolval;
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

}// end of class
