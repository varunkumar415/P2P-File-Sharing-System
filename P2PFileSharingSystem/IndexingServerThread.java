/**
* IndexingServerThread class implements register, search functionality
* For Indexing Server
* @author  Varun Kumar [A20365139]
* @version 1.0
* @since   2015-09-07 
*/

import java.io.*;
import java.net.*;
import java.util.*;



public class IndexingServerThread implements Runnable {

	private Socket clientSocket;
	private BufferedReader in = null;
	public static HashMap<String, String> hMap = new HashMap<String, String>();

	public IndexingServerThread(Socket client) {
		this.clientSocket = client;
	}

	public void run() {
		try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				int choice;
				String clientSelection;
				while ((clientSelection = in.readLine()) != null) {
				choice = Integer.parseInt(clientSelection.trim());
				switch (choice) {
				case 1:
					RegisterFiles();
					break;
				case 2:
					GetListFiles();
					break;
				case 3:
					SearchFile();
					break;
				default:
					//System.out.println("Incorrect command received.");
					break;
				}
				in.close();
				break;
		}

		} 
		catch (IOException ex) {
			System.err.println(ex);
		}
}

    /*RegisterFiles: This Method implements registration of files from Peers 
     and Store file info and peer info into a Hashmap*/	
	public void RegisterFiles() {
		try {
				PrintWriter ps = new PrintWriter(clientSocket.getOutputStream());
				BufferedReader breader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String key = null;
				String peer = clientSocket.getRemoteSocketAddress().toString(); 
				while ((breader.read()) != '~') 
				{
					key = breader.readLine();
					hMap.put(key, peer);
				}
			
			// Then get data from Hash Map
			Iterator<String> keyItr = hMap.keySet().iterator();
			while (keyItr.hasNext()) {
				String Filename = keyItr.next();
				//System.out.println("File Name:" + Filename + "\t\tPEER:"+ hMap.get(Filename));
			}

			InetAddress ipAddr = InetAddress.getLocalHost(); //it will print only IP Address
			String IP = ipAddr.getHostAddress();

			ps.println("\tFiles Registered Successfully On Indexing Server " + IP);
			ps.println("~");// end of writing
			ps.flush();
			//ps.close();

		} catch (IOException ex) {
			System.err.println("Client error. Connection closed.");
		}
	}

	/*GetListFiles: This Method implements gives list of file info and peer info to Requested Peer*/
	
	public void GetListFiles() {
		try {
			PrintWriter ps = new PrintWriter(clientSocket.getOutputStream());
			// get data from HashMap
			Iterator<String> keyIterator = hMap.keySet().iterator();
			while (keyIterator.hasNext()) 
			{
				String Filename = keyIterator.next();
				ps.println(":File Name: " + Filename + "\t\t\tPeer Info: "+ hMap.get(Filename));
			}
			ps.println("~");// end of writing
			ps.flush();

			} catch (IOException ex) {
				System.err.println("Client error. Connection closed.");
			}
	}

	/*SearchFile: This Method implements Searching of a file from HashMap and return File and Peer Info to Peers*/
	public void SearchFile() {
		try {
				PrintWriter ps = new PrintWriter(clientSocket.getOutputStream());
				BufferedReader breader = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
				String fileName = null;
				while ((breader.read()) != '~')
				fileName = breader.readLine();
				Iterator<String> keyIterator = hMap.keySet().iterator();
				while (keyIterator.hasNext()) {
				String hashkey = keyIterator.next();
				if (hashkey.equals(fileName)) {
					ps.println(":File Found !!");					
					ps.println(":File Info: " + hashkey + "\tPeer Info: "
							+ hMap.get(hashkey));
					ps.println("~");// end of writing
					ps.flush();
					return;
				}

				}
				ps.println(":File Not Found!!");
				ps.println("~");// end of writing
				ps.flush();

		} catch (IOException ex) {
			System.err.println("Client error. Connection closed.");
		}
	}

}