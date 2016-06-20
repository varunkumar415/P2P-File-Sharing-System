/**
 * PeerServerThread class implements Server Functionality for a peer when a peer acts as a server for other Peer
 * 
 * @author  Varun Kumar
 * @version 1.0
 * @since   2015-09-07 
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
//import java.lang.*;

public class NodeThread implements Runnable {

	private Socket sock = null;
	//private BufferedReader in;
	private static PrintWriter ps;
	static BufferedReader breader;
	private static String key = null;
	private static String value = null;
	private static boolean boolval = false;
	public static ConcurrentHashMap<String, String> hMap = new ConcurrentHashMap<String, String>();
	//public static Hashtable<String, String> htable = new HashTable<String, String> ();
	static Logger debug = Logger.getLogger("[VARUN]");

	public NodeThread(Socket client) {
		this.sock = client;

	}

	/* run Function will be called when a thread will start */
	public synchronized void run() {
		//debug.info("");
		System.out.println("server RUN");
		try {
			breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			ps = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

			while (true) {

				int choice;
				String clientMessage = breader.readLine(); // read case from
																// client

				choice = Integer.parseInt((clientMessage.substring(0,1)));
				//System.out.println("Server choice" + choice);
				switch (choice) {
				case 1:
					//System.out.println("Server case: 1");
					//key = breader.readLine();
					key=clientMessage.substring(1,11);
					//System.out.println(key + "\n");
					//value = breader.readLine();
					value = clientMessage.substring(11,101);
					//System.out.println(value + "\n");
					Put();
					break;
				case 2:
					//System.out.println("Server case: 2");
					//key = breader.readLine();
					key=clientMessage.substring(1,11);
					value = Get(key);
					ps.println(value);
					ps.flush();
					break;
				case 3:
					//System.out.println("Server case: 3");
					//key = breader.readLine();
					key=clientMessage.substring(1,11);
					boolval = Delete(key);
					ps.println(boolval);
					ps.flush();
					break;
				default:
					System.out.println("Incorrect command received.");
					break;
				}

			} //

		} catch (IOException ex) {
			
		}
	}

	
	public synchronized boolean Put() {
		boolean bval = false;
		try {
			ps = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			if (key != null) {
				hMap.put(key, value);
				bval = true;
				ps.println(bval);
				ps.flush();
			}
		} catch (IOException ex) {
			System.err.println("[varun2]Client error. Connection closed.");
			ex.printStackTrace();
		}
		return bval;
	}

	public synchronized String Get(String Key) {
		String getValue = null;
		try {
			//debug.info("");
			//PrintWriter ps = new PrintWriter(sock.getOutputStream());
			Iterator<String> keyIterator = hMap.keySet().iterator();

			while (keyIterator.hasNext()) {
				String hashkey = keyIterator.next();
				if (hashkey.equals(Key)) 
				{
					getValue = hMap.get(Key);
				}

			}

		} catch (Exception ex) {
			System.err.println("Client error. Connection closed.");
		}
		return getValue;
	}

	public synchronized boolean Delete(String Key) {
		boolean bdel = false;
		try {
			//debug.info("");
			breader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			Iterator<String> keyIterator = hMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String hashkey = keyIterator.next();
				if (hashkey.equals(Key)) {
					hMap.remove(hashkey);
					bdel = true;
				}
			}

		} catch (IOException ex) {
			System.err.println("Client error. Connection closed.");
		}
		return bdel;
	}

}// class end