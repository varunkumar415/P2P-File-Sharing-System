/**
 * Node2Server class implements Server Functionality for a peer when a peer acts as a server for other Peer
 * 
 * @author  Varun Kumar [A20365139]
 * @version 1.0
 * @since   2015-09-07 
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

class Server4 implements Runnable 
{

	private static ServerSocket serverPSocket;
	private static Socket clientSocket = null;
	static Logger debug = Logger.getLogger("[VARUN]");

	Server4() {
		try {
			Thread tp = new Thread(this);
			tp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* run function is called when a new thread is started */
	public void run() 
	{
		try {
			//debug.info("");
			FileReader freader = new FileReader("config.properties");
			Properties ppt = new Properties();
			ppt.load(freader);
			String P1port = ppt.getProperty("config.serverPort4");
			int P1servPort = Integer.parseInt(P1port);

			serverPSocket = new ServerSocket(P1servPort);
			System.out.println("Server4 Server started");
		} catch (Exception e) {
			System.err.println("Port already in use.");
			System.exit(1);
		}

		while (true) {
			try {
				clientSocket = serverPSocket.accept();
				System.out.println("Accepted connection from Server3 : " + clientSocket);
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
 * Node3 Class implements a peer functionality like Register files, Search File,
 * Look Up Files Info and Download File
 */

public class NodeServer4 
{
	/*
	private static Socket sock = null;
	private static String KEY;
	private static String VALUE;
	private static boolean boolval1 = false;
	private static BufferedReader stdin;
	private static PrintWriter ps;
	*/
	//static String array[][] = new String[20][1000];
	//Random rnd = new Random();

	static Logger debug = Logger.getLogger("[VARUN]");

	public static void main(String[] args) throws IOException {
		//debug.info("");
		new Server4();

	}
}
