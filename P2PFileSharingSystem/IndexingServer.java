/**
* IndexingServer class implements start server and create thread for each Client.
* Indexing server is multithreaded server
* @author  Varun Kumar [A20365139]
* @version 1.0
* @since   2015-09-07 
*/

import java.io.*;
import java.net.*;
import java.util.*;


public class IndexingServer {
	private static ServerSocket serverSocket;
	private static Socket clientSocket = null;
	public static void main(String[] args) throws IOException {	
		try {
				
				FileReader freader = new FileReader("config.properties");
				Properties ppt = new Properties();
				ppt.load(freader);
				String Iport=ppt.getProperty("config.IserverPort");
				int IservPort = Integer.parseInt(Iport);
	
				serverSocket = new ServerSocket(IservPort);
				System.out.println("Indexing Server started.");
		} catch (Exception e) {
				System.err.println("Port already in use.");
				System.exit(1);
		}
		while (true) {
			try {
					clientSocket = serverSocket.accept();
					//System.out.println("Accepted connection : " + clientSocket);
					Thread t = new Thread(new IndexingServerThread(clientSocket));
					t.start();
				}
			catch (Exception e) {
					System.err.println("Error in connection attempt.");
			}
		}
	}
}
